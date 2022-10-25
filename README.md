# Spring Security - Exercices

## Remarques préalables

### Structure de l'application

Le projet de demo de Spring Security est une application web composée de 3 pages :

* **home.jsp** : Page d'accueil de notre application
* **secured-user.jsp** : Page réservée aux utilisateurs authentifiés
* **secured-admin.jsp** : Page réservée aux administrateurs authentifiés

Chacune de ces pages est composée de deux éléments communs :

* **header.jsp** : correspond à l'en-tête affichant les informations de l'utilisateur identifié
* **footer.jsp** : correspond au bas de page affichant les liens vers les différentes pages de l'application

### Démarrage de l'application

Le projet ne nécessite pas de serveur web "externe" pour être démarré et testé.  
Maven a été configuré pour pouvoir démarrer un serveur web embarqué : Jetty

Exécuter la commande Maven suivante pour démarrer le serveur et déployer l'application :

> mvn jetty:run-war

Une fois démarré et déployé, l'application est disponible à l'adresse suivante :

> http://localhost:8080/

## Exercices

### Exercice 1 - Activation de Spring Security

L'application n'est pour le moment pas protégée.   
N'importe qui peut accéder à n'importe quelle page, ce qui pose des problèmes de sécurités et confidentialités.

#### 1. Activer Spring Security

Ajouter la **configuration nécessaire** dans le fichier **web.xml** pour que Spring Security protège notre application web.

#### 2. Test

Pour vérifier la bonne configuration, démarrer le serveur web Jetty et ouvrir l'application. (Voir _Remarques Préalables_)


L'en-tête sur chaque page devrait afficher l'utilisateur : **anonymousUser**

### Exercice 2 - Authentification utilisateur

L'authentification anonymous est un bon début pour commencer avec Spring Security et vérifier sa bonne "activation".  
Mais pour protéger notre application, ce n'est pas suffisant.

Nous devons ajouter une authentification username/password pour nos utilisateurs.

#### 1. Authentification Username/Password

Dans le fichier **security-context.xml**, du répertoire _src/main/resources/spring_ :

* Ajouter l'authentification par nom d'utilisateur et mot de passe, via un formulaire
* Ajouter explicitement la fonctionnalité de "logout". Ceci permet à nos utilisateurs de fermer leur session.
  * Faites-en sorte que lorsque le logout réussit, l'utilisateur soit redirigé vers l'url "/"
* Supprimer le bean avec l'id "forbiddenEntryPoint", qui n'est plus nécessaire
* Supprimer l'attribut "entry-point-ref" de l'élément < sec:http />

#### 2. Définition des utilisateurs

Dans le fichier **security-context.xml**, du répertoire _src/main/resources/spring_ :

Configurer un authentication manager et authentication service pour définir les utilisateurs.  

Les utilisateurs suivants doivent être définis :
* User
    * username :  anthony_user
    * password :  user123
    * roles : ROLE_USER
* Administrateur
    * username : anthony_admin
    * password : admin123
    * roles : ROLE_USER, ROLE_ADMIN

(Pour l'encodage du mot de passe, utiliser l'algorithme de hash **{noop}** pour cet exercice)

#### 3. Test

Pour vérifier la bonne configuration, démarrer le serveur web Jetty et ouvrir l'application. (Voir _Remarques Préalables_)

L'en-tête sur chaque page devrait afficher l'utilisateur : **anonymousUser**

1. Cliquer sur le lien **Login** en bas de page  
    => Le formulaire de login devrait s'afficher
2. Entrer le username **anthony_user** et le mot de passe **user123**  
    => L'authentification doit réussir et l'en-tête affiche à présent l'utilisateur : **anthony_user**
3. Cliquer sur le lien Logout pour terminer la session de l'utilisateur _anthony_user_
4. Répéter les opérations 1 à 3 mais avec l'utilisateur **anthony_admin** et le mot de passer **admin123**  
    

### Exercice 3 - Autorisation utilisateur

Les utilisateurs peuvent maintenant s'authentifier dans notre application.  
Cependant, les pages restent consultables par n'importe qui : anonymous, user et admin.

L'exercice consiste à ajouter le contrôle d'autorisation sur les pages suivantes :
* /secured-user.jsp : autorisé uniquement aux utilisateurs ayant le rôle **ROLE_USER**
* /secured-admin.jsp : autorisé uniquement aux utilisateurs ayant le rôle **ROLE_ADMIN**


#### 1. Contrôle des autorisations

Dans le fichier **security-context.xml**, du répertoire _src/main/resources/spring_ :

1. Ajouter la configuration nécessaire pour contrôler les autorisations de la page **/secure-user.jsp**
2. Ajouter la configuration nécessaire pour contrôler les autorisations de la page **/secure-admin.jsp**

#### 2. Test

Pour vérifier la bonne configuration, démarrer le serveur web Jetty et ouvrir l'application. (Voir _Remarques Préalables_)

1. Accès anonymous

En tant que **anonymousUser**, les liens suivants doivent donner une erreur "Access Denied" :

* Secured for authenticated user
* Secured for authenticated ADMIN

2. Accès utilisateur: anthony_user

* S'authentifier, via le lien "Login", en tant qu'utilisateur : anthony_user
* La page avec le lien "Secured for authenticated user" doit s'afficher sans erreur
* La page avec le lien "Secured for authenticated ADMIN" doit donner une erreur "Access Denied"

3. Accès utilisateur : anthony_admin

* Fermer la session de l'utilisateur anthony_user en cliquant sur le lien "Logout"
* S'authentifier, via le lien "Login", en tant qu'utilisateur : anthony_admin
* La page avec le lien "Secured for authenticated user" doit s'afficher sans erreur
* La page avec le lien "Secured for authenticated ADMIN" doit s'afficher sans erreur

### Exercice 4 - Test avec MockUser

#### Introduction

Le projet contient la classe **HelloServiceImpl** avec deux méthodes **sayHello** et **sayHelloForAdmin**  
Ces méthodes retournent comme résultat un String contenant la valeur : 

> "Hello < username >"

Où < username > est le nom de l'utilisateur actuellement authentifié (dans le Security Context).  
La méthode **sayHelloForAdmin** nécessite que l'utilisateur ait le rôle ROLE_ADMIN pour être autorisé à appeler la méthode.

La classe de test **HelloServiceImplTest** est déjà écrite avec les différents scénarios possibles :
* utilisateur anonyme
* utilisateur user
* utilisateur admin

Mais il manque l'initialisation du Security Context

#### 1. Rôle Admin - sayHelloForAdmin

Dans la classe **HelloWorldServiceImpl**, ajouter l'annotation nécessaire sur la méthode **sayHelloForAdmin**.
L'annotation ne doit autoriser l'accès à cette méthode que pour les utilisateurs ayant le rôle **ROLE_ADMIN**.

#### 2. Activation des method interceptor

Dans le fichier **security-context.xml**, ajouter les configurations nécessaires de Spring Security pour activer les "method interceptor", 
incontournable pour protéger les accès à la classe et méthodes de **HelloServiceImpl**.

#### 3. HelloServiceImplTest - Initialisation du Security Context

Ajouter le code nécessaire dans la classe de test **HelloServiceImplTest** pour initialiser un Security Context.  
Utiliser l'approche par annotation Spring Security Test, au lieu de la création manuelle.

En fonction du nom de la méthode de test, initialiser un Security Context avec un utilisateur : 
* anonyme
* user
* admin

#### 3. Test

Pour tester vos modifications, exécuter tous les tests de la classe **HelloServiceImplTest**.  
N'oubliez pas d'enlever l'annotation @Ignore.
