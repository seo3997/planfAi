package com.whomade.planfAi.admin.sys.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/admin/user/selectPageListUserMgt.do")
    public String userList(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/user/userList";
    }

    @GetMapping("/admin/user/insertFormUserMgt.do")
    public String userInsertForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/user/userForm";
    }

    @GetMapping("/admin/user/selectUserMgt.do")
    public String userDetail(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/user/userDetail";
    }

    @GetMapping("/admin/user/updateFormUserMgt.do")
    public String userUpdateForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/sys/user/userUpdate";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }
}
