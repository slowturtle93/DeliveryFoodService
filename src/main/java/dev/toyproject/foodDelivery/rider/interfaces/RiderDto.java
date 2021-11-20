package dev.toyproject.foodDelivery.rider.interfaces;

import dev.toyproject.foodDelivery.common.util.SHA256Util;
import dev.toyproject.foodDelivery.rider.domain.Rider;
import dev.toyproject.foodDelivery.rider.domain.RiderCommand;
import dev.toyproject.foodDelivery.rider.domain.RiderInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

public class RiderDto {

    /******************************** request ********************************/

    @Getter
    @Setter
    @ToString
    public static class RegisterRequest{

        @NotEmpty(message = "riderLoginId 는 필수값입니다")
        private String riderLoginId;

        @NotEmpty(message = "riderPwd 는 필수값입니다")
        private String riderPwd;

        @NotEmpty(message = "riderTel 는 필수값입니다")
        private String riderTel;

        @NotEmpty(message = "riderName 는 필수값입니다")
        private String riderName;

        @NotEmpty(message = "residence 는 필수값입니다")
        private String residence;

        @NotEmpty(message = "deliveryRegion 는 필수값입니다")
        private String deliveryRegion;

        @NotEmpty(message = "deliveryMethod 는 필수값입니다")
        private String deliveryMethod;

        public RiderCommand toCommand(){
            return RiderCommand.builder()
                    .riderLoginId(riderLoginId)
                    .riderPwd(SHA256Util.encryptSHA256(riderPwd))
                    .riderTel(riderTel)
                    .riderName(riderName)
                    .residence(residence)
                    .deliveryRegion(deliveryRegion)
                    .deliveryMethod(deliveryMethod)
                    .build();
        }
    }

    /******************************** response ********************************/

    @Getter
    @ToString
    public static class response{
        private final String riderLoginId;
        private final String riderToken;
        private final String riderTel;
        private final String riderName;
        private String residence;
        private String deliveryRegion;
        private String deliveryMethod;
        private final Rider.Status status;

        public response(RiderInfo riderInfo){
            this.riderLoginId   = riderInfo.getRiderLoginId();
            this.riderToken     = riderInfo.getRiderToken();
            this.riderTel       = riderInfo.getRiderTel();
            this.riderName      = riderInfo.getRiderName();
            this.residence      = riderInfo.getResidence();
            this.deliveryRegion = riderInfo.getDeliveryRegion();
            this.deliveryMethod = riderInfo.getDeliveryMethod();
            this.status         = riderInfo.getStatus();
        }
    }
    
}
