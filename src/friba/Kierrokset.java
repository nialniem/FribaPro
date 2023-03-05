/**
 * 
 */
package friba;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

import kanta.SailoException;

/**
 * @author jimi_
 * @version 19.3.2022
 *
 */
public class Kierrokset {
    

    @SuppressWarnings("unused")
    private String                      tiedostonNimi = "";

    private boolean muutettu = false;
    
    private List<Kierros> alkiot = new ArrayList<Kierros>();


    
    
    /**
     * Kierrosten alustaminen
     */
    public Kierrokset() {
        // toistaiseksi ei tarvitse tehdä mitään
    }
    

    /**
     * Lisää uuden kierroksen tietorakenteeseen.  Ottaa kierroksen omistukseensa.
     * @param kier lisättävä kierros.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Kierros kier) {
        alkiot.add(kier);
    }
    
    
    /**
     * Korvaa harrastuksen tietorakenteessa.  Ottaa harrastuksen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva harrastus.  Jos ei löydy,
     * niin lisätään uutena harrastuksena.
     * @param kierros lisättävän harrastuksen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Kierrokset kierrokset = new Kierrokset();
     * Kierros kier1 = new Kierros(), kier2 = new Kierros();
     * kier1.rekisteroi(); kier2.rekisteroi();
     * kierrokset.getLkm() === 0;
     * kierrokset.korvaaTaiLisaa(kier1); kierrokset.getLkm() === 1;
     * kierrokset.korvaaTaiLisaa(kier2); kierrokset.getLkm() === 2;
     * Kierros kier3 = kier1.clone();
     * kier3.aseta(2,"kkk");
     * </pre>
     */ 
    public void korvaaTaiLisaa(Kierros kierros) throws SailoException {
        int id = kierros.getTunnusNro();
        for (int i = 0; i < alkiot.size(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, kierros);
                muutettu = true;
                return;
            }
        }
        lisaa(kierros);
    }
    
    /**
     * Palauttaa tietueiden lukumäärän
     * @return harrastusten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Poistaa valitun kierrokset
     * @param kierros poistettava kierros
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Kierrokset kierrokset = new Kierrokset();
     *  Kierros pitsi21 = new Kierros(); pitsi21.vastaaLaajisFrisbee(2);
     *  Kierros pitsi11 = new Kierros(); pitsi11.vastaaLaajisFrisbee(1);
     *  Kierros pitsi22 = new Kierros(); pitsi22.vastaaLaajisFrisbee(2); 
     *  Kierros pitsi12 = new Kierros(); pitsi12.vastaaLaajisFrisbee(1); 
     *  Kierros pitsi23 = new Kierros(); pitsi23.vastaaLaajisFrisbee(2); 
     *  kierrokset.lisaa(pitsi21);
     *  kierrokset.lisaa(pitsi11);
     *  kierrokset.lisaa(pitsi22);
     *  kierrokset.lisaa(pitsi12);
     *  kierrokset.poista(pitsi23) === false ; kierrokset.getLkm() === 4;
     *  kierrokset.poista(pitsi11) === true;   kierrokset.getLkm() === 3;
     *  List<Kierros> h = kierrokset.annaKierrokset(1);
     *  h.size() === 1; 
     *  h.get(0) === pitsi12;
     * </pre>
     */
    public boolean poista(Kierros kierros) {
        boolean ret = alkiot.remove(kierros);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn pelaajan kierrokset
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Kierrokset kierrokset = new Kierrokset();
     *  Kierros pitsi21 = new Kierros(); pitsi21.vastaaLaajisFrisbee(2);
     *  Kierros pitsi11 = new Kierros(); pitsi11.vastaaLaajisFrisbee(1);
     *  Kierros pitsi22 = new Kierros(); pitsi22.vastaaLaajisFrisbee(2); 
     *  Kierros pitsi12 = new Kierros(); pitsi12.vastaaLaajisFrisbee(1); 
     *  Kierros pitsi23 = new Kierros(); pitsi23.vastaaLaajisFrisbee(2); 
     *  kierrokset.lisaa(pitsi21);
     *  kierrokset.lisaa(pitsi11);
     *  kierrokset.lisaa(pitsi22);
     *  kierrokset.lisaa(pitsi12);
     *  kierrokset.lisaa(pitsi23);
     *  kierrokset.poistaPelaajanKierrokset(2) === 3;  kierrokset.getLkm() === 2;
     *  kierrokset.poistaPelaajanKierrokset(3) === 0;  kierrokset.getLkm() === 2;
     *  List<Kierros> k = kierrokset.annaKierrokset(2);
     *  k.size() === 0; 
     *  k = kierrokset.annaKierrokset(1);
     *  k.get(0) === pitsi11;
     *  k.get(1) === pitsi12;
     * </pre>
     */
    public int poistaPelaajanKierrokset(int tunnusNro) {
        int n = 0;
        for (Iterator<Kierros> it = alkiot.iterator(); it.hasNext();) {
            Kierros kier = it.next();
            if ( kier.getJasenNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }



    
    
    /**
     * Lukee harrastukset tiedostosta.
     * @param hakemisto tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String nimi = hakemisto + "/kierrokset.dat";
        File ftied = new File(nimi);
        try (Scanner fi = new Scanner(new FileInputStream(ftied))) { // Jotta UTF8/ISO-8859 toimii'
            while ( fi.hasNext() ) {
                String s = fi.nextLine().trim();
                if ( "".equals(s) || s.charAt(0) == ';' ) continue;
                Kierros har = new Kierros();
                har.parse(s); // voisi olla virhekäsittely
                lisaa(har);
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Ei saa luettua tiedostoa " + nimi);
        }
        muutettu = false;
    }



    /**
     * Tallentaa jäsenistön tiedostoon.  
     * Tiedoston muoto:
     * <pre>
     * 2|Ankka Aku|121103-706Y|Paratiisitie 13|12345|ANKKALINNA|12-1234|||1996|50.0|30.0|Velkaa Roopelle
     * 3|Ankka Tupu|121153-706Y|Paratiisitie 13|12345|ANKKALINNA|12-1234|||1996|50.0|30.0|Velkaa Roopelle
     * </pre>
     * @param hakemisto tallennettavan tiedoston hakemisto
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna(String hakemisto) throws SailoException {
        if ( !muutettu ) return;

        File ftied = new File(hakemisto + "/kierrokset.dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
            for (var har: alkiot) {
                fo.println(har.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + ftied.getAbsolutePath() + " ei aukea");
        }        
        
        muutettu = false;
    }

 
    
    /**
     * Haetaan kaikki jäsen harrastukset
     * @param tunnusnro jäsenen tunnusnumero jolle harrastuksia haetaan
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
    public List<Kierros> annaKierrokset(int tunnusnro) {
        List<Kierros> loydetyt = new ArrayList<Kierros>();
        for (Kierros har : alkiot) // iteraattori
            if (har.getJasenNro() == tunnusnro) loydetyt.add(har);
        return loydetyt;
    }

    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Kierrokset kierrokset = new Kierrokset();
        
        try {
            kierrokset.lueTiedostosta("FribaPRO tiedonhallinta");
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }

        Kierros round1 = new Kierros();
        round1.vastaaLaajisFrisbee(2);
        Kierros round2 = new Kierros();
        round2.vastaaLaajisFrisbee(1);
        Kierros round3 = new Kierros();
        round3.vastaaLaajisFrisbee(2);
        Kierros round4 = new Kierros();
        round4.vastaaLaajisFrisbee(2);

        kierrokset.lisaa(round1);
        kierrokset.lisaa(round2);
        kierrokset.lisaa(round3);
        kierrokset.lisaa(round2);
        kierrokset.lisaa(round4);

        System.out.println("============= Harrastukset testi =================");

        List<Kierros> kierrokset2 = kierrokset.annaKierrokset(2);

        for (Kierros kier : kierrokset2) {
            System.out.print(kier.getJasenNro() + " ");
            kier.tulosta(System.out);
        }

        try {
            kierrokset.tallenna("FribaPRO tiedonhallinta");
        } catch (SailoException e) {
            e.printStackTrace();
        }        

    }


    public int size() {        
        return alkiot.size();
    }
    
}
