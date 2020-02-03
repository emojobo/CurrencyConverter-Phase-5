package com.Company;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        String exchangeRate = Var1.substring(0,3) + "." + Var2.substring(0,3);
        Enumeration keys = exchange.propertyNames();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            if (key.equals(exchangeRate)) {
                result = input * Double.parseDouble(exchange.getProperty(exchangeRate));
                txtTo.setText("" + result);
                return result;
            }
        }

        return result;
    }

    public static void delete(JComboBox delete, Properties config) {
        String key = delete.getSelectedItem().toString().substring(0, 3);
        config.remove(key);
        delete.removeItem(delete.getSelectedItem());
        //currencyMap.remove(key);

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

    public static void addExchangeRate(Properties exchange, Properties currency, String abbrv) {

    }

    public static void edit() {

    }
}
