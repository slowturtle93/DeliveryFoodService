package dev.toyproject.foodDelivery.order.interfaces;

import dev.toyproject.foodDelivery.common.response.CommonResponse;
import dev.toyproject.foodDelivery.order.application.OrderFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderDtoMapper orderDtoMapper;

    /**
     * 장바구니 메뉴 등록
     *
     * @param request
     * @return
     */
    @PostMapping("/basket")
    public CommonResponse registerMenuBasket(@RequestBody @Valid OrderDto.OrderBasketRequest request){
        var command = orderDtoMapper.of(request);
        var orderMenuBasketList = orderFacade.registerMenuBasket(command);
        return CommonResponse.success(orderMenuBasketList);
    }
}
