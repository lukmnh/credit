package org.example;

import org.example.controller.CreditSimulatorController;
import org.example.model.Loan;
import org.example.service.impl.LoanServiceImpl;
import org.example.service.impl.WebLoaderServiceImpl;
import org.example.util.FileUtils;
import org.example.view.CreditSimulatorView;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CreditSimulatorView view = new CreditSimulatorView();
        LoanServiceImpl loanService = new LoanServiceImpl();
        WebLoaderServiceImpl webLoader = new WebLoaderServiceImpl();

        CreditSimulatorController controller = new CreditSimulatorController(view, loanService, webLoader);
        if (args.length > 0) {
            String filePath = args[0];
            try {
                List<Loan> loans = FileUtils.parseFile(filePath);

                if (loans.isEmpty()) {
                    System.err.println("File '" + filePath + "' tidak mengandung data valid.");
                    System.exit(1);
                }
                controller.startWithFile(loans);
            } catch (Exception e) {
                System.err.println("Gagal menjalankan mode file: " + e.getMessage());
                System.exit(1);
            }
        } else {
            controller.start();
        }
    }
}