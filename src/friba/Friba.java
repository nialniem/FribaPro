/**
 * 
 */
package friba;

import java.io.File;
import java.util.*;

import kanta.SailoException;

/**
 * Friba-luokka, joka huolehtii pelaajistosta.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" pelaajistoon.

 * 
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import kanta.SailoException;
 *  private Friba friba;
 *  private Pelaaja aku1;
 *  private Pelaaja aku2;
 *  private int jid1;
 *  private int jid2;
 *  private Kierros pitsi21;
 *  private Kierros pitsi11;
 *  private Kierros pitsi22; 
 *  private Kierros pitsi12; 
 *  private Kierros pitsi23;
 *  
 *  // @SuppressWarnings("javadoc")
 *  public void alustaFriba() {
 *    friba = new Friba();
 *    aku1 = new Pelaaja(); aku1.vastaaEtuSuku(); aku1.rekisteroi();
 *    aku2 = new Pelaaja(); aku2.vastaaEtuSuku(); aku2.rekisteroi();
 *    jid1 = aku1.getTunnusNro();
 *    jid2 = aku2.getTunnusNro();
 *    pitsi21 = new Kierros(jid2); pitsi21.vastaaLaajisFrisbee(jid2);
 *    pitsi11 = new Kierros(jid1); pitsi11.vastaaLaajisFrisbee(jid1);
 *    pitsi22 = new Kierros(jid2); pitsi22.vastaaLaajisFrisbee(jid2); 
 *    pitsi12 = new Kierros(jid1); pitsi12.vastaaLaajisFrisbee(jid1); 
 *    pitsi23 = new Kierros(jid2); pitsi23.vastaaLaajisFrisbee(jid2);
 *    try {
 *    friba.lisaa(aku1);
 *    friba.lisaa(aku2);
 *    friba.lisaa(pitsi21);
 *    friba.lisaa(pitsi11);
 *    friba.lisaa(pitsi22);
 *    friba.lisaa(pitsi12);
 *    friba.lisaa(pitsi23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
*/
public class Friba {
    
    private Pelaajat pelaajat = new Pelaajat();
    private Kierrokset kierrokset = new Kierrokset();
    
    private String hakemisto = "FribaPRO tiedonhallinta";
    
    /**
    * Poistaa pelaajista ja kierroksista pelaajan tiedot 
    * @param pelaaja pelaaja joka poistetaan
    * @return montako pelaajaa poistettiin
    * @example
    * <pre name="test">
    * #THROWS Exception
    *   alustaFriba();
    *   friba.etsi("*",0).size() === 2;
    *   friba.annaKierrokset(aku1).size() === 2;
    *   friba.poista(aku1) === 1;
    *   friba.etsi("*",0).size() === 1;
    *   friba.annaKierrokset(aku1).size() === 0;
    *   friba.annaKierrokset(aku2).size() === 3;
    * </pre>
    */

    public int poista(Pelaaja pelaaja) {
        if ( pelaaja == null ) return 0;
        int ret = pelaajat.poista(pelaaja.getTunnusNro()); 
        kierrokset.poistaPelaajanKierrokset(pelaaja.getTunnusNro()); 
        return ret; 
    }
    
    
    /** 
     * Poistaa tämän kierroksen 
     * @param kierros poistettava kierros
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaFriba();
     *   friba.annaKierrokset(aku1).size() === 2;
     *   friba.poistaKierros(pitsi11);
     *   friba.annaKierrokset(aku1).size() === 1;
     */ 
    public void poistaKierros(Kierros kierros) { 
        kierrokset.poista(kierros); 
    } 

    
    /**
     * Lisää fribaan uuden pelaajan
     * @param pelaaja lisättävä pelaaja
     * @example
     * <pre name="test">
     * Friba friba = new Friba();
     * Pelaaja niklas1 = new Pelaaja(), niklas2 = new Pelaaja();
     * niklas1.rekisteroi(); niklas2.rekisteroi();
     * friba.getPelaajia() === 0;
     * friba.lisaa(niklas1); friba.getPelaajia() === 1;
     * friba.lisaa(niklas2); friba.getPelaajia() === 2;
     * friba.lisaa(niklas1); friba.getPelaajia() === 3;
     * friba.getPelaajia() === 3;
     * friba.annaPelaaja(0) === niklas1;
     * friba.annaPelaaja(1) === niklas2;
     * friba.annaPelaaja(2) === niklas1;
     * friba.annaPelaaja(3) === niklas1; #THROWS IndexOutOfBoundsException 
     * friba.lisaa(niklas1); friba.getPelaajia() === 4;
     * friba.lisaa(niklas1); friba.getPelaajia() === 5;
     * </pre>
     */
    public void lisaa(Pelaaja pelaaja) {
        pelaajat.lisaa(pelaaja);
    }
    
    


    
    /** 
     * Korvaa pelaajan tietorakenteessa.  Ottaa pelaajan omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva pelaaja.  Jos ei löydy, 
     * niin lisätään uutena pelaajana. 
     * @param pelaaja lisätäävän pelaajan viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 

    public void korvaaTaiLisaa(Pelaaja pelaaja) throws SailoException {
        pelaajat.korvaaTaiLisaa(pelaaja);
        
    }
    
    /** 
     * Korvaa harrastuksen tietorakenteessa.  Ottaa harrastuksen omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva harrastus.  Jos ei löydy, 
     * niin lisätään uutena harrastuksena. 
     * @param kierros lisärtävän harrastuksen viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Kierros kierros) throws SailoException { 
        kierrokset.korvaaTaiLisaa(kierros); 
    } 


    
    /**
     * Lisää fribaan uuden kierrokset
     * @param kier lisättävä kierros
     */
    public void lisaa(Kierros kier) {
        kierrokset.lisaa(kier);
    }

