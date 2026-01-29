package com.whomade.planfAi.admin.mgt.board.controller;

import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import com.whomade.planfAi.admin.mgt.board.service.BoardService;
import com.whomade.planfAi.common.util.RequestUtil;
import com.whomade.planfAi.common.vo.DataMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/mgt/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(HttpServletRequest request) {
        DataMap paramMap = RequestUtil.getDataMap(request);

        // Paging logic
        int currentPage = paramMap.getInt("page") > 0 ? paramMap.getInt("page") : 1;
        int pageSize = paramMap.getInt("size") > 0 ? paramMap.getInt("size") : 10;
        paramMap.put("limitStart", (currentPage - 1) * pageSize);
        paramMap.put("limitEnd", pageSize);

        // Default category code if needed (spec R010170)
        paramMap.put("bbs_se_code_l", "R010170");

        Map<String, Object> result = new HashMap<>();
        result.put("list", boardService.selectPageListBoard(paramMap));
        result.put("totalCount", boardService.selectTotCntBoard(paramMap));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{bbsSeq}")
    public ResponseEntity<DataMap> detail(@PathVariable Long bbsSeq) {
        DataMap param = new DataMap();
        param.put("bbs_seq", bbsSeq);
        return ResponseEntity.ok(boardService.selectBoard(param));
    }

    @PostMapping
    public ResponseEntity<String> save(MultipartHttpServletRequest request, HttpSession session) {
        UserInfoVo adminUser = (UserInfoVo) session.getAttribute("adminUser");
        if (adminUser == null)
            return ResponseEntity.status(401).body("Unauthorized");

        DataMap paramMap = RequestUtil.getDataMap(request);
        paramMap.put("ss_user_no", adminUser.getUserNo());
        paramMap.put("bbs_se_code_l", "R010170");

        List<MultipartFile> files = request.getFiles("files");

        try {
            boardService.insertBoard(paramMap, files);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{bbsSeq}")
    public ResponseEntity<String> update(@PathVariable Long bbsSeq, MultipartHttpServletRequest request,
            HttpSession session) {
        UserInfoVo adminUser = (UserInfoVo) session.getAttribute("adminUser");
        if (adminUser == null)
            return ResponseEntity.status(401).body("Unauthorized");

        DataMap paramMap = RequestUtil.getDataMap(request);
        paramMap.put("bbs_seq", bbsSeq);
        paramMap.put("ss_user_no", adminUser.getUserNo());

        List<MultipartFile> files = request.getFiles("files");

        try {
            boardService.updateBoard(paramMap, files);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{bbsSeq}")
    public ResponseEntity<String> delete(@PathVariable Long bbsSeq) {
        DataMap param = new DataMap();
        param.put("bbs_seq", bbsSeq);
        boardService.deleteBoard(param);
        return ResponseEntity.ok("success");
    }
}
