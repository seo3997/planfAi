package com.whomade.planfAi.admin.code.mapper;

import com.whomade.planfAi.admin.code.dto.CodeDto;
import com.whomade.planfAi.admin.code.dto.CodeGroupDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CodeMapper {
    // Group Code
    List<CodeGroupDto> selectGroupCodeList(Map<String, Object> params);

    int selectGroupCodeCount(Map<String, Object> params);

    CodeGroupDto selectGroupCodeDetail(String groupId);

    int insertGroupCode(CodeGroupDto dto);

    int updateGroupCode(CodeGroupDto dto);

    int deleteGroupCode(String groupId);

    // Sub Code
    List<CodeDto> selectCodeList(String groupId);

    int insertCode(CodeDto dto);

    int updateCode(CodeDto dto);

    int deleteCode(@Param("groupId") String groupId, @Param("code") String code);

    int deleteCodesByGroupId(String groupId);
}
