package server;

import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import server.model.DbConnUtil;
import server.model.HashUtil;
import server.model.ServerWork;
import sun.awt.windows.ThemeReader;
import utils.RsaGenerator;
import utils.RsaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.*;
import java.util.Base64;
import java.util.Scanner;

public class ServerStart {
    private String prompt="Server#> ";
    public static void main(String[] args) throws NoSuchAlgorithmException {

        PrivateKey privateKey;
        PublicKey publicKey;
        DbConnUtil dbConnUtil;
        RsaGenerator rsaGenerator=new RsaGenerator();
        privateKey=rsaGenerator.getPrivateKey();
        publicKey=rsaGenerator.getPublicKey();


        //inizializzo la classe del db e setto la path
        dbConnUtil=new DbConnUtil("jdbc:h2:c:\\tmp\\test");
        /*
        * Inserisco degli utenti esempio
        * */
        //Method 1
        System.out.println("INSERIMENTO USERS DI PROVA");
        dbConnUtil.insertUser("WalterWhite",HashUtil.hashWsalt("Ciao123"));
        dbConnUtil.insertUser("ivan",HashUtil.hashWsalt("Password"));
        //Method 2
        String[] passSalt=HashUtil.hashWsalt("Password");
        dbConnUtil.insertUser("alexa",passSalt[0],passSalt[1]);

        /*
         * Start Server in un thread a parte
         * */

        ServerWork serverWork=new ServerWork(dbConnUtil,publicKey,privateKey);
        Thread serverThread=new Thread(serverWork);
        serverThread.start();
        /*
         * Gestione interazione con il server
         * da parte di un tecnico
         * */
        String cmd="";
        System.out.println("Menù:\n1)Inserisci nuovo utente\n2)Mostra Tabella utenti\n3)Elimina dati dalla tabella utenti\nm)Mostra menù\nquit)Per terminare\n");
        while (!cmd.equals("quit")){
            System.out.print("Server#> ");
            Scanner reader=new Scanner(System.in);
            cmd=reader.nextLine();
            switch (cmd){
                case "1":{
                    System.out.println("Digita <nome> <password> separati da uno spazio");
                    String[] user = reader.nextLine().split(" ",2);
                    try {
                        dbConnUtil.insertUser(user[0],HashUtil.hashWsalt(user[1]));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "2":{
                    System.out.println("Tabella Utenti");
                    ResultSet rs=null;
                    try {
                        Connection connection= DriverManager.getConnection(dbConnUtil.getPathDb());
                        Statement st=connection.createStatement();
                        rs=st.executeQuery("SELECT * FROM USER");
                    }catch (JdbcSQLSyntaxErrorException e){
                        System.out.println("Errore tabella non trovata");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Errore con la query");
                    }
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("#<|>ID<|>Nome<|>Password<|>Salt\n");
                    try {
                        if(!rs.equals(null)){
                            int n_Colums=rs.getMetaData().getColumnCount();
                            while(rs.next()){
                                stringBuilder.append(rs.getRow());
                                for(int i=1;i<=n_Colums;i++){
                                    stringBuilder.append("<|>").append(rs.getString(i));
                                }
                                stringBuilder.append("\n");
                            }
                        }else{
                            System.out.println("Problema con l'estrazione dei dati!");
                        }
                    } catch (NullPointerException e){
                        break;
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(stringBuilder.toString());
                    break;
                }
                case "3": {
                    System.out.println("Eliminazione tabella");
                    dbConnUtil.deleteDataTable("USER");
                    break;
                }
                case "m":{
                    System.out.println("Menù:\n1)Inserisci nuovo utente\n2)Mostra Tabella utenti\n3)Elimina dati dalla tabella utenti\nm)Mostra menù\nquit)Per terminare\n");
                    break;
                }
                default:
                    break;
            }
        }
    }

}
