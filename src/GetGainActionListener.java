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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * This class handles GUI and functionality for getting the gain for investments
 *
 * @author Matias
 */
public class GetGainActionListener implements ActionListener {

    private Portfolio p;

    private JTextField gainTextField;

    /**
     *
     * @param p portfolio instance with JFrame
     */
    public GetGainActionListener(Portfolio p) {
        this.p = p;
    }

    /**
     *
     * @param e actionEvent that fires when menu item is pressed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        p.removeAllElements();

        // create an empty border for the buttons
        Border border1 = BorderFactory.createLineBorder(Color.WHITE);
        p.eastPanel.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(50, 130, 50, 70)));

        p.centerPanel.setLayout(new BorderLayout());

        // create the title for the screen
        JTextArea getGainTextArea = new JTextArea();
        getGainTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        getGainTextArea.setMargin(new Insets(10, 10, 0, 0));
        getGainTextArea.setText("Getting total gain");
        getGainTextArea.setEditable(false);

        // create the field to show the total gain
        p.centerPanel.add(getGainTextArea, BorderLayout.NORTH);
        JPanel fields = new JPanel();
        fields.setBackground(Color.WHITE);
        fields.setLayout(new GridLayout(1, 2, 0, 100));

        fields.add(new JLabel("Total gain"));
        gainTextField = new JTextField();
        gainTextField.setEditable(false);
        gainTextField.setText(getGain(p.investments) + "");
        fields.add(gainTextField);

        fields.setBorder(BorderFactory.createCompoundBorder(border1, BorderFactory.createEmptyBorder(60, 40, 80, 10)));

        p.centerPanel.add(fields, BorderLayout.CENTER);

        // create the messages text area and its label
        p.southPanel.setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel();
        messageLabel.setText("Individual gains");
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
     * @return Total gain of all investments
     */
    public double getGain(ArrayList<Investment> investments) {
        double gain = 0;
        p.messages.setText("");
        // gets total gain for investments
        for (int i = 0; i < investments.size(); i++) {
            p.messages.setText(p.messages.getText() + "\n" + investments.get(i).getSymbol() + ": " + investments.get(i).getGain());
            gain += investments.get(i).getGain();
        }
        return gain;
    }
}
