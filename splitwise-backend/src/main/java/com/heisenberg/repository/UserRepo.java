package com.heisenberg.repository;

import com.heisenberg.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.HashSet;

public interface UserRepo extends CrudRepository<User, Integer> {

    HashSet<User> findByIdIn(ArrayList<Integer> members);
}
