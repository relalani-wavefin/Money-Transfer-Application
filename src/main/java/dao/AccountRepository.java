package dao;

import model.Account;

import java.sql.Connection;
import java.util.List;

public interface AccountRepository extends BaseRepository<Account> {
    List<Account> findByUser(Integer userId);
    void updateBalance(Integer accountId, Double balance, Connection connection);
}
