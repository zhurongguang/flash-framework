package com.flash.framework.tools.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map;

/**
 * JWT token 生成/校验工具
 *
 * @author zhurg
 * @date 2019/4/9 - 下午2:02
 */
public class JwtHelper {

    public static final String DEFAULT_TOKEN_KEY = "token";

    private static final String JWT_TOKEN_ISSUE = "uc";

    /**
     * 基于用户ID和登录密码生成token
     *
     * @param userId
     * @param password
     * @param expireTime
     * @return
     */
    public static String getToken(String userId, String password, int expireTime) {
        Date currentTime = new Date();
        return JWT.create()
                .withIssuer(JWT_TOKEN_ISSUE)
                .withIssuedAt(currentTime)
                .withNotBefore(currentTime)
                .withClaim(DEFAULT_TOKEN_KEY, userId)
                .withExpiresAt(DateUtils.addSeconds(currentTime, expireTime))
                .sign(Algorithm.HMAC256(password));
    }

    /**
     * t
     * 基于自定义Token对象和登录密码生成token
     *
     * @param params
     * @param password
     * @param expireTime
     * @return
     */
    public static String getToken(Map<String, String> params, String password, int expireTime) {
        Date currentTime = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(JWT_TOKEN_ISSUE);
        builder.withIssuedAt(currentTime);
        builder.withNotBefore(currentTime);
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((key, val) -> builder.withClaim(key, val));
        }
        builder.withExpiresAt(DateUtils.addSeconds(currentTime, expireTime));
        return builder.sign(Algorithm.HMAC256(password));
    }

    /**
     * 获取token中的数据
     *
     * @param token
     * @return
     */
    public static String decode(String token, String password) {
        DecodedJWT decodedJWT = verify(token, password);
        return decodedJWT.getClaim(DEFAULT_TOKEN_KEY).asString();
    }

    /**
     * 获取token中的数据
     *
     * @param token
     * @return
     */
    public static Map<String, String> decode2Map(String token, String password) {
        DecodedJWT decodedJWT = verify(token, password);
        Map<String, Claim> claimMap = decodedJWT.getClaims();
        if (MapUtils.isNotEmpty(claimMap)) {
            Map<String, String> map = Maps.newHashMapWithExpectedSize(claimMap.size());
            claimMap.forEach((key, claim) -> map.put(key, claim.asString()));
            return map;
        }
        return null;
    }

    /**
     * 基于用户ID和密码的token校验
     *
     * @param token
     * @param password
     * @return
     */
    public static DecodedJWT verify(String token, String password) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password))
                .withIssuer(JWT_TOKEN_ISSUE)
                .acceptLeeway(1)
                .build();
        return verifier.verify(token);
    }
}