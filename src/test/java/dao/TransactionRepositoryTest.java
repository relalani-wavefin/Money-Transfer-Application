package dao;

import configuration.DatabaseConfiguration;
import dao.impl.TransactionRepositoryImpl;
import dto.TransactionDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class TransactionRepositoryTest {

    private static TransactionRepositoryImpl transactionRepository;
    private static DatabaseConfiguration databaseConfiguration;

    @BeforeClass
    public static void setUp() throws SQLException {
        databaseConfiguration = DatabaseConfiguration.getInstance();
        databaseConfiguration.createTables();
        databaseConfiguration.insertDummyData();
        transactionRepository = new TransactionRepositoryImpl(databaseConfiguration);
        try (Connection connection =databaseConfiguration.getConnection()) {
            transactionRepository.createTransaction(TransactionDto.builder()
                    .srcAccountId(1)
                    .destAccountId(2)
                    .amount(12d)
                    .build(), connection);
        }
    }

    @Test
    public void findValidTransactionTest() {
        assert transactionRepository.get(1).getId() == 1;
        assert transactionRepository.getAll().size() >= 1;
    }

    @Test
    public void findInvalidTransactionTest() {
        assert transactionRepository.get(100) == null;
    }

    @Test
    public void createValidTransactionTest() throws SQLException {

        try (Connection connection =databaseConfiguration.getConnection()) {
            transactionRepository.createTransaction(TransactionDto.builder()
                    .amount(5000.00)
                    .srcAccountId(1)
                    .destAccountId(2)
                    .build(), connection);
        }

        assert transactionRepository.getAll()
                .stream()
                .filter(transaction -> transaction.getAmount() == 5000d)
                .collect(Collectors.toList()).size() == 1;
    }

}
