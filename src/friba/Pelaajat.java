package friba;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;
import kanta.SailoException;

/**
 * @author Nisse
 *
 */
public class Pelaajat implements Iterable<Pelaaja> {
    
    private static final int MAX_JASENIA   = 5;
    private int lkm = 0;
    private Pelaaja[] alkiot;
    private boolean muutettu = false;
    
    
    /**
     * Luodaan alustava taulukko
     */
    public Pelaajat() {
        alkiot = new Pelaaja[MAX_JASENIA];
    }
    

    
    
    
    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.             
     * @param pelaaja lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi    
     * @example                                                                         
     * <pre name="test">                                                                                                                       
     * Pelaajat pelaajat = new Pelaajat();                                              
     * Pelaaja niklas1 = new Pelaaja(), niklas2 = new Pelaaja();                        
     * pelaajat.getLkm() === 0;                                                         
     * pelaajat.lisaa(niklas1); pelaajat.getLkm() === 1;                                
     * pelaajat.lisaa(niklas2); pelaajat.getLkm() === 2;                                
     * pelaajat.lisaa(niklas1); pelaajat.getLkm() === 3;                                
     * pelaajat.anna(0) === niklas1;                                                    
     * pelaajat.anna(1) === niklas2;                                                    
     * pelaajat.anna(2) === niklas1;                                                    
     * pelaajat.anna(1) == niklas1 === false;                                           
     * pelaajat.anna(1) == niklas2 === true;                                                         
     * pelaajat.lisaa(niklas1); pelaajat.getLkm() === 4;                                
     * pelaajat.lisaa(niklas1); pelaajat.getLkm() === 5;   
     * pelaajat.lisaa(niklas1); pelaajat.getLkm() === 6;                                                       
     * </pre>                                                                                        
     */
    public void lisaa(Pelaaja pelaaja) {
        if (lkm >= alkiot.length) {
            suurempiTaulukko();
            }
            alkiot[lkm++] = pelaaja;
            muutettu = true;
        }
        
