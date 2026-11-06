# Projet E-Commerce : Imaginaire (Framework Scrum / Agile)

Plateforme d'e-commerce dédiée à la vente au détail de produits dérivés officiels du monde des jeux vidéo et du rétrogaming.

## Présentation du Projet

Le projet Imaginaire a pour objectif de développer une plateforme web d'e-commerce sur mesure répondant aux besoins de l'entreprise située à Toulouse. Le site permet la consultation du catalogue, la gestion des stocks en temps réel, l'achat sécurisé multi-paiement, la gestion des comptes clients, ainsi que l'administration complète des produits et des commandes.

Ce projet est mené en mode Agile en utilisant le framework Scrum sous GitHub.

## Composition de l'Équipe (MOE)

* Scrum Master : Morado Antoine
* Product Owner : Mdéré Khaled
* Équipe de Développeurs :
  * Morado Antoine [@Crepe17](https://github.com/Crepe17)
  * Mdéré Khaled [@Negrus976](https://github.com/Negrus976)
  * Pierrot Gabriel [@gabriel-pierrot](https://github.com/gabriel-pierrot)
  * Moumtaz Wail [@wawa3133](https://github.com/wawa3133)

## Stack Technique Imposée

* Serveur Web : Apache
* Langages : PHP, HTML5, CSS3 (JavaScript facultatif)
* Système de Gestion de Base de Données (SGBD) : MySQL
* Gestion de Versioning & Projets : Git, GitHub (Issues, Projects, Milestones)
* Design : Responsive (Adapté Ordinateurs, Tablettes, Smartphones)

## Arborescence du Dépôt

```text
.
├── docs/                   # Documentations techniques, fonctionnelles & SEP
├── src/                    # Code source du site web (Front-Office & Back-Office)
│   ├── assets/             # Images, styles CSS et médias
│   ├── config/             # Fichiers de configuration (connexion BDD)
│   ├── includes/           # Composants et fonctions réutilisables
│   └── admin/              # Console d'administration sur mesure
├── sql/                    # Scripts SQL (MCD, Schéma relationnel, Triggers, Procédures)
├── iot/                    # Scripts et documentations spécifiques à l'IoT
├── tests/                  # Scripts de jeux de données (Faker PHP) & Cahier de recette
└── README.md               # Présentation du projet
```

## Organisation des Sprints (Agile / Scrum)

Le projet est découpé en sprints de 2 semaines gérés via l'onglet Projects (Tableau Kanban) et les Milestones de GitHub.

### Structure du SprintBoard :

1. US du sprint : User Stories retenues pour le sprint en cours.
2. À faire : Tâches techniques (Issues) découpées, d'une durée maximale d'un jour.
3. En cours : Tâches activement prises en charge par un développeur.
4. Terminée : Tâches développées, testées et validées.

## Principales User Stories (Backlog Produit)

* [US] Moteur de Recherche & Filtrage : Filtrer les produits par licence, marque, intervalle de prix et avis.
* [US] Gestion Stricte des Ruptures de Stock : Afficher l'état des stocks et désactiver le bouton d'achat en cas d'épuisement.
* [US] Espace Client & Sécurité : Créer un compte, gérer son carnet d'adresses et consulter l'historique des commandes.
* [US] Panier & Paiement : Sélectionner le mode de livraison et effectuer un paiement sécurisé (CB, PayPal, Virement).
* [US] Avis Clients Vérifiés : Déposer une note (sur 5) et un commentaire après réception de la commande.
* [US] Console d'Administration : Espace sécurisé pour la gestion du contenu, des stocks et des promotions.

## Installation & Lancement en local

1. Cloner le dépôt :
   ```bash
   git clone https://github.com/[Nom-Organisation-ou-Utilisateur]/[Nom-Du-Depot].git
   ```

2. Configurer le serveur local :
   Placer le répertoire `src/` dans le dossier web du serveur local (ex: `htdocs` sur XAMPP/WAMP).

3. Importer la base de données :
   Exécuter les scripts situés dans le dossier `sql/` dans l'instance MySQL.
   
