package com.mountain.spring.service;

import com.mountain.entity.role.Role;
import com.mountain.entity.user.User;
import com.mountain.library.domain.AppConstant;
import com.mountain.library.helper.NumberUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${mountain.app.jwtSecret}")
    private String jwtSecret;

    @Value("${mountain.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private JwtParser jwtParser;

    public String generateJwtToken(User u, LocalDateTime expired) {

        boolean isSystemService = false;

        String roles = "";

        for (Role r : u.getRoles()) {
            roles = r.getName().name();

            isSystemService = isSystemService || r.getName().name().startsWith("SYSTEM_");
        }

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim("role", roles)
                .claim("phoneNumber", u.getPhoneNumber())
                .claim("username", u.getUsername())
                .claim("email", u.getEmail())
                .setSubject(u.getPhoneNumber())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret);

        if (isSystemService) jwtBuilder.setExpiration(null);
        return jwtBuilder.compact();
    }

    public String generateJwtToken(User u) {
        LocalDateTime tokenExpired = LocalDateTime.now().plusDays(
                NumberUtils.parseLongOrDefault(AppConstant.APP_ACCESS_TOKEN, 1));
        return generateJwtToken(u, tokenExpired);
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Jws<Claims> parseJwt(String token) {
        return jwtParser.parseClaimsJws(token);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
