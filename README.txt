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

L'applicazione realizzata è divisa in due parti:
    -da una parte server con porta default 4000;
    -da una parte client servita da una Gui realizzata non considerando la qualità grafica ma l'utilità

 Il server oltre ad essere raggiungibile da socket è possibile interagire da console per l'inserimento di utenti, l'eliminazione dei record o la lettura;
 Nel Client sono state gestite le principali o quasi tutte eccezioni per un corretto funzionamento a prova di Bug.

 In oltre il programma è disponibile su GitHub all'indirizzo:
    https://github.com/IvanRomeo-Dev/AppLogin


IDE utilizzato Intellij