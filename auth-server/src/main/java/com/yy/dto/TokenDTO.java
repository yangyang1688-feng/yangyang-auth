package com.yy.dto;

import lombok.Data;

/**
 * tokenDTO类
 * @ClassName: TokenDTO
 * @author: yangfeng
 * @date 2025/12/3 13:52
 * @version: 1.0.0
 */
@Data
public class TokenDTO {
    private String access_token;

    private String token_type;

    private long expiresIn;

    private String refresh_token;
    private String scope;
}
