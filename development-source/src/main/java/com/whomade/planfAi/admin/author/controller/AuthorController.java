package com.whomade.planfAi.admin.author.controller;

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
        return "forward:/admin/author/authorMgt.html";
    }
}
