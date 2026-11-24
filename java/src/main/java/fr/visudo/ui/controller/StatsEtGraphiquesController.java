package fr.visudo.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StatsEtGraphiquesController implements Initializable {

    @FXML
    private TableView<ProcessusLigne> processusTableView;
    @FXML
    private TableColumn<ProcessusLigne, String> colId;
    @FXML
    private TableColumn<ProcessusLigne, Integer> colSoumission;
    @FXML
    private TableColumn<ProcessusLigne, Integer> colDebut;
    @FXML
    private TableColumn<ProcessusLigne, Integer> colFin;
    @FXML
    private TableColumn<ProcessusLigne, Integer> colRam;

    @FXML
    private TableView<MetriqueLigne> metriquesTableView;
    @FXML
    private TableColumn<MetriqueLigne, String> colAlgo;
    @FXML
    private TableColumn<MetriqueLigne, Double> colAttenteMoy;
    @FXML
    private TableColumn<MetriqueLigne, Double> colReponseMoy;
    @FXML
    private TableColumn<MetriqueLigne, Integer> colMakespan;

    @FXML
    private BarChart<String, Number> metriquesBarChart;

    private final ObservableList<ProcessusLigne> processusData = FXCollections.observableArrayList();
    private final ObservableList<MetriqueLigne> metriquesData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurerColonnes();
        processusTableView.setItems(processusData);
        metriquesTableView.setItems(metriquesData);

        try {
            chargerCSVProcess();
            chargerCSVMetrique();
            mettreAJourGraphique();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configurerColonnes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSoumission.setCellValueFactory(new PropertyValueFactory<>("soumission"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("debut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("fin"));
        colRam.setCellValueFactory(new PropertyValueFactory<>("ram"));

        colAlgo.setCellValueFactory(new PropertyValueFactory<>("politique"));
        colAttenteMoy.setCellValueFactory(new PropertyValueFactory<>("attenteMoy"));
        colReponseMoy.setCellValueFactory(new PropertyValueFactory<>("reponseMoy"));
        colMakespan.setCellValueFactory(new PropertyValueFactory<>("makespan"));
    }

    public void chargerCSVProcess() throws IOException {
        Path chemin = Path.of("Outputs/Test1/processus.csv");
        List<String> lignes = Files.readAllLines(chemin);
        processusData.clear();

        for (int i = 1; i < lignes.size(); i++) {
            String ligne = lignes.get(i).trim();
            if (ligne.isEmpty()) {
                continue;
            }
            String[] colonnes = ligne.split(",");
            String id = colonnes[0].trim();
            int soumission = Integer.parseInt(colonnes[1].trim());
            int debut = Integer.parseInt(colonnes[2].trim());
            int fin = Integer.parseInt(colonnes[3].trim());
            int ram = Integer.parseInt(colonnes[4].trim());

            processusData.add(new ProcessusLigne(id, soumission, debut, fin, ram));
        }
    }

    public void chargerCSVMetrique() throws IOException {
        Path chemin = Path.of("Outputs/Test1/metriques.csv");
        List<String> lignes = Files.readAllLines(chemin);
        metriquesData.clear();

        for (int i = 1; i < lignes.size(); i++) {
            String ligne = lignes.get(i).trim();
            if (ligne.isEmpty()) {
                continue;
            }
            String[] colonnes = ligne.split(",");
            String politique = colonnes[0].trim();
            Double attenteMoy = Double.parseDouble(colonnes[1].trim());
            Double reponseMoy = Double.parseDouble(colonnes[2].trim());
            int makespan = Integer.parseInt(colonnes[3].trim());

            metriquesData.add(new MetriqueLigne(politique, attenteMoy, reponseMoy, makespan));
        }
    }

    private void mettreAJourGraphique() {
        XYChart.Series<String, Number> serieAttente = new XYChart.Series<>();
        serieAttente.setName("Attente moyenne");

        XYChart.Series<String, Number> serieReponse = new XYChart.Series<>();
        serieReponse.setName("Réponse moyenne");

        XYChart.Series<String, Number> serieMakespan = new XYChart.Series<>();
        serieMakespan.setName("Makespan");

        for (MetriqueLigne m : metriquesData) {
            serieAttente.getData().add(new XYChart.Data<>(m.getPolitique(), m.getAttenteMoy()));
            serieReponse.getData().add(new XYChart.Data<>(m.getPolitique(), m.getReponseMoy()));
            serieMakespan.getData().add(new XYChart.Data<>(m.getPolitique(), m.getMakespan()));
        }

        metriquesBarChart.getData().setAll(serieAttente, serieReponse, serieMakespan);
    }

    public static class ProcessusLigne {
        private final String id;
        private final int soumission;
        private final int debut;
        private final int fin;
        private final int ram;

        public ProcessusLigne(String id, int soumission, int debut, int fin, int ram) {
            this.id = id;
            this.soumission = soumission;
            this.debut = debut;
            this.fin = fin;
            this.ram = ram;
        }

        public String getId() {
            return id;
        }

        public int getSoumission() {
            return soumission;
        }

        public int getDebut() {
            return debut;
        }

        public int getFin() {
            return fin;
        }

        public int getRam() {
            return ram;
        }
    }

    public static class MetriqueLigne {
        private final String politique;
        private final Double attenteMoy;
        private final Double reponseMoy;
        private final int makespan;

        public MetriqueLigne(String politique, Double attenteMoy, Double reponseMoy, int makespan) {
            this.politique = politique;
            this.attenteMoy = attenteMoy;
            this.reponseMoy = reponseMoy;
            this.makespan = makespan;
        }

        public String getPolitique() {
            return politique;
        }

        public Double getAttenteMoy() {
            return attenteMoy;
        }

        public Double getReponseMoy() {
            return reponseMoy;
        }

        public int getMakespan() {
            return makespan;
        }
    }
}