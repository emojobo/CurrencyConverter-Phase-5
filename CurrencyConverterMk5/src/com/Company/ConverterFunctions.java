package com.Company;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConverterFunctions {
    public static void populateCurrencyDropdown(JComboBox convert) {
        Properties curr = new Properties();
        Map<String, String> currencyMap = new HashMap<String, String>();

        try (InputStream stream = CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties")) {
            curr.load(CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties"));
            Enumeration keys = curr.propertyNames();

            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = curr.getProperty(key);
                currencyMap.put(key, value);
            }

            for (Map.Entry mapElement : currencyMap.entrySet()) {
                String key = (String) mapElement.getKey();
                String value = (String) mapElement.getValue();
                String dropdownValue = key + ": " + value;
                convert.addItem(dropdownValue);
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void refreshCurrencyDropdown(JComboBox currency, Properties curr) {
        Map<String, String> currencyMap = new HashMap<String, String>();
        Enumeration keys = curr.propertyNames();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = curr.getProperty(key);
            currencyMap.put(key, value);
        }

        for (Map.Entry mapElement : currencyMap.entrySet()) {
            String key = (String) mapElement.getKey();
            String value = (String) mapElement.getValue();
            String dropdownValue = key + ": " + value;
            currency.addItem(dropdownValue);
        }
    }

    public static double convert(double input, double result, Properties exchange, JComboBox convertFrom, JComboBox convertTo, JTextField txtTo) {
        String Var1 = (String)convertFrom.getSelectedItem();
        String Var2 = (String)convertTo.getSelectedItem();
        String exchangeRate = "";
        String exchangeRate1 = Var1.substring(0,3);
        String exchangeRate2 = Var2.substring(0,3);

        if(Var1.contains("USD")) {
            exchangeRate = exchangeRate2;
        }
        else {
            exchangeRate = exchangeRate1;
        }

        Enumeration keys = exchange.propertyNames();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            if (key.equals(exchangeRate)) {
                if(Var1.contains("USD")) {
                    result = input * Double.parseDouble(exchange.getProperty(exchangeRate2));
                }
                else if(Var2.contains("USD")) {
                    result = input / Double.parseDouble(exchange.getProperty(exchangeRate1));
                }
                else {
                    Double from = Double.parseDouble(exchange.getProperty(exchangeRate1));
                    Double to = Double.parseDouble(exchange.getProperty(exchangeRate2));
                    Double rate = to / from;

                    result = input * rate;
                }

                DecimalFormat df = new DecimalFormat();
                df.setRoundingMode(RoundingMode.DOWN);
                String product = df.format(result);
                txtTo.setText("" + product);
                return Double.parseDouble(product);
            }
        }

        return result;
    }

    public static void delete(JComboBox delete, Properties config) {
        String key = delete.getSelectedItem().toString().substring(0, 3);
        config.remove(key);
        delete.removeItem(delete.getSelectedItem());

        try {
            OutputStream os = new FileOutputStream("currencies.properties");
            config.store(os, null);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void add(String abbrv, String curr, Properties currency) {
        currency.setProperty(abbrv, curr);

        try {
            OutputStream os = new FileOutputStream("currencies.properties");
            currency.store(os, null);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void addExchangeRate(Properties exchange, String abbrv, String rate) {
        exchange.setProperty(abbrv, rate);

        try {
            OutputStream os = new FileOutputStream("exchangeRate.properties");
            exchange.store(os, null);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void edit(Properties curr, Properties exchange, String selected, String abbrv, String desc, String rate) {
        String key = selected.substring(0,3);

        if(abbrv.isEmpty()) {
            abbrv = key;
        }
        else if(desc.isEmpty()) {
            desc = curr.getProperty(key);
        }
        else if(rate.isEmpty()) {
            rate = exchange.getProperty(key);
        }

        curr.remove(key);
        add(abbrv, desc, curr);

        exchange.remove(key);
        addExchangeRate(exchange, abbrv, rate);
    }

    public static void isNumber(JTextField txt) {
        if(!txt.getText().matches(".*\\d.*") || txt.getText().contains("-")) {
            int choice = JOptionPane.showConfirmDialog(null, "Please input a positive number",
                    "Inproper Input", JOptionPane.DEFAULT_OPTION);

            if(choice == 0) {
                txt.setText("");
            }
        }
    }

    public static void isString(JTextField txt) {
        if(txt.getText().matches(".*\\d.*")) {
            int choice = JOptionPane.showConfirmDialog(null, "Please input a text description",
                    "Inproper Input", JOptionPane.DEFAULT_OPTION);

            if(choice == 0) {
                txt.setText("");
            }
        }
    }

    public static void isProperFormat(JTextField txt) {
        if(txt.getText().length() > 3) {
            int choice = JOptionPane.showConfirmDialog(null, "Please input a 3 letter abbreviation for the currency",
                    "Inproper Input", JOptionPane.DEFAULT_OPTION);

            if(choice == 0) {
                txt.setText("");
            }
        }
    }
}
