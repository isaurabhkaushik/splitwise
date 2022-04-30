package com.heisenberg.controller;

import com.heisenberg.payload.request.AddExpense;
import com.heisenberg.payload.request.CreateGroup;
import com.heisenberg.payload.request.CreateUser;
import com.heisenberg.payload.request.SimplifyDebt;
import com.heisenberg.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class Controller {

    @Autowired
    SettlementService settlementService;

    @PostMapping("/v1/user")
    public ResponseEntity<?> createUser(@RequestBody CreateUser createUserRequest) {
        try {
            log.trace("Inside createUser : {}", createUserRequest);
            return ResponseEntity.ok(settlementService.createUser(createUserRequest));
        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("/v1/group")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroup createGroupRequest) {
        try {
            log.trace("Inside createGroup : {}", createGroupRequest);
            return ResponseEntity.ok(settlementService.createGroup(createGroupRequest));
        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/v1/expense")
    public ResponseEntity<?> addExpense(@RequestBody AddExpense addExpenseRequest) {
        try {
            log.trace("Inside addExpense : {}", addExpenseRequest);
            return ResponseEntity.ok(settlementService.addExpense(addExpenseRequest));
        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/v1/simplifyDebt/{groupId}")
    public ResponseEntity<?> simplifyDebt(@PathVariable int groupId) {
        try {
            SimplifyDebt simplifyDebtRequest = new SimplifyDebt(groupId);
            log.trace("Inside simplifyDebt : {}", simplifyDebtRequest);
            return ResponseEntity.ok(settlementService.simplifyDebt(simplifyDebtRequest));
        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
