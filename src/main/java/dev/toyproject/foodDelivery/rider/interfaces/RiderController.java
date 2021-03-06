package dev.toyproject.foodDelivery.rider.interfaces;

import dev.toyproject.foodDelivery.common.aop.LoginCheck;
import dev.toyproject.foodDelivery.common.response.CommonResponse;
import dev.toyproject.foodDelivery.common.util.SHA256Util;
import dev.toyproject.foodDelivery.rider.application.RiderFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rider")
public class RiderController {

    private final RiderFacade riderFacade;
    private final RiderDtoMapper riderDtoMapper;

    /**
     * 라이더 등록
     *
     * @param request
     * @return
     */
    @PostMapping
    public CommonResponse registerRider(@RequestBody @Valid RiderDto.RegisterRequest request){
        var Command = request.toCommand();              // request Data Convert (Command)
        var riderInfo = riderFacade.registerRider(Command); // Rider 정보 조회
        var response = new RiderDto.response(riderInfo);             // RiderInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 아이디 중복 확인
     *
     * @param loginId
     * @return
     */
    @GetMapping("/duplicate/{loginId}")
    public CommonResponse duplicateLoginId(@PathVariable("loginId") @NotNull String loginId){
        riderFacade.duplicateLoginId(loginId); // 아이디 중복 확인
        return CommonResponse.success("OK");
    }

    /**
     * 라이더 로그인 진행
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public CommonResponse loginMember(@RequestBody @Valid RiderDto.LoginRequest request, HttpSession session){
        var Command = request.toCommand();                    // request Data Convert (Command)
        var riderInfo = riderFacade.loginRider(Command, session); // Rider 로그인
        var response = new RiderDto.response(riderInfo);                   // RiderInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 라이더 로그아웃 진행
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    @LoginCheck(type = LoginCheck.UserType.RIDER)
    public CommonResponse logoutRider(HttpSession session){
        riderFacade.logoutRider(session); // RIDER 로그아웃
        return CommonResponse.success("OK");
    }

    /**
     * 라이더 정보 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update")
    @LoginCheck(type = LoginCheck.UserType.RIDER)
    public CommonResponse updateRider(@RequestBody @Valid RiderDto.UpdateRequest request){
        var Command = request.toCommand();            // request Data Convert (Command)
        var riderInfo = riderFacade.updateRider(Command); // Rider 정보 수정
        var response = new RiderDto.response(riderInfo);           // RiderInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 라이더 비밀번호 변경
     *
     * @param request
     * @return
     */
    @PatchMapping("/update/password")
    @LoginCheck(type = LoginCheck.UserType.RIDER)
    public CommonResponse updateMemberPassword(@RequestBody @Valid RiderDto.UpdatePasswordRequest request, HttpSession session){
        var Command = request.toCommand(); // request Data Convert (Command)
        var afterPassword = SHA256Util.encryptSHA256(request.getAfterPassword());           // AfterPassword Data Convert (String)
        var riderInfo = riderFacade.updateRiderPassword(Command, afterPassword, session); // Rider 비밀번호 변경
        var response = new RiderDto.response(riderInfo); // RiderInfo Data Convert (response)
        return CommonResponse.success(response);
    }

    /**
     * 비밀번호 찾기 본인인증
     *
     * @param request
     * @return
     */
    @PostMapping("/auth/check")
    public CommonResponse authCheck(@RequestBody @Valid RiderDto.AuthCheckRequest request){
        var riderCommand = request.toCommand();
        var riderInfo = riderFacade.authCheck(riderCommand);
        var response = new RiderDto.response(riderInfo);
        return CommonResponse.success(response);
    }

    /**
     * 신규 비밀번호 업데이트
     *
     * @param request
     * @return
     */
    @PatchMapping("/new/password")
    public CommonResponse newPasswordUpdate(@RequestBody @Valid RiderDto.NewPasswordUpdateRequest request){
        var riderCommand = request.toCommand();
        riderFacade.newPasswordUpdate(riderCommand);
        return CommonResponse.success("OK");
    }

    /**
     * 라이더 배달 가능한 주문 목록 리스트 조회
     *
     * @return
     */
    @GetMapping("/enable/orders")
    @LoginCheck(type = LoginCheck.UserType.RIDER)
    public CommonResponse retrieveEnableOrderList(@RequestBody @Valid RiderDto.RiderCurrentLocation request){
        var command = riderDtoMapper.of(request);
        var response = riderFacade.retrieveEnableOrderList(command);
        return CommonResponse.success(response);
    }

    /**
     * 단건 주문 배달 Pick
     *
     * @param orderToken
     * @return
     */
    @GetMapping("/order/pick/{orderToken}")
    public CommonResponse riderOrderPick(@PathVariable("orderToken") String orderToken){
        var response = riderFacade.riderOrderPick(orderToken);
        return CommonResponse.success(response);
    }

    /**
     * 단건 주문 배달 음식 pickup
     *
     * @param request
     * @return
     */
    @GetMapping("/order/pickup")
    public CommonResponse riderOrderPickup(@RequestBody @Valid RiderDto.RiderOrderMenuRequest request){
        var command = riderDtoMapper.of(request);
        riderFacade.riderOrderPickup(command);
        return CommonResponse.success("OK");
    }

    /**
     * 라이더 배달 완료
     *
     * @param request
     * @return
     */
    @GetMapping("/order/complete")
    public CommonResponse riderOrderComplete(@RequestBody @Valid RiderDto.RiderOrderMenuRequest request){
        var command = riderDtoMapper.of(request);
        riderFacade.riderOrderComplete(command);
        return CommonResponse.success("OK");
    }
}
