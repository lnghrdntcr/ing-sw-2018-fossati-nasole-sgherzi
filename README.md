# Progetto di Ingegneria del Software spring2018

## Partecipanti 
* Francesco Sgherzi 
* Angelo Nasole
* Nicola Fossati

## TODO
* Aggiugnere un altro layer di comunicazione per eliminare serializable
* finire i test
* aggiungere caricamento schema card da cartella resources (usando getResources)
* Spostare generazione PublicObjective in model
* Spostare setActive /setInactive delle CLI in un'interfaccia
* Eliminare un evento mettendo parametro default (Placeanotherdicewithnumberselection) e guardare anche stati

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
  
  #Tested toolcards:
  * Tampone diamantato
  * Eglomise brush
  * Wheeled Pincer
  * CorkRow