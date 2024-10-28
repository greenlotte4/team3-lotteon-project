package com.lotteon.interceptor;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.entity.FooterInfo;
import com.lotteon.service.FooterInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class FooterInterceptor implements HandlerInterceptor {

   private final FooterInfoService footerInfoService;

    public FooterInterceptor(FooterInfoService footerInfoService) {
        this.footerInfoService = footerInfoService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // footerInfo 객체를 모델에 추가합니다.
            FooterInfoDTO footerInfo = footerInfoService.getFooterInfo();
            modelAndView.addObject("footerInfo", footerInfo);
        }
    }
}
