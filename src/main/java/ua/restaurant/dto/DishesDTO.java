package ua.restaurant.dto;

import lombok.*;
import ua.restaurant.entity.Dish;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DishesDTO {
    private List<Dish> dishes;
}
