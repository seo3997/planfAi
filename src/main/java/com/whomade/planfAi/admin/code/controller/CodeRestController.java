package com.whomade.planfAi.admin.code.controller;

import com.whomade.planfAi.admin.code.dto.CodeDto;
import com.whomade.planfAi.admin.code.dto.CodeGroupDto;
import com.whomade.planfAi.admin.code.service.CodeService;
import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/code")
@RequiredArgsConstructor
public class CodeRestController {

    private final CodeService codeService;

    @GetMapping("/groups")
    public ResponseEntity<?> getGroups(
            @RequestParam(required = false) String schGroupNm,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> result = codeService.getGroupCodeList(schGroupNm, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable String groupId) {
        CodeGroupDto result = codeService.getGroupCodeDetail(groupId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody CodeGroupDto dto, HttpSession session) {
        UserInfoVo user = (UserInfoVo) session.getAttribute("adminUser");
        dto.setRegisterNo(user.getUserNo());
        codeService.saveGroupCode(dto, false);
        return ResponseEntity.ok("등록되었습니다.");
    }

    @PutMapping("/groups")
    public ResponseEntity<?> updateGroup(@RequestBody CodeGroupDto dto, HttpSession session) {
        UserInfoVo user = (UserInfoVo) session.getAttribute("adminUser");
        dto.setUpdusrNo(user.getUserNo());
        codeService.saveGroupCode(dto, true);
        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String groupId) {
        codeService.deleteGroupCode(groupId);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @GetMapping("/sub/{groupId}")
    public ResponseEntity<?> getCodes(@PathVariable String groupId) {
        List<CodeDto> result = codeService.getCodeList(groupId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sub")
    public ResponseEntity<?> createCode(@RequestBody CodeDto dto, HttpSession session) {
        UserInfoVo user = (UserInfoVo) session.getAttribute("adminUser");
        dto.setRegisterNo(user.getUserNo());
        codeService.insertCode(dto);
        return ResponseEntity.ok("등록되었습니다.");
    }

    @PutMapping("/sub")
    public ResponseEntity<?> updateCode(@RequestBody CodeDto dto, HttpSession session) {
        UserInfoVo user = (UserInfoVo) session.getAttribute("adminUser");
        dto.setUpdusrNo(user.getUserNo());
        codeService.updateCode(dto);
        return ResponseEntity.ok("수정되었습니다.");
    }

    @DeleteMapping("/sub/{groupId}/{code}")
    public ResponseEntity<?> deleteCode(@PathVariable String groupId, @PathVariable String code) {
        codeService.deleteCode(groupId, code);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
