package dev.toyproject.foodDelivery.order.domain;

import dev.toyproject.foodDelivery.AbstracEntity;
import dev.toyproject.foodDelivery.address.domain.AddressFragment;
import dev.toyproject.foodDelivery.common.exception.IllegalStatusException;
import dev.toyproject.foodDelivery.common.exception.InvalidParamException;
import dev.toyproject.foodDelivery.common.util.TokenGenerator;
import dev.toyproject.foodDelivery.order.domain.menu.OrderMenu;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class Order extends AbstracEntity {

    private final static String ORDER_PREFIX = "odr_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderToken;               // 주문 토큰
    private String memberToken;              // 사용자 토큰 정보
    private String shopToken;                // 가게 토큰 정보
    private String paymentMethod;            // 결제 방식
    private Long totalAmount;                // 주문 총 가격

    @Embedded
    private AddressFragment addressFragment; // 배달 주소

    private ZonedDateTime orderDate;         // 주문 일시

    @Enumerated(EnumType.STRING)
    private Status status;                   // 주문 상태

    @Getter
    @RequiredArgsConstructor
    public enum Status{
        INIT("주문시작"),
        ORDER_COMPLETE("주문완료"),
        DELIVERY_PREPARE("배송준비"),
        IN_DELIVERY("배송중"),
        DELIVERY_COMPLETE("배송완료");

        private final String description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderMenu> orderMenuList = Arrays.asList();

    @Builder
    public Order(
            String memberToken,
            String shopToken,
            String paymentMethod,
            Long totalAmount,
            AddressFragment addressFragment
    ){
        if (StringUtils.isEmpty(memberToken))   throw new InvalidParamException("order.memberToken");
        if (StringUtils.isEmpty(shopToken))     throw new InvalidParamException("order.shopToken");
        if (StringUtils.isEmpty(paymentMethod)) throw new InvalidParamException("order.paymentMethod");
        if (totalAmount == null)                throw new InvalidParamException("order.totalAmount");
        if (addressFragment == null)            throw new InvalidParamException("order.addressFragment");

        this.orderToken      = TokenGenerator.randomCharacterWithPrefix(ORDER_PREFIX);
        this.memberToken     = memberToken;
        this.shopToken       = shopToken;
        this.paymentMethod   = paymentMethod;
        this.totalAmount     = totalAmount;
        this.addressFragment = addressFragment;
        this.orderDate       = ZonedDateTime.now();
        this.status          = Status.INIT;
    }


    public void orderComplete() {
        if (this.status != Status.INIT) throw new IllegalStatusException();
        this.status = Status.ORDER_COMPLETE;
    }

    public boolean isAlreadyPaymentComplete() {
        switch (this.status) {
            case ORDER_COMPLETE:
            case DELIVERY_PREPARE:
            case IN_DELIVERY:
            case DELIVERY_COMPLETE:
                return true;
        }
        return false;
    }
}