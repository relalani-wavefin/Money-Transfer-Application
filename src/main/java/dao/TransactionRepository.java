package dao;

import dto.TransactionDto;
import model.Transaction;

import java.sql.Connection;

public interface TransactionRepository extends BaseRepository<Transaction> {
    Transaction createTransaction(TransactionDto transactionDto, Connection connection);
}
