Questa applicazione è stata sviluppata da Ivan Romeo 5B Informatica come esercitazione per il prof. Impastato
La consegna era:
       /*************************************************************************************\
        Creare un'applicazione di rete per la gestione del login degli utenti.
        Il server dovrà gestire un database relazionale (utilizzare H2 per semplicità)
        contenente l'anagrafica degli utenti e le relative password memorizzate con i metodi
        di crittografia precedentemente visti (hash con salt).
        Il server dovrà inoltre prevedere la possibilità di inserire da linea di comando un
        nuovo utente e memorizzarlo sul database e la possibilità di vedere l'elenco degli
        utenti memorizzati.
        Il client dovrà semplicemente prevedere la connessione al server e l'invio delle
        credenziali di accesso. Il server risponderà con un apposito messaggio qualora le
        credenziali siano corrette o errate.
       \*************************************************************************************/
User preregistrati
 USER1: WalterWhite  Ciao123
 USER2: ivan         Password
 USER3: alexa        Password
è possibile costatare l'utilizzo del salt stampando la tabella,così fatto sarà possibile vedere che le password hashate del 2° e 3° user saranno diverse;

L'applicazione realizzata è divisa in due parti:
    -da una parte server con porta default 4000;
    -da una parte client servita da una Gui realizzata non considerando la qualità grafica ma l'utilità;

 Il server oltre ad essere raggiungibile da socket è possibile interagire da console per l'inserimento di utenti, l'eliminazione dei record o la lettura;
 Nel Client e nel Server sono state gestite le principali o quasi tutte eccezioni per un corretto funzionamento a prova di Bug.

 Il client comunica le credenziali al server una volta criptate con la chiave pubblica del server;

*Una volta avviato il server verrà creato un database nella directory \tmp;


 In oltre il programma è disponibile su GitHub all'indirizzo:
    https://github.com/IvanRomeo-Dev/AppLogin

Buon Test!

IDE utilizzato Intellij