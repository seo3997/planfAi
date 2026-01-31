package com.whomade.planfAi.admin.code.mapper;

import com.whomade.planfAi.admin.code.dto.CodeDto;
import com.whomade.planfAi.admin.code.dto.CodeGroupDto;
import com.whomade.planfAi.admin.code.dto.SclasCodeDto;
import com.whomade.planfAi.admin.code.dto.SdclasCodeDto;
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

    // Small Classification (Sclas)
    List<SclasCodeDto> selectSclasCodeList(@Param("groupId") String groupId, @Param("code") String code);

    int insertSclasCode(SclasCodeDto dto);

    int updateSclasCode(SclasCodeDto dto);

    int deleteSclasCode(@Param("groupId") String groupId, @Param("code") String code,
            @Param("sclasCode") String sclasCode);

    // Sub Classification (Sdclas)
    List<SdclasCodeDto> selectSdclasCodeList(@Param("groupId") String groupId, @Param("code") String code,
            @Param("sclasCode") String sclasCode);

    int insertSdclasCode(SdclasCodeDto dto);

    int updateSdclasCode(SdclasCodeDto dto);

    int deleteSdclasCode(@Param("groupId") String groupId, @Param("code") String code,
            @Param("sclasCode") String sclasCode, @Param("sdclasCode") String sdclasCode);
}
