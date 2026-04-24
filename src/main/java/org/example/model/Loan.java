package org.example.model;

import java.util.List;

public class Loan {
    private Vehicle vehicle;
    private long totalLoanAmount;
    private int loanTenure;
    private long downPayment;
    private List<Installment> installment;

    public Loan(Vehicle vehicle, long totalLoanAmount, int loanTenure, long downPayment) {
        this.vehicle = vehicle;
        this.totalLoanAmount = totalLoanAmount;
        this.loanTenure = loanTenure;
        this.downPayment = downPayment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public long getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public long getDownPayment() {
        return downPayment;
    }

    public List<Installment> getInstallment() {
        return installment;
    }

    public void setInstallment(List<Installment> installment) {
        this.installment = installment;
    }

    public static class Installment {
        private final int year;
        private final double interestRate;
        private final double monthlyInstallment;

        public Installment(int year, double interestRate, double monthlyInstallment) {
            this.year = year;
            this.interestRate = interestRate;
            this.monthlyInstallment = monthlyInstallment;
        }
        public int getYear() {
            return year;
        }
        public double getInterestRate() {
            return interestRate;
        }
        public double getMonthlyInstallment() {
            return monthlyInstallment;
        }
    }
}
