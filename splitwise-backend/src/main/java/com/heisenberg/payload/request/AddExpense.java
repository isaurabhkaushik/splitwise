package com.heisenberg.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.heisenberg.domain.Share;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddExpense {

    @JsonAlias("group_id")
    private int groupId;

    @JsonAlias("user_id")
    private int userId;

    private int amount;

    @JsonAlias("receipt_url")
    private String receiptUrl;

    private ArrayList<Share> shares;
}
