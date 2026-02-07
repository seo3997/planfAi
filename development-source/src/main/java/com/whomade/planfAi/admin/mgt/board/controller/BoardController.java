package com.whomade.planfAi.admin.mgt.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/mgt/mboard")
public class BoardController {

    @GetMapping("/selectPageListBoard.do")
    public String boardList(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/mgt/mboard/boardList";
    }

    @GetMapping("/insertFormBoard.do")
    public String boardInsertForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/mgt/mboard/boardForm";
    }

    @GetMapping("/selectBoard.do")
    public String boardDetail(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/mgt/mboard/boardDetail";
    }

    @GetMapping("/updateFormBoard.do")
    public String boardUpdateForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "admin/mgt/mboard/boardUpdate";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }
}
