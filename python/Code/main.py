import sys
from pathlib import Path

import affichage
import metriques
import politique
import reader
import writer

# nom dans `algorithmes` -> (suffixe des clés de sortie dans config.txt, fonction)
ALGORITHMES = {
    "FIFO": ("FIFO", lambda procs, res, cfg: politique.executer(procs, res)),
    "RR": ("RoundRobin", lambda procs, res, cfg: politique.executer(procs, res, cfg["quantum"])),
    # "EDF": à implémenter plus tard
}

DOSSIER = Path(__file__).resolve().parent
CONFIG_PAR_DEFAUT = DOSSIER / "../Inputs/test2/"
DOSSIER_OUT = DOSSIER / "../Outputs/test2/"

chemin_in = CONFIG_PAR_DEFAUT
chemin_out = DOSSIER_OUT

chemin_in = Path(chemin_in)
chemin_out = Path(chemin_out)

if not chemin_in.exists():
    raise FileNotFoundError(f"Fichier de configuration introuvable : {chemin_in}")
elif not chemin_out.exists():
    raise FileNotFoundError(f"Dossier de sortie introuvable : {chemin_out}")

def chemin_sortie(config: dict, cle: str, defaut: str):
    return chemin_out / config.get(cle, defaut)


def main():
    config = reader.lire_config(chemin_in, "config.txt")
    processus = reader.lire_processus(chemin_in / config["fichier_processus"])
    ressources = reader.lire_ressources(chemin_in / config["fichier_ressources"])

    toutes_metriques = []
    for nom in config["algorithmes"]:
        if nom not in ALGORITHMES:
            print(f"[ignoré] {nom} : algorithme non implémenté")
            continue

        suffixe, executer = ALGORITHMES[nom]
        resultat = executer(processus, ressources, config)
        m = metriques.calculer(resultat, ressources)
        toutes_metriques.append(m)

        writer.ecrire_global(
            chemin_sortie(config, f"fichier_global_{suffixe}", f"resultats_{suffixe}_global.csv"), resultat)
        writer.ecrire_allocations(
            chemin_sortie(config, f"fichier_allocations_{suffixe}", f"resultats_{suffixe}_allocations.csv"), resultat)

        print(f"\n===== {nom} =====")
        affichage.afficher_gantt(resultat)
        affichage.afficher_resultats(resultat)
        affichage.afficher_metriques(m)

    writer.ecrire_metriques(
        chemin_sortie(config, "fichier_metriques", "metriques.csv"), toutes_metriques)


if __name__ == "__main__":
    try:
        main()
    except (FileNotFoundError, ValueError) as e:
        sys.exit(f"Erreur : {e}")