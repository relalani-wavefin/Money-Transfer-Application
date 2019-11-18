package service;

import dao.AccountRepository;
import dao.impl.AccountRepositoryImpl;
import dto.AccountDto;
import exception.EntityNotFoundException;
import model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import service.impl.AccountServiceImpl;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {


    private AccountServiceImpl accountService;
    @Mock
    private AccountRepositoryImpl accountRepository;

    @Before
    public void init() {
        accountService=new AccountServiceImpl(accountRepository);
    }

    @Test
    public  void findValidAccountTest() throws EntityNotFoundException {
        Account account = Account.builder()
                .id(1)
                .balance(212d)
                .name("Test Account")
                .userId(1)
                .build();
        Mockito.when(accountRepository.get(Mockito.anyInt())).thenReturn(account);
        assert  accountService.findById(1).getId() == 1;
    }

    @Test
    public  void getAllAccountTest() throws EntityNotFoundException {
        Account account = Account.builder()
                .id(1)
                .balance(212d)
                .name("Test Account")
                .userId(1)
                .build();
        Mockito.when(accountRepository.getAll()).thenReturn(Arrays.asList(account));
        assert  accountService.getAll().size() == 1;
    }

    @Test(expected = EntityNotFoundException.class)
    public void findInValidAccountTest() throws EntityNotFoundException {
        Account account = Account.builder()
                .id(1)
                .balance(212d)
                .name("Test Account")
                .userId(1)
                .build();
        Mockito.when(accountRepository.get(Mockito.anyInt())).thenReturn(null);
        assert  accountService.findById(1) == null;
    }

}
