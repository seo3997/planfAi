package com.whomade.planfAi.admin.user.controller;

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
        return "forward:/admin/user/userList.html";
    }

    @GetMapping("/admin/user/insertFormUserMgt.do")
    public String userInsertForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/user/userForm.html";
    }

    @GetMapping("/admin/user/selectUserMgt.do")
    public String userDetail(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/user/userDetail.html";
    }

    @GetMapping("/admin/user/updateFormUserMgt.do")
    public String userUpdateForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/user/userUpdate.html";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }
}
