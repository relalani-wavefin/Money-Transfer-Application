package controller;

import com.google.gson.Gson;
import dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import service.TransactionService;
import service.impl.TransactionServiceImpl;

import static spark.Spark.get;
import static spark.Spark.post;

@Slf4j
public class TransactionController {

    private static TransactionController transactionController;
    private final TransactionService transactionService;
    private final Gson gson;

    public TransactionController() {
        this.transactionService = new TransactionServiceImpl();
        gson = new Gson();
    }

    public static TransactionController getInstance() {
        if (transactionController == null) {
            transactionController = new TransactionController();
        }

        return transactionController;

    }

    public void registerController() {

        get("/transaction/:id", (request, response) -> {
            try {
                TransactionDto transactionDto = transactionService.findById(Integer.valueOf(request.params(":id")));
                return gson.toJson(transactionDto);
            } catch (Exception ex) {
                log.error("Failed to get transaction by id {} ", request.params(":id"), ex);
                response.status(500);
                return gson.toJson(ex.getMessage());
            }
        });

        post("/transaction", (request, response) -> {
            try {
                TransactionDto transactionDto = gson.fromJson(request.body(), TransactionDto.class);
                transactionDto = transactionService.transfer(transactionDto);
                response.status(201); // 201 Created
                return gson.toJson(transactionDto);
            } catch (Exception ex) {
                log.error("Transaction failed ", ex);
                response.status(500);
                return gson.toJson(ex.getMessage());
            }
        });
    }
}
