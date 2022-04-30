package com.heisenberg.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heisenberg.domain.Transaction;
import com.heisenberg.model.*;
import com.heisenberg.payload.request.AddExpense;
import com.heisenberg.payload.request.CreateGroup;
import com.heisenberg.payload.request.CreateUser;
import com.heisenberg.payload.request.SimplifyDebt;
import com.heisenberg.payload.response.SimplifyDebtResponse;
import com.heisenberg.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class SettlementService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private GroupShareRepo groupShareRepo;


    public SimplifyDebtResponse simplifyDebt(SimplifyDebt simplifyDebtRequest) throws IOException {
        SimplifyDebtResponse simplifyDebtResponse = new SimplifyDebtResponse();
        ArrayList<String> txns = new ArrayList<>();

        Optional<Group> optionalGroup = groupRepo.findById(simplifyDebtRequest.getGroupId());
        if (optionalGroup.isEmpty()) return null;

        // simplify
        ArrayList<Integer> difference = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<GroupShare> groupShares = groupShareRepo.findByGroupId(simplifyDebtRequest.getGroupId());
        groupShares.forEach(gs -> difference.add(gs.getTotalPaid() - gs.getTotalOwed()));
        Collections.sort(difference);
        groupShares.sort(Comparator.comparingInt(o -> (o.getTotalPaid() - o.getTotalOwed())));

        for (int i = 0; i < difference.size(); i++) {
            for (int j = i + 1; j < difference.size() && difference.get(i) != 0; j++) {
                if (difference.get(j) > 0) {
                    Transaction txn = new Transaction();
                    int amount;
                    if (-1 * difference.get(i) < difference.get(j)) {
                        amount = -1 * difference.get(i);
                        difference.set(j, difference.get(j) - (-1) * difference.get(i));
                        difference.set(i, 0);
                    } else {
                        amount = difference.get(j);
                        difference.set(i, (-1) * difference.get(i) - difference.get(j));
                        difference.set(j, 0);
                    }

                    txn.setAmount(Math.abs(amount));
                    txn.setPayeeId(groupShares.get(j).getUserId());
                    txn.setPayerId(groupShares.get(i).getUserId());
                    transactions.add(txn);
                    log.info("Simplifying . . . Txn Added {}", txn);
                }
            }
        }

        optionalGroup.get().setTxns(transactions.toString());
        optionalGroup.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        groupRepo.save(optionalGroup.get());

        String tx = "";
        for (Transaction txn : transactions) {
            Optional<User> optionalPayee = userRepo.findById(txn.getPayeeId());
            Optional<User> optionalPayer = userRepo.findById(txn.getPayerId());
            if (optionalPayee.isPresent() && optionalPayer.isPresent()) {
                tx = optionalPayer.get().getName() + " pays Rs" + txn.getAmount() + " to " +
                        optionalPayee.get().getName();
            }
            txns.add(tx);
        }

        simplifyDebtResponse.setTxns(txns);
        return simplifyDebtResponse;
    }

    public boolean createUser(CreateUser createUser) {
        User user = new User();
        user.setName(createUser.getName());
        user.setEmail(createUser.getEmail());
        user.setPassword(createUser.getPassword());
        user.setUsername(createUser.getUserName());
        user.setPhone(createUser.getPhone());
        user.setProfileUrl(createUser.getProfileUrl());
        user.setActive(true);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        userRepo.save(user);
        log.info("User saved successfully: {}", user);
        return true;
    }

    public boolean createGroup(CreateGroup request) {
        boolean success = true;
        Group group = new Group();
        group.setName(request.getName());
        group.setCreatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        group.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        groupRepo.save(group);
        log.info("Group saved successfully: {}", group);

        request.getMembers().forEach(userId -> {
            GroupShare groupShare = new GroupShare(userId, group.getId());
            groupShare = groupShareRepo.save(groupShare);
            log.info("GroupShare saved successfully for group: {}", groupShare);
        });
        return success;
    }

    public boolean addExpense(AddExpense request) {
        boolean success = true;
        Expense expense = new Expense();
        log.info("addExpense: request: {}\n\n", request);
        Optional<Group> group = groupRepo.findById(request.getGroupId());

        if (group.isEmpty()) {
            log.error("Group not found with id : {}", request.getGroupId());
            return false;
        }

        expense.setReceiptUrl(request.getReceiptUrl());
        expense.setCreatedBy(request.getUserId());
        expense.setGroupId(request.getGroupId());
        expense.setTotalAmount(request.getAmount());
        expense.setShares(request.getShares().toString());
        expense.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        expense.setCreatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
        expenseRepo.save(expense);

        log.info("Expense saved successfully: {}", expense);

        request.getShares().forEach(share -> {
            GroupShare groupShare = groupShareRepo.findByUserIdAndGroupId(
                    share.getUserId(), group.get().getId()
            );

            groupShare.setTotalPaid(groupShare.getTotalPaid() + share.getPaidAmount());
            groupShare.setTotalOwed(groupShare.getTotalOwed() + share.getOwedAmount());
            groupShare.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toInstant());
            groupShareRepo.save(groupShare);
            log.info("Group share updated successfully: {}", groupShare);
        });

        group.get().setTotalAmount(group.get().getTotalAmount() + request.getAmount());
        groupRepo.save(group.get());
        return success;
    }
}
