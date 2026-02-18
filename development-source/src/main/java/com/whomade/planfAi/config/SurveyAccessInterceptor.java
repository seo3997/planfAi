package com.whomade.planfAi.config;

import com.whomade.planfAi.admin.mgt.survey.service.SurveyService;
import com.whomade.planfAi.admin.mgt.survey.vo.TbSurvey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SurveyAccessInterceptor implements HandlerInterceptor {

    private final SurveyService surveyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        // /survey/v/{surveyId} 패턴 추출
        if (uri.startsWith("/survey/v/")) {
            String surveyId = uri.substring("/survey/v/".length());

            TbSurvey searchVO = new TbSurvey();
            searchVO.setSurveyId(surveyId);
            TbSurvey survey = surveyService.selectSurveyDetail(searchVO);

            if (survey == null || !"OPENED".equals(survey.getOpened())) {
                // 수집중이 아니면 안내 페이지로 리다이렉트
                response.sendRedirect("/survey/error/not_available");
                return false;
            }
        }

        return true;
    }
}
