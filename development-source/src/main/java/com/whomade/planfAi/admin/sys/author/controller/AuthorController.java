package com.whomade.planfAi.admin.sys.author.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {

    @GetMapping("/admin/author/selectPageListAuthorMgt.do")
    public String selectPageListAuthorMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "admin/sys/author/authorMgt";
    }

    @GetMapping("/admin/author/insertFormAuthorMgt.do")
    public String insertFormAuthorMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "admin/sys/author/authorForm";
    }

    @GetMapping("/admin/author/updateFormAuthorMgt.do")
    public String updateFormAuthorMgt(jakarta.servlet.http.HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login.do";
        }
        return "admin/sys/author/authorForm";
    }
}
