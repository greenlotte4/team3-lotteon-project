package com.lotteon.controller;

import com.lotteon.dto.User.UserDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.UserRepository;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class AdminUserController {

    private final MemberService memberService;

    @GetMapping("/list")
    public String adminUserList(Model model) {

        model.addAttribute("cate", "user");
        model.addAttribute("content", "list");

        List<Member> memberList = memberService.getAllMembers();

        log.info("회원 목록: {}", memberList);
        model.addAttribute("memberList", memberList);
        return "content/admin/user/memberlist";
    }

    @GetMapping("/admin/user/list/{uid}")
    @ResponseBody
    public ResponseEntity<Member> getUserDetails(@PathVariable String uid) {
        // uid를 기반으로 member 정보를 가져오기
        Optional<Member> memberOptional = memberService.findByUid(uid);

        if (memberOptional.isPresent()) {
            return ResponseEntity.ok(memberOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/members/update")
    public String updateMember(@ModelAttribute Member member) {
        memberService.updateMember(member.getId(), member); // 회원 수정 로직 호출
        return "redirect:/memberlist"; // 수정 후 회원 목록으로 리다이렉트
    }


    @GetMapping("/point")
    public String adminUserPoint(Model model) {
        model.addAttribute("cate", "user");
        model.addAttribute("content", "point");
        return "content/admin/user/memberpoint";
    }
}
