package com.whomade.planfAi.admin.authmenu.mapper;

import com.whomade.planfAi.admin.authmenu.vo.AuthMenuVo;
import com.whomade.planfAi.common.vo.DataMap;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AuthMenuMapper {
    List<DataMap> selectPageListAuthor(DataMap searchMap);

    int selectTotCntAuthor(DataMap searchMap);

    DataMap selectAuthorInfo(String authorId);

    List<AuthMenuVo> selectListAuthMenu(String authorId);

    int insertAuthMenu(AuthMenuVo authMenuVo);

    int deleteAuthMenu(String authorId);
}
