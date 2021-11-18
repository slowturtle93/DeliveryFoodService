package dev.toyproject.foodDelivery.owner.application;

import dev.toyproject.foodDelivery.owner.domain.OwnerCommand;
import dev.toyproject.foodDelivery.owner.domain.OwnerInfo;
import dev.toyproject.foodDelivery.owner.domain.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerFacade {

    private final OwnerService ownerService;

    /**
     * 회원가입 진행
     *
     * @param command
     * @return
     */
    public OwnerInfo registerOwner(OwnerCommand command){
        var ownerInfo = ownerService.registerOwner(command); // 사장 등록
        return ownerInfo;
    }

    /**
     * 사장님 로그인 아이디 중복 확인
     *
     * @param ownerLoginId
     */
    public void duplicateOwnerLoginId(String ownerLoginId){
        ownerService.duplicateOwnerLoginId(ownerLoginId); // 로그인아이디 중복 확인
    }
}
