/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * This class handles the GUI and functionality for when a user wants to buy an
 * investment
 *
 * @author Matias
 */
public class BuyActionListener implements ActionListener {

    private Portfolio p;

    private JComboBox type;
    private JTextField symbol;
    private JTextField name;
    private JTextField quantity;
    private JTextField price;

    /**
     *
     * @param p portfolio instance with JFrame
     */
    public BuyActionListener(Portfolio p) {
        this.p = p;
    }

    /**
     *
     * @param e actionEvent that fires when menu item is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        p.removeAllElements();

        // create the title of the screen
        p.centerPanel.setLayout(new BorderLayout());
        JTextArea buyInvestment = new JTextArea();
        buyInvestment.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        buyInvestment.setMargin(new Insets(10, 10, 0, 0));
        buyInvestment.setText("Buying an investment");
        buyInvestment.setEditable(false);

        // create the dropdown box for the type of investment
        p.centerPanel.add(buyInvestment, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(5, 2, 0, 10));
        fields.add(new JLabel("Type"));
        String[] typeArray = {"Stock", "Mutual Fund"};
        type = new JComboBox(typeArray);
        fields.add(type);

        // create the textField for the symbol
        fields.add(new JLabel("Symbol"));
        symbol = new JTextField();
        fields.add(symbol);

        // create the textField for the name
        fields.add(new JLabel("Name"));
        name = new JTextField();
        fields.add(name);

        // create the textField for the quantity
        fields.add(new JLabel("Quantity"));
        quantity = new JTextField();
        fields.add(quantity);

        // create the textField for the price
        fields.add(new JLabel("Price"));
        price = new JTextField();
        fields.add(price);

        // create an empty border for the fields for spacing on the screen
        Border border1 = BorderFactory.createLineBorder(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(10, 40, 10, 10)));
        p.centerPanel.add(fields, BorderLayout.CENTER);

        // create an empty border for the buttons
        p.eastPanel.setLayout(new GridLayout(2, 1, 0, 60));
        p.eastPanel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(50, 70, 50, 70)));

        // set functionality for the reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTextFields();
            }
        });

        // set functionality for the buy button
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // check if any required fields are empty
                    if (symbol.getText().isEmpty()) {
                        throw new EmptyStringException("symbol");
                    }
                    boolean stock = false;
                    if (type.getSelectedItem().equals("Stock")) {
                        stock = true;
                    } else if (type.getSelectedItem().equals("Mutual Fund")) {
                        stock = false;
                    }
                    if (name.getText().isEmpty()) {
                        throw new EmptyStringException("name");
                    }

                    // check if the numbers are valid or not
                    int numQuantity = Integer.parseInt(quantity.getText());
                    if (numQuantity <= 0) {
                        throw new NumberOutOfRangeException("quantity");
                    }
                    double numPrice = Double.parseDouble(price.getText());
                    if (numPrice <= 0) {
                        throw new NumberOutOfRangeException("price");
                    }

                    // create the investment to add to portfolio
                    Investment toBuy = null;
                    if (stock) {
                        toBuy = new Stock(symbol.getText(), name.getText(), numQuantity, numPrice);
                    } else {
                        toBuy = new MutualFund(symbol.getText(), name.getText(), numQuantity, numPrice);
                    }
                    buyInvestment(p.investments, p.map, toBuy);
                    p.messages.setText(p.messages.getText() + "\n" + "Successfully bought investment.");
                    clearTextFields();

                } catch (NumberFormatException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + "The quantity or price is invalid.");
                } catch (EmptyStringException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + ex.getMessage());
                } catch (NumberOutOfRangeException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + ex.getMessage());
                }
            }
        });

        p.eastPanel.add(resetButton);
        p.eastPanel.add(buyButton);

        // create the label for the messages text area
        p.southPanel.setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel();
        messageLabel.setText("Messages");
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        messageLabel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(0, 20, 0, 0)));
        p.southPanel.add(messageLabel, BorderLayout.NORTH);

        p.southPanel.add(p.scrollPane, BorderLayout.SOUTH);

        p.frame.revalidate();
        p.frame.repaint();
    }

    /**
     *
     * @param investments list of existing stocks and mutualFunds
     * @param map to update with new investments
     * @param toBuy investment that the user wants to add to their Portfolio
     */
    public void buyInvestment(ArrayList<Investment> investments, HashMap<String, ArrayList<Integer>> map, Investment toBuy) {

        int index = investments.indexOf(toBuy);

        // checks if it should add quantity to an existing investment or create a new one
        if (index != -1) {
            investments.get(index).addQuantityAtPrice(toBuy.getQuantity(), toBuy.getPrice());

        } else {
            investments.add(toBuy);
            String[] keys = toBuy.getName().split(" ");
            for (int i = 0; i < keys.length; i++) {
                Integer indexAdded = investments.size() - 1;
                if (map.get(keys[i]) == null) {
                    ArrayList<Integer> indexes = new ArrayList<Integer>();
                    indexes.add(indexAdded);
                    map.put(keys[i], indexes);

                } else {
                    map.get(keys[i]).add(indexAdded);
                }
            }
        }
    }

    public void clearTextFields() {
        // clear all textFields after adding a stock and resetting
        String emptyString = "";
        type.setSelectedIndex(0);
        symbol.setText(emptyString);
        name.setText(emptyString);
        quantity.setText(emptyString);
        price.setText(emptyString);
    }

}
