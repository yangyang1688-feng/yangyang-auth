package com.yy.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 * mybatis自动设置时间
 * @ClassName: AutoFillHandler
 * @author: yangfeng
 * @date: 2026/1/9 11:13
 * @version: 1.0.0
 */
@Component
@Slf4j
public class AutoFillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        this.setFieldValByName("createAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);

//        this.strictInsertFill(metaObject, "createAt1", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "updateAt1", LocalDateTime.class, LocalDateTime.now());
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        this.setFieldValByName("updateAt", LocalDateTime.now(), metaObject);
    }
}