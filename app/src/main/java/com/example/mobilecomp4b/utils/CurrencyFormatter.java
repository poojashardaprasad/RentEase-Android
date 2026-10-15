package com.example.mobilecomp4b.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public static String formatInr(long amount) {
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("IN").build();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    public static String formatRent(long amount) {
        return formatInr(amount) + " / month";
    }

    public static String formatDeposit(long amount) {
        return "Deposit: " + formatInr(amount);
    }
}
