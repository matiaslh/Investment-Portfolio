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
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * This class handles GUI and functionality for selling investments
 *
 * @author Matias
 */
public class SellActionListener implements ActionListener {

    private Portfolio p;

    private JTextField symbol;
    private JTextField quantity;
    private JTextField price;

    /**
     *
     * @param p portfolio instance with JFrame
     */
    public SellActionListener(Portfolio p) {
        this.p = p;
    }

    /**
     *
     * @param e actionEvent that fires when menu item is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        p.removeAllElements();

        // creates a title for the screen
        p.centerPanel.setLayout(new BorderLayout());
        JTextArea sellInvestment = new JTextArea();
        sellInvestment.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        sellInvestment.setMargin(new Insets(10, 10, 0, 0));
        sellInvestment.setText("Selling an investment");
        sellInvestment.setEditable(false);

        // creates the panel for the text fields
        p.centerPanel.add(sellInvestment, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(3, 2, 0, 35));

        // creates the text field for the symbol
        fields.add(new JLabel("Symbol"));
        symbol = new JTextField();
        fields.add(symbol);

        // creates the text field for the quantity
        fields.add(new JLabel("Quantity"));
        quantity = new JTextField();
        fields.add(quantity);

        // creates the text field for the price
        fields.add(new JLabel("Price"));
        price = new JTextField();
        fields.add(price);

        // creates an empty border for the text fields
        Border border1 = BorderFactory.createLineBorder(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(10, 40, 10, 10)));

        p.centerPanel.add(fields, BorderLayout.CENTER);

        // creates an empty border for the buttons
        p.eastPanel.setLayout(new GridLayout(2, 1, 0, 60));
        p.eastPanel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(50, 70, 50, 70)));

        // sets the functionality for the reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTextFields();
            }
        });

        // sets the functionality for the sell button
        JButton sellButton = new JButton("Sell");
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // checks if any required text field is empty
                    if (symbol.getText().isEmpty()) {
                        throw new EmptyStringException("symbol");
                    }

                    // checks if the numbers are valid
                    Investment toSell = new Investment(symbol.getText());
                    int numQuantity = Integer.parseInt(quantity.getText());
                    if (numQuantity <= 0) {
                        throw new NumberOutOfRangeException("quantity");
                    }
                    double numPrice = Double.parseDouble(price.getText());
                    if (numPrice <= 0) {
                        throw new NumberOutOfRangeException("price");
                    }

                    // creates the investment to sell
                    toSell.setQuantity(numQuantity);
                    toSell.setPrice(numPrice);
                    if (!sellInvestment(p.investments, p.map, toSell)) {
                        p.messages.setText(p.messages.getText() + "\n" + "Error selling investment. Either investment does not exist or the quantity is too high.");

                    } else {
                        p.messages.setText(p.messages.getText() + "\n" + "Successfully sold investment.");
                    }
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
        p.eastPanel.add(sellButton);

        // creates the messages panel and its label
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
     * @param investments that the user already owns
     * @param map to update with investments that were removed
     * @param toSell investments to sell from their portfolio
     * @return if the selling of the stock was successful or not
     */
    public boolean sellInvestment(ArrayList<Investment> investments, HashMap<String, ArrayList<Integer>> map, Investment toSell) {
        int index = investments.indexOf(toSell);

        if (index != -1) {
            // checks if the investment has enough quantity for it to be sold
            if (investments.get(index).getQuantity() > toSell.getQuantity()) {
                investments.get(index).subtractQuantityAtPrice(toSell.getQuantity(), toSell.getPrice());
                return true;

            } else if (investments.get(index).getQuantity() == toSell.getQuantity()) {
                Integer indexToSell = new Integer(index);
                investments.remove(toSell);

                // updates the hashmap by removing the index that was sold
                Iterator<String> iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    ArrayList<Integer> currentIndexes = map.get(key);
                    if (currentIndexes.contains(indexToSell)) {
                        currentIndexes.remove(indexToSell);
                        if (currentIndexes.isEmpty()) {
                            iter.remove();
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void clearTextFields() {
        // clears text fields when reset button is pressed or an investment is sold
        String emptyString = "";
        symbol.setText(emptyString);
        quantity.setText(emptyString);
        price.setText(emptyString);
    }
}
