package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int AUTH_COOKIE_TTL = 30 * 24 * 60 * 60;
    private static final int KEY_LENGTH = 32;
    private final Pbkdf2PasswordEncoder passwordEncoder;

    @Value("${password.secret}")
    private String jwtSecret;

    @Value("${authCookieName}")
    private String authCookieName;

    private final UserService userService;

    public Cookie createAuthCookie(String token) throws Exception {
        Cookie cookieTtl = new Cookie(authCookieName, token);
        cookieTtl.setPath("/");
        cookieTtl.setHttpOnly(true);
        cookieTtl.setMaxAge(AUTH_COOKIE_TTL);
        return cookieTtl;
    }

    public String generateAuthToken(User user, String email, String password) throws Exception {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new Exception("Please provide a valid email address and password.");
        }

        if (!userService.isCorrectPasswordFormat(password)) {
            throw new Exception("Incorrect password.");
        }

        return generateJWT(new UserDetailsImpl(user.getId(), user.getEmail(), passwordEncoder.encode(password)));
    }

    public String generateJWT(UserDetailsImpl user) {
        Date now = new Date();
        Date jwtTokenExp = new Date(now.getTime() + AUTH_COOKIE_TTL * 1000L);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyWithPadding());

        return Jwts.builder()
                .subject(user.getEmail())
                .id(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(jwtTokenExp)
                .issuer("/")
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        Date expDate = getExpDateFromToken(token);
        Date now = new Date();
        return now.before(expDate);
    }

    public Claims getAllClaimsFromToken(String token) {
        if (!StringUtils.hasText(token)){
            return null;
        }
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyWithPadding());
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .decryptWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload();
    }

    private Date getExpDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private byte[] secretKeyWithPadding() {
        byte[] key = jwtSecret.getBytes();
        byte[] result = new byte[KEY_LENGTH];
        System.arraycopy(key, 0, result, 0, key.length);

        for (int j = key.length; j < KEY_LENGTH; j++) {
            result[j] = 0;
        }

        return result;
    }

    public String login(String email, String password, UserDetailsImpl userDetails) throws Exception {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new Exception("Please provide a valid email address and password.");
        }

        User user = userService.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new Exception("Incorrect email or password.");
        }
        if(validateToken(user.getUserToken())){
            return generateJWT(userDetails);
        } else {
            return generateAuthToken(user, email, password);
        }
    }
}
