
from models import Ressources, Resultat


def calculer(resultat: Resultat, ressources: Ressources) -> dict:
    """Calcul des métriques d'un résultat d'une simulation.
    Et renvoie un dictionnaire comportant ces résultats de métriques.
    Args:
        resultat (Resultat): résultat d'une simulation
        ressources (Ressources): ressources alloué à la simulation

    Returns:
        dict: résultat des différentes métriques
    """    
    procs = resultat.processus
    n = len(procs)
    ticks_actifs = sum(1 for ligne in resultat.gantt.values() for x in ligne if x is not None)
    capacite = ressources.nb_processeurs * resultat.duree_totale

    # Hypothèse : energieParProc est consommée à chaque unité de temps où un CPU travaille
    return {
        "algorithme": resultat.algorithme,
        "duree_totale": resultat.duree_totale,
        "attente_moyenne": round(sum(p.attente for p in procs) / n, 2),
        "rotation_moyenne": round(sum(p.rotation for p in procs) / n, 2),
        "reponse_moyenne": round(sum(p.reponse for p in procs) / n, 2),
        "utilisation_cpu_pct": round(100 * ticks_actifs / capacite, 2) if capacite else 0.0,
        "energie": ticks_actifs * ressources.energie_par_proc,
        "butoirs_respectes": sum(p.butoir_respecte for p in procs),
        "nb_processus": n,
    }