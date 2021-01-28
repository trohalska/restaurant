package ua.restaurant.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.restaurant.dto.CategoriesDTO;
import ua.restaurant.dto.DishesDTO;
import ua.restaurant.service.CategoryService;
import ua.restaurant.service.DishService;

import java.util.Arrays;

import static ua.restaurant.config.Constants.LANGUAGE_EN;


@RestController
@RequestMapping(value = "api/")
public class MainController {
    private static final Logger LOGGER = LogManager.getLogger(MainController.class);
    private final DishService dishService;
    private final CategoryService categoryService;

    @Autowired
    public MainController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/get")
    public DishesDTO getMain() {
//        log.info("{}", loginService.getAllUsers());
//        LOGGER.trace(LocaleContextHolder.getLocale().toString());

        test(LOGGER);

        LOGGER.trace("A TRACE Message");
        LOGGER.debug("A DEBUG Message");
        LOGGER.info("An INFO Message");
        LOGGER.warn("A WARN Message");
        LOGGER.error("An ERROR Message");

        LocaleContextHolder.getLocale().toString().equals(LANGUAGE_EN);

        return dishService.getAllDishes();
    }

    public static void test(Logger log) {
        System.out.println("Current log level " + log.getName() + " " + log.getLevel());
        System.out.println(log.getName());

        Level[] levels = Level.values();
        Arrays.sort(levels);
        for (Level l : levels) {
            log.log(l, l.toString() + " {}", l.intLevel());
        }
    }


}
