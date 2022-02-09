package dev.toyproject.foodDelivery.coupon.infrastructure;

import dev.toyproject.foodDelivery.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponToken(String couponToken);
}
