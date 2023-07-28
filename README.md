# ingsw
CheckPoint:
  -  [Casi d'uso](https://github.com/speacock17/ingsw/tree/main/USE%20CASE)
  -  [Tabelle Cockburn](https://github.com/speacock17/ingsw/tree/main/CockBurn)
  -  [Mockup Figma](https://github.com/speacock17/ingsw/blob/main/Figma%20Progetto.zip)
  -  [Class Diagram di ANALISI](https://github.com/speacock17/ingsw/tree/main/ClassDiagramAnalisi)
  -  [Sequence Diagram di ANALISI](https://github.com/speacock17/ingsw/tree/main/SEQUENCE_DIAGRAM_ANALISI)
  -  [Statechart di ANALISI](https://github.com/speacock17/ingsw/tree/main/STATECHART)
  -  [Database](https://github.com/speacock17/ingsw/blob/main/IngswDB.txt)
  -  [Codice Server Java](https://github.com/speacock17/ingsw/blob/main/server_280723.zip)
  -  [File JAR Server](https://github.com/speacock17/ingsw/blob/main/server_280723.jar)
  -  [Richieste al server](https://github.com/speacock17/ingsw/tree/main/richieste_server)
  -  [Documentazione Testing Server](https://github.com/speacock17/ingsw/blob/main/Documentazione_strategie_test.txt)

Ultima versione del server:   server_280723
  - test jUnit di 4 metodi
  - pattern DAO per la persistenza dei dati
  - openFoodData service implementato
  - autenticazione implementata

Come si avvia il server:
  - bisogna accedere ad AWS con email e password
  - bisogna accedere alla console EC2 e selezionare l'unica istanza disponibile
  - tra le operazione possibili su 'stato dell'istanza' bisogna premere "Avvia"
  - sempre selezionando l'istanza, compare in basso l'indirizzo DNS ipv4 pubblico da copiare per l'operazione successiva
  - (qui si trova anche l'indirizzo ipv4 pubblico necessario per le richieste http al server)
  - bisogna accedere al proprio terminale e spostarsi nella directory dove si trova la chiave .pem
  - a questo punto scrivere il comando "ssh -i nomeChiave.pem Ubuntu@DNSipv4pubblico"
  - una volta entrati nella shell della macchina virtuale AWS bisogna andare nella cartella '/home/ubuntu/ingsw'
  - qui eseguire il comando 'java -jar server_280723.jar'

Come funziona il server:
  - bisogna inviare una richiesta "http://indirizzoIPv4pubblico:8080/auth" con due parametri nel body (in formato JSON): "login" e "password"
  - si riceve un access token che deve essere inserito nell'header di ogni richiesta successiva
  - per inserire il token si mette nell'Header il campo "Authorization" con valore "Bearer valoreToken"


