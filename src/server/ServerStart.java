package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerStart {


    public static void main(String[] args) {
        int port =4000;
        ServerSocket serverSocket;
        Socket socket;
        String sqlCreate="CREATE TABLE IF NOT EXISTS USER (ID INTEGER NOT NULL AUTO_INCREMENT,NAME VARCHAR(25) NOT NULL,PASSWORD VARCHAR(64) NOT NULL,SALT VARCHAR(8) NOT NULL,CONSTRAINT \"id\" PRIMARY KEY (ID),CONSTRAINT USER_UN UNIQUE (NAME))";
        DbUtil dbUtil=new DbUtil();
        //setto la path del db
        dbUtil.setPathDb("jdbc:h2:c:\\tmp\\test");
        //eseguo la query che crea la tabella
        dbUtil.executeUpdate(sqlCreate);
        HashUtil hashUtil=new HashUtil();
        /*
        * Inserisco degli utenti esempio
        * */
        String[] passSalt=hashUtil.hash("Ciao123");
        dbUtil.insertUser("WalterWhite",passSalt[0],passSalt[1]);
        passSalt=hashUtil.hash("Password");
        dbUtil.insertUser("ivan",passSalt[0],passSalt[1]);
        passSalt=hashUtil.hash("Password");
        dbUtil.insertUser("alexa",passSalt[0],passSalt[1]);
        /*String sqlDrop="DROP TABLE user IF EXISTS";
        String sqlCreate="CREATE TABLE IF NOT EXISTS PUBLIC.\"user\" (ID INTEGER NOT NULL AUTO_INCREMENT,NAME VARCHAR(25) NOT NULL,PASSWORD VARCHAR(64) NOT NULL,SALT VARCHAR(8) NOT NULL,CONSTRAINT \"id\" PRIMARY KEY (ID))";
        try(Connection conn = DriverManager.getConnection("jdbc:h2:c:\\tmp\\test")){
            System.out.println("Connesso al database, Creazione Statement...");
            try(Statement stat=conn.createStatement()){
                stat.executeUpdate(sqlCreate);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }*/
    }
}
