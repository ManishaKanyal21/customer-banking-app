package com.pismo.banking.transaction.api.controller;

import com.pismo.banking.transaction.api.TransactionApi;
import com.pismo.banking.transaction.api.TransactionService;
import com.pismo.banking.transaction.api.dto.TransactionRequest;
import com.pismo.banking.transaction.api.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionResponse> createTransaction(final TransactionRequest transactionRequest){
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionResponse);
    }
}
