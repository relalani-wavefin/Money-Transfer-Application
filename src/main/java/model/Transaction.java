package model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Transaction {

    private Integer id;
    private Integer srcAccountId;
    private Integer destAccountId;
    private Double amount;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;

}
