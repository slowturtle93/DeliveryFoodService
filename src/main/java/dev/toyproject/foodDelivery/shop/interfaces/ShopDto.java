package dev.toyproject.foodDelivery.shop.interfaces;

import dev.toyproject.foodDelivery.address.domain.AddressFragment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ShopDto {

    @Getter
    @Setter
    @ToString
    public static class ShopRequest{
        private String ownerToken;         // 사장님 토큰 정보
        private String shopBuildingInfoId; // 가게 건물 정보
        private String shopCategoryId;     // 가게 카테고리 정보
        private String shopNm;             // 가게명
        private String shopDeliveryRegion; // 가게 배달 지역
        private String shopTel;            // 가게 전화번호
        private String shopInfo;           // 가게 소개
        private String shopMinOrdPrice;    // 최소 주문금액
        private String shopNotice;         // 가게 공지사항
        private String shopOperatingTime;  // 가게 운영시간
        private String shopClosedDate;     // 가게 휴무일
        private String shopOrderType;      // 주문타입
        private String shopOriginInfo;     // 원산지 정보
        private ShopBusinessInfoRequest shopBusinessInfoRequest; // 사업장정보
        private ShopAddressInfoRequest shopAddressInfoRequest;   // 가게 주소 정보
    }

    @Getter
    @Builder
    @ToString
    public static class ShopBusinessInfoRequest{
        private final String shopBusinessId;                 // 사업자등록번호
        private final String shopBusinessTaxationType;       // 사업자과세구분
        private final String shopBusinessStatus;             // 사업자 업태
        private final String shopBusinessCategory;           // 사업자 종목
        private final String shopBusinessName;               // 사업자 상호
        private final String shopBusinessRepresentativeType; // 사업자 대표자구분
        private final String shopBusinessRepresentativeName; // 사업자 대표자성명
        private final String shopBusinessLocation;           // 사업장 소재지
        private final String regDate;
        private final String modDate;
    }

    @Getter
    @Builder
    @ToString
    public static class ShopAddressInfoRequest{
        private final AddressFragment addressFragment; // 가게 주소
        private final String detail;                   // 가게 상세 주소
        private final String regDate;
        private final String modDate;
    }
}