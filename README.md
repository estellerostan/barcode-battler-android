# barcode-battler-android

Créé pour le cours de Master 2 "Dev. d'applications mobile Android".   
Application android sur le même principe que la console vintage “Barcode Battler”.   

Il s’agit donc de concevoir un jeu qui permet :
* Gérer ses créatures et équipements (ajouter / supprimer / ….)
* Ajoute des créatures / équipements / potions  en scannant des codes barres
* Lancer un combat en local en faisant affronter deux créatures sur le même téléphone
* Lancer un combat en réseau 

Contraintes :
* Un code barre scanné donnera toujours le même résultat.
* Le réseau fonctionnera par échange de fichiers XML/ JSON (à définir).
* Les combats seront au tour par tour, à minima, l’attaquant devra tirer un nombre aléatoire entre 0 et sa capacité d’attaque , l’adversaire devra tirer un nombre aléatoire entre 0 et sa capacité de défense, l’ennemi sera blessé (d’attaque - défense) point de vie.

## Importer le projet sur Android Studio
* Dans un terminal :
```
$ cd ~/AndroidStudioProjects
$ git clone https://github.com/estellerostan/barcode-battler-android.git
```
* Ouvrir Android Studio
* File > New > Import Project... > ~/AndroidStudioProjects/barcode-battler-android/app/build.gradle
