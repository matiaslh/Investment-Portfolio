/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * This class holds the portfolio for all investments
 *
 * @author Matias Levy-Hara
 */
public class Portfolio extends JFrame {

    public JPanel mainPanel;
    public JMenuBar commandsMenuBar;
    public JMenu commandsMenu;
    public ArrayList<Investment> investments;
    public HashMap<String, ArrayList<Integer>> map;
    public JPanel centerPanel;
    public JPanel eastPanel;
    public JPanel westPanel;
    public JPanel southPanel;
    public JScrollPane scrollPane;
    public JTextArea messages;
    public JFrame frame;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("File not found.");
            System.exit(0);
        }
        Portfolio p = new Portfolio(args[0]);
        p.setVisible(true);
    }

    public Portfolio(final String filename) {

        // create frame with title, size and location
        super("Investment Portfolio");
        setSize(700, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        investments = getInputFromFile(filename);
        map = initializeHashMap(investments);
        frame = this;

        // set function for when user exits
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow(investments, filename);
            }
        };
        addWindowListener(exitListener);
        setResizable(false);

        // create messages text area with scroll pane
        messages = new JTextArea(5, 1);
        messages.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        messages.setMargin(new Insets(10, 10, 0, 0));
        Border border2 = BorderFactory.createLineBorder(Color.BLACK);
        messages.setBorder(BorderFactory.createCompoundBorder(border2, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        messages.setEditable(false);
        messages.setLineWrap(true);

        scrollPane = new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // create all panels for the four sides: north east south west
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);

        eastPanel = new JPanel();
        eastPanel.setBackground(Color.WHITE);

        westPanel = new JPanel();
        westPanel.setBackground(Color.WHITE);

        southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        southPanel.setPreferredSize(new Dimension(getWidth(), 130));

        add(centerPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
        add(southPanel, BorderLayout.SOUTH);

        // set the welcome message at the beginning of the program
        centerPanel.setLayout(new BorderLayout());
        JTextArea welcomeMessage = new JTextArea();
        welcomeMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        welcomeMessage.setMargin(new Insets(50, 20, 0, 0));
        welcomeMessage.setText("Welcome to Investment Portfolio.\n\n"
                + "Choose a command from the “Commands” menu to buy or sell\n"
                + "an investment, update prices for all investments, get gain for\n"
                + "the portfolio, search for relevant investments, or quit the\n"
                + "program.");
        welcomeMessage.setEditable(false);

        // create the menu bar for the commands
        commandsMenuBar = new JMenuBar();
        commandsMenu = new JMenu("Commands");

        // create the menu item for the buy command
        JMenuItem buy = new JMenuItem("Buy");
        buy.addActionListener(new BuyActionListener(this));

        // create the menu item for the sell command
        JMenuItem sell = new JMenuItem("Sell");
        sell.addActionListener(new SellActionListener(this));

        // create the menu item for the update command
        JMenuItem update = new JMenuItem("Update");
        update.addActionListener(new UpdateActionListener(this));

        // create the menu item for the get gain command
        JMenuItem getGain = new JMenuItem("Get Gain");
        getGain.addActionListener(new GetGainActionListener(this));

        // create the menu item for the search command
        JMenuItem search = new JMenuItem("Search");
        search.addActionListener(new SearchActionListener(this));

        // create the menu item for the quit command and its functionality
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow(investments, filename);
            }
        });

        commandsMenu.add(buy);
        commandsMenu.add(sell);
        commandsMenu.add(update);
        commandsMenu.add(getGain);
        commandsMenu.add(search);
        commandsMenu.add(quit);
        commandsMenuBar.add(commandsMenu);
        add(commandsMenuBar, BorderLayout.NORTH);
        centerPanel.add(welcomeMessage);
    }

    public static void closeWindow(ArrayList<Investment> list, String filename) {
        // open a dialog to confirm the exit of the program
        int confirm = JOptionPane.showOptionDialog(
                null, "Are You Sure to Close Application?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == 0) {
            saveListToFile(list, filename);
            System.exit(0);
        }
    }

    public void removeAllElements() {
        // removes all elements in all panels
        messages.setText("");
        centerPanel.removeAll();
        eastPanel.removeAll();
        westPanel.removeAll();
        southPanel.removeAll();
    }

    /**
     *
     * @param filename file name of the file to get input from
     * @return list of investments
     */
    public static ArrayList<Investment> getInputFromFile(String filename) {
        ArrayList<Investment> list = new ArrayList<Investment>();
        BufferedReader reader = null;
        try {

            // open the file for reading and parsing
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));

            String line, type, symbol, name, buffer;
            int quantity;
            double price, bookValue;
            boolean isStock = false;
            Investment toAdd = null;

            while ((line = reader.readLine()) != null) {

                if (!line.isEmpty()) {

                    // get type of investment
                    type = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")).toLowerCase();
                    if (type.equals("stock")) {
                        isStock = true;
                    } else if (type.equals("mutualfund")) {
                        isStock = false;
                    }

                    // get symbol of investment
                    line = reader.readLine();
                    symbol = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));

                    // get name of investment
                    line = reader.readLine();
                    name = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));

                    // get quantity of investment
                    line = reader.readLine();
                    buffer = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    quantity = Integer.parseInt(buffer);

                    // get price of investment
                    line = reader.readLine();
                    buffer = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    price = Double.parseDouble(buffer);

                    // get bookvalue of investment
                    line = reader.readLine();
                    buffer = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                    bookValue = Double.parseDouble(buffer);

                    // create the investment and add it to the list
                    if (isStock) {
                        toAdd = new Stock(symbol, name, quantity, price);

                    } else if (!isStock) {
                        toAdd = new MutualFund(symbol, name, quantity, price);
                    }

                    toAdd.setBookValue(bookValue);
                    list.add(toAdd);

                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("File had an error.");
            System.exit(0);
        }

        return list;
    }

    /**
     *
     * @param list of investments to save to file
     * @param filename name of the file to save to
     */
    public static void saveListToFile(ArrayList<Investment> list, String filename) {
        BufferedWriter writer = null;
        try {

            // open the file for writing all investment details
            writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < list.size(); i++) {
                // write the details to file
                writer.write(list.get(i).toStringForFile());
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }

    /**
     *
     * @param list list of investments
     * @return hashmap of keywords to indexes
     */
    public static HashMap<String, ArrayList<Integer>> initializeHashMap(ArrayList<Investment> list) {

        HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();

        // create the initial keys and values for hashmap
        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            String[] keys = name.toLowerCase().split(" ");

            for (int j = 0; j < keys.length; j++) {

                ArrayList<Integer> currentIndexes = map.get(keys[j]);
                if (currentIndexes == null) {
                    ArrayList<Integer> indexes = new ArrayList<Integer>();
                    indexes.add(i);
                    map.put(keys[j], indexes);

                } else {
                    currentIndexes.add(i);
                }
            }
        }
        return map;
    }
}
