package dev.toyproject.foodDelivery.owner.interfaces;

import dev.toyproject.foodDelivery.common.aop.LoginCheck;
import dev.toyproject.foodDelivery.common.response.CommonResponse;
import dev.toyproject.foodDelivery.common.util.SHA256Util;
import dev.toyproject.foodDelivery.owner.application.OwnerFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/owner")
public class OwnerController {

    private final OwnerFacade ownerFacade;

    /**
     * 이메일, 비밀번호 request 를 받아 회원가입 진행
     *
     * @param request
     * @return
     */
    @PostMapping
    public CommonResponse registerOwner(@Valid OwnerDto.RegisterRequest request){
        var Command = request.toCommand();              // request Data Convert (Command)
        var ownerInfo = ownerFacade.registerOwner(Command);  // Owner 정보 조회
        var response = new OwnerDto.response(ownerInfo);                // OwnerInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사장님 로그인 아이디 중복확인
     *
     * @param ownerLoginId
     * @return
     */
    @GetMapping("/duplicatedOwnerLoginId/{ownerLoginId}")
    public CommonResponse duplicateOwnerLoginId(@PathVariable("ownerLoginId") @NotNull String ownerLoginId){
        ownerFacade.duplicateOwnerLoginId(ownerLoginId); // 이메일 중복 확인
        return CommonResponse.success("OK");
    }

    /**
     * 사장님 로그인 진행
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public CommonResponse loginOwner(@Valid OwnerDto.LoginRequest request, HttpSession session){
        var Command = request.toCommand();                       // request Data Convert (Command)
        var ownerInfo = ownerFacade.loginOwner(Command, session);     // OWNER 로그인
        var response = new OwnerDto.response(ownerInfo);                         // OwnerInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사장님 로그아웃 진행
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    @LoginCheck(type = LoginCheck.UserType.OWNER)
    public CommonResponse logoutOwner(HttpSession session){
        ownerFacade.logoutOwner(session); // OWNER 로그아웃
        return CommonResponse.success("OK");
    }

    /**
     * 사장님 정보 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update")
    @LoginCheck(type = LoginCheck.UserType.OWNER)
    public CommonResponse updateOwner(@Valid OwnerDto.UpdateRequest request){
        var Command = request.toCommand();              // request Data Convert (Command)
        var ownerInfo = ownerFacade.updateOwner(Command);   // OWNER 정보 수정
        var response = new OwnerDto.response(ownerInfo);               // OwnerInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사장님 비밀번호 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update/password")
    @LoginCheck(type = LoginCheck.UserType.OWNER)
    public CommonResponse updateOwnerPassword(@Valid OwnerDto.UpdatePasswordRequest request, HttpSession session){
        var Command = request.toCommand();                                 // request Data Convert (Command)
        var afterPassword = SHA256Util.encryptSHA256(request.getAfterPassword());  // AfterPassword Data Convert (String)
        var ownerInfo = ownerFacade.updateOwnerPassword(Command, afterPassword, session); // OWNER 비밀번호 변경
        var response = new OwnerDto.response(ownerInfo);                                  // OwnerInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 사장님 회원탈퇴 (상태 [Disable] 변경)
     *
     * @param request
     * @return
     */
    @PostMapping("/disable")
    public CommonResponse disableOwner(@Valid OwnerDto.ChangeOwnerRequest request, HttpSession session){
        var ownerToken = request.getOwnerToken(); // request Data Convert (String)
        ownerFacade.disableOwner(ownerToken, session);   // 사장님 상태 [DISABLE] 변경
        return CommonResponse.success("OK");
    }
}
