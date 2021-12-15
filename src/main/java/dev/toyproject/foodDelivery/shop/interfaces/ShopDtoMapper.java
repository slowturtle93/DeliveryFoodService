package dev.toyproject.foodDelivery.shop.interfaces;

import dev.toyproject.foodDelivery.shop.domain.ShopCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel       = "spring",
        injectionStrategy    = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ShopDtoMapper {

    /******************* request  *********************/

    ShopCommand.ShopRequest of(ShopDto.ShopRequest request);
}