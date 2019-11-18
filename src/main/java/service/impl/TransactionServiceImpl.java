package service.impl;

import com.google.gson.Gson;
import configuration.DatabaseConfiguration;
import dao.AccountRepository;
import dao.TransactionRepository;
import dao.impl.AccountRepositoryImpl;
import dao.impl.TransactionRepositoryImpl;
import dto.TransactionDto;
import exception.EntityNotFoundException;
import exception.TransactionFailedException;
import lombok.extern.slf4j.Slf4j;
import mapper.TransactionMapper;
import model.Account;
import model.Transaction;
import service.TransactionService;

import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private DatabaseConfiguration databaseConfiguration = DatabaseConfiguration.getInstance();

    public TransactionServiceImpl() {
        this.transactionRepository = new TransactionRepositoryImpl();
        this.accountRepository = new AccountRepositoryImpl();
    }

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionDto findById(Integer transactionId) throws EntityNotFoundException {
        Transaction transaction = transactionRepository.get(transactionId);
        if (transaction == null) {
            throw new EntityNotFoundException("Transaction with id: {} not found");
        }
        return TransactionMapper.makeTransactionDto(transaction);

    }

    @Override
    public TransactionDto transfer(TransactionDto transactionDto) throws TransactionFailedException, EntityNotFoundException {
        Account srcAccount = accountRepository.get(transactionDto.getSrcAccountId());

        if (srcAccount == null){
            throw new EntityNotFoundException("Invalid Source Account");
        }

        Account destAccount = accountRepository.get(transactionDto.getDestAccountId());
        if (destAccount == null){
            throw new EntityNotFoundException("Invalid Destination Account");
        }

        if (srcAccount.getBalance() < transactionDto.getAmount()) {
            throw new TransactionFailedException("Insufficient funds available in the source account");
        }

        try (Connection connection = databaseConfiguration.getConnection()) {

            accountRepository.updateBalance(srcAccount.getId(), srcAccount.getBalance() - transactionDto.getAmount() ,connection);
            accountRepository.updateBalance(destAccount.getId(), destAccount.getBalance() + transactionDto.getAmount() , connection);

            transactionDto = TransactionMapper.makeTransactionDto(transactionRepository.createTransaction(
                    TransactionDto.builder()
                            .amount(transactionDto.getAmount())
                            .srcAccountId(srcAccount.getId())
                            .destAccountId(destAccount.getId())
                            .build(),connection));

            return transactionDto;
        } catch (SQLException e) {
            throw new TransactionFailedException("failed to transfer amount for payload" + new Gson().toJson(transactionDto));
        }
    }
}
