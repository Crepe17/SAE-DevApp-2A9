package fr.visudo.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.*;
import javafx.application.Platform;

public class GanttParProcesseurController {

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button simulerFIFO;

    @FXML
    private Button simulerRR;

    @FXML
    private Button RAZ;

    @FXML
    private Slider sliderLongueurUniteDeTemps;

    @FXML
    private Slider sliderHauteurCase;

    @FXML
    private Slider sliderMargeX;

    @FXML
    private Slider sliderMargeY;

    @FXML
    private Button reinitialiser;

    private int hauteurCase;
    private int margeX;
    private int margeY;
    private int longueurUniteDeTemps;

    private List<String[]> allocations;

    @FXML
    public void initialize() {
        anchorPane.widthProperty().addListener((obs, oldVal, newVal) -> { canvas.setWidth(newVal.doubleValue()); });
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) -> { canvas.setHeight(newVal.doubleValue()); });

        sliderHauteurCase.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
        sliderMargeX.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
        sliderMargeY.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
        sliderLongueurUniteDeTemps.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
    }

    @FXML
    private void simulerFIFO() {
        executerPython("FIFO");
        dessinerGantt(allocations);
    }

    @FXML
    private void simulerRR() {
        executerPython("RR");
        dessinerGantt(allocations);
    }

    @FXML
    private void remettreAZero() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void executerPython(String algo) {
        Thread lanceurPython = new Thread(() -> {

        try {
            ProcessBuilder pb = new ProcessBuilder("python", "main.py");

            File dossierPython = new File("python/Code").getCanonicalFile();
            pb.directory(dossierPython);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    System.out.println(ligne);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                String fichierCSV;
                if (algo.equals("FIFO")) {
                    fichierCSV = "Outputs/test1/resultats_FIFO_allocations.csv";
                } else {
                    fichierCSV = "Outputs/test1/resultats_RoundRobin_allocations.csv";
                }

                File csv = new File(dossierPython.getParentFile(), fichierCSV);
                List<String[]> alloc = lireCSV(csv);

                Platform.runLater(() -> {
                    allocations = alloc;
                });

                } else {
                    Platform.runLater(() ->
                        System.out.println("Erreur lors de la simulation Python")
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        lanceurPython.start();
    }

    private List<String[]> lireCSV(File fichier) throws IOException {

        List<String[]> allocations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {

            String ligne;
            boolean premiereLigne = true;

            while ((ligne = br.readLine()) != null) {

                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                allocations.add(ligne.split(","));
            }
        }

        return allocations;
    }

    private void dessinerGantt(List<String[]> allocations) {

        remettreAZero();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        int tempsMax = 0;
        int processeurMax = 1;

        longueurUniteDeTemps = (int) Math.round(sliderLongueurUniteDeTemps.getValue());
        hauteurCase = (int) Math.round(sliderHauteurCase.getValue());
        margeX = (int) Math.round(sliderMargeX.getValue());
        margeY = (int) Math.round(sliderMargeY.getValue());

        for (String[] allocation : allocations) {

            String processeur = allocation[0];
            String processus = allocation[1];
            int debut = Integer.parseInt(allocation[2]);
            int fin = Integer.parseInt(allocation[3]);

            int numeroProcesseur = Integer.parseInt(processeur.replace("CPU", ""));
            if (numeroProcesseur > processeurMax) processeurMax = numeroProcesseur;
            if (fin > tempsMax) tempsMax = fin;

            int dessinX = (debut * longueurUniteDeTemps) + margeX;
            int dessinY = (hauteurCase * (numeroProcesseur - 1)) + margeY;
            int largeurDessin = (fin - debut) * longueurUniteDeTemps;
            int hauteurDessin = hauteurCase;

            // Dessin de la case
            switch (Integer.parseInt(processus.replace("P", "")) % 5) {
                case 1: {
                    gc.setFill(Color.CORNFLOWERBLUE);
                    break;
                }
                case 2: {
                    gc.setFill(Color.MEDIUMSEAGREEN);
                    break;
                }
                case 3: {
                    gc.setFill(Color.CORAL);
                    break;
                }
                case 4: {
                    gc.setFill(Color.MEDIUMPURPLE);
                    break;
                }
                case 0: {
                    gc.setFill(Color.SANDYBROWN);
                    break;
                }
            }
            gc.fillRect(dessinX, dessinY, largeurDessin, hauteurDessin);

            // Dessin du contour
            gc.setStroke(Color.BLACK);
            gc.strokeRect(dessinX, dessinY, largeurDessin, hauteurDessin);

            // Écriture processus
            gc.setFill(Color.BLACK);
            gc.fillText(processus, dessinX + 3, dessinY + hauteurDessin - 3);
        }

        gc.setFill(Color.BLACK);

        // Écriture abscisse
        for (int i = 0; i < tempsMax; i++) {
            gc.fillText(Integer.toString(i), (i * longueurUniteDeTemps) + margeX, margeY - 5);
        }

        // Écriture ordonnée
        for (int j = 0; j < processeurMax; j++) {
            gc.fillText("CPU" + Integer.toString(j), margeX - 40, (j * hauteurCase) + (hauteurCase/2) + margeY + 5);
        }
    }

    @FXML
    private void reinitialiser() {
        sliderLongueurUniteDeTemps.setValue(30.0);
        sliderHauteurCase.setValue(25.0);
        sliderMargeX.setValue(25.0);
        sliderMargeY.setValue(25.0);
    }
}
