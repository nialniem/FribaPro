package harkka;

import java.awt.Desktop;
import java.awt.Label;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import friba.Friba;
import friba.Kierrokset;
import friba.Kierros;
import friba.Pelaaja;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import kanta.SailoException;
import javafx.scene.control.TableView;
import static harkka.TietueDialogController.getFieldId; 

/**
 * @author jimi_
 * @version 1.2.2022
 *
 */
@SuppressWarnings("unused")
public class FribaGUIController implements Initializable {
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Pelaaja> chooserPelaajat;
    @FXML private ScrollPane panelPelaaja;
    
    @FXML private GridPane gridPelaaja;
    
    @FXML StringGrid<Kierros> tableKierrokset;
    
    private String pelaajannimi = "Niklas Nopea";
    private String radannimi = "Keljonkankaan frisbeegolf";
    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta(); 
    }
    
    
    @FXML private void handleHakuehto() {
        hae(0);
    }
    
    @FXML private void handleTallenna() {
        tallenna();
    }

    @FXML private void handleAvaa() {
        avaa();
    }
    
    // TODO tee tulostus
    // @FXML private void handleTulosta() {
    //     TulostusController.tulosta(null);
    // } 

    
    @FXML private void handleApua() {
        avustus();
    }
    
    @FXML private void handleValmis() {
        tallenna();
        Platform.exit();
    }
    
    @FXML private void handleLisaaPelaaja() {
       uusiPelaaja();
    }

    @FXML private void handleMuokkaaPelaaja() {
      muokkaa(kentta);
    }
    
    @FXML private void handlePoistaPelaaja() {
        poistaPelaaja();
    }
    
    @FXML private void handleLisaaKierros() {
        lisaaKierros();
    }
    
    @FXML private void handleMuokkaaKierrosta() {
        muokkaaKierrosta();
   }

    @FXML private void handlePoistaKierros() {
        poistaKierros();
    }
    
    @FXML private void handleAloitaKierros() {
        ModalController.showModal(FribaGUIController.class.getResource("TuloskorttiGUIView.fxml"), radannimi, null, "");
    }

    @FXML private void handleTietoja() {
        Dialogs.showMessageDialog("Ei osata vielä tietoja");
        // ModalController.showModal(FribaGUIController.class.getResource("AboutView.fxml"), "Friba", null, "");

   }
    
    @FXML private void handleTulosta() {
        Dialogs.showMessageDialog("Ei osata vielä tulostaa");
    } 

    
  



