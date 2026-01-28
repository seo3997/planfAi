package com.whomade.planfAi.admin.menu.service;

import com.whomade.planfAi.admin.menu.mapper.MenuMapper;
import com.whomade.planfAi.admin.menu.vo.MenuVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuMapper menuMapper;

    public List<MenuVo> getMenuList(MenuVo menuVo) {
        return menuMapper.selectMenuList(menuVo);
    }

    public MenuVo getMenuDetail(String menuId) {
        return menuMapper.selectMenuDetail(menuId);
    }

    @Transactional
    public void saveMenu(MenuVo menuVo) {
        if (menuVo.getParentMenuId() == null || menuVo.getParentMenuId().isEmpty()) {
            throw new IllegalArgumentException("하위 메뉴만 등록 가능합니다. 부모 메뉴를 선택해 주세요.");
        }

        // 1. 메뉴 ID 생성 및 레벨 설정
        String subId = menuVo.getSubMenuId(); // 사용자가 입력한 4자리

        if (subId == null || subId.length() != 4 || !subId.matches("\\d{4}")) {
            throw new IllegalArgumentException("메뉴 ID 하위 4자리는 반드시 숫자 4자리여야 합니다.");
        }

        // 전체 MENU_ID 생성 (부모ID + 입력한 4자리)
        String fullMenuId = menuVo.getParentMenuId() + subId;
        menuVo.setMenuId(fullMenuId);
        menuVo.setMenuLevel(fullMenuId.length() / 4);

        // 2. 정렬 로직 (Denormalized Sort)
        MenuVo parent = menuMapper.selectParentMenuInfo(menuVo.getParentMenuId());
        if (parent != null) {
            // 부모의 정렬 순서 상속
            menuVo.setSortOrdr1(parent.getSortOrdr1());
            menuVo.setSortOrdr2(parent.getSortOrdr2());
            menuVo.setSortOrdr3(parent.getSortOrdr3());
            menuVo.setSortOrdr4(parent.getSortOrdr4());
            menuVo.setSortOrdr5(parent.getSortOrdr5());
            menuVo.setSortOrdr6(parent.getSortOrdr6());
        }

        // 기본 타입 설정 (메뉴: 20)
        if (menuVo.getMenuTyCode() == null || menuVo.getMenuTyCode().isEmpty()) {
            menuVo.setMenuTyCode("20");
        }

        // 현재 레벨에 자신의 정렬 순번 저장
        int level = menuVo.getMenuLevel();
        int sort = menuVo.getSortOrdr();
        switch (level) {
            case 1:
                menuVo.setSortOrdr1(sort);
                break;
            case 2:
                menuVo.setSortOrdr2(sort);
                break;
            case 3:
                menuVo.setSortOrdr3(sort);
                break;
            case 4:
                menuVo.setSortOrdr4(sort);
                break;
            case 5:
                menuVo.setSortOrdr5(sort);
                break;
            case 6:
                menuVo.setSortOrdr6(sort);
                break;
        }

        // 3. 중복 체크 후 저장
        if (menuMapper.selectMenuDetail(fullMenuId) != null) {
            menuMapper.updateMenu(menuVo);
        } else {
            menuMapper.insertMenu(menuVo);
        }
    }

    @Transactional
    public void deleteMenu(String menuId) {
        menuMapper.deleteMenu(menuId);
    }
}
