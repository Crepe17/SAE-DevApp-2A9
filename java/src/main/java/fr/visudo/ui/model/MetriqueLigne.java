package fr.visudo.ui.model;

public class MetriqueLigne {
    private final String algo;
    private final double attenteMoy;
    private final double reponseMoy;
    private final int makespan;
    private final double energieConso;
    private final double energieDispo;
    private final double tauxEnergie;

    public MetriqueLigne(
            String algo,
            double attenteMoy,
            double reponseMoy,
            int makespan,
            double energieConso,
            double energieDispo,
            double tauxEnergie) {
        this.algo = algo;
        this.attenteMoy = attenteMoy;
        this.reponseMoy = reponseMoy;
        this.makespan = makespan;
        this.energieConso = energieConso;
        this.energieDispo = energieDispo;
        this.tauxEnergie = tauxEnergie;
    }

    public String getAlgo() {
        return algo;
    }

    public double getAttenteMoy() {
        return attenteMoy;
    }

    public double getReponseMoy() {
        return reponseMoy;
    }

    public int getMakespan() {
        return makespan;
    }

    public double getEnergieConso() {
        return energieConso;
    }

    public double getEnergieDispo() {
        return energieDispo;
    }

    public double getTauxEnergie() {
        return tauxEnergie;
    }
}