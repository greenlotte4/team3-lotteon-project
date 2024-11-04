package com.lotteon.controller.user;

import com.lotteon.entity.User.Member;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.lotteon.entity.User.QUser.user;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEmailController {

    private final MemberService memberService;

    @GetMapping("/findid")
    public String findid(Model model, HttpSession session) {
        // 세션에서 member 객체 가져오기
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        return "content/user/findid";
    }

    @PostMapping("/findid")
    @ResponseBody // JSON 응답을 반환하기 위해 추가
    public ResponseEntity<?> findId(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        String email = requestBody.get("email");

        Member member = memberService.findIdByNameAndEmail(name, email);

        System.out.println("findId 호출됨: name=" + name + ", email=" + email); // 로그 추가

        if (member != null) {
            // 사용자 정보를 JSON으로 반환
            System.out.println("찾은 회원: " + member);
            return ResponseEntity.ok(member);
        } else {
            // 실패 시 에러 메시지를 포함한 응답 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }


    @GetMapping("/findresult")
    public String findresult() {

        return "content/user/findresult";
    }

    @GetMapping("/findpass")
    public String findPass(Model model) {
        model.addAttribute("content", "findpass");
        return "content/user/findpass";
    }
}


