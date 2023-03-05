package harkka;


import fi.jyu.mit.fxgui.Dialogs;
// import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
// import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
// import javafx.scene.control.TextArea;

/**
 * @author jimi_
 * @version 21.2.2022
 *
 */
public class TuloskorttiController implements ModalControllerInterface<String> {
    ObservableList<String> radatList = FXCollections.observableArrayList("1. Keljonkankaan frisbeegolf", "2. Laajis Frisbeegolf", "3. Vesanka DiscGolfPark", "4. Kuokkalan frisbeegolf", "5. Huhtasuo DiscGolfPark", "6. Seppälänkankaan frisbeegolfrata");
    
    
    @FXML private ChoiceBox<String> radanNimiBox;

    
    @FXML private void handleTallenna() {
        tallenna1();
    }
    
    @FXML private void handleTakas() {
        Dialogs.showMessageDialog("Ei toimi vielä, paina ruksia!");
    }
    
    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
    @FXML
    private void initialize() {
        radanNimiBox.setItems(radatList);
        radanNimiBox.setValue("Radat");
    }
   
    // =======================================================================================================
    
    /**
     * Tietojen tallennus
     */
    private void tallenna1() {
        Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi vielä");
    }
    

}

