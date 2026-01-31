package com.whomade.planfAi.admin.menu.controller;

import com.whomade.planfAi.admin.menu.service.MenuService;
import com.whomade.planfAi.admin.menu.vo.MenuVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuVo>> list(MenuVo menuVo) {
        return ResponseEntity.ok(menuService.getMenuList(menuVo));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuVo> detail(@PathVariable String menuId) {
        return ResponseEntity.ok(menuService.getMenuDetail(menuId));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody MenuVo menuVo) {
        try {
            menuService.saveMenu(menuVo);
            return ResponseEntity.ok("메뉴가 성공적으로 저장되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("메뉴 저장 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<String> delete(@PathVariable String menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok("메뉴가 성공적으로 삭제되었습니다.");
    }

    @GetMapping("/user-menus")
    public ResponseEntity<java.util.List<MenuVo>> getUserMenus(
            @RequestParam(required = false) Integer menuLevel,
            @RequestParam(required = false) String parentMenuId,
            jakarta.servlet.http.HttpSession session) {

        com.whomade.planfAi.admin.common.vo.UserInfoVo user = (com.whomade.planfAi.admin.common.vo.UserInfoVo) session
                .getAttribute("adminUser");
        if (user == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(menuService.getUserMenuList(user.getAuthorId(), menuLevel, parentMenuId));
    }

    @PostMapping("/order")
    public ResponseEntity<String> saveOrder(@RequestBody List<MenuVo> menuList) {
        try {
            menuService.saveMenuOrder(menuList);
            return ResponseEntity.ok("메뉴 순서가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("메뉴 순서 저장 중 오류가 발생했습니다.");
        }
    }
}
