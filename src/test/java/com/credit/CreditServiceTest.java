package com.credit;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;
import org.example.service.impl.LoanServiceImpl;
import org.example.service.impl.WebLoaderServiceImpl;
import org.example.util.FileUtils;

import java.util.List;

public class CreditServiceTest {
    public static void main(String[] args){
        int passed = 0;
        int failed = 0;

        if(testLoanCalculationMobil()) passed++; else failed++;
        if(testLoanCalculationMotor()) passed++; else failed++;
        if(testLoanCalculationFromFile()) passed++;  else failed++;
        if(testLoanCalculationFromApi())  passed++;  else failed++;

        System.out.println("\n===========================");
        System.out.println("TOTAL PASSED: " + passed);
        System.out.println("TOTAL FAILED: " + failed);
        System.out.println("===========================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean testLoanCalculationMobil() {
        try {
            Vehicle v = VehicleFactory.createVehicle("Mobil", "Baru", 2025);
            Loan loan = new Loan(v, 100_000_000L, 6, 40_000_000L);

            LoanServiceImpl service = new LoanServiceImpl();
            service.calculate(loan);

            if (loan.getInstallment() == null || loan.getInstallment().isEmpty()) {
                throw new RuntimeException("Installment kosong");
            }

            for (Loan.Installment i : loan.getInstallment()) {
                if (i.getMonthlyInstallment() <= 0) {
                    throw new RuntimeException("Cicilan bulan ke-" + i.getYear() + " tidak boleh <= 0");
                }
            }

            System.out.println("PASSED");
            return true;
        } catch (RuntimeException e) {
            System.out.println("FAILED: " + e.getMessage());
            return false;
        }
    }

    private static boolean testLoanCalculationMotor() {
        try {
            Vehicle v = VehicleFactory.createVehicle("Motor", "Bekas", 2022);
            Loan loan = new Loan(v, 30_000_000L, 6, 9_000_000L);

            LoanServiceImpl service = new LoanServiceImpl();
            service.calculate(loan);

            if (loan.getInstallment() == null || loan.getInstallment().isEmpty()) {
                throw new RuntimeException("Installment kosong");
            }

            for (Loan.Installment i : loan.getInstallment()) {
                if (i.getMonthlyInstallment() <= 0) {
                    throw new RuntimeException("Cicilan bulan ke-" + i.getYear() + " tidak boleh <= 0");
                }
            }

            System.out.println("PASSED");
            return true;
        } catch (RuntimeException e) {
            System.out.println("FAILED: " + e.getMessage());
            return false;
        }
    }

    private static boolean testLoanCalculationFromFile() {
        try {
            List<Loan> loans = FileUtils.parseFile("file_inputs.txt");

            if (loans == null || loans.isEmpty()) {
                throw new RuntimeException("Hasil parse file kosong");
            }

            LoanServiceImpl service = new LoanServiceImpl();
            for (Loan loan : loans) {
                service.calculate(loan);

                if (loan.getInstallment() == null || loan.getInstallment().isEmpty()) {
                    throw new RuntimeException("Installment kosong untuk loan dari file");
                }

                for (Loan.Installment i : loan.getInstallment()) {
                    if (i.getMonthlyInstallment() <= 0) {
                        throw new RuntimeException("Cicilan tidak valid di loan dari file");
                    }
                }
            }

            System.out.println("PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            return false;
        }
    }

    private static boolean testLoanCalculationFromApi() {
        try {
            WebLoaderServiceImpl webLoader = new WebLoaderServiceImpl();
            List<Loan> loans = webLoader.fetchLoanData();

            if (loans == null || loans.isEmpty()) {
                throw new RuntimeException("Data dari API kosong");
            }

            LoanServiceImpl service = new LoanServiceImpl();
            for (Loan loan : loans) {
                service.calculate(loan);

                if (loan.getInstallment() == null || loan.getInstallment().isEmpty()) {
                    throw new RuntimeException("Installment kosong untuk loan dari API");
                }

                for (Loan.Installment i : loan.getInstallment()) {
                    if (i.getMonthlyInstallment() <= 0) {
                        throw new RuntimeException("Cicilan tidak valid di loan dari API");
                    }
                }
            }

            System.out.println("PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            return false;
        }
    }
}
