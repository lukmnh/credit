package org.example.service;

import org.example.model.Loan;

public interface WebLoaderService {
    Loan fetchLoanData() throws Exception;
    boolean isApiReady();
}
