package ua.restaurant.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.restaurant.dto.LoginDTO;
import ua.restaurant.dto.LoginsDTO;
import ua.restaurant.service.AccountsService;

//@Slf4j
@RestController
@RequestMapping(value = "/")
public class LoginFormController {

    private final AccountsService accountsService;
    private static final Logger log = LogManager.getLogger(LoginFormController.class);

    @Autowired
    public LoginFormController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    //@RequestMapping(value = "login", method = RequestMethod.POST)
    @PostMapping(value = "login")
    public void loginFormController(LoginDTO user){
        log.info("{}", accountsService.findByUserLogin(user));
        log.info("{}", user);
//        userService.saveNewUser(User.builder()
//                .firstName("Ann")
//                .lastName("Reizer")
//                .email("AnnReizer@testing.ua")
//                .role(RoleType.ROLE_USER)
//                .build());
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public LoginsDTO getAllUser() {
        log.info("{}", accountsService.getAllUsers());
        return accountsService.getAllUsers();
    }
}
