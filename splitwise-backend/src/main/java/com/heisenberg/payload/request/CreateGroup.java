package com.heisenberg.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroup {

    private String name;

    private ArrayList<Integer> members;
}
