package fr.visudo.ui.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerPageAffichage {

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
    private TableColumn<MetriqueLigne, Double> colEnergieConso;
    @FXML
    private TableColumn<MetriqueLigne, Double> colEnergieDispo;
    @FXML
    private TableColumn<MetriqueLigne, Double> colTauxEnergie;

    @FXML
    private BarChart<String, Number> metriquesBarChart;
    @FXML
    private LineChart<Number, Number> energieLineChart;

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerExemple();
    }

    private void configurerColonnes() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSoumission.setCellValueFactory(new PropertyValueFactory<>("soumission"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("debut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("fin"));
        colRam.setCellValueFactory(new PropertyValueFactory<>("ram"));

        colAlgo.setCellValueFactory(new PropertyValueFactory<>("algo"));
        colAttenteMoy.setCellValueFactory(new PropertyValueFactory<>("attenteMoy"));
        colReponseMoy.setCellValueFactory(new PropertyValueFactory<>("reponseMoy"));
        colMakespan.setCellValueFactory(new PropertyValueFactory<>("makespan"));
        colEnergieConso.setCellValueFactory(new PropertyValueFactory<>("energieConso"));
        colEnergieDispo.setCellValueFactory(new PropertyValueFactory<>("energieDispo"));
        colTauxEnergie.setCellValueFactory(new PropertyValueFactory<>("tauxEnergie"));
    }

    private void chargerExemple() {
        ObservableList<ProcessusLigne> processus = FXCollections.observableArrayList(
                new ProcessusLigne("P1", 0, 0, 5, 512),
                new ProcessusLigne("P2", 1, 2, 5, 256),
                new ProcessusLigne("P3", 2, 5, 11, 1024)
        );
        processusTableView.setItems(processus);

        ObservableList<MetriqueLigne> metriques = FXCollections.observableArrayList(
                new MetriqueLigne("FIFO", 2.0, 6.3, 11, 120.0, 200.0, 60.0),
                new MetriqueLigne("RR", 1.5, 5.8, 12, 130.0, 200.0, 65.0)
        );
        metriquesTableView.setItems(metriques);

        mettreAJourBarChart(metriques);

        XYChart.Series<Number, Number> serieDispo = new XYChart.Series<>();
        serieDispo.setName("Énergie disponible");
        serieDispo.getData().add(new XYChart.Data<>(0, 100.0));
        serieDispo.getData().add(new XYChart.Data<>(1, 120.0));
        serieDispo.getData().add(new XYChart.Data<>(2, 150.0));

        XYChart.Series<Number, Number> serieConso = new XYChart.Series<>();
        serieConso.setName("Énergie consommée");
        serieConso.getData().add(new XYChart.Data<>(0, 50.0));
        serieConso.getData().add(new XYChart.Data<>(1, 80.0));
        serieConso.getData().add(new XYChart.Data<>(2, 100.0));

        energieLineChart.getData().setAll(serieDispo, serieConso);
    }

    public void chargerDonnees(Path fichierProcessus, Path fichierMetriques, Path fichierEnergie) throws IOException {
        ObservableList<ProcessusLigne> processus = FXCollections.observableArrayList();
        List<String> lignesProc = Files.readAllLines(fichierProcessus);
        for (int i = 1; i < lignesProc.size(); i++) {
            String ligne = lignesProc.get(i).trim();
            if (ligne.isEmpty()) {
                continue;
            }
            String[] c = ligne.split(",");
            processus.add(new ProcessusLigne(
                    c[0].trim(),
                    Integer.parseInt(c[1].trim()),
                    Integer.parseInt(c[2].trim()),
                    Integer.parseInt(c[3].trim()),
                    Integer.parseInt(c[4].trim())
            ));
        }
        processusTableView.setItems(processus);

        ObservableList<MetriqueLigne> metriques = FXCollections.observableArrayList();
        List<String> lignesMet = Files.readAllLines(fichierMetriques);
        for (int i = 1; i < lignesMet.size(); i++) {
            String ligne = lignesMet.get(i).trim();
            if (ligne.isEmpty()) {
                continue;
            }
            String[] c = ligne.split(",");
            metriques.add(new MetriqueLigne(
                    c[0].trim(),
                    Double.parseDouble(c[1].trim()),
                    Double.parseDouble(c[2].trim()),
                    Integer.parseInt(c[3].trim()),
                    Double.parseDouble(c[4].trim()),
                    Double.parseDouble(c[5].trim()),
                    Double.parseDouble(c[6].trim())
            ));
        }
        metriquesTableView.setItems(metriques);
        mettreAJourBarChart(metriques);

        XYChart.Series<Number, Number> serieDispo = new XYChart.Series<>();
        serieDispo.setName("Énergie disponible");

        XYChart.Series<Number, Number> serieConso = new XYChart.Series<>();
        serieConso.setName("Énergie consommée");

        List<String> lignesEner = Files.readAllLines(fichierEnergie);
        for (int i = 1; i < lignesEner.size(); i++) {
            String ligne = lignesEner.get(i).trim();
            if (ligne.isEmpty()) {
                continue;
            }
            String[] c = ligne.split(",");
            int instant = Integer.parseInt(c[0].trim());
            double dispo = Double.parseDouble(c[1].trim());
            double conso = Double.parseDouble(c[2].trim());

            serieDispo.getData().add(new XYChart.Data<>(instant, dispo));
            serieConso.getData().add(new XYChart.Data<>(instant, conso));
        }
        energieLineChart.getData().setAll(serieDispo, serieConso);
    }

    private void mettreAJourBarChart(List<MetriqueLigne> metriques) {
        XYChart.Series<String, Number> serieAttente = new XYChart.Series<>();
        serieAttente.setName("Attente moyenne");

        XYChart.Series<String, Number> serieReponse = new XYChart.Series<>();
        serieReponse.setName("Réponse moyenne");

        for (MetriqueLigne m : metriques) {
            serieAttente.getData().add(new XYChart.Data<>(m.getAlgo(), m.getAttenteMoy()));
            serieReponse.getData().add(new XYChart.Data<>(m.getAlgo(), m.getReponseMoy()));
        }

        metriquesBarChart.getData().setAll(serieAttente, serieReponse);
    }
}