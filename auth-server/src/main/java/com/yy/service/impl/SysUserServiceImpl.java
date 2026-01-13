package com.yy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.domain.SysUser;
import com.yy.service.SysUserService;
import com.yy.mapper.SysUserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2026-01-09 11:25:29
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{
    private final PasswordEncoder passwordEncoder; // 依赖注入

    public SysUserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SysUser registerUser(String username, String rawPassword) {
        Optional<SysUser> sysUser = lambdaQuery().eq(SysUser::getUsername, username).oneOpt();
        if (sysUser.isPresent()) {
            throw new RuntimeException(username+"已存在");
        }
        // 2. 使用 PasswordEncoder 加密密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(encodedPassword); // 存储的是密文！
        save(user);
        return user;
    }
}




