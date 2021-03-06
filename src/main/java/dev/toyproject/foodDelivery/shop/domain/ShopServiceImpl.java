package dev.toyproject.foodDelivery.shop.domain;

import dev.toyproject.foodDelivery.notification.common.domain.CommonApiService;
import dev.toyproject.foodDelivery.order.domain.OrderRead;
import dev.toyproject.foodDelivery.shop.domain.shopAddress.ShopAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{

    private final ShopStore shopStore;
    private final ShopFactory shopFactory;
    private final ShopReader shopReader;
    private final ShopMenuFactory shopMenuFactory;
    private final ShopOrderMenuFactory shopOrderMenuFactory;
    private final CommonApiService commonApiService;

    /**
     * 사장님 가게 등록
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public String registerShop(ShopCommand.ShopRequest command) {
        var initShop = command.toEntity();
        Shop shop = shopStore.store(initShop);                          // 가게 등록
        shopFactory.shopBusinessStore(shop, command.getShopBusinessInfoRequest()); // 가게 사업자 정보 등록
        shopFactory.shopAddressStore(shop, command.getShopAddressInfoRequest());   // 가게 주소 정보 등록
        return shop.getShopToken();
    }

    /**
     * 사장님 가게 삭제
     *
     * @param shopToken
     */
    @Override
    @Transactional
    public void disableShop(String shopToken) {
        Shop shop = shopReader.getShopByToken(shopToken);
        shop.disable();
    }

    /**
     * 사장님 가게 정보 수정
     *
     * @param command
     * @return
     */
    @Override
    @Transactional
    public ShopInfo.Main updateShop(String shopToken, ShopCommand.ShopRequest command) {
        Shop shop = shopReader.getShopByToken(shopToken);
        ShopAddress shopAddress = shop.getShopAddress();
        shop.updateShopInfo(command);
        shopAddress.updateAddress(command.getShopAddressInfoRequest().getAddressFragment(), command.getShopAddressInfoRequest().getDetail());
        return shopReader.getShopSeries(shop);
    }

    /**
     * 메뉴 등록
     *
     * @param shopToken
     * @param command
     * @return
     */
    @Override
    @Transactional
    public String registerMenu(String shopToken, List<ShopCommand.MenuGroupRequest> command) {
        Shop shop = shopReader.getShopByToken(shopToken);
        shopMenuFactory.shopMenuStore(shop, command);
        return shop.getShopToken();
    }

    /**
     * 메뉴 수정
     *
     * @param command
     */
    @Override
    @Transactional
    public void updateMenu(List<ShopCommand.MenuGroupRequest> command) {
        shopMenuFactory.updateMenu(command);
    }

    /**
     * 메뉴 그룹 삭제
     *
     * @param command
     */
    @Override
    @Transactional
    public void deleteMenuGroup(ShopCommand.MenuGroupRequest command) {
        shopMenuFactory.deleteMenuGroup(command);
    }

    /**
     * 메뉴 삭제
     *
     * @param command
     */
    @Override
    @Transactional
    public void deleteMenu(ShopCommand.MenuRequest command) {
        shopMenuFactory.deleteMenu(command);
    }

    /**
     * 메뉴 옵션 그룹 삭제
     *
     * @param command
     */
    @Override
    @Transactional
    public void deleteMenuOptionGroup(ShopCommand.MenuOptionGroupRequest command) {
        shopMenuFactory.deleteMenuOptionGroup(command);
    }

    /**
     * 메뉴 옵션 삭제
     *
     * @param command
     */
    @Override
    @Transactional
    public void deleteMenuOption(ShopCommand.MenuOptionRequest command) {
        shopMenuFactory.deleteMenuOption(command);
    }

    /**
     * 사장님 가게 단건 조회
     *
     * @param shopToken
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ShopInfo.Main retrieveShopInfo(String shopToken) {
        var shop = shopReader.getShopByToken(shopToken);
        var shopInfo = shopReader.getShopSeries(shop);
        return shopInfo;
    }

    /**
     * 특정 좌표 위치 기반으로 반경 2km 이내의 가게 조회
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShopInfo.ShopMain> searchShop(ShopCommand.MemberLocationRequest request) {
        var shopInfo = shopReader.searchShop(request);
        var shopMainList = shopReader.getShopListSeries(shopInfo);
        return shopMainList;
    }

    /**
     * 가게 주문 정보 조회
     *
     * @param command
     */
    @Override
    @Transactional(readOnly = true)
    public List<ShopInfo.ShopOrderList> retrieveShopOrderMenu(ShopCommand.ShopOrderMenuRequest command) {
        return shopOrderMenuFactory.retrieveOrderMenuList(command);
    }

    /**
     * 특정 주문 주문 승인 처리
     *
     * @param orderToken
     */
    @Override
    public void shopOrderApproval(String orderToken) {
        commonApiService.OrderApprovalApiRequest(orderToken);
    }

    /**
     * 특정 주문 주문 취소 처리
     *
     * @param orderToken
     */
    @Override
    public void shopOrderCancel(String orderToken) {
        commonApiService.OrderCancelApiRequest(orderToken);
    }
}
