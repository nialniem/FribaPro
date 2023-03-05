package harkka;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * @author jimi_
 * @version 21.2.2022
 *
 */
public class TulostusController implements ModalControllerInterface<String> {
    @FXML TextArea tulostusAlue;
    
    @FXML private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }

    
    @FXML private void handleTulosta() {
        Dialogs.showMessageDialog("Ei osata vielä tulostaa");
        //tulosta();
    }
    

    
    @Override
    public String getResult() {
        return null;
    } 

    
    @Override
    public void setDefault(String oletus) {
        if ( oletus == null ) return;
        tulostusAlue.setText(oletus);
    }

    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        //
    }
    
    
    /**
     * Näyttää tulostusalueessa tekstin
     * @param tulostus tulostettava teskti
     */
    public static void tulosta(String tulostus) {
        ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"),
                "Tulostus", tulostus);
    }

}

