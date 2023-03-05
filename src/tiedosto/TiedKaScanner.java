package tiedosto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import fi.jyu.mit.ohj2.Mjonot;



/**
 * @author jimi_
 * @version 20.3.2022
 *
 */
@SuppressWarnings("unused")
public class TiedKaScanner {

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        double summa = 0;
        int n = 0;
        String tiedNimi = "luvut.dat";
        if (args.length > 0 ) tiedNimi = args[0];

        
        try (Scanner fi = new Scanner(new FileInputStream(new File(tiedNimi)))) { // Jotta UTF8/ISO-8859 toimii
            while ( fi.hasNext() ) {
                try {
                    String s = fi.nextLine();
                    double luku = Double.parseDouble(s);
                    summa += luku;
                    n++;
                } catch (NumberFormatException ex) {
                    // Hyltn
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Tiedosto ei aukea! " + ex.getMessage());
            return;
        }

        double ka = 0;
        if (n > 0) ka = summa / n;
        System.out.printf("Lukuja oli %d kappaletta.%n",n);
        System.out.printf("Niiden summa oli %4.2f%n",summa);
        System.out.printf("ja keskiarvo oli %4.2f%n",ka);
    }
}

      
