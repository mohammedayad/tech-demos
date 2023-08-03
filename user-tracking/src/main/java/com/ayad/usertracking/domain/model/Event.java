package com.ayad.usertracking.domain.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Event {

    private User user;

    private Product product;

}
