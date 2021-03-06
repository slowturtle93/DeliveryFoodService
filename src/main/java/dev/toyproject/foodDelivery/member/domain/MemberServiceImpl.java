package dev.toyproject.foodDelivery.member.domain;

import dev.toyproject.foodDelivery.common.exception.IllegalStatusException;
import dev.toyproject.foodDelivery.common.util.redis.RedisCacheUtil;
import dev.toyproject.foodDelivery.common.util.redis.RedisKeyFactory;
import dev.toyproject.foodDelivery.member.domain.address.MemberAddress;
import dev.toyproject.foodDelivery.member.domain.address.MemberAddressFactory;
import dev.toyproject.foodDelivery.member.domain.address.MemberAddressReader;
import dev.toyproject.foodDelivery.member.domain.address.MemberAddressStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MEMBER 도메인 요구사항 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberStore memberStore;
    private final MemberReader memberReader;

    private final RedisCacheUtil redisCacheUtil;

    private final MemberAddressReader memberAddressReader;
    private final MemberAddressFactory memberAddressFactory;
    private final MemberAddressStore memberAddressStore;

    /**
     * 사용자 회원가입
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Main registerMember(MemberCommand.Main command) {
        var initMember = command.toEntity();  // command convert to initMember
        Member member = memberStore.store(initMember); // initMember register
        return new MemberInfo.Main(member);
    }

    /**
     * 사용자 로그인 진행
     *
     * @param memberMail
     * @param memberPwd
     * @return
     */
    @Override
    public MemberInfo.Main loginMemberInfo(String memberMail, String memberPwd) {
        Member member = memberReader.getLoginMember(memberMail, memberPwd); // Mail, Pwd 조건으로 사용자 정보 조회
        return new MemberInfo.Main(member);
    }

    /**
     * 이메일 중복 확인
     *
     * @param memberMail
     * @return
     */
    @Override
    public void duplicateMemberMail(String memberMail) {
        memberReader.DuplicateCheckMemberMail(memberMail); // email Duplicate Check;
    }

    /**
     * 사용자 정보 변경
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Main updateMember(MemberCommand.Main command) {
        Member member = memberReader.getMemberByToken(command.getMemberToken());      // MEMBER 정보 조회
        member.updateMemberInfo(command.getMemberTel(), command.getMemberNickname()); // MEMBER 정보 수정
        return new MemberInfo.Main(member);
    }

    /**
     * 사용자 비밀번호 변경
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Main updateMemberPassword(MemberCommand.Main command, String afterPassword) {
        Member member = memberReader.getMemberByTokenAndPwd(command.getMemberToken(), command.getMemberPwd()); // Token, Pwd 조건으로 MEMBER 정보 조회
        member.updateMemberPassword(afterPassword); // MEMBER 비밀번호 변경
        return new MemberInfo.Main(member);
    }

    /**
     * 사용자 회원탈퇴
     *
     * @param memberToken
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Main disableMember(String memberToken) {
        Member member = memberReader.getMemberByToken(memberToken); // Token 조건으로 사용자 정보 조회
        member.disable(); // 사용자 상태 [DISABLE] 변경
        return new MemberInfo.Main(member);
    }

    /**
     * 본인인증
     *
     * @param command
     * @return
     */
    @Override
    public MemberInfo.Main authCheck(MemberCommand.Main command) {
        Member member = memberReader.authCheck(command.getMemberMail(), command.getMemberTel());
        return new MemberInfo.Main(member);
    }

    /**
     * 인증번호 Redis 저장
     * @param command
     * @param authNumber
     */
    @Override
    public void authNumberRegister(MemberCommand.Main command, String authNumber) {
        redisCacheUtil.setRedisCacheAuthNumber(command.getMemberToken(), RedisKeyFactory.MEMBER_AUTH_NUMBER, authNumber);
    }

    /**
     * 인증번호 확인
     *
     * @param memberToken
     * @param authNumber
     */
    @Override
    public void authNumberCheck(String memberToken, String authNumber) {
        String value = redisCacheUtil.getAuthNumber(memberToken, RedisKeyFactory.MEMBER_AUTH_NUMBER);

        if(!authNumber.equals(value)) {
            throw new IllegalStatusException("인증번호가 일치하지 않습니다.");
        } else {
            redisCacheUtil.removeAuthNumber(memberToken, RedisKeyFactory.MEMBER_AUTH_NUMBER);
        }
    }

    /**
     * 신규 비밀번호 업데이트
     *
     * @param command
     */
    @Override
    @Transactional
    public void newPasswordUpdate(MemberCommand.Main command) {
        Member member = memberReader.getMemberByToken(command.getMemberToken());
        member.updateMemberPassword(command.getMemberPwd());
    }

    /**
     * 사용자 배달 주소 저장
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Address registerAddress(MemberCommand.Address command) {
        var memberAddressList = memberAddressReader.getMemberAddressByMemberTokenAndStatus(command.getMemberToken(), MemberAddress.Status.ENABLE);
        memberAddressFactory.memberAddressListDisable(memberAddressList);
        var initMemberAddress = command.toEntity();
        var memberInfo = memberAddressStore.store(initMemberAddress);
        return new MemberInfo.Address(memberInfo);
    }

    /**
     * 사용자 배달 상세 주소 수정
     *
     * @param memberAddressToken
     * @param command
     * @return
     */
    @Override
    @Transactional
    public MemberInfo.Address updateAddress(String memberAddressToken, MemberCommand.Address command) {
        MemberAddress memberAddress = memberAddressReader.getMemberAddressByMemberAddressToken(memberAddressToken);
        memberAddress.updateAddress(command.getDetail());
        return new MemberInfo.Address(memberAddress);
    }

    /**
     * 사용자 배달 주소 상태 [DELETE] 변경
     *
     * @param memberAddressToken
     */
    @Override
    @Transactional
    public void deleteAddress(String memberAddressToken) {
        MemberAddress memberAddress = memberAddressReader.getMemberAddressByMemberAddressToken(memberAddressToken);
        memberAddress.delete();
    }

    /**
     * 사용자 배달 주소 list 조회
     *
     * @param memberToken
     * @return
     */
    @Override
    public List<MemberInfo.Address> retrieveAddressList(String memberToken) {
        var memberAddressList = memberAddressReader.getMemberAddressByMemberToken(memberToken);
        var memberAddressInfoList = memberAddressReader.getMemberAddressSeries(memberAddressList);
        return memberAddressInfoList;
    }

}
