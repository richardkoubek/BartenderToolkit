package com.bartendertoolkit.services;

import com.bartendertoolkit.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    public String generateAuthToken(User user) {
        UserDetailsImpl userDetails = UserDetailsImpl.fromUser(user);
        Date now = new Date();
        Date jwtTokenExp = new Date(now.getTime() + AUTH_COOKIE_TTL * 1000L);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyWithPadding());

        return Jwts.builder()
                .subject(userDetails.getEmail())
                .id(String.valueOf(userDetails.getId()))
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

    public Cookie login(String email, String password) throws Exception {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new Exception("Please provide a valid email address and password.");
        }

        User user = userService.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Incorrect email or password.");
        }

        String token = user.getUserToken();
        if (!validateToken(token)) {
            token = generateAuthToken(user);
            userService.setTokenToUser(user, token);
        }
        return createAuthCookie(token);
    }

    public ResponseEntity<?> logoutUserFromCookie(String authCookieValue) throws UnsupportedEncodingException {
        if (authCookieValue != null) {
            String decodedCookie = URLDecoder.decode(authCookieValue, StandardCharsets.UTF_8.toString());
            String[] cookieParts = decodedCookie.split(";\\s*");
            String token = null;
            for (String part : cookieParts) {
                if (part.startsWith("authToken=")) {
                    token = part.substring("authToken=".length());
                    break;
                }
            }
            if (token != null) {
                UserDetailsImpl userDetails = getUserDetailsFromToken(token);
                if (userDetails != null) {
                    userService.removeToken(userDetails);
                    return ResponseEntity.status(204).build();
                }
            }
        } else {
            throw new RuntimeException("Decoding cookie failed");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public UserDetailsImpl getUserDetailsFromToken (String token){
        if (!StringUtils.hasText(token)) {
            return null;
        }

        try {
            Claims claims = getAllClaimsFromToken(token);
            if (claims != null) {
                String userId = claims.getId();
                String email = claims.getSubject();

                if (userId != null && email != null) {
                    return new UserDetailsImpl(Long.parseLong(userId), email, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
