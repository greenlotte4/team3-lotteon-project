package com.lotteon.interceptor;

import com.lotteon.service.VisitorCountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class VisitorInterceptor implements HandlerInterceptor {

    private final VisitorCountService visitorCountService;

    public VisitorInterceptor(VisitorCountService visitorCountService) {
        this.visitorCountService = visitorCountService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        visitorCountService.incrementVisitorCount(); // 방문자 수 증가
        return true; // 요청을 계속 처리하도록 허용
    }
}
