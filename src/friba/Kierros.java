package friba;

import static kanta.Metodit.*;
import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;
import harkka.Tietue;

/**
 * Pelaaja voi lisätä kierroksen, jossa 
 * @author jimi_
 * @version 20.3.2022
 *
 */
public class Kierros implements Cloneable, Tietue {
    
    private int     tunnusNro; 
    private int     jasenNro;
    private         String rata                 = "";
    private         String pvm                  = "";
    private int     tulos;

    
    
    private static int seuraavaNro = 1;
    



    /**
     * Alustetaan kierros. Toistaiseksi ei tarvitse tehdä mitään
     */
    public Kierros() {
        // Vielä ei tarvita mitään
    }
    
    /**
     * Alustetaan tietyn jäsenen kierros.
     * @param jasenNro jäsenen viitenumero
     */
    public Kierros(int jasenNro) {
        this.jasenNro = jasenNro;
    }
    
    /**
     * @return harrastukse kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 5;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    

    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "jäsenId";
            case 2:
                return "rata";
            case 3:
                return "pvm";
            case 4:
                return "tulos";
            default:
                return "???";
        }
    }


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Kierros kier = new Kierros();
     *   kier.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   kier.anna(0) === "2";   
     *   kier.anna(1) === "10";   
     *   kier.anna(2) === "Kalastus";   
     *   kier.anna(3) === "1949";   
     *   kier.anna(4) === "22";   
     *   
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
                return "" + jasenNro;
            case 2:
                return "" + rata;
            case 3:
                return "" + pvm;
            case 4:
                return "" + tulos;
            default:
                return "???";
        }
    }


    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Kierros kier = new Kierros();
     *   kier.aseta(3,"kissa") === null;
     *   kier.aseta(3,"1940")  === null;
     *   kier.aseta(4,"20")    === null;
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                jasenNro = Mjonot.erota(sb, '$', jasenNro);
                return null;
            case 2:
                rata = st;
                return null;
            case 3:
                try {
                    pvm = Mjonot.erotaEx(sb, '§', pvm);
                } catch (NumberFormatException ex) {
                    return "aloitusvuosi: Ei kokonaisluku ("+st+")";
                }
                return null;

            case 4:
                try {
                    tulos = Mjonot.erotaEx(sb, '§', tulos);
                } catch (NumberFormatException ex) {
                    return "h/vko: Ei kokonaisluku ("+st+")";
                }
                return null;

            default:
                return "Väärä kentän indeksi";
        }
    }


    /**
     * Tehdään identtinen klooni jäsenestä
     * @return Object kloonattu jäsen
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Kierros kier = new Kierros();
     *   kier.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   Kierros kopio = kier.clone();
     *   kopio.toString() === kier.toString();
     *   kier.parse("   1   |  11  |   Uinti  | 1949 | 22 t ");
     *   kopio.toString().equals(kier.toString()) === false;
     * </pre>
     */
    @Override
    public Kierros clone() throws CloneNotSupportedException { 
        return (Kierros)super.clone();
    }

    
        
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Kierrokselle.
     * Tulos arvotaan, jotta kahdella harrastuksella ei olisi samoja tietoja.
     * @param nro viite henkilöön, jonka harrastuksesta on kyse
     */
    public void vastaaLaajisFrisbee(int nro) {
        jasenNro = nro;
        rata = "Laajis Frisbeegolf";
        pvm = "12.1.2022";
        tulos = annaLuku(-20, 20);
        
    }
    
    
    /**
     * Tulostetaan kierroksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(rata + " " + pvm + " " + tulos);
    }

        
    
    
    /**
     * Tulostetaan henkilön tiedot
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
     * Palautetaan harrastuksen oma id
     * @return harrastuksen id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palautetaan mille jäsenelle kierros kuuluu
     * @return pelaajan id
     */
    public int getJasenNro() {
        return jasenNro;
    }
    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }

    
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
    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    

    @Override
    public int hashCode() {
        return tunnusNro;
    }

  

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kierros kier = new Kierros();
        kier.vastaaLaajisFrisbee(2);
        kier.tulosta(System.out);
    }

   



}


