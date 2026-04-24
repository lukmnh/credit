package org.example.service.impl;

import org.example.model.Loan;
import org.example.model.Loan.Installment;
import org.example.model.Vehicle;
import org.example.service.LoanService;

import java.util.ArrayList;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private static final long MAX_LOAN_AMOUNT = 1_000_000_000L;

    @Override
    public List<String> validate(Vehicle vehicle, long totalLoan, int tenure, long downPayment) {
        List<String> errors = new ArrayList<>();
        int currentYear = java.time.Year.now().getValue();

        if (vehicle.getYear() > currentYear) {
            errors.add("Tahun kendaraan tidak valid (maksimal tahun " + currentYear + ").");
        }

        if (vehicle.getCondition().equalsIgnoreCase("baru")) {
            if (vehicle.getYear() < (currentYear - 1)) {
                errors.add("Kendaraan baru minimal harus tahun " + (currentYear - 1));
            }
        }

        if (tenure < 1 || tenure > 6) {
            errors.add("Tenor harus antara 1-6 tahun.");
        }

        if (totalLoan <= 0 || totalLoan > MAX_LOAN_AMOUNT) {
            errors.add("Pinjaman harus > 0 dan maksimal 1 Miliar.");
        }

        double minPercent = vehicle.getCondition().equalsIgnoreCase("baru") ? 35.0 : 25.0;
        double actualPercent = (double) downPayment / totalLoan * 100;
        if (actualPercent < minPercent) {
            errors.add("DP minimal untuk kendaraan " + vehicle.getCondition() + " adalah " + minPercent + "%");
        }

        return errors;
    }

    @Override
    public Loan calculate(Loan loan) {
        Vehicle vehicle = loan.getVehicle();
        long principal = loan.getTotalLoanAmount() - loan.getDownPayment();
        int tenure = loan.getLoanTenure();

        List<Installment> installments = new ArrayList<>();

        double currentRate = (vehicle.getVehicleType().getDisplayVehicleName().equalsIgnoreCase("mobil")) ? 8.0 : 9.0;

//        for (int year = 1; year <= tenure; year++) {
//            if (year % 2 == 0) {
//                currentRate += 0.5;
//            } else {
//                currentRate += 0.1;
//            }

        for (int year = 1; year <= tenure; year++) {
            if (year > 1) {
                if (year % 2 == 0) {
                    currentRate += 0.1;
                } else {
                    currentRate += 0.5;
                }
            }
            double monthlyAmount = calculateMonthly(principal, currentRate, tenure);
            installments.add(new Loan.Installment(year, currentRate, monthlyAmount));
        }

        loan.setInstallment(installments);
        return loan;
    }

    private double calculateMonthly(long principal, double annualRate, int tenure) {
        int totalMonths = tenure * 12;
        double principalPerMonth = (double) principal / totalMonths;
        double interestPerMonth = (principal * (annualRate / 100.0)) / 12.0;
        return principalPerMonth + interestPerMonth;
    }
}
