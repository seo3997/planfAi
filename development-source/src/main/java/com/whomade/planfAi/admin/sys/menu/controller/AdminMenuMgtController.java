package com.whomade.planfAi.admin.sys.menu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminMenuMgtController {

    /**
     * 메뉴 관리 화면 이동
     */
    @GetMapping("/admin/menu/selectListMenuMgt.do")
    public String selectListMenuMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "admin/sys/menu/menuMgt";
    }
}
