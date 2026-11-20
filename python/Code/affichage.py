"""Affichage console."""
from models import Resultat


def afficher_gantt(r: Resultat) -> None:
    largeur = max(2, max(len(p.id) for p in r.processus), len(str(r.duree_totale)))
    print(f"{'t':<6}" + " ".join(f"{t:>{largeur}}" for t in range(r.duree_totale)))
    for nom, ligne in r.gantt.items():
        print(f"{nom:<6}" + " ".join(f"{(x or '-'):>{largeur}}" for x in ligne))


def afficher_resultats(r: Resultat) -> None:
    print(f"\n{'ID':<6}{'Début':>6}{'Fin':>5}{'Attente':>9}{'Rotation':>10}{'Butoir':>8}  Respecté")
    for p in r.processus:
        print(f"{p.id:<6}{p.debut:>6}{p.fin:>5}{p.attente:>9}{p.rotation:>10}{p.butoir:>8}  "
              f"{'oui' if p.butoir_respecte else 'NON'}")


def afficher_metriques(m: dict) -> None:
    print()
    for cle, valeur in m.items():
        if cle != "algorithme":
            print(f"  {cle:<22}: {valeur}")