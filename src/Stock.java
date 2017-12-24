/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

/**
 *
 * This class is for Investments of type Stock which hold a few details about
 * the Investment
 *
 * @author Matias Levy-Hara
 */
public class Stock extends Investment {

    public static final double COMMISSION_FEE = 9.99;

    /**
     *
     * @param symbol stock symbol
     * @param name name of the stock
     * @param quantity amount of stock to hold
     * @param price value in dollars of each stock
     */
    public Stock(String symbol, String name, int quantity, double price) {
        super(symbol, name);
        addQuantityAtPrice(quantity, price);
    }

    /**
     *
     * @param symbol stock symbol
     * @param quantity name of the stock
     * @param price value in dollars of each stock
     */
    public Stock(String symbol, int quantity, double price) {
        super(symbol);
        addQuantityAtPrice(quantity, price);
    }

    /**
     *
     * @param symbol stock symbol
     */
    public Stock(String symbol) {
        super(symbol);
    }

    /**
     *
     * @return total gain in dollars for this investment
     */
    @Override
    public double getGain() {
        return (getQuantity() * getPrice() - COMMISSION_FEE) - getBookValue();
    }

    /**
     *
     * @param amount quantity to add to stock
     * @param price value in dollars of each stock
     */
    public void addQuantityAtPrice(int amount, double price) {
        setQuantity(getQuantity() + amount);
        setBookValue(getBookValue() + amount * price + COMMISSION_FEE);
        setPrice(price);
    }

    /**
     *
     * @return current details of the stock to print to output file
     */
    public String toStringForFile() {
        return "type = \"stock\"\nsymbol = \"" + getSymbol() + "\"\nname = \""
                + getName() + "\"\nquantity = \"" + getQuantity() + "\"\nprice = \""
                + getPrice() + "\"\nbookValue = \"" + getBookValue() + "\"\n";
    }
}
