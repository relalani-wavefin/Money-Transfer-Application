package dao.impl;

import configuration.DatabaseConfiguration;
import dao.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AccountRepositoryImpl implements AccountRepository {

    private final DatabaseConfiguration databaseConfiguration;

    public AccountRepositoryImpl(){
        databaseConfiguration = DatabaseConfiguration.getInstance();
    }

    public AccountRepositoryImpl(DatabaseConfiguration databaseConfiguration){
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    public List<Account> findByUser(Integer userId ) {
        try (Connection connection = databaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from account where user_id = ?")) {
            preparedStatement.setInt(1,userId);
            List<Account> accounts = new ArrayList<>();
            try (ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    accounts.add(Account.builder()
                            .id(rs.getInt("id"))
                            .balance(rs.getDouble("balance"))
                            .name(rs.getString("name"))
                            .userId(rs.getInt("user_id"))
                            .createdDate(rs.getTimestamp("created_date"))
                            .lastUpdatedDate(rs.getTimestamp("last_updated"))
                            .build());
                }
            }
            return accounts;
        } catch (SQLException e) {
           log.error("Failed to fetch account by user", e);
        }

        return null;
    }

    @Override
    public void updateBalance(Integer accountId, Double balance , Connection connection) {
        try (PreparedStatement preparedStatement = connection.
                     prepareStatement("Update account set balance = ? where id = ?")) {

            preparedStatement.setDouble(1, balance);
            preparedStatement.setInt(2, accountId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Failed to update balance of account id: {} ", accountId,  e);
        }
    }

    @Override
    public Account get(Integer id) {
        try (Connection connection = databaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from account where id = ?")) {
            preparedStatement.setInt(1,id);
            Account account = null;
            try (ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    account =  Account.builder()
                            .id(rs.getInt("id"))
                            .balance(rs.getDouble("balance"))
                            .name(rs.getString("name"))
                            .userId(rs.getInt("user_id"))
                            .createdDate(rs.getTimestamp("created_date"))
                            .lastUpdatedDate(rs.getTimestamp("last_updated"))
                            .build();
                }
            }
            return account;
        } catch (SQLException e) {
            log.error("Failed to get account by id {} ",id, e);
        }

        return null;
    }

    @Override
    public List<Account> getAll() {

        try (Connection connection = databaseConfiguration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from account ")) {
            List<Account> accounts = new ArrayList<>();
            try (ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()){
                    accounts.add(Account.builder()
                            .id(rs.getInt("id"))
                            .balance(rs.getDouble("balance"))
                            .name(rs.getString("name"))
                            .userId(rs.getInt("user_id"))
                            .createdDate(rs.getTimestamp("created_date"))
                            .lastUpdatedDate(rs.getTimestamp("last_updated"))
                            .build());
                }
            }
            return accounts;
        } catch (SQLException e) {
            log.error("Failed to get all accounts", e);

        }

        return null;
    }
}
