package com.whomade.planfAi.admin.authmenu.service;

import com.whomade.planfAi.admin.authmenu.mapper.AuthMenuMapper;
import com.whomade.planfAi.admin.authmenu.vo.AuthMenuVo;
import com.whomade.planfAi.common.vo.DataMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthMenuServiceImpl implements AuthMenuService {

    private final AuthMenuMapper authMenuMapper;

    @Override
    public List<DataMap> getAuthorPageList(DataMap searchMap) {
        return authMenuMapper.selectPageListAuthor(searchMap);
    }

    @Override
    public int getAuthorTotCnt(DataMap searchMap) {
        return authMenuMapper.selectTotCntAuthor(searchMap);
    }

    @Override
    public DataMap getAuthorInfo(String authorId) {
        return authMenuMapper.selectAuthorInfo(authorId);
    }

    @Override
    public List<AuthMenuVo> getAuthMenuList(String authorId) {
        return authMenuMapper.selectListAuthMenu(authorId);
    }

    @Override
    @Transactional
    public void saveAuthMenu(String authorId, List<String> menuIds, Integer registerNo) {
        // 1. Delete existing mapping
        authMenuMapper.deleteAuthMenu(authorId);

        // 2. Insert new mapping
        if (menuIds != null && !menuIds.isEmpty()) {
            for (String menuId : menuIds) {
                AuthMenuVo vo = new AuthMenuVo();
                vo.setAuthorId(authorId);
                vo.setMenuId(menuId);
                vo.setRegisterNo(registerNo);
                authMenuMapper.insertAuthMenu(vo);
            }
        }
    }
}
