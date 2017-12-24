/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investmentPortfolio;

/**
 *
 * This class handles invalid input for number fields
 *
 * @author Matias
 */
public class NumberOutOfRangeException extends Exception {

    /**
     *
     * @param s name of the field which was invalid
     */
    public NumberOutOfRangeException(String s) {
        super("The following field is out of range: " + s);
    }
}
