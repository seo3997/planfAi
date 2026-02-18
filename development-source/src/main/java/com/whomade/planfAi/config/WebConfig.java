package com.whomade.planfAi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.board.resource-path}")
    private String boardResourcePath;

    @Value("${file.survey.resource-path}")
    private String surveyResourcePath;

    private final SurveyAccessInterceptor surveyAccessInterceptor;

    public WebConfig(SurveyAccessInterceptor surveyAccessInterceptor) {
        this.surveyAccessInterceptor = surveyAccessInterceptor;
    }

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        // board image/file serving
        registry.addResourceHandler("/common/img/board/**")
                .addResourceLocations(boardResourcePath);

        // survey image serving
        registry.addResourceHandler("/common/img/survey/**")
                .addResourceLocations(surveyResourcePath);
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(surveyAccessInterceptor)
                .addPathPatterns("/survey/v/**");
    }
}
