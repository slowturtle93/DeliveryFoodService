package dev.toyproject.foodDelivery.rider.domain;

public interface RiderService {

    RiderInfo registerRider(RiderCommand command);

    void duplicateLoginId(String loginId);
}
