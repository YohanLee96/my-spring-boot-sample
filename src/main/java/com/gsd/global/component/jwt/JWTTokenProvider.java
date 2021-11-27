package com.gsd.global.component.jwt;

import com.gsd.domain.user.constant.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JWTTokenProvider {


    public static final String BEARER  = "Bearer ";
    public static final String AUTH_HEADER  = "Authorization";
    private static final long HOURS = 60 * 60;
    private static final long ACCESS_TOKEN_TIME = HOURS * 24 * 365; //토큰 유효시간 지정.(1년)

    @Value("${jwt.secretkey-value}")
    private String secretKeyValue;

    private Key secretKey;

    private JwtParser jwtParser;

    @PostConstruct
    void init() {
        this.secretKey  = Keys.hmacShaKeyFor(secretKeyValue.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }


    /**
     * JWT 토큰에서 Claims 값을 가져온다.
     * @param token JWT 토큰
     * @return 토큰에 담겨있는 Claims 데이터
     */
    public ClaimDTO get(String token) {
        Claims body = jwtParser
                .parseClaimsJws(token)
                .getBody();

        return new ClaimDTO(body);
    }

    /**
     * JWT 유효한지 검증한다.
     * 1. 토큰이 변조되지 않았는지 확인.
     * 2. 토큰 만료기한을 초과하지 않았는지 확인.
     * @param token JWT 토큰
     * @return 검증 결과
      */
    public boolean validToken(String token) {
        try{
            jwtParser.parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Request Header에 있는 토큰값을 가져온다.
     * @param request HTTP Request
     * @return JWT 토큰
     */
    public String getTokenByHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }


    /**
     * AccessToken을 생성한다.
     * @param subject Token에 파싱할 Subject 값
     * @param userType Token에 파싱할 유저권한 값
     * @return JWT 토큰
     */
    public String generateAccessToken(String subject, Long userId, UserType userType) {
        return createToken(secretKey, subject, userId, userType, addExpireTime(ACCESS_TOKEN_TIME));
    }


    /**
     *
     * @param secretKey 토큰 생성 시 해싱할 JWT 시그니쳐값
     * @param subject Token에 파싱할 Subject 값
     * @param userType Token에 파싱할 유저권한 값
     * @param expireTime Token 만료기한
     * @return JWT 토큰
     */
    private String createToken(Key secretKey, String subject, Long userId, UserType userType, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("subject", subject);
        claims.put("userId", userId);
        claims.put("role", userType);
        claims.put("exp",  expireTime);

        return Jwts.builder()
                .setClaims(claims) //토큰에 담을 정보.
                .setIssuedAt(new Date()) //토큰 발행시간.
                .signWith(secretKey, SignatureAlgorithm.HS256) //해싱값.
                .compact();
    }

    private long addExpireTime(long addTime) {
        return Instant.now().getEpochSecond() + addTime;
    }


}
