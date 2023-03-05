package kanta;
import static kanta.SisaltaaTarkistaja.*;

/**
 * @author Nisse
 *
 */
public class TasoTarkistus {

    /**
     * Tarkistetaan taso.  Sallitaan vain tietyt kirjaimet.
     * @param taso jota tutkitaan.
     * @return null jos oikein, muuten virhett kuvaava teksti
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * </pre>
     */  
    public String tarkista(String taso) {
        int pituus = taso.length();
        if ( pituus < 3) return "Taso voi olla Pro, Perus tai Noob";
        if ( pituus == 3 ) {
            if (taso.equals("Pro") == false)
            return "Yrititkö kirjoittaa Pro? Yritä uudelleen isolla alkukirjaimella"; 
        }
        if ( pituus == 4 ) {
            if (taso.equals("Noob") == false)
            return "Yrititkö kirjoittaa Noob? Yritä uudelleen isolla alkukirjaimella"; 
        }
        if ( pituus >= 5 ) {
            if (taso.equals("Perus") == false)
            return "Yrititkö kirjoittaa Perus? Yritä uudelleen isolla alkukirjaimella";
        }
        if ( onkoVain(taso,NUMEROT)) return "Saa olla vain kirjaimia!";        
        return null;
    }

}

