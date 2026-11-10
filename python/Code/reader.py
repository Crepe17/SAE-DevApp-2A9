import csv
import json
import os


def lire_config():
  fd = os.open("python/Inputs/test1/config.txt", os.O_RDONLY)
  config = {}
  buffer = ""
  while True:
    octets = os.read(fd, 1024)
    if not octets:
      break
    buffer += octets.decode("utf-8")
    while "\n" in buffer:
      ligne, buffer = buffer.split("\n", 1)
      ligne = ligne.strip()

      if ligne and not ligne.startswith("#"):
        if "=" in ligne:
          cle, valeur = ligne.split("=", 1)
          cle = cle.strip()
          valeur = valeur.strip()
          config[cle] = valeur
  os.close(fd)
  print("config : ", config)


def lire_ressource():
  fd = os.open("python/Inputs/test1/ressources.json", os.O_RDONLY)
  buffer = ""
  while True:
    octets = os.read(fd, 1024)
    if not octets:
      break
    buffer += octets.decode("utf-8")
  os.close(fd)
  dictonaire_res = json.loads(buffer)
  return dictonaire_res


def lire_processus():
  fd = os.open("python/Inputs/test1/processus.csv", os.O_RDONLY)
  buffer = ""
  processus = []
  entete_ignoree = False
  while True:
    octets = os.read(fd, 1024)
    if not octets:
      break
    buffer += octets.decode("utf-8")
    while "\n" in buffer:
      ligne, buffer = buffer.split("\n", 1)
      ligne = ligne.strip()
      if not ligne:
        continue
      if not entete_ignoree:
        entete_ignoree = True
        continue

      if "," in ligne:
        elements = [elem.strip() for elem in ligne.split(",")]

        p = {
            "id": elements[0],
            "date_soumission": int(elements[1]),
            "duree_execution": int(elements[2]),
            "ram_necessaire": int(elements[3]),
            "priorite": int(elements[4]),
            "date_butoir": int(elements[5]),
        }
        processus.append(p)

  os.close(fd)
  print("Processus chargés :", processus)
  return processus


def lire_energie():
  fd = os.open("python/Inputs/test1/energie.csv", os.O_RDONLY)
  buffer = ""
  energie_list = []
  entete_ignoree = False
  while True:
    octets = os.read(fd, 1024)
    if not octets:
      break
    buffer += octets.decode("utf-8")
    while "\n" in buffer:
      ligne, buffer = buffer.split("\n", 1)
      ligne = ligne.strip()
      if not ligne:
        continue
      if not entete_ignoree:
        entete_ignoree = True
        continue

      if "," in ligne:
        elements = [elem.strip() for elem in ligne.split(",")]

        e = {
            "instant": int(elements[0]),
            "energie_disponible": int(
                elements[1]
            ), 
        }
        energie_list.append(e)

  os.close(fd)
  return energie_list