import csv

from models import Resultat


def _ecrire(chemin, entetes: list[str], lignes: list[list]) -> None:
    with open(chemin, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(entetes)
        w.writerows(lignes)


def ecrire_global(chemin, resultat: Resultat) -> None:
    """Une ligne par processus."""
    _ecrire(chemin,
            ["id_processus", "date_soumission", "date_debut", "date_fin", "attente",
             "rotation", "date_butoir", "butoir_respecte"],
            [[p.id, p.arrivee, p.debut, p.fin, p.attente, p.rotation, p.butoir,
              "oui" if p.butoir_respecte else "non"] for p in resultat.processus])


def ecrire_allocations(chemin, resultat: Resultat) -> None:
    """Une ligne par intervalle d'occupation d'un CPU."""
    _ecrire(chemin,
            ["processeur", "id_processus", "debut", "fin", "ram_allouee"],
            [[a.cpu, a.id_processus, a.debut, a.fin, a.ram] for a in resultat.allocations])


def ecrire_metriques(chemin, metriques: list[dict]) -> None:
    """Une ligne par algorithme exécuté."""
    if not metriques:
        return
    entetes = list(metriques[0])
    _ecrire(chemin, entetes, [[m[k] for k in entetes] for m in metriques])