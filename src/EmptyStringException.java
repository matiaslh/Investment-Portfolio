/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

/**
 *
 * This class is for handling empty strings for a text field
 *
 * @author Matias
 */
public class EmptyStringException extends Exception {

    /**
     *
     * @param s name of the field which was empty
     */
    public EmptyStringException(String s) {
        super("You must enter a value for " + s + ".");
    }
}
