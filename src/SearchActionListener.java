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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * This class handles GUI and functionality for searching for investments
 *
 * @author Matias
 */
public class SearchActionListener implements ActionListener {

    private Portfolio p;

    private JTextField symbol;
    private JTextField nameKeywords;
    private JTextField lowPrice;
    private JTextField highPrice;

    /**
     *
     * @param p portfolio instance with JFrame
     */
    public SearchActionListener(Portfolio p) {
        this.p = p;
    }

    /**
     *
     * @param e actionEvent that fires when menu item is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        p.removeAllElements();

        // create the title for the screen
        p.centerPanel.setLayout(new BorderLayout());
        JTextArea sellInvestment = new JTextArea();
        sellInvestment.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        sellInvestment.setMargin(new Insets(10, 10, 0, 0));
        sellInvestment.setText("Searching investments");
        sellInvestment.setEditable(false);

        // create the fields panel
        p.centerPanel.add(sellInvestment, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(4, 2, 0, 20));

        // add the text field for symbol
        fields.add(new JLabel("Symbol"));
        symbol = new JTextField();
        fields.add(symbol);

        // add the text field for name keywords
        fields.add(new JLabel("Name Keywords"));
        nameKeywords = new JTextField();
        fields.add(nameKeywords);

        // add the text field for the low price
        fields.add(new JLabel("Low Price"));
        lowPrice = new JTextField();
        fields.add(lowPrice);

        // add the text field for the high price
        fields.add(new JLabel("High Price"));
        highPrice = new JTextField();
        fields.add(highPrice);

        // create an empty border for the fields for spacing
        Border border1 = BorderFactory.createLineBorder(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(10, 40, 10, 10)));
        p.centerPanel.add(fields, BorderLayout.CENTER);

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

        // set functionality for the search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                p.messages.setText("");
                double lowBoundPrice = -1, highBoundPrice = -1;

                try {

                    // checks if the text fields are empty
                    if (!lowPrice.getText().isEmpty()) {
                        lowBoundPrice = Double.parseDouble(lowPrice.getText());
                    }
                    if (!highPrice.getText().isEmpty()) {
                        highBoundPrice = Double.parseDouble(highPrice.getText());
                    }

                    // checks if the low price is higher than the high price
                    if (lowBoundPrice != -1 && highBoundPrice != -1 && highBoundPrice < lowBoundPrice) {
                        throw new NumberOutOfRangeException("low and high bound prices");
                    }

                } catch (NumberOutOfRangeException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + "Invalid high or low prices.");
                }

                ArrayList<Investment> validInvestments = new ArrayList<Investment>();
                validInvestments.addAll(p.investments);

                // matches any word input with the name words in any order ie. 'bank of canada' matches 'canada bank'
                if (nameKeywords.getText().length() > 0) {
                    String[] keys = nameKeywords.getText().split(" ");
                    ArrayList<Investment> matchesForKey;

                    for (int i = 0; i < keys.length; i++) {
                        matchesForKey = new ArrayList<Investment>();
                        ArrayList<Integer> indexes = p.map.get(keys[i]);

                        for (Integer index : indexes) {
                            matchesForKey.add(p.investments.get(index));
                        }
                        validInvestments = intersection(validInvestments, matchesForKey);
                    }
                }

                boolean searchSuccess = false;

                for (int i = 0; i < validInvestments.size(); i++) {
                    Investment currInv = validInvestments.get(i);
                    boolean valid = true;

                    // checks if a symbol was entered
                    if (!symbol.getText().isEmpty() && !symbol.getText().equals(currInv.getSymbol())) {
                        valid = false;
                    }

                    // checks if a range was entered
                    if (lowBoundPrice != -1 && currInv.getPrice() < lowBoundPrice) {
                        valid = false;
                    }
                    if (highBoundPrice != -1 && currInv.getPrice() > highBoundPrice) {
                        valid = false;
                    }

                    if (valid) {
                        p.messages.setText(p.messages.getText() + "\n" + currInv.toString());
                        searchSuccess = true;
                    }
                }
                if (!searchSuccess) {
                    p.messages.setText(p.messages.getText() + "\n" + "No investments found.");
                }
            }
        });

        p.eastPanel.add(resetButton);
        p.eastPanel.add(searchButton);

        // sets the label for the messages panel
        p.southPanel.setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel();
        messageLabel.setText("Search Results");
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        messageLabel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(0, 20, 0, 0)));

        p.southPanel.add(messageLabel, BorderLayout.NORTH);
        p.southPanel.add(p.scrollPane, BorderLayout.SOUTH);

        p.frame.revalidate();
        p.frame.repaint();
    }

    /**
     *
     * @param list1 to get intersection
     * @param list2 to get intersection
     * @return list of the intersection of both parameters
     */
    public static ArrayList<Investment> intersection(ArrayList<Investment> list1, ArrayList<Investment> list2) {
        ArrayList<Investment> returnList = new ArrayList<Investment>();
        for (Investment t : list1) {
            if (list2.contains(t)) {
                returnList.add(t);
            }
        }
        return returnList;
    }

    public void clearTextFields() {
        // clears the text fields when you reset
        String emptyString = "";
        symbol.setText(emptyString);
        nameKeywords.setText(emptyString);
        lowPrice.setText(emptyString);
        highPrice.setText(emptyString);
    }
}
