package com.whomade.planfAi.admin.sys.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CodeController {

    @GetMapping("/admin/code/selectPageListGroupCodeMgt.do")
    public String groupCodeList(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/code/groupCodeList";
    }

    @GetMapping("/admin/code/insertFormGroupCodeMgt.do")
    public String groupCodeForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/code/groupCodeForm";
    }

    @GetMapping("/admin/code/selectListCodeMgt.do")
    public String codeMgt(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/code/codeMgt";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }
}
