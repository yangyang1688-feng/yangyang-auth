-- ========================================================
-- 表名: oauth2_authorization
-- 描述: OAuth2 授权核心表
-- 用途: 存储完整的OAuth2授权生命周期中的所有状态和数据
-- Spring Authorization Server 1.5.1 核心表
-- 支持: authorization_code, client_credentials, refresh_token,
--       device_code, password, JWT bearer 等授权类型
-- ========================================================
CREATE TABLE IF NOT EXISTS oauth2_authorization (
    -- 授权记录唯一标识符
    -- 通常使用UUID或类似唯一值
    -- 主键，用于快速查找授权记录
    id VARCHAR(100) NOT NULL COMMENT '授权记录唯一ID（通常为UUID）',

    -- 客户端注册ID
    -- 关联 oauth2_registered_client 表的 id 字段
    -- 表示此授权属于哪个客户端应用
    registered_client_id VARCHAR(100) NOT NULL COMMENT '客户端注册ID，关联oauth2_registered_client.id',

    -- 主体名称（用户标识）
    -- 授权用户的主体标识，在密码授权、授权码授权中为用户标识
    -- 在 client_credentials 授权中通常为客户端ID
    -- 格式: username, email, sub claim 等
    principal_name VARCHAR(200) NOT NULL COMMENT '主体名称（用户/客户端标识）',

    -- 授权类型
    -- OAuth2 授权类型，支持标准授权类型：
    -- 'authorization_code' - 授权码模式
    -- 'client_credentials' - 客户端凭证模式
    -- 'refresh_token' - 刷新令牌模式
    -- 'device_code' - 设备码模式
    -- 'password' - 密码模式（不推荐，OAuth2.1已移除）
    -- 'urn:ietf:params:oauth:grant-type:jwt-bearer' - JWT bearer
    authorization_grant_type VARCHAR(100) NOT NULL COMMENT '授权类型（authorization_code等）',

    -- 授权范围
    -- 用户同意授予的权限范围，逗号分隔
    -- 例如: 'openid profile email message.read'
    -- 对应OAuth2标准中的scope参数
    authorized_scopes VARCHAR(1000) DEFAULT NULL COMMENT '授权范围，逗号分隔的权限字符串',

    -- 授权属性
    -- 存储授权的额外属性，序列化的Map结构
    -- 包含自定义属性、会话信息、认证上下文等
    -- 格式: 通常为JSON或Java序列化对象
    attributes BLOB DEFAULT NULL COMMENT '授权属性，序列化的Map<String, Object>',

    -- 状态参数
    -- 防止CSRF攻击的状态值，授权码流程中使用
    -- 授权请求时传递，授权响应时返回验证
    state VARCHAR(500) DEFAULT NULL COMMENT '防CSRF状态参数',

    -- ==================== 授权码相关字段 ====================
    -- 授权码值（加密存储）
    -- 授权码流程中生成的授权码，敏感信息需加密
    authorization_code_value BLOB DEFAULT NULL COMMENT '授权码值（加密存储）',

    -- 授权码颁发时间
    -- 记录授权码生成的时间戳
    authorization_code_issued_at TIMESTAMP DEFAULT NULL COMMENT '授权码颁发时间',

    -- 授权码过期时间
    -- 授权码的有效期，通常较短（如5分钟）
    authorization_code_expires_at TIMESTAMP DEFAULT NULL COMMENT '授权码过期时间',

    -- 授权码元数据
    -- 授权码的额外元数据，如元数据、扩展信息
    authorization_code_metadata BLOB DEFAULT NULL COMMENT '授权码元数据',

    -- ==================== 访问令牌相关字段 ====================
    -- 访问令牌值（加密存储）
    -- 颁发的访问令牌，JWT格式或不透明令牌
    -- 敏感信息，需加密存储
    access_token_value BLOB DEFAULT NULL COMMENT '访问令牌值（加密存储）',

    -- 访问令牌颁发时间
    access_token_issued_at TIMESTAMP DEFAULT NULL COMMENT '访问令牌颁发时间',

    -- 访问令牌过期时间
    -- 根据客户端配置的访问令牌有效期
    access_token_expires_at TIMESTAMP DEFAULT NULL COMMENT '访问令牌过期时间',

    -- 访问令牌元数据
    -- 访问令牌的元数据，如token类型、签名算法等
    access_token_metadata BLOB DEFAULT NULL COMMENT '访问令牌元数据',

    -- 访问令牌类型
    -- OAuth2令牌类型，如: 'Bearer', 'MAC', 'DPoP'
    -- RFC 6750定义的Bearer令牌最常用
    access_token_type VARCHAR(100) DEFAULT NULL COMMENT '访问令牌类型（Bearer等）',

    -- 访问令牌范围
    -- 此访问令牌的实际权限范围
    -- 可能是authorized_scopes的子集
    access_token_scopes VARCHAR(1000) DEFAULT NULL COMMENT '访问令牌的实际权限范围',

    -- ==================== OIDC ID Token相关字段 ====================
    -- ID Token值（加密存储）
    -- OpenID Connect的ID Token，JWT格式
    oidc_id_token_value BLOB DEFAULT NULL COMMENT 'OIDC ID Token值（JWT，加密存储）',

    -- ID Token颁发时间
    oidc_id_token_issued_at TIMESTAMP DEFAULT NULL COMMENT 'ID Token颁发时间',

    -- ID Token过期时间
    -- 通常与访问令牌过期时间相同或独立配置
    oidc_id_token_expires_at TIMESTAMP DEFAULT NULL COMMENT 'ID Token过期时间',

    -- ID Token元数据
    -- ID Token的元数据，如claims、签名信息
    oidc_id_token_metadata BLOB DEFAULT NULL COMMENT 'ID Token元数据',

    -- ==================== 刷新令牌相关字段 ====================
    -- 刷新令牌值（加密存储）
    -- 用于获取新的访问令牌，长期有效
    refresh_token_value BLOB DEFAULT NULL COMMENT '刷新令牌值（加密存储）',

    -- 刷新令牌颁发时间
    refresh_token_issued_at TIMESTAMP DEFAULT NULL COMMENT '刷新令牌颁发时间',

    -- 刷新令牌过期时间
    -- 刷新令牌的有效期，通常较长（如30天）
    refresh_token_expires_at TIMESTAMP DEFAULT NULL COMMENT '刷新令牌过期时间',

    -- 刷新令牌元数据
    refresh_token_metadata BLOB DEFAULT NULL COMMENT '刷新令牌元数据',

    -- ==================== 设备码授权相关字段 ====================
    -- 用户码值（设备码流程）
    -- 设备授权流程中的用户码
    user_code_value BLOB DEFAULT NULL COMMENT '设备码流程-用户码值',

    -- 用户码颁发时间
    user_code_issued_at TIMESTAMP DEFAULT NULL COMMENT '用户码颁发时间',

    -- 用户码过期时间
    -- 用户码有效期，通常较短
    user_code_expires_at TIMESTAMP DEFAULT NULL COMMENT '用户码过期时间',

    -- 用户码元数据
    user_code_metadata BLOB DEFAULT NULL COMMENT '用户码元数据',

    -- 设备码值
    -- 设备授权流程中的设备码
    device_code_value BLOB DEFAULT NULL COMMENT '设备码流程-设备码值',

    -- 设备码颁发时间
    device_code_issued_at TIMESTAMP DEFAULT NULL COMMENT '设备码颁发时间',

    -- 设备码过期时间
    device_code_expires_at TIMESTAMP DEFAULT NULL COMMENT '设备码过期时间',

    -- 设备码元数据
    device_code_metadata BLOB DEFAULT NULL COMMENT '设备码元数据',

    -- 主键
    PRIMARY KEY (id) COMMENT '主键：授权记录ID'

) COMMENT = 'OAuth2授权核心表，存储授权生命周期中的所有令牌和状态';