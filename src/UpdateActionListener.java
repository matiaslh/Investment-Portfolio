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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * This class handles GUI and functionality for updating investments
 *
 * @author Matias
 */
public class UpdateActionListener implements ActionListener {

    private Portfolio p;

    private int currentIndex;

    private JButton prevButton;
    private JButton nextButton;
    private JButton saveButton;

    private JTextField symbol;
    private JTextField name;
    private JTextField price;

    /**
     *
     * @param p portfolio instance with JFrame
     */
    public UpdateActionListener(Portfolio p) {
        this.p = p;
    }

    /**
     *
     * @param e actionEvent that fires when menu item is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        p.removeAllElements();
        currentIndex = 0;

        // creates the title for the screen
        p.centerPanel.setLayout(new BorderLayout());
        JTextArea sellInvestment = new JTextArea();
        sellInvestment.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        sellInvestment.setMargin(new Insets(10, 10, 0, 0));
        sellInvestment.setText("Updating investments");
        sellInvestment.setEditable(false);

        // creates the panel for the text fields
        p.centerPanel.add(sellInvestment, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(3, 2, 0, 35));

        // creates the text field for the symbol
        fields.add(new JLabel("Symbol"));
        symbol = new JTextField();
        symbol.setEditable(false);
        fields.add(symbol);

        // creates the text field for the name
        fields.add(new JLabel("Name"));
        name = new JTextField();
        name.setEditable(false);
        fields.add(name);

        // creates the text field for the price
        fields.add(new JLabel("Price"));
        price = new JTextField();
        fields.add(price);

        // creates an empty border for the text fields
        Border border1 = BorderFactory.createLineBorder(Color.WHITE);
        fields.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(10, 40, 10, 10)));

        p.centerPanel.add(fields, BorderLayout.CENTER);

        // creates an empty border for the buttons
        p.eastPanel.setLayout(new GridLayout(3, 1, 0, 20));
        p.eastPanel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(40, 70, 40, 70)));

        // sets the functionality for the previous button
        prevButton = new JButton("Prev");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentIndex--;
                setFields();
            }
        });

        // sets the functionality for the next button
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentIndex++;
                setFields();
            }
        });

        // sets the functionality for the save button
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    // checks if any required fields are empty
                    if (price.getText().isEmpty()) {
                        throw new EmptyStringException("price");
                    }

                    // checks if the new price number is valid
                    double newPrice = Double.parseDouble(price.getText());
                    if (newPrice <= 0) {
                        throw new NumberOutOfRangeException("price");
                    }

                    // updates the price with the new price
                    p.investments.get(currentIndex).setPrice(newPrice);
                    p.messages.setText(p.messages.getText() + "\n" + "Successfully updated investment.");

                } catch (EmptyStringException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + ex.getMessage());
                } catch (NumberOutOfRangeException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + ex.getMessage());
                } catch (NumberFormatException ex) {
                    p.messages.setText(p.messages.getText() + "\n" + "The price is invalid.");
                }
            }
        });

        setFields();

        p.eastPanel.add(prevButton);
        p.eastPanel.add(nextButton);
        p.eastPanel.add(saveButton);

        // creates the label for the messages text area
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

    public void setFields() {

        // sets text fields to the current investment for details to be displayed
        symbol.setText(p.investments.get(currentIndex).getSymbol());
        name.setText(p.investments.get(currentIndex).getName());
        price.setText(p.investments.get(currentIndex).getPrice() + "");

        // disables buttons if there is no previous or next investment
        if (currentIndex <= 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }
        if (currentIndex >= p.investments.size() - 1) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
    }
}
