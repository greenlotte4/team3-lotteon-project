//package com.lotteon.controller;
//
//import com.lotteon.entity.User.Member;
//import com.lotteon.security.MyUserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//
//@ControllerAdvice
//public class GlobalControllerAdvice {
//
//    @ModelAttribute
//    public void addAttributes(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
//            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
//
//            Member member = userDetails.getMember();
//            String memberName = member != null ? member.getName() : "이름 없음"; // null 체크
//
//            model.addAttribute("memberName", memberName);
//        }
//    }
//}