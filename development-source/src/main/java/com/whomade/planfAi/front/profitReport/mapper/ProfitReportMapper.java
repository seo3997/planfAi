package com.whomade.planfAi.front.profitReport.mapper;

import com.whomade.planfAi.front.profitReport.vo.ProfitVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProfitReportMapper {
    void insertReport(ProfitVo profitVo);

    void updateReport(ProfitVo profitVo);

    List<ProfitVo> selectReportsByUserNo(Long userNo);

    void deleteReport(Long reportId);

    ProfitVo selectReportById(Long id);

    // Simple user check for demo purposes (usually UserMapper lies separately)
    Long selectUserNoByEmail(String email);

    void insertUser(String email, String password, String userName);
}
