package dev.toyproject.foodDelivery.member.infrastructure;

import dev.toyproject.foodDelivery.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByMemberMailAndMemberPwdAndStatus(String memberMail, String memberPwd, Member.Status status);

    Optional<Member> findByMemberMail(String memberMail);

    Optional<Member> findByMemberTokenAndStatus(String memberToken, Member.Status status);

    Optional<Member> findByMemberTokenAndMemberPwdAndStatus(String memberToken, String memberPwd, Member.Status status);

    Optional<Member> findByMemberMailAndMemberTelAndStatus(String memberMail, String memberTel, Member.Status status);
}
