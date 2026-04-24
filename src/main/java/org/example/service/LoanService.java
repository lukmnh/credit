package org.example.service;

import org.example.model.Loan;
import org.example.model.Vehicle;

import java.util.List;

public interface LoanService {
    List<String> validate(Vehicle vehicle, long totalLoan, int tenure, long downPayment);
    Loan calculate(Loan loan);
}
