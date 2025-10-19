package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.LoginRequest;
import com.example.meeting.DTO.LoginResult;
import com.example.meeting.DTO.RegisterRequest;
import com.example.meeting.DTO.RegisterResult;
import com.example.meeting.entity.User;
import com.example.meeting.mapper.UserMapper;
import com.example.meeting.config.JwtUtil;
import com.example.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseEntity<RegisterResult> register(RegisterRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "请填写完整的注册信息"));
        }

        String username = request.getUsername();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        if (
            username == null || username.isEmpty() || password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()
        ) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "请填写完整的注册信息"));
        }

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "两次输入的密码不一致"));
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "用户名已存在"));
        }

        User user = new User(null, username, passwordEncoder.encode(password));
        userMapper.insert(user);
        return ResponseEntity.ok(new RegisterResult(true, "注册成功"));
    }

    @Override
    public ResponseEntity<LoginResult> login(LoginRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(new LoginResult(false, "请填写完整的登录信息", null));
        }

        String username = request.getUsername();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            return ResponseEntity.badRequest().body(new LoginResult(false, "用户名不存在", null));
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new LoginResult(false, "密码错误", null));
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId().toString());
        return ResponseEntity.ok(new LoginResult(true, "登录成功", token));
    }
}
