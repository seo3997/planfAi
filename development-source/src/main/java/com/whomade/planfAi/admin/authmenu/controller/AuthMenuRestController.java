package com.whomade.planfAi.admin.authmenu.controller;

import com.whomade.planfAi.admin.authmenu.service.AuthMenuService;
import com.whomade.planfAi.admin.authmenu.vo.AuthMenuVo;
import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import com.whomade.planfAi.common.vo.DataMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whomade.planfAi.common.util.RequestUtil;

@RestController
@RequestMapping("/api/admin/authmenu")
@RequiredArgsConstructor
public class AuthMenuRestController {

    private final AuthMenuService authMenuService;

    @GetMapping("/authors")
    public ResponseEntity<Map<String, Object>> authorList(jakarta.servlet.http.HttpServletRequest request) {
        DataMap searchMap = RequestUtil.getDataMap(request);

        Map<String, Object> result = new HashMap<>();
        result.put("list", authMenuService.getAuthorPageList(searchMap));
        result.put("totCnt", authMenuService.getAuthorTotCnt(searchMap));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/menus/{authorId}")
    public ResponseEntity<List<AuthMenuVo>> menuList(@PathVariable String authorId) {
        return ResponseEntity.ok(authMenuService.getAuthMenuList(authorId));
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestBody Map<String, Object> payload,
            jakarta.servlet.http.HttpSession session) {

        String authorId = (String) payload.get("authorId");
        @SuppressWarnings("unchecked")
        List<String> menuIds = (List<String>) payload.get("menuIds");

        UserInfoVo adminUser = (UserInfoVo) session.getAttribute("adminUser");
        Integer registerNo = null;
        if (adminUser != null) {
            registerNo = Math.toIntExact(adminUser.getUserNo());
        }

        try {
            authMenuService.saveAuthMenu(authorId, menuIds, registerNo);
            return ResponseEntity.ok("저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("저장 중 오류 발생");
        }
    }
}
