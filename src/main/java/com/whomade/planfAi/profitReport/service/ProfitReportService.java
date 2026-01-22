package com.whomade.planfAi.profitReport.service;

import com.whomade.planfAi.profitReport.mapper.ProfitReportMapper;
import com.whomade.planfAi.profitReport.vo.ProfitVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfitReportService {

    private final ProfitReportMapper profitReportMapper;

    public ProfitReportService(ProfitReportMapper profitReportMapper) {
        this.profitReportMapper = profitReportMapper;
    }

    /**
     * Finds or creates a user and returns their userNo.
     * This is a simplified logic for the demo (no real auth).
     */
    @Transactional
    public Long findOrCreateUser(String email) {
        Long userNo = profitReportMapper.selectUserNoByEmail(email);
        if (userNo == null) {
            // Create dummy user
            profitReportMapper.insertUser(email, "dummy_password", email.split("@")[0]);
            userNo = profitReportMapper.selectUserNoByEmail(email);
        }
        return userNo;
    }

    @Transactional
    public void saveReport(Long userNo, String reportDataJson, Long reportId) {
        ProfitVo vo = new ProfitVo();
        vo.setUserNo(userNo);
        vo.setReportData(reportDataJson);

        if (reportId != null && reportId > 0) {
            vo.setId(reportId);
            profitReportMapper.updateReport(vo);
        } else {
            profitReportMapper.insertReport(vo);
        }
    }

    public List<ProfitVo> getReports(Long userNo) {
        return profitReportMapper.selectReportsByUserNo(userNo);
    }

    @Transactional
    public void deleteReport(Long reportId) {
        profitReportMapper.deleteReport(reportId);
    }
}
