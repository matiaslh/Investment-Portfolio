/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

/**
 *
 * This class holds a Mutual Fund investment and a few details along with it
 *
 * @author Matias Levy-Hara
 */
public class MutualFund extends Investment {

    public static final double REDEMPTION_FEE = 45;

    /**
     *
     * @param symbol MutualFund symbol
     * @param name name of the MutualFund
     * @param quantity amount of MutualFund to hold
     * @param price value in dollars of each MutualFund
     */
    public MutualFund(String symbol, String name, int quantity, double price) {
        super(symbol, name);
        addQuantityAtPrice(quantity, price);
    }

    /**
     *
     * @param symbol MutualFund symbol
     * @param quantity name of the MutualFund
     * @param price value in dollars of each MutualFund
     */
    public MutualFund(String symbol, int quantity, double price) {
        super(symbol);
        addQuantityAtPrice(quantity, price);
    }

    /**
     *
     * @param symbol mutualfund symbol
     */
    public MutualFund(String symbol) {
        super(symbol);
    }

    /**
     *
     * @return total gain in dollars for this investment
     */
    @Override
    public double getGain() {
        return (getQuantity() * getPrice() - REDEMPTION_FEE) - getBookValue();
    }

    /**
     *
     * @param amount quantity to add to mutualFund
     * @param price value in dollars of each mutualFund
     */
    public void addQuantityAtPrice(int amount, double price) {
        setQuantity(getQuantity() + amount);
        setBookValue(getBookValue() + amount * price);
        setPrice(price);
    }

    /**
     *
     * @return current details of the mutual fund to print to output file
     */
    public String toStringForFile() {
        return "type = \"mutualfund\"\nsymbol = \"" + getSymbol() + "\"\nname = \""
                + getName() + "\"\nquantity = \"" + getQuantity() + "\"\nprice = \""
                + getPrice() + "\"\nbookValue = \"" + getBookValue() + "\"\n";
    }
}
