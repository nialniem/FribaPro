package friba;

import static kanta.Metodit.annaLuku;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;
import harkka.Tietue;
import kanta.TasoTarkistus;
import kanta.IkaTarkistus;


/**
 * Kopioi CRC-kortin sisältö
 * @author jimi_
 * @version 3.3.2022
 *
 */
public class Pelaaja implements Cloneable, Tietue {
    
    private int     tunnusNro;  
    private         String nimi             = "";
    private         String ika              = "";
    private         String kotipaikkakunta  = "";
    private         String taso             = "";

    private static int seuraavaNro = 1;
    private TasoTarkistus tasot = new TasoTarkistus();
    private IkaTarkistus iat = new IkaTarkistus();

    
    /**
     * @author Nisse
     *
     */
    public static class Vertailija implements Comparator<Pelaaja> {

        @Override
        public int compare(Pelaaja pelaaja1, Pelaaja pelaaja2) {
            return pelaaja1.getNimi().compareTo(pelaaja2.getNimi());
            
        }
    }
    /**
     * Palauttaa pelaajan kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 5;
    }
    
    /**
     * Eka kenttä, joka on mielekäs kysyttäväksi
     * @return ekan kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    /**
     * Alustetaan pelaajan merkkijono-attribuutti tyhjiksi jonoiksi ja tunnusnro = 0.
     */
    public Pelaaja() {
        // Toistaiseksi ei tarvita mitään
    }
    

    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }

    /**
     * @return palauttaa pelaajan nimen
     */
    public String getNimi() {
        return nimi;
    }

    
    /**
     * @return pelaajan ika
     */
    public String getIka() {
        return ika;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + ika;
        case 3: return "" + kotipaikkakunta;
        case 4: return "" + taso;
        default: return "Äääliö";
        }
    }
    

    
    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.aseta(1,"Ankka Aku") === null;
     *   pelaaja.aseta(9,"kissa") === "ÄÄliö";
     *   pelaaja.aseta(9,"1940") === "ÄÄliö";
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            String virhe = iat.tarkista(tjono);
            if (virhe !=  null) return virhe;
            ika = tjono;
            return null;
        case 3:
            kotipaikkakunta = tjono;
            return null;
        case 4:
            virhe = tasot.tarkista(tjono);
            if (virhe !=  null) return virhe;
            taso = tjono;
            return null;
            
        default:
            return "ÄÄliö";
        }
    }
    
    
    /**
     * Palauttaa k:tta pelaajan kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnusnumero";
        case 1: return "Nimi";
        case 2: return "Ikä";
        case 3: return "Kotikaupunki";
        case 4: return "Taso";
        default: return "Ääälio";
        }
    }

    /**
     * @return pelaajan kotikaupunki
     */
    public String getKKaupunki() {
        return kotipaikkakunta;
    }

    /**
     * @return pelaajan taso
     */
    public String getTaso() {
        return taso;
    }
    

    
    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro));
        out.println(nimi); 
        out.println(ika);
        out.println(String.format(kotipaikkakunta));
        out.println(String.format(taso));
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Antaa pelaajalle seuraavan rekisterinumeron.
     * @return pelaajan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Pelaaja niklas1 = new Pelaaja();
     *   niklas1.getTunnusNro() === 0;
     *   niklas1.rekisteroi();
     *   Pelaaja niklas2 = new Pelaaja();
     *   niklas2.rekisteroi();
     *   int n1 = niklas1.getTunnusNro();
     *   int n2 = niklas2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }
    
    /**
     * @return palauttaa pelaajan tunnusnumeron
     */
    public String getID() {
        return String.format("%03d", tunnusNro);
    }
    
    
    /**
     * Palauttaa pelaajan tunnusnumeron.
     * @return pelaajan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    

    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot pelaajalle.
     */

    public void vastaaEtuSuku() {
        nimi = "Niklas Nopea " + annaLuku(15, 40);
        ika = "" + annaLuku(15, 40);
        kotipaikkakunta = "Madrid";
        taso = "Pro" + annaLuku(0, 3);
    }    
        
    /**
     * Palauttaa jäsenen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return jäsen tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse(" 4  | Vladimir Naapuri   | 39   | Vaasa          |Perus");
     * </pre>  
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();  
    }
    /**
     * @param rivi pelaaja tiedot
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
        
    }
    
    //TODO: Tee testit
    @Override
    public Pelaaja clone() throws CloneNotSupportedException {
        Pelaaja uusi;
        uusi = (Pelaaja) super.clone();
        return uusi;
    }
    
    /**
     * Tutkii onko pelaajan tiedot samat kuin parametrina tuodun pelaajan tiedot
     * @param pelaaja jäsen johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja1 = new Pelaaja();
     *   pelaaja1.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Pelaaja pelaaja2 = new Pelaaja();
     *   pelaaja2.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Pelaaja pelaaja3 = new Pelaaja();
     *   pelaaja3.parse("   3  |  Ankka Aku   | 030201-115H");
     *   
     *   pelaaja1.equals(pelaaja2) === true;
     *   pelaaja2.equals(pelaaja1) === true;
     *   pelaaja1.equals(pelaaja3) === true;
     *   pelaaja3.equals(pelaaja2) === true;
     * </pre>
     */
    public boolean equals(Pelaaja pelaaja) {
        if ( pelaaja == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(pelaaja.anna(k)) ) return false;
        return true;
    }
    
    @Override
    public boolean equals(Object pelaaja) {
        if ( pelaaja instanceof Pelaaja ) return equals((Pelaaja)pelaaja);
        return false;
    }
       
    @Override
    public int hashCode() {
        return tunnusNro;
    }

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Pelaaja niklas = new Pelaaja();
        Pelaaja jimi = new Pelaaja();
        
        niklas.rekisteroi();
        jimi.rekisteroi();
        
        niklas.tulosta(System.out);
        
        
        niklas.vastaaEtuSuku();
        jimi.vastaaEtuSuku();
        
        niklas.tulosta(System.out);
        jimi.tulosta(System.out);
    }

   

   






  
}
