package ua.restaurant.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.restaurant.dto.DishDTO;
import ua.restaurant.dto.OrderItemDTO;
import ua.restaurant.entity.*;
import ua.restaurant.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final LoginsRepository loginsRepository;
    private final BasketsService basketsService;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository,
                         LoginsRepository loginsRepository,
                         BasketsService basketsService) {
        this.ordersRepository = ordersRepository;
        this.loginsRepository = loginsRepository;
        this.basketsService = basketsService;
    }

    public List<Orders> findAllOrders(String username) {
        return ordersRepository.findOrdersByLogin_Login(username);
    }

    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public Orders saveNewItem (@NonNull String username) throws NoSuchElementException {
        try {
            Logins user = loginsRepository.findByLogin(username)
                    .orElseThrow(NoSuchElementException::new);
            log.info(user.toString());
            log.info(username);

            List<DishDTO> basketsItems = basketsService.findAllDishes(username);
            log.info(basketsItems.toString());
            if (basketsItems.isEmpty()) {
                throw new NoSuchElementException();
            }
            log.info(basketsItems.toString());

            BigDecimal total = basketsItems.stream()
                    .map(DishDTO::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            log.info(total.toString());

            basketsService.deleteByLogin(username);
            log.info("delete success");

            // TODO create orderList

            return ordersRepository.save(Orders.builder()
                    .login(user)
                    .totalPrice(total)
                    .status(Status.NEW)
                    .time(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            log.info("Cannot create order");
            throw new NoSuchElementException();
        }
    }

    @Transactional
    public void confirm(OrderItemDTO item) {
        Orders order = ordersRepository.findById(item.getOrderId())
                .orElseThrow(NoSuchElementException::new);

        if (!order.getStatus().equals(Status.DONE) &&
                !order.getStatus().equals(Status.NEW)) {
            order.setStatus(order.getStatus().next());
            ordersRepository.save(order);
        }
    }

    @Transactional
    public void payment(String username) {
        Orders order = ordersRepository.findTopByLogin_LoginOrderByTimeDesc(username)
                .orElseThrow(NoSuchElementException::new);

        if (order.getStatus().equals(Status.NEW)) {
            ordersRepository.updateStatus(order.getId(), Status.PAYED);
        }
    }
}
