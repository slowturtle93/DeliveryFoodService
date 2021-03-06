package dev.toyproject.foodDelivery.rider.domain;

import dev.toyproject.foodDelivery.mapper.rider.RiderMapper;
import dev.toyproject.foodDelivery.notification.common.domain.CommonApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService{

    private final RiderStore riderStore;
    private final RiderReader riderReader;
    private final RiderMapper riderMapper;
    private final CommonApiService commonApiService;

    /**
     * 라이더 등록
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public RiderInfo registerRider(RiderCommand command) {
        var initRider = command.toEntity();
        Rider rider = riderStore.store(initRider);
        return new RiderInfo(rider);
    }

    /**
     * 로그인 아이디 중복 확인
     *
     * @param loginId
     */
    @Override
    public void duplicateLoginId(String loginId) {
        riderReader.DuplicateLoginId(loginId);
    }

    /**
     * 라이더 로그인 진행
     *
     * @param riderLoginId
     * @param riderPwd
     * @return
     */
    @Override
    public RiderInfo loginRiderInfo(String riderLoginId, String riderPwd) {
        Rider rider = riderReader.getLoginRider(riderLoginId, riderPwd); // Mail, Pwd 조건으로 사용자 정보 조회
        return new RiderInfo(rider);
    }

    /**
     * 라이더 정보 수정
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public RiderInfo updateRider(RiderCommand command) {
        Rider rider = riderReader.getRiderByToken(command.getRiderToken()); // MEMBER 정보 조회
        rider.updateRiderInfo(command); // MEMBER 정보 수정
        return new RiderInfo(rider);
    }

    /**
     * 라이더 비밀번호 변경
     *
     * @param command
     * @param afterPassword
     * @return
     */
    @Override
    @Transactional
    public RiderInfo updateRiderPassword(RiderCommand command, String afterPassword) {
        Rider rider = riderReader.getRiderByTokenAndPwd(command.getRiderToken(), command.getRiderPwd()); // Token, Pwd 조건으로 MEMBER 정보 조회
        rider.updateRiderPassword(afterPassword); // MEMBER 비밀번호 변경
        return new RiderInfo(rider);
    }

    /**
     * 비밀번호 찾기 본인인증
     *
     * @param command
     * @return
     */
    @Override
    public RiderInfo authCheck(RiderCommand command) {
        Rider rider = riderReader.authCheck(command.getRiderLoginId(), command.getRiderTel());
        return new RiderInfo(rider);
    }

    /**
     * 신규 비밀번호 업데이트
     *
     * @param command
     */
    @Override
    @Transactional
    public void newPasswordUpdate(RiderCommand command) {
        Rider rider = riderReader.getRiderByToken(command.getRiderToken());
        rider.updateRiderPassword(command.getRiderPwd());
    }

    /**
     * 라이더 현재 위치 기준 반경 3km 이내 배달 가능한 주문 목록 리스트 조회
     *
     * @return
     */
    @Override
    public List<RiderInfo.AvailableOrders> retrieveEnableOrderList(RiderCommand.RiderCurrentLocation command) {
        List<RiderInfo.AvailableOrders> orderList = riderMapper.getOrdersAvailableDelivery(command);
        return orderList;
    }

    /**
     * 단건 주문 배달 pick
     *
     * @param orderToken
     * @return
     */
    @Override
    public RiderInfo.AvailableOrders riderOrderPick(String orderToken) {
        commonApiService.OrderDeliveryPrepareApiRequest(orderToken);
        return riderMapper.getDeliveryOrderPickInfo(orderToken);
    }

    /**
     * 주문 배달 음식 pickup
     *
     * @param orderToken
     * @return
     */
    @Override
    public void riderOrderPickup(String orderToken) {
        commonApiService.OrderDeliveryPrepareApiRequest(orderToken);
    }

    /**
     * 라이더 주문 배달 완료
     *
     * @param orderToken
     */
    @Override
    public void riderOrderComplete(String orderToken) {
        commonApiService.OrderCompleteApiRequest(orderToken);
    }
}
