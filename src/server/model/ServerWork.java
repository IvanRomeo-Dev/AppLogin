package server.model;

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
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


/*
* Classe creata per predisporre il programma a un futuro multithread
* per accettare più connessioni contemporaneamente;
* Al momento è sufficente poter accettare una sola connessione;
* */
public class ServerWork extends Thread {

    private ServerSocket serverSocket;
    private BufferedReader reader;
    private DataOutputStream writer;
    private int port =4000;
    private Socket socket;
    private DbConnUtil dbConnUtil;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private boolean serverrunning;


    public ServerWork(DbConnUtil dbConnUtil,PublicKey publicKey,PrivateKey privateKey){
        this.dbConnUtil=dbConnUtil;
        this.privateKey=privateKey;
        this.publicKey=publicKey;
    }

    @Override
    public void run(){
        inizializeServer();
        serverrunning=true;
        startServer();

    }

    public  void inizializeServer(){
        try{
            serverSocket=new ServerSocket(port);
            System.out.println("\n**********Server in ascolto sulla porta "+port+"*********\n");
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                socket=serverSocket.accept();
                System.out.println("Connessione accettata");
                if(true){
                    reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer=new DataOutputStream(socket.getOutputStream());
                    while (!socket.isClosed()){
                        String msg="";
                        try{
                            msg=reader.readLine().trim();
                            System.out.println("Il client : "+msg);//Messaggio di debug per leggere cosa richiede un client
                        }catch(Exception e){
                            writer.close();
                            reader.close();
                            socket.close();
                            startServer();
                        }
                        String[] value=msg.split(" ",3);//Nella prima posizione troviamo il cmd
                        switch (value[0]){
                            case "getPublickey":{
                                writer.writeBytes(Base64.getEncoder().encodeToString(publicKey.getEncoded())+"\n");
                                System.out.println("public key inviata");
                                break;
                            }
                            case "login":{
                                byte[] credentials=Base64.getDecoder().decode(value[1].trim());
                                String cred= RsaUtils.decrypt(credentials,privateKey);
                                String[] user_pass=cred.split(" ",2);
                                if(dbConnUtil.checkuser(user_pass[0],user_pass[1]))
                                    writer.writeBytes("1\n");
                                else
                                    writer.writeBytes("-1\n");
                                break;
                            }
                            default:
                                System.out.println("Comando sconoscriuto");
                                break;
                        }
                    }

                }
            }

        }catch (SocketException e){
            System.out.println("Server chiuso");
        }catch (IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
    public void closeall() throws IOException {
        serverSocket.close();
        interrupt();

    }
}
