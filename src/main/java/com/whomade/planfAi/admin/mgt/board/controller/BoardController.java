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
        return "forward:/admin/mgt/mboard/boardList.html";
    }

    @GetMapping("/insertFormBoard.do")
    public String boardInsertForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/mgt/mboard/boardForm.html";
    }

    @GetMapping("/selectBoard.do")
    public String boardDetail(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/mgt/mboard/boardDetail.html";
    }

    @GetMapping("/updateFormBoard.do")
    public String boardUpdateForm(HttpServletRequest request) {
        if (!isAuthenticated(request))
            return "redirect:/admin/login.do";
        return "forward:/admin/mgt/mboard/boardUpdate.html";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("adminUser") != null;
    }
}
