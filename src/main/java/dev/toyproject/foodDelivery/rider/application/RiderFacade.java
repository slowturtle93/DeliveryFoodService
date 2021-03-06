package dev.toyproject.foodDelivery.rider.application;

import dev.toyproject.foodDelivery.common.util.redis.RedisCacheUtil;
import dev.toyproject.foodDelivery.common.util.redis.SessionUtil;
import dev.toyproject.foodDelivery.notification.fcm.domain.FcmNotificationRequest;
import dev.toyproject.foodDelivery.notification.fcm.domain.FcmService;
import dev.toyproject.foodDelivery.notification.fcm.infrastructrue.FcmNotificationInfo;
import dev.toyproject.foodDelivery.rider.domain.RiderCommand;
import dev.toyproject.foodDelivery.rider.domain.RiderInfo;
import dev.toyproject.foodDelivery.rider.domain.RiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderFacade {

    private final RiderService riderService;
    private final RedisCacheUtil redisCacheUtil;
    private final FcmService fcmService;

    /**
     * 라이더 등록
     *
     * @param command
     * @return
     */
    public RiderInfo registerRider(RiderCommand command){
        var riderInfo = riderService.registerRider(command);
        return riderInfo;
    }

    /**
     * 로그인 아이디 중복 확인
     *
     * @param loginId
     */
    public void duplicateLoginId(String loginId){
        riderService.duplicateLoginId(loginId);
    }

    /**
     * 라이더 로그인 진행
     *
     * @param command
     * @param session
     * @return
     */
    public RiderInfo loginRider(RiderCommand command, HttpSession session){
        var riderInfo = riderService.loginRiderInfo(command.getRiderLoginId(), command.getRiderPwd()); // 로그인 정보 확인
        SessionUtil.setLoginRiderToken(session, riderInfo.getRiderToken()); // session 에 라이더 Token 정보 저장
        return riderInfo;
    }

    /**
     * 라이더 로그아웃 진행
     *
     * @param session
     */
    public void logoutRider(HttpSession session){
        SessionUtil.removeLogoutRider(session); // session 에 라이더 Token 정보 삭제
    }

    /**
     * 라이더 정보 수정
     *
     * @param command
     * @return
     */
    public RiderInfo updateRider(RiderCommand command){
        var riderInfo = riderService.updateRider(command); // 사용자 정보 수정
        return riderInfo;
    }

    /**
     * 라이더 비밀번호 변경
     *
     * @param command
     * @return
     */
    public RiderInfo updateRiderPassword(RiderCommand command, String afterPassword, HttpSession session){
        var riderInfo = riderService.updateRiderPassword(command, afterPassword); // 비밀번호 변경
        SessionUtil.removeLogoutRider(session); // 비밀번호 변경 후 재접속을 위해 session 값 삭제
        return riderInfo;
    }

    /**
     * 비밀번호 찾기 본인인증
     *
     * @param command
     * @return
     */
    public RiderInfo authCheck(RiderCommand command){
        var riderInfo = riderService.authCheck(command);
        return riderInfo;
    }

    /**
     * 신규 비밀번호 업데이트
     *
     * @param command
     */
    public void newPasswordUpdate(RiderCommand command){
        riderService.newPasswordUpdate(command);
    }

    /**
     * 라이더 배달 가능한 주문 목록 리스트 조회
     *
     * @return
     */
    public List<RiderInfo.AvailableOrders> retrieveEnableOrderList(RiderCommand.RiderCurrentLocation command){
        return riderService.retrieveEnableOrderList(command);
    }

    /**
     * 단건 배달 주문 pick
     *
     * @param orderToken
     * @return
     */
    public RiderInfo.AvailableOrders riderOrderPick(String orderToken){
        return riderService.riderOrderPick(orderToken);
    }

    /**
     * 단건 배달 주문 pick
     *
     * @param command
     * @return
     */
    public void riderOrderPickup(RiderCommand.RiderOrderMenuCommand command){
        riderService.riderOrderPickup(command.getOrderToken());
        var deviceToken = redisCacheUtil.getDeviceTokenInfo(command.getMemberToken());
        fcmService.sendFcm(new FcmNotificationRequest(FcmNotificationInfo.FCM_RIDER_ORDER_IN_DELIVERY_TITLE,FcmNotificationInfo.FCM_RIDER_ORDER_IN_DELIVERY_MESSAGE, deviceToken));
    }

    /**
     * 단건 배달 주문 pick
     *
     * @param command
     * @return
     */
    public void riderOrderComplete(RiderCommand.RiderOrderMenuCommand command){
        riderService.riderOrderComplete(command.getOrderToken());
        var deviceToken = redisCacheUtil.getDeviceTokenInfo(command.getMemberToken());
        fcmService.sendFcm(new FcmNotificationRequest(FcmNotificationInfo.FCM_RIDER_ORDER_COMPLETE_TITLE,FcmNotificationInfo.FCM_RIDER_ORDER_COMPLETE_MESSAGE, deviceToken));
    }
}
