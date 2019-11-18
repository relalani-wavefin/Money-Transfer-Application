package model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Account  {
    private Integer id;
    private String name;
    private Integer userId;
    private Double balance;
    private Timestamp createdDate;
    private Timestamp lastUpdatedDate;
}
