/**
 * 
 */
package harkka;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author jimi_
 * @version 21.2.2022
 *
 */
public class PelaajanNimiController implements ModalControllerInterface<String> {
    
    
    @FXML private TextField textVastaus;
    private String vastaus = null;
    
    @FXML private void handleOK() {
        vastaus = textVastaus.getText();
        ModalController.closeStage(textVastaus);
    }

    @FXML private void handleCancel() {
        ModalController.closeStage(textVastaus);
    }
    

    @Override
    public String getResult() {
        return vastaus;
    }

    @Override
    public void handleShown() {
        textVastaus.requestFocus();
        
    }

    @Override
    public void setDefault(String oletus) {
        textVastaus.setText(oletus);
        
    }
    

    /**
     * Luodaan nimenkysymisdialogi ja palautetaan siihen kirjoitettu nimi tai null
     * @param modalityStage mille ollaan modaalisia, null = sovelluksell
     * @param oletus mitä nimeä käytetään oletuksena
     * @return null jos painetaan Cancel, muuten kirjoitettu nimi
     */
    public static String kysyNimi(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                PelaajanNimiController.class.getResource("PelaajanNimiGUIView.fxml"),
                "Niklas Nopea",
                modalityStage, oletus);
                
   
    }

}
