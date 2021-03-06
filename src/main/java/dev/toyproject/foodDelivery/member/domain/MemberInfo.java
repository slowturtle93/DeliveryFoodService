package dev.toyproject.foodDelivery.member.domain;

import dev.toyproject.foodDelivery.address.domain.AddressFragment;
import dev.toyproject.foodDelivery.member.domain.address.MemberAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * INFO - 사용자 정보 Select 시 사용되는 객체
 */
public class MemberInfo {

    @Getter
    @ToString
    public static class Main{
        private String memberMail;           // 사용자 이메일
        private String memberToken;          // 사용자 Token 정보
        private String memberPwd;            // 사용자 비밀번호
        private String memberTel;            // 사용자 전화번호
        private String memberNickname;       // 사용자 닉네임
        private String addressCode;          // 사용자 주소코드
        private Member.Status status;        // 사용자 상태

        public Main(Member member){
            this.memberMail     = member.getMemberMail();
            this.memberToken    = member.getMemberToken();
            this.memberPwd      = member.getMemberPwd();
            this.memberTel      = member.getMemberTel();
            this.memberNickname = member.getMemberNickname();
            this.addressCode    = member.getAddressCode();
            this.status         = member.getStatus();
        }
    }

    @Getter
    @ToString
    public static class Address {
        private String memberToken;
        private String memberAddressToken;
        private String detail;
        private MemberAddress.Status status;
        private AddressFragment addressFragment;

        public Address(MemberAddress memberAddress) {
            this.memberToken = memberAddress.getMemberToken();
            this.memberAddressToken = memberAddress.getMemberAddressToken();
            this.detail = memberAddress.getDetail();
            this.status = memberAddress.getStatus();
            this.addressFragment = memberAddress.getAddressFragment();
        }
    }
}
