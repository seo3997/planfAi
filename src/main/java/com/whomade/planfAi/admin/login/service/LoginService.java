package com.whomade.planfAi.admin.login.service;

import com.whomade.planfAi.admin.common.vo.UserInfoVo;
import com.whomade.planfAi.admin.login.mapper.LoginMapper;
import com.whomade.planfAi.common.vo.DataMap;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 사용자 인증 및 정보 조회
     */
    @Transactional
    public UserInfoVo authenticate(String userId, String rawPassword) {
        DataMap params = new DataMap();
        params.put("id", userId);

        UserInfoVo user = loginMapper.selectUserInfo(params);

        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            // 로그인 일시 업데이트
            loginMapper.updateUserLoginDt(user.getUserNo().toString());
            return user;
        }

        return null;
    }
}
