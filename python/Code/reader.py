"""Lecture et validation des fichiers d'entrée (config, processus, ressources)."""
import csv
import json
import re

from models import Processus, Ressources

CLES_OBLIGATOIRES = ("fichier_processus", "fichier_ressources", "algorithmes")
COLONNES_PROCESSUS = ("id_processus", "date_soumission", "duree_execution",
                      "ram_necessaire", "priorite", "date_butoir")


def lire_config(chemin, configuration) -> dict:
    """Prend en paramètre un chemin et un fichier configuration.
    Lit le fichier config et ajoute crée un dictionnaire qui associe (cle = valeur)
    nom = fichier_specifique (les lignes # et vides sont ignorées)."""

    confile = chemin / configuration
    config = {}
    with open(confile, encoding="utf-8") as f:
        for n, ligne in enumerate(f, 1):
            ligne = ligne.strip()
            if not ligne or ligne.startswith("#"):
                continue
            if "=" not in ligne:
                raise ValueError(f"{confile.name}, ligne {n} : format 'cle = valeur' attendu")
            cle, valeur = ligne.split("=", 1)
            config[cle.strip()] = valeur.strip()

    manquantes = [c for c in CLES_OBLIGATOIRES if c not in config]
    if manquantes:
        raise ValueError(f"Clés manquantes dans {confile.name} : {', '.join(manquantes)}")

    config["algorithmes"] = [a.strip().upper() for a in config["algorithmes"].split(",") if a.strip()]
    try:
        config["quantum"] = int(config.get("quantum", 2))
    except ValueError:
        raise ValueError("quantum doit être un entier") from None
    if config["quantum"] <= 0:
        raise ValueError("quantum doit être strictement positif")

    return config


def lire_processus(fichier) -> list[Processus]:
    """Lit un fichier contenant différent processus
    et renvoie une liste de processus (supporte seulement les .csv)"""

    with open(fichier, newline="", encoding="utf-8-sig") as f:
        lecteur = csv.DictReader(f)
        manquantes = set(COLONNES_PROCESSUS) - set(lecteur.fieldnames or [])
        if manquantes:
            raise ValueError(f"{fichier.name} : colonnes manquantes {sorted(manquantes)}")

        liste, vus = [], set()
        for n, l in enumerate(lecteur, 2):
            try:
                p = Processus(
                    id=l["id_processus"].strip(),
                    arrivee=int(l["date_soumission"]),
                    duree=int(l["duree_execution"]),
                    ram=int(l["ram_necessaire"]),
                    priorite=int(l["priorite"]),
                    butoir=int(l["date_butoir"]),
                )
            except (ValueError, TypeError, AttributeError) as e:
                raise ValueError(f"{fichier.name}, ligne {n} : valeur invalide ({e})") from e
            if p.id in vus:
                raise ValueError(f"{fichier.name}, ligne {n} : id '{p.id}' en double")
            if p.duree <= 0 or p.arrivee < 0 or p.ram < 0:
                raise ValueError(f"{fichier.name}, ligne {n} : durée/arrivée/RAM incohérente")
            vus.add(p.id)
            liste.append(p)

    if not liste:
        raise ValueError(f"{fichier.name} : aucun processus")
    return liste


def lire_ressources(fichier) -> Ressources:
    """Prend en parametre un fichier resources en paramètre et renvoie
        les ressources disponibles. Accepte un .json ou un .csv """

    if fichier.suffix.lower() == ".json":
        with open(fichier, encoding="utf-8") as f:
            brut = json.load(f)
    else:
        with open(fichier, newline="", encoding="utf-8-sig") as f:
            brut = next(csv.DictReader(f), None)
        if brut is None:
            raise ValueError(f"{fichier.name} : fichier vide")

    try:
        noms = brut.get("processeur")
        if isinstance(noms, str):
            noms = [n.strip() for n in re.split(r"[;|]", noms) if n.strip()]
        nb = int(brut["nbProcesseurs"]) if "nbProcesseurs" in brut else len(noms or [])
        if not noms:
            noms = [f"CPU{i}" for i in range(1, nb + 1)]
        if len(noms) != nb:
            raise ValueError(f"nbProcesseurs={nb} mais {len(noms)} noms de CPU")
        res = Ressources(ram_totale=int(brut["ramTotale"]),
                         energie_par_proc=int(brut["energieParProc"]),
                         noms_processeurs=noms)
    except KeyError as e:
        raise ValueError(f"{fichier.name} : clé manquante {e}") from e
    if res.nb_processeurs <= 0 or res.ram_totale <= 0:
        raise ValueError(f"{fichier.name} : au moins 1 CPU et de la RAM sont nécessaires")
    return res