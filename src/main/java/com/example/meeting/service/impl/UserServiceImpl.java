package com.example.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.meeting.DTO.*;
import com.example.meeting.config.JwtUtil;
import com.example.meeting.entity.User;
import com.example.meeting.entity.UserWithoutPassword;
import com.example.meeting.mapper.DepartmentMapper;
import com.example.meeting.mapper.UserMapper;
import com.example.meeting.service.UserService;
import com.example.meeting.service.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;

    private static final String DEFAULT_PASSWORD = "123456";

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

        String name = request.getName();
        String workNum = request.getWorkNum();
        String phoneNum = request.getPhoneNum();
        Long departmentId = request.getDepartmentId();

        System.out.println("name: " + name + " workNum: " + workNum + " phoneNum: " + phoneNum);

        if (name == null || name.isEmpty() || workNum == null || workNum.isEmpty() || phoneNum == null || phoneNum.isEmpty() || departmentId == null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "请填写完整的用户信息"));
        }
        if (departmentMapper.selectById(departmentId) == null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "部门不存在"));
        }

        if (userMapper.selectOne(new QueryWrapper<User>().eq("work_num", workNum)) != null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "工号已存在"));
        }

        if (userMapper.selectOne(new QueryWrapper<User>().eq("phone_num", phoneNum)) != null) {
            return ResponseEntity.badRequest().body(new RegisterResult(false, "手机号已存在"));
        }

        User user = new User(null, username, passwordEncoder.encode(password), false, name, workNum, phoneNum, departmentId);
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

    @Override
     public ResponseEntity<VerifyTokenResult> verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(new VerifyTokenResult(false, null, null, null));
        }

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().body(new VerifyTokenResult(false, null, null, null));
        }

        Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(new VerifyTokenResult(false, null, null, null));
        }
         return ResponseEntity.ok(new VerifyTokenResult(true, userId, user.getUsername(), user.getIsAdmin()));
     }

    @Override
    public ResponseEntity<UserGetResult> getUsers() {
        List<User> users = userMapper.selectList(null);
        List<UserWithoutPassword> userWithoutPasswords = users.stream().map(user -> new UserWithoutPassword(user.getId(), user.getUsername(), user.getIsAdmin(), user.getName(), user.getWorkNum(), user.getPhoneNum(), user.getDepartmentId())).toList();
        return ResponseEntity.ok(new UserGetResult(true, "", userWithoutPasswords));
    }

    @Override
    public ResponseEntity<UserUpdateResult> updateUser(UserUpdateRequest request) {
        Long id = request.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "请选择用户"));
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "用户不存在"));
        }
        String name = request.getName();
        String workNum = request.getWorkNum();
        String phoneNum = request.getPhoneNum();
        Long departmentId = request.getDepartmentId();
        if (name == null || name.isEmpty() || workNum == null || workNum.isEmpty() || phoneNum == null || phoneNum.isEmpty() || departmentId == null) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "请填写完整的用户信息"));
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_num", workNum);
        User existingUser = userMapper.selectOne(queryWrapper);
        if (existingUser != null && !existingUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "工号已存在"));
        }

        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("phone_num", phoneNum);
        User existingUser2 = userMapper.selectOne(queryWrapper2);
        if (existingUser2 != null && !existingUser2.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "手机号已存在"));
        }

        if (departmentMapper.selectById(departmentId) == null) {
            return ResponseEntity.badRequest().body(new UserUpdateResult(false, "部门不存在"));
        }

        user.setName(name);
        user.setWorkNum(workNum);
        user.setPhoneNum(phoneNum);
        user.setDepartmentId(departmentId);
        userMapper.updateById(user);
        return ResponseEntity.ok(new UserUpdateResult(true, "更新用户成功"));
    }

    @Override
    public ResponseEntity<UserDeleteResult> deleteUser(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new UserDeleteResult(false, "请选择用户"));
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body(new UserDeleteResult(false, "用户不存在"));
        }
        userMapper.deleteById(id);
        return ResponseEntity.ok(new UserDeleteResult(true, "删除用户成功"));
    }

    @Override
    public ResponseEntity<UserImportResult> importUser(MultipartFile file) {
        List<User> users = ExcelUtils.parseExcel(file);
        if (users == null) {
            return ResponseEntity.badRequest().body(new UserImportResult(false, "导入失败"));
        }

        if (users.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserImportResult(false, "导入失败，表格无有效数据"));
        }

        int successCount = 0;
        int failCount = 0;
        List<String> errorMessages = new ArrayList<>();
        for (User user : users) {
            String name = user.getName();
            String workNum = user.getWorkNum();
            String phoneNum = user.getPhoneNum();
            Long departmentId = user.getDepartmentId();

            if (name == null || name.isEmpty() || workNum == null || workNum.isEmpty() || phoneNum == null || phoneNum.isEmpty() || departmentId == null) {
                failCount ++ ;
                errorMessages.add("第" + (successCount + failCount) + "行数据不完整");
                continue;
            }

            if (userMapper.exists(new QueryWrapper<User>().eq("work_num", workNum))) {
                failCount ++ ;
                errorMessages.add("第" + (successCount + failCount) + "行工号已存在");
                continue;
            }

            if (userMapper.exists(new QueryWrapper<User>().eq("phone_num", phoneNum))) {
                failCount ++ ;
                errorMessages.add("第" + (successCount + failCount) + "行手机号已存在");
                continue;
            }

            if (departmentMapper.selectById(departmentId) == null) {
                failCount ++ ;
                errorMessages.add("第" + (successCount + failCount) + "行部门不存在");
                continue;
            }

            String username = name;
            if (userMapper.exists(new QueryWrapper<User>().eq("username", username))) {
                username = username + "_" + UUID.randomUUID().toString().substring(0, 8);
            }
            user.setUsername(username);

            user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            userMapper.insert(user);
            successCount ++ ;
        }

        boolean success = successCount > 0;
        String message = "";
        if (successCount > 0) {
            message = "成功导入" + successCount + "条数据";
        }
        if (failCount > 0) {
            message += "，失败" + failCount + "条数据";
            if (!errorMessages.isEmpty()) {
                message += "，错误信息：" + String.join(";", errorMessages);
            }
        }
        return ResponseEntity.ok(new UserImportResult(success, message));
    }

    @Override
    public ResponseEntity<UserAddResult> addUser(UserAddRequest request) {
        String name = request.getName();
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "请填写用户名称"));
        }

        name = name.trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "用户名称不能为空"));
        }
        String workNum = request.getWorkNum();
        if (workNum == null || workNum.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "请填写工号"));
        }
        workNum = workNum.trim();
        if (workNum.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "工号不能为空"));
        }
        if (userMapper.exists(new QueryWrapper<User>().eq("work_num", workNum))) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "工号已存在"));
        }

        String phoneNum = request.getPhoneNum();
        if (phoneNum == null || phoneNum.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "请填写手机号"));
        }
        phoneNum = phoneNum.trim();
        if (phoneNum.isEmpty()) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "手机号不能为空"));
        }
        if (userMapper.exists(new QueryWrapper<User>().eq("phone_num", phoneNum))) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "手机号已存在"));
        }

        Long departmentId = request.getDepartmentId();
        if (departmentId == null) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "请选择部门"));
        }
        if (departmentMapper.selectById(departmentId) == null) {
            return ResponseEntity.badRequest().body(new UserAddResult(false, "部门不存在"));
        }

        String username = name;
        if (userMapper.exists(new QueryWrapper<User>().eq("username", username))) {
            username = username + "_" + UUID.randomUUID().toString().substring(0, 8);
        }

        User user = new User(null, username, passwordEncoder.encode(DEFAULT_PASSWORD), false, name, workNum, phoneNum, departmentId);
        userMapper.insert(user);
        return ResponseEntity.ok(new UserAddResult(true, "添加成功"));
    }
}
