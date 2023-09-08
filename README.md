# Frontend-Android

Le code de l'application Android GroceryPal permet de créer automatiquement des listes de courses en se basant sur les ingrédients d'une recette. 
De plus, elle offre la possibilité de gérer le contenu en mode hors ligne ou de le synchroniser avec une base de données en ligne.

## Instruction pour faire tourner le projet Frontend en local :

### Avec l'APK

Vous pouvez télécharger et installer l'APK se trouvant dans notre repo github, l'APK utilisable est téléchargeable dans la dernière release.

### Avec Android Studio

- Si besoin, télécharger et installer Android Studio

- Cloner le répertoire github du projet
Ou lancer android studio, aller dans File -> New -> Project from Version Control 
  pour cloner à travers Android Studio


- Si besoin, créer votre configuration (Run/Debug) : Dans l'onget Add New Configuration (petit bouton + en haut à gauche), choisissez
  Android App, sous module, choisissez PDG_GroceryPal.app.main.

### Android API version
Pour que l'application fonctionne, l'API level doit être entre 28 et 34 (28 et 34 compris)

### Lancer l'application
Pour faire fonctionner l'application avec les back-end locale, se référer au guide d'utilisation back-end pour l'installation
Ensuite, changer la config locale se trouvant à package ch.heigvd.pdg_grocerypal.config dans le projet avec l'url local (voir commentaires).

Il vous suffit ensuite de lancer l'application, créer votre nouveau compte ou utiliser votre compte si vous en avez un et tester les fonctionnalités !
