/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

/**
 *
 * This class holds an investment and a few details about the item
 *
 * @author Matias Levy-Hara
 */
public class Investment {

    private String symbol;
    private String name;
    private int quantity;
    private double price;
    private double bookValue;

    /**
     *
     * @param symbol investment symbol
     * @param name of the investment
     */
    public Investment(String symbol, String name) {
        setSymbol(symbol);
        setName(name);
    }

    /**
     *
     * @param symbol investment symbol
     */
    public Investment(String symbol) {
        setSymbol(symbol);
    }

    /**
     *
     * @return symbol of investment
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol symbol of investment
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     * @return name of the investment
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of the investment
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return amount of investment being held
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return value of each investment in dollars
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price value of each investment in dollars
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return current value of this investment
     */
    public double getBookValue() {
        return bookValue;
    }

    /**
     *
     * @param value value of this investment
     */
    public void setBookValue(double value) {
        this.bookValue = value;
    }

    /**
     *
     * @param amount of investments to add
     * @param price of each investment
     */
    public void addQuantityAtPrice(int amount, double price) {
        return;
    }

    /**
     *
     * @param amount quantity to add to investment
     * @param price value in dollars of each investment
     */
    public void subtractQuantityAtPrice(int amount, double price) {
        setBookValue(getBookValue() * (getQuantity() - amount) / (double) getQuantity());
        this.quantity -= amount;
        setPrice(price);
    }

    /**
     *
     * @return total gain in dollars for this investment
     */
    public double getGain() {
        return -1;
    }

    /**
     *
     * @param toCompare other object to compare to
     * @return true if their symbols are the same
     */
    @Override
    public boolean equals(Object toCompare) {
        Investment other = (Investment) toCompare;
        if (other != null) {
            return (getSymbol().toLowerCase().equals(other.getSymbol().toLowerCase()));
        }
        return false;
    }

    /**
     *
     * @return current details of the investment
     */
    @Override
    public String toString() {
        return name + "(" + symbol + ") : " + quantity + " @ $" + String.format("%.2f", price) + " Book Value: $" + String.format("%.2f", getBookValue());
    }

    /**
     *
     * @return current details of the investment to print to output file
     */
    public String toStringForFile() {
        return "";
    }

}
