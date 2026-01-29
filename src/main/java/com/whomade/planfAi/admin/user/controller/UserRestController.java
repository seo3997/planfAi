package com.whomade.planfAi.admin.user.controller;

import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import com.whomade.planfAi.admin.user.dto.UserDto;
import com.whomade.planfAi.admin.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getUserList(UserDto searchDto) {
        Map<String, Object> result = userService.selectUserPageList(searchDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userNo}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long userNo) {
        UserDto user = userService.selectUserDetail(userNo);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto, HttpSession session) {
        UserInfoVo admin = (UserInfoVo) session.getAttribute("adminUser");
        if (admin != null) {
            userDto.setRegisterNo(admin.getUserNo().intValue());
        }
        userService.insertUser(userDto);
        return ResponseEntity.ok("등록되었습니다.");
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, HttpSession session) {
        UserInfoVo admin = (UserInfoVo) session.getAttribute("adminUser");
        if (admin != null) {
            userDto.setUpdusrNo(admin.getUserNo().intValue());
        }
        userService.updateUser(userDto);
        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/{userNo}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userNo) {
        userService.deleteUser(userNo);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @GetMapping("/authors")
    public ResponseEntity<?> getAuthors() {
        List<Map<String, Object>> authors = userService.selectAuthorList();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/status-codes")
    public ResponseEntity<?> getStatusCodes() {
        List<Map<String, Object>> codes = userService.selectCommonCodes("R010050");
        return ResponseEntity.ok(codes);
    }
}
