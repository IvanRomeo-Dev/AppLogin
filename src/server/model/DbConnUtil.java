package server.model;

import javafx.scene.chart.ScatterChart;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DbConnUtil {

    private String pathDb;
    //Tabella User query per la creazione
    private String sqlCreate="CREATE TABLE IF NOT EXISTS USER (ID INTEGER NOT NULL AUTO_INCREMENT,NAME VARCHAR(25) NOT NULL,PASSWORD VARCHAR(64) NOT NULL,SALT VARCHAR(8) NOT NULL,CONSTRAINT \"id\" PRIMARY KEY (ID),CONSTRAINT USER_UN UNIQUE (NAME))";

    public DbConnUtil(String pathDb){
        this.pathDb=pathDb;

        //eseguo la query che crea la tabella non appena creato l'oggetto controllando se già esiste
        executeUpdate(sqlCreate);
    }


    public void testConnection(String pathDb){
        try(Connection conn=DriverManager.getConnection(pathDb)){
            System.out.println("Connessione Creata correttamente");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void executeUpdate(String sqlUpdate){
        try(Connection conn=DriverManager.getConnection(this.pathDb)) {
            Statement statement=conn.createStatement();
            statement.executeUpdate(sqlUpdate);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean checkuser(String user, String pass) {
        String sqlSelect="SELECT NAME,PASSWORD,SALT FROM USER WHERE NAME='"+user+"';";
        try(Connection conn=DriverManager.getConnection(this.pathDb)) {
            Statement statement=conn.createStatement();
            ResultSet rS=statement.executeQuery(sqlSelect);
            if(rS.next()){
                String hashedpass=rS.getString(2);//Estrazione password hashata dal database
                String salt=rS.getString(3);//Estrazione salt dal database
                String passtemp=pass+salt;
                if(HashUtil.hashWoutsalt(passtemp).equals(hashedpass)){
                    return true;
                }else
                    return false;
            }else{
                return false;
            }
        }catch (SQLException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return false;
    }
    public int insertUser(String name,String password,String salt){
        if(!existUser(name)){
            String sqlInsertUser="INSERT INTO USER (name,password,salt) VALUES (?,?,?)";
            try(Connection conn=DriverManager.getConnection(this.pathDb)) {
                PreparedStatement preparedStat= conn.prepareCall(sqlInsertUser);
                preparedStat.setString(1,name);
                preparedStat.setString(2,password);
                preparedStat.setString(3,salt);
                preparedStat.executeUpdate();
                return 1;
            }catch (SQLException e){
                e.printStackTrace();
                return 0;
            }
        }else{
            System.out.println("\""+name+"\" Utente Già iscritto");
            return -1;
        }
    }
    public int insertUser(String name,String[] passSalt){
        if(!existUser(name)){
            String sqlInsertUser="INSERT INTO USER (name,password,salt) VALUES (?,?,?)";
            try(Connection conn=DriverManager.getConnection(this.pathDb)) {
                PreparedStatement preparedStat= conn.prepareCall(sqlInsertUser);
                preparedStat.setString(1,name);
                preparedStat.setString(2,passSalt[0]);
                preparedStat.setString(3,passSalt[1]);
                preparedStat.executeUpdate();
                return 1;
            }catch (SQLException e){
                e.printStackTrace();
                return 0;
            }
        }else{
            System.out.println("\""+name+"\" Utente Già iscritto");
            return -1;
        }
    }
    public void deleteTable(String table){
        String sqlDelete="DROP TABLE IF EXISTS "+table;
        try(Connection conn=DriverManager.getConnection(this.pathDb)) {
            Statement statement=conn.createStatement();
            statement.executeUpdate(sqlDelete);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void deleteDataTable(String table){
        String sqlDelete="DELETE FROM "+table;
        try(Connection conn=DriverManager.getConnection(this.pathDb)) {
            Statement statement=conn.createStatement();
            statement.executeUpdate(sqlDelete);
            System.out.println("Eliminazione di tutti i dati completata.");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean existUser(String user){
        String sqlSelect="SELECT NAME FROM USER WHERE NAME='"+user+"';";
        try(Connection conn=DriverManager.getConnection(this.pathDb)) {
            Statement statement=conn.createStatement();
            ResultSet rS=statement.executeQuery(sqlSelect);
            if(rS.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
/*
TEST METODO PROBLEMI CON LO STATMENT E RESULTSET(viene chiuso lo statment senza permettere di recuperare i valori)
    public ResultSet getTable(String table){
        String sqlSelect="SELECT * FROM "+table;
        try(Connection conn=DriverManager.getConnection(this.pathDb)){
            return conn.createStatement().executeQuery(sqlSelect);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

 */
    public String getPathDb() {
        return pathDb;
    }

    public void setPathDb(String pathDb) {
        this.pathDb = pathDb;
    }


}
