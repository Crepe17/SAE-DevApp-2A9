package fr.visudo.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.*;
import javafx.application.Platform;

public class GanttParProcesseurController {

    @FXML
    private Canvas canvas;

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
    private Slider sliderMarge;

    private int hauteurCase;
    private int marge;
    private int longueurUniteDeTemps;

    private List<String[]> allocations;

    @FXML
    public void initialize() {
        sliderHauteurCase.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
        sliderMarge.valueProperty().addListener((observable, ancienneValeur, nouvelleValeur) -> { dessinerGantt(allocations); });
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

            //
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    System.out.println(ligne);
                }
            }
            //

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

        hauteurCase = (int) Math.round(sliderHauteurCase.getValue());
        marge = (int) Math.round(sliderMarge.getValue());
        longueurUniteDeTemps = (int) Math.round(sliderLongueurUniteDeTemps.getValue());

        for (String[] allocation : allocations) {

            String processeur = allocation[0];
            String processus = allocation[1];
            int debut = Integer.parseInt(allocation[2]);
            int fin = Integer.parseInt(allocation[3]);

            int niveauProcesseur = Integer.parseInt(processeur.replace("CPU", "")) - 1;
            if (fin > tempsMax) tempsMax = fin;

            int dessinX = (debut * longueurUniteDeTemps) + marge;
            int dessinY = (hauteurCase * niveauProcesseur) + marge;
            int largeurDessin = (fin - debut) * longueurUniteDeTemps;
            int hauteurDessin = hauteurCase;

            // Dessin de la case
            gc.setFill(Color.GREEN);
            gc.fillRect(dessinX, dessinY, largeurDessin, hauteurDessin);
            // Dessin du contour
            gc.setStroke(Color.BLACK);
            gc.strokeRect(dessinX, dessinY, largeurDessin, hauteurDessin);
            // Écriture processus
            gc.setFill(Color.WHITE);
            gc.fillText(processus, dessinX + 3, dessinY + hauteurDessin - 3);
        }

        // Écriture abscisse
        gc.setFill(Color.BLACK);
        for (int i = 0; i < tempsMax; i++) {
            gc.fillText(Integer.toString(i), (i * longueurUniteDeTemps) + marge/* + (longueurUniteDeTemps / 2)*/, marge - 5);
        }
    }
}
