package dev.toyproject.foodDelivery.member.domain;

public interface MemberReader {

    Member getLoginMember(String memberMail, String memberPwd);
}
