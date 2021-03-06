package dev.toyproject.foodDelivery.owner.domain;

public interface OwnerService {

    OwnerInfo registerOwner(OwnerCommand command);

    void duplicateOwnerLoginId(String ownerLoginId);

    OwnerInfo loginOwnerInfo(String ownerLoginId, String ownerPwd);

    OwnerInfo updateOwner(OwnerCommand command);

    OwnerInfo updateOwnerPassword(OwnerCommand command, String afterPassword);

    OwnerInfo disableOwner(String ownerToken);

    OwnerInfo authCheck(OwnerCommand command);

    void newPasswordUpdate(OwnerCommand command);
}
