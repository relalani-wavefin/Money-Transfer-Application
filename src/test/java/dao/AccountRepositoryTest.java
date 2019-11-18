package dao;

import configuration.DatabaseConfiguration;
import dao.impl.AccountRepositoryImpl;
import model.Account;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AccountRepositoryTest {

    static DatabaseConfiguration databaseConfiguration;

    private static AccountRepositoryImpl accountRepository;

    @BeforeClass
    public static void setUp() throws SQLException {
        databaseConfiguration = DatabaseConfiguration.getInstance();
        databaseConfiguration.createTables();
        databaseConfiguration.insertDummyData();;
        accountRepository = new AccountRepositoryImpl(databaseConfiguration);
    }

    @Test
    public void findByValidUserTest() throws SQLException {
        List<Account> accounts = accountRepository.findByUser(1);
        assert accounts.size() == 1;
    }

    @Test
    public void findByInvalidUserTest() throws SQLException {
        List<Account> accounts =  accountRepository.findByUser(312);
        assert accounts.size() == 0;
    }

    @Test
    public void updateBalanceWithValidAccountTest() throws SQLException {
        Connection connection = databaseConfiguration.getConnection();
        accountRepository.updateBalance(1, 215d, connection);
        connection.commit();
        connection.close();

        assert  accountRepository.get(1).getBalance() == 215d;

    }

    @Test
    public void findValidAccountTest() {
       assert accountRepository.get(1).getId() == 1;
    }

    @Test
    public void findInvalidAccount() {
        assert accountRepository.get(12) == null;
    }

    @Test
    public void findValidAccountsTest() throws SQLException {
       assert  accountRepository.getAll().size() == 3;
    }
}
