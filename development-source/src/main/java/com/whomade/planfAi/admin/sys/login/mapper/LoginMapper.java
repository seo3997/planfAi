package com.whomade.planfAi.admin.sys.login.mapper;

import com.whomade.planfAi.admin.sys.common.vo.UserInfoVo;
import com.whomade.planfAi.common.vo.DataMap;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    UserInfoVo selectUserInfo(DataMap params);

    void updateUserLoginDt(String userNo);
}
