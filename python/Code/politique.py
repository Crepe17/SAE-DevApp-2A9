from models import Processus, Ressources, Resultat
from scheduler import simuler

def executer_fifo(processus: list[Processus], ressources: Ressources) -> Resultat:
  """Ordonnancement FIFO (premier arrivé, premier servi), non préemptif."""
    return simuler("FIFO", processus, ressources, quantum=None)

def executer_rr(processus: list[Processus], ressources: Ressources, quantum: int) -> Resultat:
  """Ordonnancement Round Robin (tourniquet) avec quantum."""
    return simuler("RR", processus, ressources, quantum=quantum)
