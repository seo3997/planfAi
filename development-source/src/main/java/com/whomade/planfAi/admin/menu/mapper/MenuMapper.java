package com.whomade.planfAi.admin.menu.mapper;

import com.whomade.planfAi.admin.menu.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuVo> selectMenuList(MenuVo menuVo);

    MenuVo selectMenuDetail(String menuId);

    int insertMenu(MenuVo menuVo);

    int updateMenu(MenuVo menuVo);

    int deleteMenu(String menuId);

    // 상위 메뉴 정보 조회 (ID 결합 및 정렬 상속용)
    MenuVo selectParentMenuInfo(String parentMenuId);

    // 권한 기반 동적 메뉴 조회
    List<MenuVo> selectUserMenuList(java.util.Map<String, Object> params);

    void updateMenuSortOrder(MenuVo menuVo);

    void updateChildrenSortOrder(java.util.Map<String, Object> params);
}
