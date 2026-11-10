import json
import csv
import os

def lire_config():
    fd = os.open("python/Inputs/test1/config.txt", os.O_RDONLY)
    config = {}
    buffer = ""
    while True:
        octets = os.read(fd,1024)
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
    print ("config : ", config)


def lire_ressource():
    fd = os.open("python/Inputs/test1/ressources.json", os.O_RDONLY)
    buffer =""
    while True:
        octets = os.read(fd,1024)
        if not octets:
            break
        buffer+=octets.decode("utf-8")
    os.close(fd)
    dictonaire_res = json.loads(buffer)
    return dictonaire_res

