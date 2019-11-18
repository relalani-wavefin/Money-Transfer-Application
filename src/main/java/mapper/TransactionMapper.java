package mapper;

import dto.TransactionDto;
import model.Transaction;

public class TransactionMapper {

    public static TransactionDto makeTransactionDto(Transaction transaction)
    {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .srcAccountId(transaction.getSrcAccountId())
                .destAccountId(transaction.getDestAccountId())
                .build();
    }
}
