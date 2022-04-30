package com.heisenberg.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {

    private String name;

    @JsonAlias("user_name")
    private String userName;

    private String email;

    private String password;

    private String phone;

    @JsonAlias("profile_url")
    private String profileUrl;
}
