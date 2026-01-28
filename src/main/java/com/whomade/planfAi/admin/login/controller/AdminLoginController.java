package com.whomade.planfAi.admin.login.controller;

import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import com.whomade.planfAi.admin.login.service.LoginService;
import com.whomade.planfAi.common.vo.DataMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {

    private final LoginService loginService;

    /**
     * 관리자 로그인 화면 이동
     */
    @GetMapping("/admin/login.do")
    public String adminLogin() {
        return "forward:/admin/login.html";
    }

    /**
     * 관리자 로그인 처리 (JSON API)
     */
    @PostMapping("/api/admin/login")
    @ResponseBody
    public ResponseEntity<String> loginProcess(@RequestBody DataMap loginData, HttpServletRequest request) {
        String userId = (String) loginData.get("userId");
        String password = (String) loginData.get("password");

        try {
            UserInfoVo user = loginService.authenticate(userId, password);

            // 세션에 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("adminUser", user);
            return ResponseEntity.ok("success");

        } catch (IllegalArgumentException e) {
            // "존재하지 않는 사용자", "비밀번호 불일치" 등 상세 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    /**
     * 관리자 로그아웃
     */
    @GetMapping("/admin/logout.do")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/admin/login.do";
    }

    /**
     * 관리자 메인(대시보드) 화면 이동
     */
    @GetMapping("/admin/main.do")
    public String adminMain(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "forward:/admin/main.html";
    }
}
