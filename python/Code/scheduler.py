from copy import deepcopy
from dataclasses import dataclass, field

from models import Allocation, Processus, Ressources, Resultat


@dataclass
class _Cpu:
    nom: str
    courant: Processus | None = None
    tranche: int = 0
    gantt: list = field(default_factory=list)


def _valider(processus: list[Processus], ressources: Ressources) -> None:
    """Vérifie que les processus sont valide en vérifiant s'ils respectent
    les contraintes de mémoire notamment.

    Args:
        processus (list[Processus]): liste des processus à vérifier
        ressources (Ressources): ressources disponible

    Raises:
        ValueError: un processus spécifique demande beaucoup trop de ram
    """    
    for p in processus:
        if p.ram > ressources.ram_totale:
            raise ValueError(
                f"{p.id} demande {p.ram} Mo mais la RAM totale est de {ressources.ram_totale} Mo : "
                "il ne pourra jamais s'exécuter")


def _prochain(file: list[Processus], ram_libre: int, strict: bool) -> Processus | None:
    """Renvoie le prochain processus à passer dans une liste de processus

    Args:
        file (list[Processus]): liste des processus dans la file d'attente
        ram_libre (int): quantite de RAM disponible
        strict (bool): exprime s'il on as besoin d'un processus suivant

    Returns:
        Processus | None: renvoie les processus pouvant être ajouter à la file
    """
    for i, p in enumerate(file):
        if p.debut is not None or p.ram <= ram_libre:
            return file.pop(i)
        if strict:
            return None
    return None


def _segments(cpu: _Cpu, ram_par_id: dict[str, int]) -> list[Allocation]:
    """Compresse le Gantt d'un CPU en intervalles consécutifs."""
    segs, debut = [], 0
    for t in range(1, len(cpu.gantt) + 1):
        if t == len(cpu.gantt) or cpu.gantt[t] != cpu.gantt[debut]:
            id_ = cpu.gantt[debut]
            if id_ is not None:
                segs.append(Allocation(cpu.nom, id_, debut, t, ram_par_id[id_]))
            debut = t
    return segs


def simuler(nom: str, processus: list[Processus], ressources: Ressources,
            quantum: int | None = None) -> Resultat:
    """Prend en paramètre une politique ainsi que les processus, ressources
    et quantum de temps pour réaliser une simulation d'une politique.

    Args:
        nom (str): nom de la politique à simuler
        processus (list[Processus]): liste des processus qu'on vas simuler
        ressources (Ressources): ressources alloué à la simulation
        quantum (int | None, optional): quantum de temps. Defaults to None.

    Returns:
        Resultat: résultat de la simulation
    """    
    _valider(processus, ressources)
    procs = deepcopy(processus)
    a_venir = sorted(procs, key=lambda p: p.arrivee)
    file: list[Processus] = []
    retour: list[Processus] = []
    cpus = [_Cpu(n) for n in ressources.noms_processeurs]
    ram_libre = ressources.ram_totale
    temps = 0

    while a_venir or file or retour or any(c.courant for c in cpus):
        # 1) Arrivées d'abord, puis processus préemptés (convention classique du RR)
        while a_venir and a_venir[0].arrivee <= temps:
            file.append(a_venir.pop(0))
        file.extend(retour)
        retour.clear()

        # 2) Affectation aux CPU libres
        for cpu in cpus:
            if cpu.courant is None:
                p = _prochain(file, ram_libre, strict=quantum is None)
                if p is not None:
                    if p.debut is None:
                        p.debut = temps
                        ram_libre -= p.ram
                    cpu.courant, cpu.tranche = p, 0

        # 3) Exécution d'une unité de temps
        for cpu in cpus:
            p = cpu.courant
            if p is None:
                cpu.gantt.append(None)
                continue
            cpu.gantt.append(p.id)
            p.restant -= 1
            cpu.tranche += 1
            if p.restant == 0:
                p.fin = temps + 1
                ram_libre += p.ram
                cpu.courant = None
            elif quantum is not None and cpu.tranche >= quantum:
                retour.append(p)
                cpu.courant = None

        temps += 1

    ram_par_id = {p.id: p.ram for p in procs}
    allocations = [a for c in cpus for a in _segments(c, ram_par_id)]
    allocations.sort(key=lambda a: (a.debut, a.cpu))
    return Resultat(algorithme=nom, processus=procs,
                    gantt={c.nom: c.gantt for c in cpus},
                    allocations=allocations, duree_totale=temps)
