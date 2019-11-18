package controller;

import com.google.gson.Gson;
import dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import service.AccountService;
import service.impl.AccountServiceImpl;

import static spark.Spark.get;

@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final Gson gson;

    private static AccountController accountController = null;

    private AccountController(){
        accountService = new AccountServiceImpl();
        gson = new Gson();
    }

    public static AccountController getInstance (){
        if (accountController == null){
            accountController = new AccountController();
        }

        return accountController;

    }

    public void registerController(){

        get("/account/:id", (request, response) -> {

            try {
                AccountDto accountDto = accountService.findById(Integer.valueOf(request.params(":id")));
                return gson.toJson(accountDto);
            } catch (Exception ex){
                log.error("Failed to get account by id {} ", request.params(":id"), ex);
                response.status(500);
                return gson.toJson(ex.getMessage());
            }
        });

    }
}
