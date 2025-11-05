package com.ahold.technl.sandbox.domain.model.mapper;


import com.ahold.technl.sandbox.domain.model.dtos.CreateDeliveryRequest;
import com.ahold.technl.sandbox.domain.model.dtos.DeliveryResponse;
import com.ahold.technl.sandbox.domain.model.entities.Delivery;
import org.mapstruct.Mapper;


/**
 * Mapper interface for converting between Delivery entities and DTOs.
 * Utilizes MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    /**
     * Converts a Delivery entity to a DeliveryResponse DTO.
     *
     * @param delivery the Delivery entity to convert
     * @return the corresponding DeliveryResponse DTO
     */
    DeliveryResponse toDeliveryResponse(Delivery delivery);

    /**
     * Converts a CreateDeliveryRequest DTO to a Delivery entity.
     *
     * @param createDeliveryRequest the CreateDeliveryRequest DTO to convert
     * @return the corresponding Delivery entity
     */
    Delivery toCreateDeliveryEntity(CreateDeliveryRequest createDeliveryRequest);
}
