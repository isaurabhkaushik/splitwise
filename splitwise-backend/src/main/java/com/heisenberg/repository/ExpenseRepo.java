package com.heisenberg.repository;

import com.heisenberg.model.Expense;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepo extends CrudRepository<Expense, Integer> {
}
