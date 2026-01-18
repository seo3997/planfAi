package com.whomade.planfAi.profitReport.mapper;

import com.whomade.planfAi.profitReport.vo.ProfitVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProfitReportMapper {
    void insertReport(ProfitVo profitVo);

    List<ProfitVo> selectReportsByUserNo(Long userNo);

    // Simple user check for demo purposes (usually UserMapper lies separately)
    Long selectUserNoByEmail(String email);

    void insertUser(String email, String password, String userName);
}