    /**
     * Haetaan kaikki jäsen harrastukset
     * @param pelaaja pelaaja, jonka kierros halutaan antaa
     * @return tietorakenne jossa viiteet löydetteyihin harrastuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kierrokset kierrokset = new Kierrokset();
     *  Kierros round21 = new Kierros(2); kierrokset.lisaa(round21);
     *  Kierros round11 = new Kierros(1); kierrokset.lisaa(round11);
     *  Kierros round22 = new Kierros(2); kierrokset.lisaa(round22);
     *  Kierros round12 = new Kierros(1); kierrokset.lisaa(round12);
     *  Kierros round23 = new Kierros(2); kierrokset.lisaa(round23);
     *  Kierros round51 = new Kierros(5); kierrokset.lisaa(round51);
     *  
     *  List<Kierros> loytyneet;
     *  loytyneet = kierrokset.annaKierrokset(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = kierrokset.annaKierrokset(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == round11 === true;
     *  loytyneet.get(1) == round12 === true;
     *  loytyneet = kierrokset.annaKierrokset(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == round51 === true;
     * </pre> 
     */
    public List<Kierros> annaKierrokset(Pelaaja pelaaja) {
        return kierrokset.annaKierrokset(pelaaja.getTunnusNro());
    }
    
    
    /**
     * Palautaa kerhon jäsenmäärän
     * @return jäsenmäärä
     */
    public int getPelaajia() {
        return pelaajat.getLkm();
    }
    
    
    /**
     * Palauttaa i:n jäsenen
     * @param i monesko jäsen palautetaan
     * @return viite i:teen jäseneen
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Pelaaja annaPelaaja(int i) throws IndexOutOfBoundsException {
        return pelaajat.anna(i);
    }
    
    /**
     * Palauttaa listan pelaajista jotka vastaavat hakuehtoa
     * @param ehto hakuehto
     * @param k kentän indeksi jonka mukaan etsitään
     * @return löytyneet pelaajat
     */
    public Collection<Pelaaja> etsi(String ehto,int k) {
       
        return pelaajat.etsi(ehto, k);
    }
    
    
    /**
     * Tallettaa kerhon tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            pelaajat.tallenna(hakemisto);
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            kierrokset.tallenna(hakemisto);
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }

    /**
     * Lukee kerhon tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        File dir = new File(nimi);
        dir.mkdir();
        pelaajat = new Pelaajat(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        kierrokset = new Kierrokset();

        hakemisto = nimi;
        pelaajat.lueTiedostosta(nimi);
        kierrokset.lueTiedostosta(nimi);
    }


    
   

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Friba friba = new Friba();
        
        try {
            friba.lueTiedostosta("FribaPRO tiedonhallinta");
        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }

        Pelaaja niklas = new Pelaaja();
        Pelaaja niklas2 = new Pelaaja();
        niklas.rekisteroi();
        niklas.vastaaEtuSuku();
        niklas2.rekisteroi();
        niklas2.vastaaEtuSuku();
        
        friba.lisaa(niklas);
        friba.lisaa(niklas2);
        
        
        for (int i = 0; i < friba.getPelaajia(); i++) {
            Pelaaja pelaaja = friba.annaPelaaja(i);
            System.out.println("pelaaja paikassa: " + i);
            pelaaja.tulosta(System.out);
        }

        try {
            friba.lisaa(niklas);
            friba.lisaa(niklas2);
            for (int i=0; i<friba.getPelaajia(); i++) {
                Pelaaja pelaaja = friba.annaPelaaja(i);
                pelaaja.tulosta(System.out);
            }
            
            friba.tallenna();
        } catch (SailoException e) {
            // e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

   

}
