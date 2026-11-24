package fr.visudo.ui;

import java.net.URL;

import fr.visudo.ui.controller.AppMainFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class VisudoApp extends Application {
    
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Configuration");

        // Initialisation de la fenêtre principale
        try {
            // Chargement du style global
            URL cssUrl = VisudoApp.class.getResource("css/style.css");
            if (cssUrl == null) {
                System.err.println("Le fichier de style n'a pas été trouvé !");
                return;
            }

            // Chargement de la main frame
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(VisudoApp.class.getResource("/fr/visudo/ui/controller/AppMainFrame.fxml"));
            BorderPane mainFrame = loader.load();

            Scene scene = new Scene(mainFrame, 800, 600);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lancement de l'application.
     */
    public static void runApp(String[] args) {
        launch(args);
    }

    /**
     * Retourne la fenêtre principale.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
