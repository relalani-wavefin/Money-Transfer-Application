package service;

import dao.impl.AccountRepositoryImpl;
import dao.impl.TransactionRepositoryImpl;
import dto.TransactionDto;
import exception.EntityNotFoundException;
import exception.TransactionFailedException;
import model.Account;
import model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.sql.Connection;

import org.mockito.runners.MockitoJUnitRunner;
import service.impl.TransactionServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {


    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepositoryImpl transactionRepository;

    @Mock
    private AccountRepositoryImpl accountRepository;

    @Before
    public void init() {
        transactionService=new TransactionServiceImpl(transactionRepository,accountRepository);
    }

    @Test
    public void findValidTransaction() throws EntityNotFoundException {
        Transaction transaction = Transaction.builder()
                .id(1)
                .amount(5000.00)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Mockito.when(transactionRepository.get(1)).thenReturn(transaction);
        assert  transactionService.findById(1).getId() == 1;
    }

    @Test(expected = EntityNotFoundException.class)
    public void findInvalidTransaction() throws EntityNotFoundException {
        Mockito.when(transactionRepository.get(Mockito.anyInt())).thenReturn(null);
        assert  transactionService.findById(1) == null;
    }

    @Test
    public void testValidTransfer() throws TransactionFailedException, EntityNotFoundException {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amount(150.0)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Transaction transaction = Transaction.builder()
                .id(1)
                .amount(200.00)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Account srcAccount = Account.builder()
                .id(1)
                .balance(212d)
                .name("Test Account 1")
                .userId(1)
                .build();

        Account destAccount = Account.builder()
                .id(2)
                .balance(212d)
                .name("Test Account 2")
                .userId(1)
                .build();

        Mockito.when(accountRepository.get(1)).thenReturn(srcAccount);
        Mockito.when(accountRepository.get(2)).thenReturn(destAccount);
        Mockito.when(transactionRepository.createTransaction(Mockito.any(), Mockito.any())).thenReturn(transaction);

        assert  transactionService.transfer(transactionDto).getId() == 1;
    }

    @Test(expected = TransactionFailedException.class)
    public void testInsufficientTransfer() throws TransactionFailedException, EntityNotFoundException {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amount(1500.0)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Transaction transaction = Transaction.builder()
                .id(1)
                .amount(200.00)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Account srcAccount = Account.builder()
                .id(1)
                .balance(212d)
                .name("Test Account 1")
                .userId(1)
                .build();

        Account destAccount = Account.builder()
                .id(2)
                .balance(212d)
                .name("Test Account 2")
                .userId(1)
                .build();

        Mockito.when(accountRepository.get(1)).thenReturn(srcAccount);
        Mockito.when(accountRepository.get(2)).thenReturn(destAccount);
        Mockito.when(transactionRepository.createTransaction(Mockito.any(), Mockito.any())).thenReturn(transaction);

        assert  transactionService.transfer(transactionDto).getId() == 1;
    }


    @Test(expected = EntityNotFoundException.class)
    public void testSourceAccountNotFoundException() throws TransactionFailedException, EntityNotFoundException {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amount(1500.0)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Account destAccount = Account.builder()
                .id(2)
                .balance(212d)
                .name("Test Account 2")
                .userId(1)
                .build();

        Mockito.when(accountRepository.get(1)).thenReturn(null);
        Mockito.when(accountRepository.get(2)).thenReturn(destAccount);

        assert  transactionService.transfer(transactionDto).getId() == 1;
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDestinationAccountNotFoundException() throws TransactionFailedException, EntityNotFoundException {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1)
                .amount(1500.0)
                .srcAccountId(1)
                .destAccountId(2)
                .build();

        Account sourceAccount = Account.builder()
                .id(2)
                .balance(212d)
                .name("Test Account 2")
                .userId(1)
                .build();

        Mockito.when(accountRepository.get(1)).thenReturn(sourceAccount);
        Mockito.when(accountRepository.get(2)).thenReturn(null);

        assert  transactionService.transfer(transactionDto).getId() == 1;
    }
}
