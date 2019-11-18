package service;

import dto.AccountDto;
import exception.EntityNotFoundException;

import java.util.List;

public interface AccountService {

    AccountDto findById(Integer accountId) throws EntityNotFoundException;

    List<AccountDto> getAll();
}