    /**
     * @param pelaaja pelaaja joka korvataan tai lisätään
     */
    public void korvaaTaiLisaa(Pelaaja pelaaja) {
        int id = pelaaja.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = pelaaja;
                muutettu = true;
                return;
            }
        }
        lisaa(pelaaja);
    }
    
    
    /**
     * Tehdään taulukosta kaksinkertaisen kokoinen
     */
    public void suurempiTaulukko() {
        Pelaaja[] uusiTaulukko = new Pelaaja[alkiot.length*2];
        
        for (int i = 0; i < alkiot.length; i++) {
            uusiTaulukko[i] = alkiot[i];
        }
        alkiot = uusiTaulukko;
    }
    

    
    /**
     * Palauttaa viitteen i:teen jäseneen.
     * @param i monennenko jäsenen viite halutaan
     * @return viite jäseneen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
 
    public Pelaaja anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || this.lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /** 
     * Poistaa jäsenen jolla on valittu tunnusnumero  
     * @param id poistettavan jäsenen tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja(), aku3 = new Pelaaja(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * pelaajat.lisaa(aku1); pelaajat.lisaa(aku2); pelaajat.lisaa(aku3); 
     * pelaajat.poista(id1+1) === 1; 
     * pelaajat.annaId(id1+1) === null; pelaajat.getLkm() === 2; 
     * pelaajat.poista(id1) === 1; pelaajat.getLkm() === 1; 
     * pelaajat.poista(id1+3) === 0; pelaajat.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Palauttaa kerhon jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Lukee jäsenistön tiedostosta.  Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String nimi = hakemisto + "/pelaajat.dat";
        File ftied = new File(nimi);
        
        try (Scanner fi = new Scanner(new FileInputStream(ftied))) { 
            while ( fi.hasNext() ) {
                String s = fi.nextLine();
                if ( s == null || "".equals(s) || s.charAt(0) == ';') continue;
                Pelaaja pelaaja = new Pelaaja();
                pelaaja.parse(s); // kertoisi onnistumista ???
                lisaa(pelaaja);
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
     * 2  | Jimi Jännittävä    | 24   | Tikkakoski     |Pro  
     * 3  | Ninni Nipottaja    | 55   | Lahti          |Pro   
     * </pre>
     * @param hakemisto tallennettavan tiedoston hakemisto
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna(String hakemisto) throws SailoException {
        if (!muutettu) return;
        File ftied = new File(hakemisto + "/pelaajat.dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(ftied, false))) {
           for (int i=0; i<this.getLkm(); i++) {
               Pelaaja pelaaja = this.anna(i);
               fo.println(pelaaja.toString());
           }
        } catch (FileNotFoundException ex)  {
            throw new SailoException("Tiedosto " + ftied.getAbsolutePath() + " ei aukea");
        }

    }
    
    
    /**
     * @author jimi_
     * @version 11.5.2022
     *
     */
    public class PelaajatIterator implements Iterator<Pelaaja> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava jäsen
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Pelaaja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori jäsenistään.
     * @return jäsen iteraattori
     */
    @Override
    public Iterator<Pelaaja> iterator() {
        return new PelaajatIterator();
    }

    
    
    
   

    /**
     * @param ehto mitä etsitään
     * @param k kentän indeksi jonka mukaan haetaan
     * @return löytyneet
     */
    public Collection<Pelaaja> etsi(String ehto, int k) {
        ArrayList<Pelaaja> loytyneet = new ArrayList<Pelaaja>();
        int hk = k;
        if(hk < 0) hk = 0;
        for ( int i = 0; i<getLkm(); i++) {
            Pelaaja pelaaja = anna(i); 
            String sisalto = pelaaja.anna(hk);
            if(WildChars.onkoSamat(sisalto, ehto))
                loytyneet.add(pelaaja);
        }
        Collections.sort(loytyneet, new Pelaaja.Vertailija());
        return loytyneet;
    }
    
    
    /** 
     * Etsii pelaajan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return pelaaja jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja(), aku3 = new Pelaaja(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * pelaajat.lisaa(aku1); pelaajat.lisaa(aku2); pelaajat.lisaa(aku3); 
     * pelaajat.annaId(id1  ) == aku1 === true; 
     * pelaajat.annaId(id1+1) == aku2 === true; 
     * pelaajat.annaId(id1+2) == aku3 === true; 
     * </pre> 
     */ 
    public Pelaaja annaId(int id) { 
        for (Pelaaja pelaaja : this) { 
            if (id == pelaaja.getTunnusNro()) return pelaaja; 
        } 
        return null; 
    } 


    /** 
     * Etsii pelaajan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen pelaajan indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Pelaajat pelaajat = new Pelaajat(); 
     * Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja(), aku3 = new Pelaaja(); 
     * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
     * int id1 = aku1.getTunnusNro(); 
     * pelaajat.lisaa(aku1); pelaajat.lisaa(aku2); pelaajat.lisaa(aku3); 
     * pelaajat.etsiId(id1+1) === 1; 
     * pelaajat.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 

    

    /**
     * Testataan pelaajan lisäystä
     * @param args no use
     */
    public static void main(String[] args) {
        Pelaajat pelaajat = new Pelaajat();
        
        try {
            pelaajat.lueTiedostosta("FribaPRO tiedonhallinta");
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }

        
        Pelaaja niklas1 = new Pelaaja();
        Pelaaja niklas2 = new Pelaaja();
      
        
        niklas1.rekisteroi();
        niklas1.vastaaEtuSuku();      // aku.taytaAkuAnkkaTiedoilla();
        niklas2.rekisteroi();
        niklas2.vastaaEtuSuku();
       
        try {
            pelaajat.lisaa(niklas1);
            pelaajat.lisaa(niklas2);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
      
        
        System.out.println("============= Pelaajat testi =================");
        
        for (int i = 0; i < pelaajat.getLkm(); i++) {
            Pelaaja pelaaja = pelaajat.anna(i);
            System.out.println("Pelaaja indeksi: " + i);
            pelaaja.tulosta(System.out);
        }
       
        try {
            pelaajat.tallenna("FribaPRO tiedonhallinta");
        }   catch (SailoException e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }
        
    }





   

}
