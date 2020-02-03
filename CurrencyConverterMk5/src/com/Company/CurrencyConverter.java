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
    private static JTextField txtFrom, txtTo, abbrvTxt, currTxt;
    private static JButton compute, exit, add, edit, delete, confirm, back;
    private static JLabel from, to, lblFrom, lblTo, abbrvLbl, currLbl, title;

    private double input = 0;
    private double result = 0;

    private String abbrv;
    private String currency;

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
        ConverterFunctions.populateCurrencyDropdown(convertFrom);
        convertFrom.setSelectedItem(convertFrom.getSelectedItem().equals("USD:United States Dollar"));
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
        abbrvLbl.setText("Enter abbreviation of new currency:");
        functionPanel.add(abbrvLbl);

        abbrvTxt = new JTextField();
        abbrvTxt.setColumns(15);
        functionPanel.add(abbrvTxt);

        currLbl = new JLabel();
        currLbl.setText("Enter the description of new currency:");
        functionPanel.add(currLbl);

        currTxt = new JTextField();
        currTxt.setColumns(20);
        functionPanel.add(currTxt);

        add = new JButton("Add");
        functionPanel.add(add);

        abbrvTxt.addActionListener(this);
        currTxt.addActionListener(this);
        add.addActionListener(this);

        return functionPanel;
    }

    private JFrame addExchangeRateFrame(Properties exchange, Properties currency, String abbrv) {
        exchangeFrame = new JFrame();
        exchangeFrame.setVisible(true);
        exchangeFrame.setLayout(null);
        exchangeFrame.setBackground(Color.lightGray);
        exchangeFrame.setSize(500, 400);
        exchangeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        title = new JLabel();
        title.setText("Input the exchange rates for the new currency");
        title.setBounds(100, 15, 300, 30);
        exchangeFrame.add(title);

        Integer y = 45;
        Set<String> prop = currency.stringPropertyNames();
        Iterator<String> itr = prop.iterator();
        while (itr.hasNext()) {
            if(!itr.equals(abbrv)) {
                lblFrom = new JLabel("Input the exchange rate for " + abbrv + " to " + itr.next());
                lblFrom.setBounds(30, y, 230, 30);
                exchangeFrame.add(lblFrom);

                y = y + 25;

                txtFrom = new JTextField(15);
                txtFrom.setBounds(30, y, 230, 30);
                exchangeFrame.add(txtFrom);

                lblTo = new JLabel("Input the exchange rate for " + itr.next() + " to " + abbrv);
                lblTo.setBounds(280, y, 230, 30);
                exchangeFrame.add(lblTo);

                y = y + 25;

                txtFrom = new JTextField(15);
                txtFrom.setBounds(280, y, 230, 30);
                exchangeFrame.add(txtFrom);

                y = y + 25;
            }
        }

        confirm = new JButton("Confirm");
        confirm.setBounds(30, y+25, 150, 30);
        exchangeFrame.add(confirm);

        back = new JButton("Back");
        back.setBounds(190, y+25, 150, 30);
        exchangeFrame.add(back);

        txtFrom.addActionListener(this);
        confirm.addActionListener(this);
        back.addActionListener(this);

        return exchangeFrame;
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

                        ConverterFunctions.add(abbrv, currency, curr);
                        convertFrom.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(convertFrom, curr);
                        convertTo.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);
                        deleteDrop.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(deleteDrop, curr);

                        addExchangeRateFrame(exchange, curr, abbrv);

                        tab.revalidate();
                        tab.repaint();
                        break;
                    case "Confirm":
                        String exchangeRate = txtFrom.getText();
                        ConverterFunctions.addExchangeRate(exchange, curr, abbrv);
                        break;
                    case "Delete":
                        ConverterFunctions.delete(deleteDrop, curr);

                        convertFrom.removeAllItems();
                        ConverterFunctions.refreshCurrencyDropdown(convertFrom, curr);
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
