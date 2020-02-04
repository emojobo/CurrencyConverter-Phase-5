package com.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CurrencyConverter extends JPanel implements ActionListener, ItemListener{
    private static JFrame converter;
    private static JTabbedPane tab;
    private static JComboBox convertFrom, convertTo, deleteDrop, editDrop, abbrvDrop, descDrop, rateDrop;
    private static JTextField txtFrom, txtTo, abbrvTxt, currTxt, exchTxt, editAbbrv, editDesc, editRate;
    private static JButton compute, exit, add, delete, confirm;
    private static JLabel from, to, lblFrom, lblTo, abbrvLbl, currLbl, exchLbl, editLbl, editAbbrvLbl, editDescLbl, editRateLbl, title;

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

        Properties exchange = new Properties();
        Properties curr = new Properties();

        try (InputStream stream = CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties")) {
            curr.load(CurrencyConverter.class.getClassLoader().getResourceAsStream("currencies.properties"));

            try (InputStream flow = CurrencyConverter.class.getClassLoader().getResourceAsStream("exchangeRate.properties")) {
                exchange.load(CurrencyConverter.class.getClassLoader().getResourceAsStream("exchangeRate.properties"));

                tab = new JTabbedPane();

                Component convert = convertTab(curr, exchange);
                Component add = addTab(curr, exchange);
                Component edit = editTab(curr, exchange);
                Component remove = removeTab(curr);

                tab.add("Convert", convert);
                tab.add("Add", add);
                tab.add("Edit", edit);
                tab.add("Remove", remove);

                add(tab);
                tab.setVisible(true);
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private static void constructGui() {
        converter = new JFrame();
        converter.add(new CurrencyConverter(), BorderLayout.CENTER);

        converter.pack();
        converter.setTitle("Currency Converter");
        converter.setVisible(true);
        converter.setBackground(Color.lightGray);
        converter.setSize(500, 300);
        converter.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel convertTab(Properties curr, Properties exchange) {
        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        lblFrom = new JLabel("From:");
        lblFrom.setForeground(Color.RED);
        functionPanel.add(lblFrom);

        convertFrom = new JComboBox();
        ConverterFunctions.populateCurrencyDropdown(convertFrom);
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

        convertFrom.addActionListener(this);
        convertTo.addActionListener(this);
        compute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txtFrom.getText().isEmpty()) {
                    ConverterFunctions.isNumber(txtFrom);
                    input = Double.parseDouble(txtFrom.getText());
                }
                ConverterFunctions.convert(input, result, exchange, convertFrom, convertTo, txtTo);
                converter.revalidate();
            }
        });
        txtFrom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isNumber(txtFrom);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Do you want to quit?",
                        "Exit", JOptionPane.YES_NO_OPTION);

                if (choice == 0) {
                    converter.dispose();
                    System.exit(0);
                }
            }
        });

        return functionPanel;
    }

    private JPanel addTab(Properties curr, Properties exchange) {
        JPanel functionPanel = new JPanel();

        title = new JLabel();
        title.setText("Hit the Enter key after typing into each input field!");
        title.setForeground(Color.RED);
        functionPanel.add(title);

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

        abbrvTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isProperFormat(abbrvTxt);
            }
        });
        currTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isString(currTxt);
            }
        });
        exchTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isNumber(exchTxt);
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abbrv = abbrvTxt.getText();
                currency = currTxt.getText();
                rate = exchTxt.getText();

                ConverterFunctions.add(abbrv, currency, curr);
                ConverterFunctions.addExchangeRate(exchange, abbrv, rate);

                int choice = JOptionPane.showConfirmDialog(null, "This currency was successfully added!",
                        "Operation Completed!", JOptionPane.DEFAULT_OPTION);

                if(choice == 0) {
                    abbrvTxt.setText("");
                    currTxt.setText("");
                    exchTxt.setText("");
                }

                convertFrom.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertFrom, curr);
                convertTo.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);
                deleteDrop.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(deleteDrop, curr);

                tab.revalidate();
                tab.repaint();
            }
        });

        return functionPanel;
    }

    private JPanel editTab(Properties curr, Properties exchange) {
        JPanel functionPanel = new JPanel();

        editLbl = new JLabel();
        editLbl.setText("Which currency would you like to edit?");
        editLbl.setForeground(Color.RED);
        functionPanel.add(editLbl);

        editDrop = new JComboBox();
        ConverterFunctions.populateCurrencyDropdown(editDrop);
        functionPanel.add(editDrop);

        editAbbrvLbl = new JLabel();
        editAbbrvLbl.setText("Would you like to change the abbreviation for the currency? Select Y to change.");
        functionPanel.add(editAbbrvLbl);

        editAbbrv = new JTextField();
        editAbbrv.setColumns(15);
        functionPanel.add(editAbbrv);

        abbrvDrop = new JComboBox();
        abbrvDrop.addItem("Y");
        abbrvDrop.addItem("N");
        functionPanel.add(abbrvDrop);

        editDescLbl = new JLabel();
        editDescLbl.setText("Would you like to change the description for the currency? Select Y to change.");
        functionPanel.add(editDescLbl);

        editDesc = new JTextField();
        editDesc.setColumns(15);
        functionPanel.add(editDesc);

        descDrop = new JComboBox();
        descDrop.addItem("Y");
        descDrop.addItem("N");
        functionPanel.add(descDrop);

        editRateLbl = new JLabel();
        editRateLbl.setText("Would you like to change the exchange rate for the currency? Select Y to change.");
        functionPanel.add(editRateLbl);

        editRate = new JTextField();
        editRate.setColumns(15);
        functionPanel.add(editRate);

        rateDrop = new JComboBox();
        rateDrop.addItem("Y");
        rateDrop.addItem("N");
        functionPanel.add(rateDrop);

        confirm = new JButton("Confirm");
        confirm.setPreferredSize(new Dimension(260, 25));
        functionPanel.add(confirm);

        abbrvDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox abbrvDrop = (JComboBox) e.getSource();
                Object selected = abbrvDrop.getSelectedItem();

                if(selected.toString().equals("N")) {
                    editAbbrv.setEditable(false);
                }
                else {
                    editAbbrv.setEditable(true);
                }
            }
        });
        descDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox descDrop = (JComboBox) e.getSource();
                Object selected = descDrop.getSelectedItem();

                if(selected.toString().equals("N")) {
                    editDesc.setEditable(false);
                }
                else {
                    editDesc.setEditable(true);
                }
            }
        });
        rateDrop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox rateDrop = (JComboBox) e.getSource();
                Object selected = rateDrop.getSelectedItem();

                if(selected.toString().equals("N")) {
                    editRate.setEditable(false);
                }
                else {
                    editRate.setEditable(true);
                }
            }
        });
        editAbbrv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isProperFormat(editAbbrv);
            }
        });
        editDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isString(editDesc);
            }
        });
        editRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.isNumber(editRate);
            }
        });
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)editDrop.getSelectedItem();
                String abbrv = editAbbrv.getText();
                String desc = editDesc.getText();
                String rate = editRate.getText();

                ConverterFunctions.edit(curr, exchange, selected, abbrv, desc, rate);

                int choice = JOptionPane.showConfirmDialog(null, "This currency was successfully edited!",
                        "Operation Completed!", JOptionPane.DEFAULT_OPTION);

                if(choice == 0) {
                    editAbbrv.setText("");
                    editDesc.setText("");
                    editRate.setText("");
                }

                convertFrom.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertFrom, curr);
                convertTo.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);
                editDrop.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(editDrop, curr);
                deleteDrop.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(deleteDrop, curr);

                tab.revalidate();
                tab.repaint();
            }
        });

        return functionPanel;
    }

    private JPanel removeTab(Properties curr) {
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
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConverterFunctions.delete(deleteDrop, curr);

                convertFrom.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertFrom, curr);
                convertTo.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(convertTo, curr);
                editDrop.removeAllItems();
                ConverterFunctions.refreshCurrencyDropdown(editDrop, curr);

                tab.revalidate();
                tab.repaint();
            }
        });

        return functionPanel;
    }

    public void actionPerformed(ActionEvent e) {

    }

    public void itemStateChanged(ItemEvent e) {

    }
}
