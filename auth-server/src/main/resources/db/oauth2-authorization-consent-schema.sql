CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
    -- 客户端注册ID
    -- 关联 oauth2_registered_client 表的 id 字段
    -- 表示哪个客户端应用
    registered_client_id VARCHAR(100) NOT NULL COMMENT '客户端注册ID，关联oauth2_registered_client.id',

    -- 主体名称（用户标识）
    -- 通常是用户的唯一标识符，如用户名、用户ID、邮箱等
    -- 表示哪个用户给予了授权同意
    principal_name VARCHAR(200) NOT NULL COMMENT '主体名称（用户标识），如用户名、邮箱等',

    -- 授权权限列表
    -- 存储用户同意授予客户端的权限/范围（scopes）
    -- 格式通常为逗号分隔的权限字符串，如："read,write,profile"
    -- 或JSON数组格式：["read", "write", "profile"]
    -- Spring Authorization Server 1.5.1 默认使用逗号分隔
    authorities VARCHAR(1000) NOT NULL COMMENT '授权权限列表，逗号分隔的权限/范围字符串',

    -- 主键：客户端ID + 用户标识
    -- 确保同一个用户对同一个客户端只有一条授权同意记录
    -- 复合主键确保数据唯一性
    PRIMARY KEY (registered_client_id, principal_name) COMMENT '主键：(客户端ID, 用户标识)'

) COMMENT = 'OAuth2 授权同意记录表，记录用户对客户端的授权同意信息';