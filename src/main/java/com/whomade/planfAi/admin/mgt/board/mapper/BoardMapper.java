package com.whomade.planfAi.admin.mgt.board.mapper;

import com.whomade.planfAi.common.vo.DataMap;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BoardMapper {
    int selectTotCntBoard(DataMap param);

    List<DataMap> selectPageListBoard(DataMap param);

    List<DataMap> selectListBoard(DataMap param);

    DataMap selectBoard(DataMap param);

    int insertBoard(DataMap param);

    int updateBoard(DataMap param);

    int deleteBoard(DataMap param);

    int insertFile(Object param);

    List<DataMap> selectFileList(DataMap param);

    DataMap selectFile(DataMap param);

    int deleteFile(DataMap param);
}
