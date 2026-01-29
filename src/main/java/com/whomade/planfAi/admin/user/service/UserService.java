package com.whomade.planfAi.admin.user.service;

import com.whomade.planfAi.admin.user.dto.UserDto;
import com.whomade.planfAi.admin.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> selectUserPageList(UserDto searchDto) {
        Map<String, Object> result = new HashMap<>();
        List<UserDto> list = userMapper.selectUserList(searchDto);
        int totalCount = userMapper.selectUserListCount(searchDto);

        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    public UserDto selectUserDetail(Long userNo) {
        return userMapper.selectUserDetail(userNo);
    }

    @Transactional
    public void insertUser(UserDto userDto) {
        // Encode password
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userMapper.insertUser(userDto);
    }

    @Transactional
    public void updateUser(UserDto userDto) {
        // Only encode password if it's being changed
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userMapper.updateUser(userDto);
    }

    @Transactional
    public void deleteUser(Long userNo) {
        userMapper.deleteUser(userNo);
    }

    public List<Map<String, Object>> selectAuthorList() {
        return userMapper.selectAuthorList();
    }

    public List<Map<String, Object>> selectCommonCodes(String groupId) {
        return userMapper.selectCommonCodes(groupId);
    }
}
