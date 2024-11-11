package com.lotteon.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "알 수 없는 오류가 발생했습니다.";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404:
                    errorMessage = "페이지를 찾을 수 없습니다.";
                    break;
                case 500:
                    errorMessage = "서버 오류가 발생했습니다.";
                    break;
                case 403:
                    errorMessage = "접근이 거부되었습니다.";
                    break;
                default:
                    errorMessage = "예기치 못한 오류가 발생했습니다.";
            }
        }

        // Model 객체에 에러 메시지를 추가하여 뷰에 전달
        model.addAttribute("errorMessage", errorMessage);
        return "errorPage"; // 사용자 정의 에러 페이지 (errorPage.html)
    }
}

