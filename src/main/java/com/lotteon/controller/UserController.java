package com.lotteon.controller;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.UserRepository;
import com.lotteon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String userJoin(Model model) {
        model.addAttribute("content", "join");
        return "content/user/join"; // Points to "content/user/join"
    }

    @GetMapping("/login")
    public String userLogin(Model model) {
        model.addAttribute("content", "login");
        return "content/user/login"; // Points to "content/user/login"
    }

    @GetMapping("/term")
    public String terms(Model model) {
        model.addAttribute("content", "term");
        return "content/user/term"; // Points to "content/user/term"
    }


    @GetMapping("/memberregister")
    public String memberRegister(Model model) {
        model.addAttribute("content", "memberregister");
        return "content/user/memberregister"; // Points to "content/user/memberregister"
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


    @GetMapping("/sellerregister")
    public String sellerRegister(Model model) {
        model.addAttribute("content", "sellerregister");
        return "content/user/sellerregister"; // Points to "content/user/sellerregister"
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

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authToken); // 인증 처리
        SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 설정

        // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
        String memberName = userService.getMemberNameByUsername(username);
        log.info("memberName:"+memberName);
        model.addAttribute("memberName", memberName);

        return "redirect:/"; // 로그인 성공 후 이동할 페이지

    }
}

