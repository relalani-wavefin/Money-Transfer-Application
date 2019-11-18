package service.impl;

import dao.AccountRepository;
import dao.impl.AccountRepositoryImpl;
import dto.AccountDto;
import exception.EntityNotFoundException;
import mapper.AccountMapper;
import model.Account;
import service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = new AccountRepositoryImpl();
    }

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto findById(Integer accountId) throws EntityNotFoundException {
        Account account = accountRepository.get(accountId);
        if (account == null) {
            throw new EntityNotFoundException("Account with id: {} not found");
        }
        return AccountMapper.makeAccountDto(account);
    }

    @Override
    public List<AccountDto> getAll() {
        return accountRepository.getAll().stream()
                .map(account -> AccountMapper.makeAccountDto(account))
                .collect(Collectors.toList());
    }
}
