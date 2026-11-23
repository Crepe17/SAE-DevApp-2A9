package fr.visudo.ui.controller;

public class ProcessusLigne {
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