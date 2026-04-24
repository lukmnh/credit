package org.example.controller;

import org.example.factory.VehicleFactory;
import org.example.model.Loan;
import org.example.model.Vehicle;
import org.example.service.LoanService;
import org.example.service.WebLoaderService;
import org.example.util.NumberUtils;
import org.example.view.CreditSimulatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditSimulatorController {
    private final CreditSimulatorView view;
    private final LoanService loanService;
    private final WebLoaderService webLoaderService;

    private Map<String, List<Loan>> sheets = new HashMap<>();
    private String currentSheet = "default";
    private List<Loan> currentData = new ArrayList<>();

    public CreditSimulatorController(CreditSimulatorView view, LoanService loanService, WebLoaderService webLoaderService) {
        this.view = view;
        this.loanService = loanService;
        this.webLoaderService = webLoaderService;
        this.sheets.put(currentSheet, currentData);
    }

    public void start() {
        handleCheckStatus(true);
        view.showBanner();
        boolean running = true;
        while (running) {
            view.showMenu();
            String input = view.readInput("").toLowerCase();

            switch (input) {
                case "1", "new"    -> handleNewSimulation();
                case "2", "load"   -> loadDataFromApi();
                case "3", "save"   -> saveSheet();
                case "4", "switch" -> switchSheet();
                case "0", "exit", "quit" -> {
                    running = false;
                    view.println("\nTerima kasih. Sampai jumpa!");
                }
                case "help", "show" -> view.showHelp();
                default -> view.println("Pilihan tidak valid. Ketik 'help' untuk daftar perintah.");
            }
        }
    }

    public void startWithFile(List<Loan> loans) {
        view.showBanner();
        view.println("Mode file input: " + loans.size() + " data ditemukan.\n");
        for (Loan loan : loans) {
            processAndDisplay(loan);
        }
    }

    private void handleNewSimulation() {
        view.println("\n=== SIMULASI BARU ===");
        try {
            String type = view.readInput("Jenis Kendaraan (Mobil/Motor): ");
            if (!type.equalsIgnoreCase("mobil") && !type.equalsIgnoreCase("motor")) {
                throw new IllegalArgumentException("Jenis kendaraan harus 'Mobil' atau 'Motor'");
            }
            String condition = view.readInput("Kondisi (Baru/Bekas): ");
            if(!condition.equalsIgnoreCase("baru") &&  !condition.equalsIgnoreCase("bekas")) {
                throw new IllegalArgumentException("Kondisi harus 'Baru' atau 'Bekas'");
            }
            int year = NumberUtils.parseIntSafe(view.readInput("Tahun Kendaraan: "), "Tahun");
            Vehicle vehicle = VehicleFactory.createVehicle(type, condition, year);
            long totalLoan = NumberUtils.parseLongSafe(view.readInput("Jumlah Pinjaman Total: "), "Pinjaman");
            int tenure = NumberUtils.parseIntSafe(view.readInput("Tenor (1-6 thn): "), "Tenor");
            long dp = NumberUtils.parseLongSafe(view.readInput("Jumlah DP: "), "DP");

            Loan loan = new Loan(vehicle, totalLoan, tenure, dp);
            processAndDisplay(loan);
        } catch (Exception e) {
            view.println("Input tidak valid: " + e.getMessage());
        }
    }

    private void loadDataFromApi() {
        try {
            Loan loan = webLoaderService.fetchLoanData();
            view.println("Data berhasil diambil.\n");
            processAndDisplay(loan);
        } catch (Exception e) {
            view.println("Gagal mengambil data dari API: " + e.getMessage());
        }
    }

    private void processAndDisplay(Loan loan) {
        currentData.add(loan);
        List<String> errors = loanService.validate(
                loan.getVehicle(), loan.getTotalLoanAmount(), loan.getLoanTenure(), loan.getDownPayment());

        if (!errors.isEmpty()) {
            view.println("Terdapat kesalahan:");
            for (String err : errors) view.println("* " + err);
            return;
        }

        loanService.calculate(loan);
        view.displayResult(loan);
    }

    private void saveSheet() {
        String name = view.readInput("Nama sheet: ");
        if (name.isEmpty()) {
            view.println("Nama tidak boleh kosong!");
            return;
        }
        sheets.put(name, new ArrayList<>(currentData));
        currentSheet = name;
        view.println("Sheet disimpan & aktif: " + name);
    }

    private void switchSheet() {
        view.println("Daftar sheet:");
        for (String s : sheets.keySet()) view.println("- " + s);

        String name = view.readInput("Pilih sheet: ");
        if (!sheets.containsKey(name)) {
            view.println("Sheet tidak ditemukan!");
            return;
        }
        currentSheet = name;
        currentData = sheets.get(name);
        view.println("Berpindah ke sheet: " + name);
    }

    private void handleCheckStatus(boolean interactive) {
        if (interactive) view.println("\n=== SYSTEM STATUS CHECK ===");

        boolean apiReady = webLoaderService.isApiReady();
        boolean serviceReady = (loanService != null);

        if (interactive) {
            view.println("1. HTTP Status    : " + (apiReady ? "Ready" : "Offline"));
            view.println("2. Loan Service   : " + (serviceReady ? "Ready" : "Offline"));
            view.println("3. Active Sheet   : [" + currentSheet + "]");

            if (apiReady) {
                view.println("\n[KESIMPULAN] Sistem siap untuk Load Data Otomatis.");
            } else {
                view.println("\n[KESIMPULAN] API bermasalah. Gunakan input manual (Menu 2).");
            }
        }
    }
}
