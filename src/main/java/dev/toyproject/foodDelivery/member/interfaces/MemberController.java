package dev.toyproject.foodDelivery.member.interfaces;


import dev.toyproject.foodDelivery.common.aop.LoginCheck;
import dev.toyproject.foodDelivery.common.response.CommonResponse;
import dev.toyproject.foodDelivery.common.util.SHA256Util;
import dev.toyproject.foodDelivery.member.application.MemberFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberFacade memberFacade;

    /**
     * 이메일, 비밀번호, 전화번호, 닉네임 request 를 받아 회원가입 진행
     *
     * @param request
     * @return
     */
    @PostMapping
    public CommonResponse registerMember(@RequestBody @Valid MemberDto.RegisterRequest request){
        var Command = request.toCommand();                 // request Data Convert (Command)
        var memberInfo = memberFacade.registerMember(Command); // Member 정보 조회
        var response = new MemberDto.response(memberInfo);                // MemberInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사용자 로그인 진행
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public CommonResponse loginMember(@RequestBody @Valid MemberDto.LoginRequest request, HttpSession session){
        var Command = request.toCommand();                       // request Data Convert (Command)
        var memberInfo = memberFacade.loginMember(Command, session); // MEMBER 로그인
        var response = new MemberDto.response(memberInfo);                      // MemberInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사용자 로그아웃 진행
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    @LoginCheck(type = LoginCheck.UserType.MEMBER)
    public CommonResponse logoutMember(HttpSession session){
        memberFacade.logoutMember(session); // MEMBER 로그아웃
        return CommonResponse.success("OK");
    }

    /**
     * 이메일 중복확인
     *
     * @param memberMail
     * @return
     */
    @GetMapping("/duplicatedMail/{memberMail}")
    public CommonResponse duplicatedMemberMail(@PathVariable("memberMail") @NotNull String memberMail){
        memberFacade.duplicateMemberMail(memberMail); // 이메일 중복 확인
        return CommonResponse.success("OK");
    }

    /**
     * 사용자 정보 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update")
    @LoginCheck(type = LoginCheck.UserType.MEMBER)
    public CommonResponse updateMember(@RequestBody @Valid MemberDto.UpdateRequest request){
        var Command = request.toCommand();               // request Data Convert (Command)
        var memberInfo = memberFacade.updateMember(Command); // MEMBER 정보 수정
        var response = new MemberDto.response(memberInfo);              // MemberInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사용자 비밀번호 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update/password")
    @LoginCheck(type = LoginCheck.UserType.MEMBER)
    public CommonResponse updateMemberPassword(@RequestBody @Valid MemberDto.UpdatePasswordRequest request, HttpSession session){
        var Command = request.toCommand(); // request Data Convert (Command)
        var afterPassword = SHA256Util.encryptSHA256(request.getAfterPassword());                 // AfterPassword Data Convert (String)
        var memberInfo = memberFacade.updateMemberPassword(Command, afterPassword, session); // MEMBER 비밀번호 변경
        var response = new MemberDto.response(memberInfo); // MemberInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사용자 회원탈퇴
     *
     * @param request
     * @return
     */
    @PatchMapping("/disable")
    @LoginCheck(type = LoginCheck.UserType.MEMBER)
    public CommonResponse disableMember(@Valid MemberDto.ChangeMemberRequest request, HttpSession session){
        var memberToken = request.getMemberToken(); // request Data Convert (String)
        memberFacade.disableMember(memberToken, session);  // 사용자 상태 [DISABLE] 변경
        return CommonResponse.success("OK");
    }

    /**
     * 이메일, 휴대폰 본인인증
     *
     * @param request
     * @return
     */
    @PostMapping("/auth/check")
    public CommonResponse authCheck(@RequestBody @Valid MemberDto.authCheckRequest request){
        var memberCommand = request.toCommand();
        var memberInfo = memberFacade.authCheck(memberCommand);
        var response = new MemberDto.response(memberInfo);
        return CommonResponse.success(response);
    }
}
