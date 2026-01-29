package com.whomade.planfAi.admin.user.mapper;

import com.whomade.planfAi.admin.user.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<UserDto> selectUserList(UserDto searchDto);

    int selectUserListCount(UserDto searchDto);

    UserDto selectUserDetail(Long userNo);

    void insertUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void deleteUser(Long userNo);

    List<Map<String, Object>> selectAuthorList();

    List<Map<String, Object>> selectCommonCodes(String groupId);
}