// ========================================================================================================================
// Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
    
    private String fribannimi = "Friba";
    private Friba friba;
    private Pelaaja pelaajanKohdalla;
    private TextField[] edits;
    private int kentta = 0; 
    private static Pelaaja apupelaaja = new Pelaaja(); 
    private static Kierros apukierros = new Kierros();
    
    private void alusta() {
        
        panelPelaaja.setFitToHeight(true);
        
        chooserPelaajat.clear();
        cbKentat.clear();
        chooserPelaajat.addSelectionListener(e -> naytaPelaaja());
        
        for (int k = apupelaaja.ekaKentta(); k <apupelaaja.getKenttia(); k++) {
            cbKentat.add(apupelaaja.getKysymys(k));
        }
        cbKentat.setSelectedIndex(0);
        edits = TietueDialogController.luoKentat(gridPelaaja, new Pelaaja());
        for (TextField edit: edits)
            if ( edit != null ) {
                edit.setEditable(false);
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(edit,kentta)); });  
            }
        
        // alustetaan kierrostaulukon otsikot 
        int eka = apukierros.ekaKentta(); 
        int lkm = apukierros.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apukierros.getKysymys(k); 
        tableKierrokset.initTable(headings); 
        tableKierrokset.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableKierrokset.setEditable(false); 
        // TODO: tableKierrokset.setPlaceholder(new Label("Ei vielä kierroksia")); 
         
        // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
        tableKierrokset.setColumnSortOrderNumber(1); 
        tableKierrokset.setColumnSortOrderNumber(2); 
        tableKierrokset.setColumnWidth(1, 60); 
        tableKierrokset.setColumnWidth(2, 60); 

        
         tableKierrokset.setEditable(false);
         tableKierrokset.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaKierrosta();
 } );
         tableKierrokset.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) 
 muokkaaKierrosta();}); 
     }
    
    private void muokkaaKierrosta() {
        pelaajanKohdalla = chooserPelaajat.getSelectedObject(); 
        int r = tableKierrokset.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Kierros kier = tableKierrokset.getObject();
        if ( kier == null ) return;
        int k = tableKierrokset.getColumnNr()+kier.ekaKentta();
        try {
            kier = TietueDialogController.kysyTietue(null, kier.clone(), k);
            if ( kier == null ) return;
            friba.korvaaTaiLisaa(kier); 
            naytaKierrokset(pelaajanKohdalla); 
            tableKierrokset.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }


    

    
    /**
     * Näyttää listasta valitun jäsenen tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
    protected void naytaPelaaja() {
        pelaajanKohdalla = chooserPelaajat.getSelectedObject();

        if (pelaajanKohdalla == null) return;
        
        TietueDialogController.naytaTietue(edits, pelaajanKohdalla);
        naytaKierrokset(pelaajanKohdalla);
  
    }
    
    private void naytaKierrokset(Pelaaja pelaaja) {
        tableKierrokset.clear();
        if (pelaaja == null) return;
        List<Kierros> kierrokset = friba.annaKierrokset(pelaaja);
        if (kierrokset.size() == 0) return;
        for (Kierros kier: kierrokset)
            naytaKierros(kier);
       
    }
    
    private void naytaKierros(Kierros kier) {
        int kenttia = kier.getKenttia(); 
        String[] rivi = new String[kenttia-kier.ekaKentta()]; 
        for (int i=0, k=kier.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = kier.anna(k); 
        tableKierrokset.add(kier,rivi);
    }

    private void tulosta(PrintStream os, final Pelaaja pelaaja) {            
        os.println("-------------------------------------------------------------");
        pelaaja.tulosta(os);
        os.println("-------------------------------------------------------------");
        List<Kierros> kierrokset = friba.annaKierrokset(pelaaja);
        for(Kierros kier : kierrokset)
            kier.tulosta(os);
        os.println("-------------------------------------------------------------");
    }


        /**
         * 
         * 
         */
    private void avustus() {
         Desktop desktop = Desktop.getDesktop();
         try {
             URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2022k/ht/hiltujx");
             desktop.browse(uri);
         } catch (URISyntaxException e) {
             return;
         } catch (IOException e) {
             return;
         }
    }
    
    
    /**
     * Asetetaan käytettävä friba
     * @param friba jota käytetään
     */
    public void setFriba(Friba friba) {
        this.friba = friba;
        
    }
    
    
    private void hae(final int pnr) {
        int pnro = pnr;
        if (pnro == 0) {
            Pelaaja kohdalla = chooserPelaajat.getSelectedObject();
            if(kohdalla != null) pnro = kohdalla.getTunnusNro();
        }
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apupelaaja.ekaKentta();
        
        chooserPelaajat.clear();
        String ehto = hakuehto.getText();
        if(ehto.indexOf('*') < 0) ehto = "*"+ ehto + "*";
        
        Collection<Pelaaja> pelaajat = friba.etsi(ehto,k);
        int index = 0;
        int ci = 0;
        for (Pelaaja pelaaja: pelaajat) {
            if (pelaaja.getTunnusNro() == pnro) index = ci;
            chooserPelaajat.add(""+pelaaja.getNimi(), pelaaja);
            ci++;
        }
        chooserPelaajat.setSelectedIndex(index);
        
    }
    
    private void uusiPelaaja() {
        Pelaaja uusi = new Pelaaja();    
        uusi = TietueDialogController.kysyTietue(null, uusi, uusi.ekaKentta()); 
        if ( uusi == null ) return;
        uusi.rekisteroi();
        friba.lisaa(uusi);
        hae(uusi.getTunnusNro());
    }
    
    
    /*
     * Tekee uuden tyhjän kierroksen editointia varten
     */
    private void lisaaKierros() {
        pelaajanKohdalla = chooserPelaajat.getSelectedObject(); 
        if ( pelaajanKohdalla == null ) return;

        Kierros uusi = new Kierros(pelaajanKohdalla.getTunnusNro());
        uusi = TietueDialogController.kysyTietue(null, uusi, 0);
        if ( uusi == null ) return;
        uusi.rekisteroi();
        friba.lisaa(uusi);
        naytaKierrokset(pelaajanKohdalla); 
        tableKierrokset.selectRow(1000);  // järjestetään viimeinen rivi valituksi
    }

    
    

    /**
     * Alustaa kerhon lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     */
    private void lueTiedosto(String nimi) {
        try {
            friba.lueTiedostosta(nimi);
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }


    /**
     * @return false jos painetaan cancel
     */
    public boolean avaa() {
        String uusinimi = PelaajanNimiController.kysyNimi(null, pelaajannimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }
    
    
    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        try {
            friba.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }
 
    private void muokkaa(int k) {
        pelaajanKohdalla = chooserPelaajat.getSelectedObject(); 
        if (pelaajanKohdalla == null) return;
        try {
            Pelaaja pelaaja = TietueDialogController.kysyTietue(null, pelaajanKohdalla.clone(), k);
            if (pelaaja == null) return;
            friba.korvaaTaiLisaa(pelaaja);
            hae(pelaaja.getTunnusNro());
        } catch (CloneNotSupportedException | SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }

    }
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }
    

    /**
     * Poistetaan harrastustaulukosta valitulla kohdalla oleva harrastus. 
     */
    private void poistaKierros() {
        int rivi = tableKierrokset.getRowNr();
        if ( rivi < 0 ) return;
        Kierros kierros = tableKierrokset.getObject();
        if ( kierros == null ) return;
        friba.poistaKierros(kierros);
        naytaKierrokset(pelaajanKohdalla);
        int kierroksia = tableKierrokset.getItems().size(); 
        if ( rivi >= kierroksia ) rivi = kierroksia-1;
        tableKierrokset.getFocusModel().focus(rivi);
        tableKierrokset.getSelectionModel().select(rivi);
    }


    /*
     * Poistetaan listalta valittu jäsen
     */
    private void poistaPelaaja() {
        Pelaaja pelaaja = pelaajanKohdalla;
        if ( pelaaja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + pelaaja.getNimi(), "Kyllä", "Ei") )
            return;
        friba.poista(pelaaja);
        int index = chooserPelaajat.getSelectedIndex();
        hae(0);
        chooserPelaajat.setSelectedIndex(index);
    }

    






}

