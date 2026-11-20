from models import Processus, Ressources, Resultat
from scheduler import simuler


def executer(processus: list[Processus], ressources: Ressources, quantum: int | None = None) -> Resultat:
    """Execute un ordonnancement Round Robin (tourniquet) ou FIFO en fonction du paramètre quantum.
    S'il est présent c'est RR sinon FIFO"""
    
    if quantum is None :
        return simuler("FIFO", processus, ressources, quantum=None)
    return simuler("RR", processus, ressources, quantum=quantum)
