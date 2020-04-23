package client.model;


import utils.RsaUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Base64;

public class ClientUtil {
    private Socket socket;
    private BufferedReader reader;
    private DataOutputStream writer;
    private PublicKey publickeyServer;
    private boolean waiting=false;

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
            String bs64cred= Base64.getEncoder().encodeToString(credentialsRsa);
            writer.writeBytes("login "+ bs64cred+"\n");
            System.out.println("login richiesto");
            waiting=true;
            int res=Integer.parseInt(reader.readLine().trim());
            waiting=false;
            return res;
        } catch (IOException e) {
            getWriter().close();
            getReader().close();
            getSocket().close();
            e.printStackTrace();
            return 0;
        }
    }
    public boolean requestPLK(){
        try {
            writer.writeBytes("getPublickey\n");
            waiting=true;
            String msg=reader.readLine().trim();
            waiting=false;
            publickeyServer= RsaUtils.getPublicKey(msg);
            System.out.println("Public Key ricevuta: "+msg);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public DataOutputStream getWriter() {
        return writer;
    }

    public PublicKey getPublickeyServer() {
        return publickeyServer;
    }

    public boolean isWaiting() {
        return waiting;
    }
}
