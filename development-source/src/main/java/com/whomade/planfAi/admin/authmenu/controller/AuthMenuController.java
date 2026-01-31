package com.whomade.planfAi.admin.authmenu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthMenuController {

    /**
     * 권한별 메뉴관리 - 권한 목록 조회
     */
    @GetMapping("/admin/authmenu/selectPageListAuthMenuMgt.do")
    public String selectPageListAuthMenuMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "forward:/admin/authmenu/authAuthorList.html";
    }

    /**
     * 권한별 메뉴관리 - 권한별 메뉴 트리 매핑
     */
    @GetMapping("/admin/authmenu/selectListAuthMenuMgt.do")
    public String selectListAuthMenuMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "forward:/admin/authmenu/authMenuMapping.html";
    }
}
