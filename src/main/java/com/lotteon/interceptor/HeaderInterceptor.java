package com.lotteon.interceptor;

import com.lotteon.entity.HeaderInfo;
import com.lotteon.service.HeaderInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    private final HeaderInfoService headerInfoService;

    public HeaderInterceptor(HeaderInfoService headerInfoService) {this.headerInfoService = headerInfoService;}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {

            HeaderInfo headerInfo = headerInfoService.getHeaderInfo();
            modelAndView.addObject("headerInfo", headerInfo);

            String imagePath = "/uploads/ConfigImg/headerLogo.jpg"; // 실제 이미지 경로로 변경하세요.
            modelAndView.addObject("logoImagePath", imagePath);

            String imagePath2 = "/uploads/ConfigImg/footerLogo.jpg"; // 실제 이미지 경로로 변경하세요.
            modelAndView.addObject("footerlogoImagePath", imagePath2);
        }
    }
}
