package com.lotteon.controller;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/{content}")
    public String join(@PathVariable String content, Model model) {
        model.addAttribute("content", content);
        return "userIndex";
    }

    @PostMapping("/memberregister")
    public String registerMember(@ModelAttribute User user, @ModelAttribute Member member) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getPass());
        user.setPass(encodedPassword);  // 인코딩된 비밀번호 설정

        System.out.println("Encoded Password: " + encodedPassword);

        userService.registerMember(user, member);
        System.out.println("user:" + user + " member:" + member);
        return "redirect:/user/login";
    }

    @PostMapping("/sellerregister")
    public String registerSeller(@ModelAttribute User user, @ModelAttribute Seller seller) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getPass());
        user.setPass(encodedPassword);  // 인코딩된 비밀번호 설정

        System.out.println("Encoded Password: " + encodedPassword);  // 인코딩된 비밀번호 로그

        userService.registerSeller(user, seller);
        System.out.println("user:" + user + " seller:" + seller);
        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("inId") String username,
                        @RequestParam("Password") String password,
                        Model model) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authToken); // 인증 처리
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 설정
            return "redirect:/home"; // 로그인 성공 후 이동할 페이지
        } catch (Exception e) {
            model.addAttribute("error", "로그인에 실패했습니다. 아이디 또는 비밀번호를 확인하세요.");
            return "login"; // 로그인 페이지로 돌아가기
        }
    }
}
