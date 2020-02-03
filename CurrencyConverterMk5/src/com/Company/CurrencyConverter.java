package com.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class CurrencyConverter extends JPanel implements ActionListener, ItemListener{
    private static JFrame converter, exchangeFrame;
    private static JTabbedPane tab;
    private static JComboBox convertFrom, convertTo, deleteDrop, editDrop;
    private static JTextField txtFrom, txtTo, abbrvTxt, currTxt, exchTxt;
    private static JButton compute, exit, add, edit, delete, confirm, back;
    private static JLabel from, to, lblFrom, lblTo, abbrvLbl, currLbl, exchLbl, title;

    private double input = 0;
    private double result = 0;

    private String abbrv;
    private String currency;
    private String rate;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                constructGui();
            }
        });
    }

    private CurrencyConverter() {
        super(new GridLayout(1, 2));
        tab = new JTabbedPane();

        Component convert = convertTab();
        Component add = addTab();
        Component edit = editTab();
        Component remove = removeTab();

        tab.add("Convert", convert);
        tab.add("Add", add);
        tab.add("Edit", edit);
        tab.add("Remove", remove);

        add(tab);
        tab.setVisible(true);
    }

    private static void constructGui() {
        converter = new JFrame();
        converter.add(new CurrencyConverter(), BorderLayout.CENTER);

        converter.pack();
        converter.setTitle("Currency Converter");
        converter.setVisible(true);
        converter.setBackground(Color.lightGray);
        converter.setSize(500, 400);
        converter.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel convertTab() {
        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        lblFrom = new JLabel("From:");
        lblFrom.setForeground(Color.RED);
        functionPanel.add(lblFrom);

        convertFrom = new JComboBox();
        ConverterFunctions.populateConvertFrom(convertFrom);
        convertFrom.setEditable(false);
        functionPanel.add(convertFrom);

        lblTo = new JLabel("To:");
        lblTo.setForeground(Color.RED);
        functionPanel.add(lblTo);

        convertTo = new JComboBox();
        ConverterFunctions.populateCurrencyDropdown(convertTo);
        functionPanel.add(convertTo);

        from = new JLabel("Enter Amount to Convert:");
        from.setText("Enter Amount to Convert:");
        functionPanel.add(from);

        txtFrom = new JTextField(22);
        functionPanel.add(txtFrom);

        to = new JLabel("Total Amount Converted:");
        to.setText("Total Amount Converted:");
        functionPanel.add(to);

        txtTo = new JTextField(22);
        txtTo.setEditable(false);
        txtTo.setForeground(Color.RED);
        functionPanel.add(txtTo);

        compute = new JButton("Compute");
        functionPanel.add(compute);

        exit = new JButton("Exit");
        functionPanel.add(exit);

        convertFrom.addItemListener(this);
        convertTo.addItemListener(this);
        compute.addActionListener(this);
        txtFrom.addActionListener(this);
        exit.addActionListener(this);

        return functionPanel;
    }

    private JPanel addTab() {
        JPanel functionPanel = new JPanel();

        abbrvLbl = new JLabel();
        abbrvLbl.setText("Enter abbreviation of the new currency:");
        functionPanel.add(abbrvLbl);

        abbrvTxt = new JTextField();
        abbrvTxt.setColumns(16);
        functionPanel.add(abbrvTxt);

        currLbl = new JLabel();
        currLbl.setText("Enter the description of new currency:");
        functionPanel.add(currLbl);

        currTxt = new JTextField();
        currTxt.setColumns(17);
        functionPanel.add(currTxt);

        exchLbl = new JLabel();
        exchLbl.setText("Enter the exchange rate from new to USD:");
        functionPanel.add(exchLbl);

        exchTxt = new JTextField();
        exchTxt.setColumns(16);
        functionPanel.add(exchTxt);

        add = new JButton("Add");
        functionPanel.add(add);

        abbrvTxt.addActionListener(this);
        currTxt.addActionListener(this);
        exchTxt.addActionListener(this);
        add.addActionListener(this);

        return functionPanel;
    }

    private JPanel editTab() {

        JPanel functionPanel = new JPanel();

        return functionPanel;
    }

    private JPanel removeTab() {
        JPanel functionPanel = new JPanel();

        lblFrom = new JLabel("Select currency to remove:");
        lblFrom.setForeground(Color.RED);
        functionPanel.add(lblFrom);

        deleteDrop = new JComboBox();
        ConverterFunctions.populateCurrencyDropdown(deleteDrop);
        functionPanel.add(deleteDrop);

        delete = new JButton("Delete");
        functionPanel.add(delete);

        deleteDrop.addActionListener(this);
        delete.addActionListener(this);

        return functionPanel;
    }

    public void actionPerformed(ActionEvent e) {
        Properties exchange = new Properties();
        Properties curr = new Properties();

        try (InputStream stream = CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties")) {
            curr.load(CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties"));

            try (InputStream flow = CurrencyConverter.class.getClassLoader().getResourceAsStream("exchangeRate.properties")) {
                exchange.load(CurrencyConverter.class.getClassLoader().getResourceAsStream("exchangeRate.properties"));

                switch (e.getActionCommand()) {
                    case "Compute":
                        if (!txtFrom.getText().isEmpty()) {
                            input = Double.parseDouble(txtFrom.getText());
                        }
                        ConverterFunctions.convert(input, result, exchange, convertFrom, convertTo, txtTo);
                        converter.revalidate();
                        break;
                    case "Add":
                        abbrv = abbrvTxt.getText();
                        currency = currTxt.getText();
                        rate = exchTxt.getText();

                        ConverterFunctions.add(abbrv, currency, curr);
                        ConverterFunctions.addExchangeRate(exchange, curr, abbrv, rate);

                        convertTo.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);
                        deleteDrop.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(deleteDrop, curr);

                        tab.revalidate();
                        tab.repaint();
                        break;
                    case "Confirm":
                        String exchangeRate = txtFrom.getText();
                        ConverterFunctions.addExchangeRate(exchange, curr, abbrv, rate);
                        break;
                    case "Delete":
                        ConverterFunctions.delete(deleteDrop, curr);

                        convertTo.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);

                        tab.revalidate();
                        tab.repaint();
                        break;
                    case "Exit":
                        int choice = JOptionPane.showConfirmDialog(null, "Do you want to quit?",
                                "Exit", JOptionPane.YES_NO_OPTION);

                        if (choice == 0) {
                            converter.dispose();
                            System.exit(0);
                        }
                        break;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void itemStateChanged(ItemEvent e) {

    }
}
