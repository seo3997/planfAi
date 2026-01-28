package com.whomade.planfAi.admin.authmenu.service;

import com.whomade.planfAi.admin.authmenu.vo.AuthMenuVo;
import com.whomade.planfAi.common.vo.DataMap;
import java.util.List;

public interface AuthMenuService {
    List<DataMap> getAuthorPageList(DataMap searchMap);

    int getAuthorTotCnt(DataMap searchMap);

    DataMap getAuthorInfo(String authorId);

    List<AuthMenuVo> getAuthMenuList(String authorId);

    void saveAuthMenu(String authorId, List<String> menuIds, Integer registerNo);
}
