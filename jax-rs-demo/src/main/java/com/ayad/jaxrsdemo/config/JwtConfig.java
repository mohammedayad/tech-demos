package com.ayad.jaxrsdemo.config;


import com.payconiq.shared.security.oauth2.jwt.token.Subject;
import com.payconiq.shared.security.oauth2.jwt.token.TokenBuilder;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JwtConfig {
    private final TokenBuilder tokenBuilder;

    private static final String SECRET_KEY = "MzJieXRzU2VjcmV0S2V5Rm9ySldUVXNpbmcMzJieXRzU2VjcmV0S2V5Rm9ySldUVXNpbmc="; // Replace with a secure key


    public JwtConfig(TokenBuilder tokenBuilder) {
        this.tokenBuilder = tokenBuilder;
    }

    @SneakyThrows
    public String generateJWT(String subjectType, String subject, String country, long expiresSec,
                              String authority) {
        Subject pqUserSubject = Subject.builder()
                .type(subjectType)
                .id(subject)
                .country(country)
                .build();

        return tokenBuilder.newBuilder()
                .subject(pqUserSubject)
                .authority(authority)
                .expiresSec(expiresSec)
                .build();

    }


//    public String generateToken(String subjectType, String subject, String authority) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("subjectType", subjectType);
//        claims.put("authority", authority);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }

//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public String extractAuthority(String token) {
//        return (String) extractAllClaims(token).get("authority");
//    }

//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }


//    public Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()  // Must call build() before parsing
//                .parseClaimsJws(token)
//                .getBody();
//    }

//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
}
