package harkka;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import friba.Pelaaja;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;

/**
 * Kysytään tietueen tiedot luomalla sille uusi dialogi
 * @author Nisse
 * @param <TYPE> Minkä tyyppisiä olioita käsitellään
 */
public class TietueDialogController<TYPE extends Tietue> implements ModalControllerInterface<TYPE>,Initializable  {

    @FXML TextField editNimi;
    @FXML TextField editIka;
    @FXML TextField editKKaupunki;
    @FXML TextField editTaso;
    @FXML TextField editID;
    
    @FXML Label labelVirhe;
    @FXML GridPane gridTietue;
    @FXML ScrollPane panelTietue;


    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // alusta();
        // Ei voi täällä vielä alustaa kun ei ole tietueKohdale olemassa, siirretty setDefault

        
    }

    @Override
    public TYPE getResult() {
        return tietueKohdalla;
    }


    @Override
    public void handleShown() {
        kentta = Math.max(tietueKohdalla.ekaKentta(), Math.min(kentta, tietueKohdalla.getKenttia()-1)); 
        edits[kentta].requestFocus(); 
    }



    @Override
    public void setDefault(TYPE oletus) {
        this.tietueKohdalla = oletus;
        alusta();
        naytaTietue(edits, tietueKohdalla);
    }

    
  
    
    @FXML private void handleOK() {
        if ( tietueKohdalla != null && tietueKohdalla.anna(tietueKohdalla.ekaKentta()).trim().equals("") ) {
            naytaVirhe("ei saa olla tyhjä");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }
   
    /**
     * 
     */
    @FXML private void handleCancel() {
        tietueKohdalla = null;
        ModalController.closeStage(labelVirhe);
        
    }
    //===============================================================================================================================
    
    private TYPE tietueKohdalla;
    private TextField[] edits;
    private int kentta = 0;
    
    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponentin id lukuna 
     */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }
    
    /**
     * Luodaan GridPaneen pelaajan tiedot
     * @param gridTietue mihin tiedot luodaan
     * @param aputietue malli josta tiedot otetaan
     * @return luodut tekstikentät
     */
    public static<TYPE extends Tietue> TextField[] luoKentat(GridPane gridTietue, TYPE aputietue) {
        gridTietue.getChildren().clear();
        TextField[] edits = new TextField[aputietue.getKenttia()];

        
        for (int i=0, k = aputietue.ekaKentta(); k < aputietue.getKenttia(); k++, i++) {
            Label label = new Label(aputietue.getKysymys(k));
            gridTietue.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridTietue.add(edit, 1, i);
        }
        return edits;
    }
    
    
    private void alusta() {
        edits = luoKentat(gridTietue, tietueKohdalla);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosTietueeseen((TextField)(e.getSource())));
        panelTietue.setFitToHeight(true);

        }

    
    private void setKentta(int kentta) {
        this.kentta = kentta; 
     }

    private void kasitteleMuutosTietueeseen(TextField edit) {
        if (tietueKohdalla == null) return;
        String s = edit.getText();
        int k = getFieldId(edit,tietueKohdalla.ekaKentta());
        String virhe = tietueKohdalla.aseta(k, s);

        if ( virhe != null) {
            Dialogs.setToolTipText(edit, virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit, "");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        }
    }

    
    /**
     * Näytetään pelaajan tiedot TextField komponentteihin
     * @param edits taulukko jossa tekstikenttiä
     * @param tietue näytettävä jäsen
     */
    public static void naytaTietue(TextField[] edits, Tietue tietue) {
        if (tietue == null) return;
        for (int k = tietue.ekaKentta(); k < tietue.getKenttia(); k++) {
            edits[k].setText(tietue.anna(k));
        }
    }
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    /**
     * Luodaan pelaajan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * TODO: korjattava toimimaan
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static<TYPE extends Tietue> TYPE kysyTietue(Stage modalityStage, TYPE oletus, int kentta) {
        return ModalController.<TYPE, TietueDialogController<TYPE>>showModal(
                TietueDialogController.class.getResource("TietueDialogView.fxml"),
                "Friba",
                modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta) 
                );
    }



}
