package fr.visudo.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    public void initialize() {
        //
    }

    @FXML
    private void simulerFIFO() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.fillText("test", 10, 10);
    }

    @FXML
    private void simulerRR() {
        //
    }

    @FXML
    private void remettreAZero() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(
            0,
            0,
            canvas.getWidth(),
            canvas.getHeight()
        );
    }
}