package client.model;


import utils.RsaUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;


/*
Questa classe si occupa della logica di funzionamento del client
 */
public class ClientUtil {
    private Socket socket;
    private BufferedReader reader;
    private DataOutputStream writer;
    private PublicKey publickeyServer;

    public ClientUtil(){
    }

    public ClientUtil(String host,int port){
        inizialize(host,port);
    }

    public int inizialize(String host,int port){
        try {
            this.socket=new Socket(host,port);
            this.reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer=new DataOutputStream(socket.getOutputStream());
            return 1;
        } catch (IOException e) {
            return 0;
        }
    }

    public int execLogin(byte[] credentialsRsa) throws IOException {
        try {
            String bs64cred= Base64.getEncoder().encodeToString(credentialsRsa);//Per il trasferimento codifico l'array di byte in base64
            writer.writeBytes("login "+ bs64cred+"\n");
            System.out.println("login richiesto");//Messaggio di debug in console
            int res=Integer.parseInt(reader.readLine().trim());
            return res;
        } catch (IOException e) {
            closeall();
            e.printStackTrace();
            return 0;
        }
    }
    public boolean requestPLK(){
        try {
            writer.writeBytes("getPublickey\n");
            String msg=reader.readLine().trim();
            publickeyServer= RsaUtils.getPublicKey(msg);
            System.out.println("Public Key ricevuta: "+msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PublicKey getPublickeyServer() {
        return publickeyServer;
    }

    public void closeall() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }

}
