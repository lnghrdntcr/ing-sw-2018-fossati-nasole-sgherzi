# Progetto di Ingegneria del Software - _Sagrada_

## Partecipanti 
* Francesco Sgherzi 
* Angelo Nasole
* Nicola Fossati


## Caratteristiche file JSON per le carte schema
Il file JSON deve essere un array di oggetti. Ogni oggetto deve avere due elementi di tipo SchemaCardFace: uno associato
 alla stringa "front", l'altro alla stringa back:
 ``` json
 [
    {
        "front":{schmaCardFace1},
        "back":{schemaCardFace2}
    },
    {
        "front":{schmaCardFace3},
        "back":{schemaCardFace4}
    },
    ...
 ]
 ``` 
 
 Ogni SchemaCardFace deve essere formattato nel seguente modo:
  ``` json
  {
      "diff": 5,
      "name": "nome di esempio",
      "restrictions":[
                        ["2",      "",  "BLUE", "4", ""],
                        ["",       "",  "",     "",  ""],
                        ["",       "6", "RED",  "",  ""],
                        ["PURPLE", "",  "BLUE", "", "4"]
                     ]
  }
  ```
  
  Le restrizioni valide sono:
  * "" = nessuna restrizione
  * "1" = restrizione numerica, da 1 a 6
  * "BLUE" = restrizione per colore, come codificate in Enum GameColor

## JAR
[CLIENT](deliveries/final/sagrada-client.jar)


[SERVER](deliveries/final/sagrada-server.jar)

## Sonarqube

![Overall results](deliveries/final/SonarQube%20Final%20-%20Global.png)
![Testing results](deliveries/final/SonarQube%20Final%20-%20Testing.png)

## UML

[Final UML Model + Controller + Network](/deliveries/final/FinalUmlModel%2BController%2BNetwork.pdf)


[View UML](deliveries/final/ViewUML.pdf)

## Funzionalità implementate
* Regole Complete

* Comunicazione 
    * Socket con oggetti serializzati
    * RMI
    * Socket attraverso stringhe (JSON)
    
* Interfaccia utente
    * GUI
    * CLI

* Funzionalità avanzate
    * Carte Schema dinamiche
    * Persistenza