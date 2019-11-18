package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDto {

    private Integer id;
    private Integer srcAccountId;
    private Integer destAccountId;
    private Double amount;

}
