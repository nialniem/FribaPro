package kanta;
import static kanta.SisaltaaTarkistaja.*;

/**
 * @author Nisse
 *
 */
public class IkaTarkistus {

    

    


    /**
     * Tarkistetaan ikä.  Sallitaan mys muoto jossa vain syntymaika.
     * @param ika joka tutkitaan.
     * @return null jos oikein, muuten virhett kuvaava teksti
     * TODO tarkistukset kuntoon mys karkausvuodesta.
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * </pre>
     */  
    public String tarkista(String ika) {
        int pituus = ika.length();
        if ( pituus > 2) return "Ikä pitää olla välillä 15-99";
        if ( !onkoVain(ika,NUMEROT)) return "Saa olla vain numeroita!";        
        return null;
    }

}

