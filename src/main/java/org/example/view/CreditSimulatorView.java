package org.example.view;

import org.example.model.Loan;
import org.example.model.Vehicle;
import org.example.util.NumberUtils;
import org.example.util.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class CreditSimulatorView {
    private final Scanner scanner = new Scanner(System.in);
    private static final NumberFormat IDR_FORMAT = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public String readInput(String input) {
        System.out.print(input);
        return scanner.nextLine().trim();
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void showBanner() {
        println(StringUtils.repeat("═", 55));
        println("║         CREDIT SIMULATOR KENDARAAN               ║");
        println("║       Simulasi Cicilan Kendaraan Bermotor        ║");
        println(StringUtils.repeat("═", 55));
        println("");
    }

    public void showMenu() {
        println(StringUtils.repeat("-", 55));
        println("│                MENU UTAMA                 │");
        println(StringUtils.repeat("-", 55));
        println("│  [1] Simulasi Baru                        │");
        println("│  [2] Load Existing (dari API)             │");
        println("│  [3] Save Sheet                           │");
        println("│  [4] Switch Sheet                         │");
        println("│  [help] Tampilkan semua perintah          │");
        println("│  [0] Keluar                               │");
        println(StringUtils.repeat("═", 55));
        System.out.print("Pilih: ");
    }

    public void showHelp() {
        println("");
        println(StringUtils.repeat("-", 55));
        println("│          DAFTAR PERINTAH YANG TERSEDIA                │");
        println(StringUtils.repeat("-", 55));
        println("│  1           Mulai simulasi baru                      │");
        println("│  2           Load data dari API dan hitung otomatis   │");
        println("│  3           Save sheet                               │");
        println("│  4           Ganti sheet yang sudah ada               │");
        println(StringUtils.repeat("-", 55));
        println("│          JALANKAN DENGAN FILE INPUT:                  │");
        println("│          ./credit_simulator file_inputs.txt           │");
        println("│          bin/credit_simulator file_inputs.txt         │");
        println(StringUtils.repeat("═", 55));
        println("");
    }

    public void displayResult(Loan loan) {
        Vehicle v = loan.getVehicle();
        println("");
        println(StringUtils.repeat("═", 55));
        println("   HASIL SIMULASI CICILAN");
        println(StringUtils.repeat("═", 55));
        System.out.printf("   Kendaraan : %s (%s)",
                v.getVehicleType().getDisplayVehicleName(), StringUtils.capitalize(v.getCondition()));
        System.out.printf("   Tahun : %d%n", v.getYear());
        System.out.printf("   Pinjaman  : %s%n", IDR_FORMAT.format(loan.getTotalLoanAmount()));
        System.out.printf("   DP        : %s%n", IDR_FORMAT.format(loan.getDownPayment()));
        System.out.printf("   Pokok     : %s%n", IDR_FORMAT.format(loan.getTotalLoanAmount() - loan.getDownPayment()));
        System.out.printf("   Tenor     : %d tahun%n", loan.getLoanTenure());
        println(StringUtils.repeat("-", 55));

        for (Loan.Installment installment : loan.getInstallment()) {
            System.out.printf("   tahun %-2d  : %s/bln , Suku Bunga : %s%%%n",
                    installment.getYear(),
                    IDR_FORMAT.format((long) installment.getMonthlyInstallment()),
                    NumberUtils.formatRate(installment.getInterestRate()));
        }

        println(StringUtils.repeat("═", 55));
        println("");
    }
}
