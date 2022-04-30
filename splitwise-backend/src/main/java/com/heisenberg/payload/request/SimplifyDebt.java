package com.heisenberg.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimplifyDebt {

    @JsonAlias("group_id")
    private int groupId;
}
