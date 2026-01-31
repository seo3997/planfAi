package com.whomade.planfAi.admin.author.controller;

import com.whomade.planfAi.admin.author.service.AuthorService;
import com.whomade.planfAi.admin.author.vo.AuthorVo;
import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.whomade.planfAi.common.util.RequestUtil;
import com.whomade.planfAi.common.vo.DataMap;

@RestController
@RequestMapping("/api/admin/author")
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(jakarta.servlet.http.HttpServletRequest request) {
        DataMap paramMap = RequestUtil.getDataMap(request);
        AuthorVo authorVo = new AuthorVo();
        authorVo.setSchAuthNm(paramMap.getString("schAuthNm"));

        Map<String, Object> result = new HashMap<>();
        result.put("list", authorService.getPageListAuthor(authorVo));
        result.put("totCnt", authorService.getTotCntAuthor(authorVo));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorVo> detail(@PathVariable String authorId) {
        return ResponseEntity.ok(authorService.getAuthorDetail(authorId));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody AuthorVo authorVo, jakarta.servlet.http.HttpSession session) {
        UserInfoVo adminUser = (UserInfoVo) session.getAttribute("adminUser");
        if (adminUser != null) {
            authorVo.setRegisterNo(Math.toIntExact(adminUser.getUserNo()));
        }

        try {
            authorService.saveAuthor(authorVo);
            return ResponseEntity.ok("권한이 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("권한 저장 중 오류가 발생했습니다.");
        }
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody AuthorVo authorVo, jakarta.servlet.http.HttpSession session) {
        UserInfoVo adminUser = (UserInfoVo) session.getAttribute("adminUser");
        if (adminUser != null) {
            authorVo.setUpdusrNo(Math.toIntExact(adminUser.getUserNo()));
        }

        try {
            authorService.updateAuthor(authorVo);
            return ResponseEntity.ok("권한이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("권한 수정 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<String> delete(@PathVariable String authorId) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok("권한이 성공적으로 삭제되었습니다.");
    }
}
