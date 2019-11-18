package mapper;

import dto.AccountDto;
import model.Account;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountDto makeAccountDto(Account account)
    {
        return AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .balance(account.getBalance())
                .userId(account.getUserId())
                .build();
    }

}
