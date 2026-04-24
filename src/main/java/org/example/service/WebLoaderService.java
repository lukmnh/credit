package org.example.service;

import org.example.model.Loan;

import java.util.List;

public interface WebLoaderService {
    List<Loan> fetchLoanData() throws Exception;
    boolean isApiReady();
}
