package com.heisenberg.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Share {

    @JsonAlias("user_id")
    private int userId;

    @JsonAlias("paid_amount")
    private int paidAmount;

    @JsonAlias("owed_amount")
    private int owedAmount;

//    @Override
//    public String toString() {
//        return this.userId+
//    }
}
