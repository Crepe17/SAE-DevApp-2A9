from dataclasses import dataclass, field


@dataclass
class Processus:
    id: str
    arrivee: int
    duree: int
    ram: int
    priorite: int
    butoir: int
    restant: int = -1            
    debut: int | None = None     
    fin: int | None = None       

    def __post_init__(self):
        if self.restant < 0:
            self.restant = self.duree

    @property
    def rotation(self) -> int:
        return self.fin - self.arrivee

    @property
    def attente(self) -> int:
        """Temps total passé à attendre (correct aussi en préemptif)."""
        return self.rotation - self.duree

    @property
    def reponse(self) -> int:
        return self.debut - self.arrivee

    @property
    def butoir_respecte(self) -> bool:
        return self.fin <= self.butoir


@dataclass
class Ressources:
    ram_totale: int
    energie_par_proc: int
    noms_processeurs: list[str]

    @property
    def nb_processeurs(self) -> int:
        return len(self.noms_processeurs)


@dataclass
class Allocation:
    """Un intervalle [debut, fin[ pendant lequel un processus occupe un CPU."""
    cpu: str
    id_processus: str
    debut: int
    fin: int
    ram: int


@dataclass
class Resultat:
    algorithme: str
    processus: list[Processus]
    gantt: dict[str, list[str | None]]
    allocations: list[Allocation] = field(default_factory=list)
    duree_totale: int = 0