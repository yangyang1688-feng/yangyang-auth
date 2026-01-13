package com.yy.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yy.domain.SysUser;
import com.yy.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 使用Lombok自动注入依赖
public class CustomUserDetailsService implements UserDetailsService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper, false);
        if (user == null) {
            throw new UsernameNotFoundException("用户未找到: " + username);
        }

        // 3. 返回UserDetails对象（这里使用Spring Security提供的UserBuilder简化构建）
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 数据库中的密码应该是加密后的
                .roles("user")
                .build();
    }
}