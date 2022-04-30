package com.heisenberg.repository;

import com.heisenberg.model.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepo extends CrudRepository<Group, Integer> {
}
