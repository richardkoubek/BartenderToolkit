package com.bartendertoolkit.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final int AUTH_COOKIE_TTL = 30 * 24 * 60 * 60;
    private static final int KEY_LENGTH = 32;
    
}
