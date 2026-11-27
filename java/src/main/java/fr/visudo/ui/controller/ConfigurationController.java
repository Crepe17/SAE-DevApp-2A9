package fr.visudo.ui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ConfigurationController implements Initializable {

    // --- À ADAPTER : chemins des pages à afficher dans le mainContentPane ---
    private static final String FXML_GANTT_PROCESSUS = "/fr/visudo/ui/view/GanttParProcessus.fxml";
    private static final String FXML_STATS = "/fr/visudo/ui/view/StatsEtGraphiques.fxml";
    private static final String FXML_GANTT_PROCESSEUR = "/fr/visudo/ui/view/GanttParProcesseur.fxml";

    private static final Path FICHIER_CONFIG = Path.of("config.txt");

    @FXML private TextField tfProcessus;
    @FXML private TextField tfRessources;
    @FXML private TextField tfMetriques;

    @FXML private CheckBox cbFIFO;
    @FXML private CheckBox cbRR;
    @FXML private CheckBox cbEDF;

    @FXML private Spinner<Integer> spQuantum;

    @FXML private TextField tfGlobalFIFO;
    @FXML private TextField tfAllocFIFO;
    @FXML private TextField tfGlobalRR;
    @FXML private TextField tfAllocRR;
    @FXML private TextField tfGlobalEDF;
    @FXML private TextField tfAllocEDF;

    @FXML private TitledPane paneFIFO;
    @FXML private TitledPane paneRR;
    @FXML private TitledPane paneEDF;

    @FXML private Label labelStatut;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spQuantum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 2));

        // Un algorithme décoché grise sa section
        paneFIFO.disableProperty().bind(cbFIFO.selectedProperty().not());
        paneRR.disableProperty().bind(cbRR.selectedProperty().not());
        paneEDF.disableProperty().bind(cbEDF.selectedProperty().not());

        appliquerValeursParDefaut();

        // Si un config.txt existe déjà, on le charge
        if (Files.exists(FICHIER_CONFIG)) {
            try {
                chargerDepuis(FICHIER_CONFIG);
            } catch (IOException e) {
                afficherStatut("Lecture de config.txt impossible : " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------
    // Valeurs par défaut / chargement / enregistrement
    // ------------------------------------------------------------------

    private void appliquerValeursParDefaut() {
        tfProcessus.setText("processus.csv");
        tfRessources.setText("ressources.json");
        tfMetriques.setText("metriques.csv");

        cbFIFO.setSelected(true);
        cbRR.setSelected(true);
        cbEDF.setSelected(true);

        spQuantum.getValueFactory().setValue(2);

        tfGlobalFIFO.setText("resultats_fifo_global.csv");
        tfAllocFIFO.setText("resultats_fifo_allocations.csv");
        tfGlobalRR.setText("resultats_roundrobin_global.csv");
        tfAllocRR.setText("resultats_roundrobin_allocations.csv");
        tfGlobalEDF.setText("resultats_EDF_global.csv");
        tfAllocEDF.setText("resultats_EDF_allocations.csv");
    }

    private void chargerDepuis(Path chemin) throws IOException {
        Map<String, String> p = new HashMap<>();
        for (String ligne : Files.readAllLines(chemin)) {
            ligne = ligne.trim();
            if (ligne.isEmpty() || ligne.startsWith("#")) {
                continue;
            }
            int idx = ligne.indexOf('=');
            if (idx < 0) {
                continue;
            }
            p.put(ligne.substring(0, idx).trim(), ligne.substring(idx + 1).trim());
        }

        setSiPresent(tfProcessus, p.get("fichier_processus"));
        setSiPresent(tfRessources, p.get("fichier_ressources"));
        setSiPresent(tfMetriques, p.get("fichier_metriques"));

        String algos = p.get("algorithmes");
        if (algos != null) {
            List<String> liste = new ArrayList<>();
            for (String a : algos.split(",")) {
                liste.add(a.trim().toUpperCase());
            }
            cbFIFO.setSelected(liste.contains("FIFO"));
            cbRR.setSelected(liste.contains("RR"));
            cbEDF.setSelected(liste.contains("EDF"));
        }

        String quantum = p.get("quantum");
        if (quantum != null) {
            try {
                spQuantum.getValueFactory().setValue(Integer.parseInt(quantum));
            } catch (NumberFormatException ignore) {
                // on garde la valeur actuelle
            }
        }

        setSiPresent(tfGlobalFIFO, p.get("fichier_global_FIFO"));
        setSiPresent(tfAllocFIFO, p.get("fichier_allocations_FIFO"));
        setSiPresent(tfGlobalRR, p.get("fichier_global_RoundRobin"));
        setSiPresent(tfAllocRR, p.get("fichier_allocations_RoundRobin"));
        setSiPresent(tfGlobalEDF, p.get("fichier_global_EDF"));
        setSiPresent(tfAllocEDF, p.get("fichier_allocations_EDF"));

        afficherStatut("Configuration chargée depuis " + chemin);
    }

    private void setSiPresent(TextField champ, String valeur) {
        if (valeur != null) {
            champ.setText(valeur);
        }
    }

    private String validerFormulaire() {
        if (tfProcessus.getText().isBlank()) return "Le fichier processus est obligatoire.";
        if (tfRessources.getText().isBlank()) return "Le fichier ressources est obligatoire.";
        if (tfMetriques.getText().isBlank()) return "Le fichier métriques est obligatoire.";
        if (!cbFIFO.isSelected() && !cbRR.isSelected() && !cbEDF.isSelected()) {
            return "Sélectionnez au moins un algorithme.";
        }
        if (cbFIFO.isSelected() && (tfGlobalFIFO.getText().isBlank() || tfAllocFIFO.getText().isBlank())) {
            return "Les fichiers de sortie FIFO sont obligatoires.";
        }
        if (cbRR.isSelected() && (tfGlobalRR.getText().isBlank() || tfAllocRR.getText().isBlank())) {
            return "Les fichiers de sortie RR sont obligatoires.";
        }
        if (cbEDF.isSelected() && (tfGlobalEDF.getText().isBlank() || tfAllocEDF.getText().isBlank())) {
            return "Les fichiers de sortie EDF sont obligatoires.";
        }
        if (cbRR.isSelected()) {
            // commitValue : prend en compte une saisie au clavier non validée
            try {
                spQuantum.increment(0);
            } catch (Exception e) {
                return "Le quantum doit être un entier positif.";
            }
            Integer q = spQuantum.getValue();
            if (q == null || q < 1) return "Le quantum doit être un entier supérieur ou égal à 1.";
        }
        return null;
    }

    private String genererContenu() {
        List<String> algos = new ArrayList<>();
        if (cbFIFO.isSelected()) algos.add("FIFO");
        if (cbRR.isSelected()) algos.add("RR");
        if (cbEDF.isSelected()) algos.add("EDF");

        StringBuilder sb = new StringBuilder();
        sb.append("# Configuration de l'ordonnanceur\n");
        sb.append("fichier_processus = ").append(tfProcessus.getText().trim()).append('\n');
        sb.append("fichier_ressources = ").append(tfRessources.getText().trim()).append('\n');
        sb.append("algorithmes = ").append(String.join(",", algos)).append("\n\n");

        sb.append("# Fichiers de sortie pour FIFO\n");
        sb.append("fichier_global_FIFO = ").append(tfGlobalFIFO.getText().trim()).append('\n');
        sb.append("fichier_allocations_FIFO = ").append(tfAllocFIFO.getText().trim()).append("\n\n");

        sb.append("# Fichiers de sortie pour RR\n");
        sb.append("fichier_global_RoundRobin = ").append(tfGlobalRR.getText().trim()).append('\n');
        sb.append("fichier_allocations_RoundRobin = ").append(tfAllocRR.getText().trim()).append("\n\n");

        sb.append("# Fichiers de sortie pour EDF\n");
        sb.append("fichier_global_EDF = ").append(tfGlobalEDF.getText().trim()).append('\n');
        sb.append("fichier_allocations_EDF = ").append(tfAllocEDF.getText().trim()).append("\n\n");

        sb.append("# Fichier métriques commun\n");
        sb.append("fichier_metriques = ").append(tfMetriques.getText().trim()).append("\n\n");

        sb.append("# Paramètres\n");
        sb.append("quantum = ").append(spQuantum.getValue()).append('\n');
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Actions : boutons de configuration
    // ------------------------------------------------------------------

    @FXML
    private void doEnregistrer() {
        String erreur = validerFormulaire();
        if (erreur != null) {
            afficherErreur(erreur);
            return;
        }
        try {
            Files.writeString(FICHIER_CONFIG, genererContenu());
            afficherStatut("Configuration enregistrée dans " + FICHIER_CONFIG.toAbsolutePath());
        } catch (IOException e) {
            afficherErreur("Écriture impossible : " + e.getMessage());
        }
    }

    @FXML
    private void doCharger() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Charger une configuration");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers texte", "*.txt"));
        File f = fc.showOpenDialog(fenetre());
        if (f == null) {
            return;
        }
        try {
            chargerDepuis(f.toPath());
        } catch (IOException e) {
            afficherErreur("Lecture impossible : " + e.getMessage());
        }
    }

    @FXML
    private void doReinitialiser() {
        appliquerValeursParDefaut();
        afficherStatut("Valeurs par défaut restaurées (non enregistrées).");
    }

    @FXML
    private void doParcourirProcessus() {
        choisirFichier(tfProcessus, "Choisir le fichier processus", "*.csv");
    }

    @FXML
    private void doParcourirRessources() {
        choisirFichier(tfRessources, "Choisir le fichier ressources", "*.json");
    }

    private void choisirFichier(TextField cible, String titre, String extension) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titre);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers " + extension, extension));
        File f = fc.showOpenDialog(fenetre());
        if (f != null) {
            cible.setText(f.getPath());
        }
    }

    // ------------------------------------------------------------------
    // Actions : navigation vers les pages de résultats
    // ------------------------------------------------------------------

    @FXML
    private void doAllerGanttProcessus(ActionEvent event) {
        naviguerVers(event, FXML_GANTT_PROCESSUS);
    }

    @FXML
    private void doAllerStats(ActionEvent event) {
        naviguerVers(event, FXML_STATS);
    }

    @FXML
    private void doAllerGanttProcesseur(ActionEvent event) {
        naviguerVers(event, FXML_GANTT_PROCESSEUR);
    }

    /** Charge le FXML demandé dans le StackPane "mainContentPane" de AppMainFrame. */
    private void naviguerVers(ActionEvent event, String cheminFxml) {
        Node source = (Node) event.getSource();
        Node zone = source.getScene().lookup("#mainContentPane");
        if (!(zone instanceof StackPane contenu)) {
            afficherErreur("Zone d'affichage 'mainContentPane' introuvable.");
            return;
        }
        URL url = getClass().getResource(cheminFxml);
        if (url == null) {
            afficherErreur("Fichier FXML introuvable : " + cheminFxml);
            return;
        }
        try {
            Node page = FXMLLoader.load(url);
            contenu.getChildren().setAll(page);
        } catch (IOException e) {
            afficherErreur("Chargement de la page impossible : " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------
    // Utilitaires
    // ------------------------------------------------------------------

    private Window fenetre() {
        return labelStatut.getScene().getWindow();
    }

    private void afficherStatut(String message) {
        labelStatut.setStyle("-fx-text-fill: #2e7d32;");
        labelStatut.setText(message);
    }

    private void afficherErreur(String message) {
        labelStatut.setStyle("-fx-text-fill: #c62828;");
        labelStatut.setText(message);
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}