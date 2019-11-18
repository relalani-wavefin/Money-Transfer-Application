package service;

import dto.TransactionDto;
import exception.EntityNotFoundException;
import exception.TransactionFailedException;

public interface TransactionService {

    TransactionDto findById(Integer id) throws EntityNotFoundException;

    TransactionDto transfer(TransactionDto transactionDto) throws EntityNotFoundException, TransactionFailedException;
}
