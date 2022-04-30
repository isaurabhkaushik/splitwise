package com.heisenberg.repository;

import com.heisenberg.model.GroupShare;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface GroupShareRepo extends CrudRepository<GroupShare, Integer> {
    GroupShare findByUserIdAndGroupId(int userId, int groupId);

    ArrayList<GroupShare> findByGroupId(int groupId);
}
