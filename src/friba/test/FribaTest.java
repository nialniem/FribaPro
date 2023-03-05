package friba.test;
// Generated by ComTest BEGIN
import kanta.SailoException;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import friba.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2022.05.11 19:28:04 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class FribaTest {


  // Generated by ComTest BEGIN  // Friba: 18
   private Friba friba; 
   private Pelaaja aku1; 
   private Pelaaja aku2; 
   private int jid1; 
   private int jid2; 
   private Kierros pitsi21; 
   private Kierros pitsi11; 
   private Kierros pitsi22; 
   private Kierros pitsi12; 
   private Kierros pitsi23; 

 // @SuppressWarnings("javadoc")
   public void alustaFriba() {
     friba = new Friba(); 
     aku1 = new Pelaaja(); aku1.vastaaEtuSuku(); aku1.rekisteroi(); 
     aku2 = new Pelaaja(); aku2.vastaaEtuSuku(); aku2.rekisteroi(); 
     jid1 = aku1.getTunnusNro(); 
     jid2 = aku2.getTunnusNro(); 
     pitsi21 = new Kierros(jid2); pitsi21.vastaaLaajisFrisbee(jid2); 
     pitsi11 = new Kierros(jid1); pitsi11.vastaaLaajisFrisbee(jid1); 
     pitsi22 = new Kierros(jid2); pitsi22.vastaaLaajisFrisbee(jid2); 
     pitsi12 = new Kierros(jid1); pitsi12.vastaaLaajisFrisbee(jid1); 
     pitsi23 = new Kierros(jid2); pitsi23.vastaaLaajisFrisbee(jid2); 
     try {
     friba.lisaa(aku1); 
     friba.lisaa(aku2); 
     friba.lisaa(pitsi21); 
     friba.lisaa(pitsi11); 
     friba.lisaa(pitsi22); 
     friba.lisaa(pitsi12); 
     friba.lisaa(pitsi23); 
     } catch ( Exception e) {
        System.err.println(e.getMessage()); 
     }
   }
  // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista69 
   * @throws Exception when error
   */
  @Test
  public void testPoista69() throws Exception {    // Friba: 69
    alustaFriba(); 
    assertEquals("From: Friba line: 72", 2, friba.etsi("*",0).size()); 
    assertEquals("From: Friba line: 73", 2, friba.annaKierrokset(aku1).size()); 
    assertEquals("From: Friba line: 74", 1, friba.poista(aku1)); 
    assertEquals("From: Friba line: 75", 1, friba.etsi("*",0).size()); 
    assertEquals("From: Friba line: 76", 0, friba.annaKierrokset(aku1).size()); 
    assertEquals("From: Friba line: 77", 3, friba.annaKierrokset(aku2).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoistaKierros93 
   * @throws Exception when error
   */
  @Test
  public void testPoistaKierros93() throws Exception {    // Friba: 93
    alustaFriba(); 
    assertEquals("From: Friba line: 96", 2, friba.annaKierrokset(aku1).size()); 
    friba.poistaKierros(pitsi11); 
    assertEquals("From: Friba line: 98", 1, friba.annaKierrokset(aku1).size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testLisaa109 */
  @Test
  public void testLisaa109() {    // Friba: 109
    Friba friba = new Friba(); 
    Pelaaja niklas1 = new Pelaaja(), niklas2 = new Pelaaja(); 
    niklas1.rekisteroi(); niklas2.rekisteroi(); 
    assertEquals("From: Friba line: 113", 0, friba.getPelaajia()); 
    friba.lisaa(niklas1); assertEquals("From: Friba line: 114", 1, friba.getPelaajia()); 
    friba.lisaa(niklas2); assertEquals("From: Friba line: 115", 2, friba.getPelaajia()); 
    friba.lisaa(niklas1); assertEquals("From: Friba line: 116", 3, friba.getPelaajia()); 
    assertEquals("From: Friba line: 117", 3, friba.getPelaajia()); 
    assertEquals("From: Friba line: 118", niklas1, friba.annaPelaaja(0)); 
    assertEquals("From: Friba line: 119", niklas2, friba.annaPelaaja(1)); 
    assertEquals("From: Friba line: 120", niklas1, friba.annaPelaaja(2)); 
    try {
    assertEquals("From: Friba line: 121", niklas1, friba.annaPelaaja(3)); 
    fail("Friba: 121 Did not throw IndexOutOfBoundsException");
    } catch(IndexOutOfBoundsException _e_){ _e_.getMessage(); }
    friba.lisaa(niklas1); assertEquals("From: Friba line: 122", 4, friba.getPelaajia()); 
    friba.lisaa(niklas1); assertEquals("From: Friba line: 123", 5, friba.getPelaajia()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnnaKierrokset173 */
  @Test
  public void testAnnaKierrokset173() {    // Friba: 173
    Kierrokset kierrokset = new Kierrokset(); 
    Kierros round21 = new Kierros(2); kierrokset.lisaa(round21); 
    Kierros round11 = new Kierros(1); kierrokset.lisaa(round11); 
    Kierros round22 = new Kierros(2); kierrokset.lisaa(round22); 
    Kierros round12 = new Kierros(1); kierrokset.lisaa(round12); 
    Kierros round23 = new Kierros(2); kierrokset.lisaa(round23); 
    Kierros round51 = new Kierros(5); kierrokset.lisaa(round51); 
    List<Kierros> loytyneet; 
    loytyneet = kierrokset.annaKierrokset(3); 
    assertEquals("From: Friba line: 186", 0, loytyneet.size()); 
    loytyneet = kierrokset.annaKierrokset(1); 
    assertEquals("From: Friba line: 188", 2, loytyneet.size()); 
    assertEquals("From: Friba line: 189", true, loytyneet.get(0) == round11); 
    assertEquals("From: Friba line: 190", true, loytyneet.get(1) == round12); 
    loytyneet = kierrokset.annaKierrokset(5); 
    assertEquals("From: Friba line: 192", 1, loytyneet.size()); 
    assertEquals("From: Friba line: 193", true, loytyneet.get(0) == round51); 
  } // Generated by ComTest END
}