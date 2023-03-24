package com.hua.project.project_first.service.registerAndLoginService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class TokenService {
    private static String secret = "!@#$%shiguohuashishuaige!@#_#@_#--_-@_-+hahahahahha";
    /**
     * 生成token
     *
     * @param hashmap
     */
    public static String InsertToken(Map<String, String> hashmap , int time , int timeType) {

        Calendar insCalendar = Calendar.getInstance(); // 提供日期格式的处理

        insCalendar.add(timeType , time); // 单位为天 默认7天后过期

        JWTCreator.Builder builder = JWT.create(); // 创建JWT builder

        hashmap.forEach((k, v) -> { // 将传入的值循环插入
            builder.withClaim(k, v);
        });

        String token = builder.withExpiresAt(insCalendar.getTime()) // 指定指令令牌过期时间 上面指定时间单位为天 单位可以自行更改

                .sign(Algorithm.HMAC256(secret)); // 传入上面定义的secret

        return token; // 最后将生成的token返回
    }

    /**
     * 验证token合法性
     */
    public static String SelectToken(String token , String yourUse) { // 验证合法性此时报错就是没有通过验证
        // 为你列出常见的报错
        /**
         * - SignatureVerificationException : 签名不一致异常 - TokenExpiredException: 令牌过期异常 -
         * AlgorithmMismatchException: 算法不匹配异常 - InvalidclaimException: 失效的payload异常
         *
         */
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build(); // 传入签名
        DecodedJWT verify = jwtVerifier.verify(token);// 传入token
        return verify.getClaim(yourUse).asString();
    }


























}
