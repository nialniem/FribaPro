/**
 * 
 */
package kanta;

/**
 * @author jimi_
 * @version 20.3.2022
 *
 */
public class Metodit {
    
    /**
     * @param min minimiarvo
     * @param max maksimiarvo
     * @return arvottu luku väliltä min-max
     */
    public static int annaLuku(int min, int max) {
        int luku = min + (int)(Math.random() * ((max - min) + 1));
        return luku;
    }
}
