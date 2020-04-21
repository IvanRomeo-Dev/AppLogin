package server;

import javafx.scene.chart.ScatterChart;

import java.sql.*;

public class DbUtil {

    private Connection connection;
    private String pathDb;

    public DbUtil(){
    }
    public DbUtil(String pathDb){
        this.pathDb=pathDb;
    }


    public Connection getConnection() {
        return connection;
    }

    public void testConnection(String pathDb){
        try(Connection conn=DriverManager.getConnection(pathDb)){
            this.connection=conn;
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

    public String getPathDb() {
        return pathDb;
    }

    public void setPathDb(String pathDb) {
        this.pathDb = pathDb;
    }
}
