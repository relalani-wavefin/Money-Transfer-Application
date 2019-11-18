package dao.impl;

import configuration.DatabaseConfiguration;
import dao.TransactionRepository;
import dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DatabaseConfiguration databaseConfiguration ;

    public TransactionRepositoryImpl(){
        databaseConfiguration = DatabaseConfiguration.getInstance();
    }

    public TransactionRepositoryImpl(DatabaseConfiguration databaseConfiguration){
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    public Transaction get(Integer id) {
        try (Connection connection = databaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from transaction where id = ?")) {
            preparedStatement.setInt(1,id);
            Transaction transaction = null;
            try (ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    transaction =  Transaction.builder()
                            .amount(rs.getDouble("amount"))
                            .id(rs.getInt("id"))
                            .srcAccountId(rs.getInt("source_account_id"))
                            .destAccountId(rs.getInt("destination_account_id"))
                            .createdDate(rs.getTimestamp("created_date"))
                            .lastUpdatedDate(rs.getTimestamp("last_updated"))
                            .build();
                }
            }
            return transaction;
        } catch (SQLException e) {
            log.error("Failed to get transaction by id {} ",id, e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Transaction> getAll() {
        try (Connection connection = databaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from transaction")) {
            List<Transaction> transactions = new ArrayList<>();
            try (ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    transactions.add(Transaction.builder()
                            .amount(rs.getDouble("amount"))
                            .id(rs.getInt("id"))
                            .srcAccountId(rs.getInt("source_account_id"))
                            .destAccountId(rs.getInt("destination_account_id"))
                            .createdDate(rs.getTimestamp("created_date"))
                            .lastUpdatedDate(rs.getTimestamp("last_updated"))
                            .build());
                }
            }
            return transactions;
        } catch (SQLException e) {
            log.error("Failed to get all transactions ", e);
        }

        return null;
    }

    @Override
    public Transaction createTransaction(TransactionDto transactionDto , Connection connection) {
        try (PreparedStatement preparedStatement = connection.
                     prepareStatement("INSERT INTO transaction VALUES (NULL, ?, ?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, transactionDto.getSrcAccountId());
            preparedStatement.setInt(2, transactionDto.getDestAccountId());
            preparedStatement.setDouble(3, transactionDto.getAmount());
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(5,new Timestamp(System.currentTimeMillis()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        connection.commit();
                        return get(id);
                    }
                }
            }

        } catch (SQLException e) {
            log.error("Failed to create transactions ", e);
        }

        return null;
    }
}
