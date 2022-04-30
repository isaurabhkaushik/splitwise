package com.heisenberg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int amount;

    private int payerId;

    private int payeeId;
}
