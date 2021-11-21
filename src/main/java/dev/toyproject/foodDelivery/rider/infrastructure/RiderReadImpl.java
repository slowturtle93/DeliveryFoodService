package dev.toyproject.foodDelivery.rider.infrastructure;

import dev.toyproject.foodDelivery.common.exception.DuplicateKeyException;
import dev.toyproject.foodDelivery.common.exception.EntityNotFoundException;
import dev.toyproject.foodDelivery.rider.domain.Rider;
import dev.toyproject.foodDelivery.rider.domain.RiderReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RiderReadImpl implements RiderReader {

    private final RiderRepository riderRepository;

    /**
     * 로그인 아이디 중복 확인
     *
     * @param loginId
     */
    @Override
    public void DuplicateLoginId(String loginId) {
        Optional<Rider> riderInfo = riderRepository.findByRiderLoginId(loginId);
        if(!riderInfo.isEmpty()){ throw new DuplicateKeyException(); }
    }

    /**
     * 라이더 로그인 진행
     *
     * @param riderLoginId
     * @param riderPwd
     * @return
     */
    @Override
    public Rider getLoginRider(String riderLoginId, String riderPwd) {
        return riderRepository.findByRiderLoginIdAndRiderPwdAndStatus(riderLoginId, riderPwd, Rider.Status.ENABLE)
                .orElseThrow(EntityNotFoundException::new);
    }
}
