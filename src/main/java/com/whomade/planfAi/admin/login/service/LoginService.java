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

        // 1. 아이디로 사용자 조회
        UserInfoVo user = loginMapper.selectUserInfo(params);

        // 2. 사용자가 없는 경우
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 3. 비밀번호 비교 (DB의 암호화된 해시와 입력값을 비교)
        String dbHash = user.getPassword().trim();

        System.out.println("=== Authentication Debug ===");
        System.out.println("Raw Password: [" + rawPassword + "]");
        System.out.println("DB Hash:      [" + dbHash + "]");
        System.out.println("New Hash of '1234': [" + passwordEncoder.encode("1234") + "]");
        System.out.println("Match? : " + passwordEncoder.matches(rawPassword, dbHash));
        System.out.println("============================");

        if (!passwordEncoder.matches(rawPassword, dbHash)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 4. 인증 성공: 로그인 일시 업데이트 및 정보 반환
        loginMapper.updateUserLoginDt(user.getUserNo().toString());
        return user;
    }
}
