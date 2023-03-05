package harkka;

import friba.Friba;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Nisse
 * @version 27.1.2022
 *
 */
public class FribaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("FribaGUIView.fxml"));
            final Pane root = ldr.load();
            final FribaGUIController fribaCtrl = (FribaGUIController) ldr.getController();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Friba.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Friba");
            
            Friba friba = new Friba();
            fribaCtrl.setFriba(friba);
            
            primaryStage.setOnCloseRequest((event) -> {
                if ( !fribaCtrl.voikoSulkea() ) event.consume();
            });
            
            primaryStage.show();
            if ( !fribaCtrl.avaa() ) Platform.exit();
            
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}