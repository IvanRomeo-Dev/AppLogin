package client.controller;

import client.model.ClientUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utils.RsaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/*
* Questa classe si occupa di gestire gli eventi della GUI (FXML) e di catturare i dati inseriti.
* Rispettando MVC
* */

public class LoginController {
    private ClientUtil clientUtil;

    public LoginController(ClientUtil clientUtil){
        this.clientUtil=clientUtil;
    }

    @FXML
    private TextField text_host;
    @FXML
    private TextField text_port;
    @FXML
    private TextField text_user;
    @FXML
    private PasswordField text_pass;
    @FXML
    private TextArea text_result;

    /*
     *Viene eseguito con la pressione del tasto test connessione
     */
    @FXML
    private void handle_test() throws IOException {
        /*
        * Controllo sulla porta;
        * Andrebbero fatti i controlli sul resto degli input
        * */
        int port=0;
        try{
            port=Integer.parseInt(text_port.getText().trim());
        }catch (NumberFormatException e){
            System.out.println("Errore porta");
        }
        if(port<65535&&port>0) {
            int res = clientUtil.inizialize(text_host.getText(), port);
            if (res == 1) {
                text_result.appendText("Connessione riuscita \n");
                clientUtil.closeall();
            } else {
                text_result.appendText("Connessione non riuscita\n");
                clientUtil.closeall();
            }
        }else{
            text_result.appendText("Porta non valida");
        }

    }

    /*
     *Viene eseguito con la pressione del tasto Login
     */
    @FXML
    private void handle_login() throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IOException {
        /*
         * Controllo sulla porta;
         * Andrebbero fatti i controlli sul resto degli input
         * */
        int port=0;
        try{
            port=Integer.parseInt(text_port.getText().trim());
        }catch (NumberFormatException e){
            System.out.println("Errore porta");
        }
        String hostname=text_host.getText();
        if(port<65535&&port>0){
            int res=clientUtil.inizialize(hostname,port);
            if(res==1)
                if(clientUtil.requestPLK()){
                    String userPass=text_user.getText().trim()+" "+text_pass.getText().trim();
                    /*
                     *Una volta catturati i dati di accesso vengono criptati con la chiave pubblica
                     *del Server, una volta sul server viene testata la corrispondenza
                     */
                    byte[] credential=RsaUtils.encrypt(userPass, Base64.getEncoder().encodeToString(clientUtil.getPublickeyServer().getEncoded()));
                    int result=clientUtil.execLogin(credential);//Metodo che richiede la verifica dei dati al server
                    if(result==1) {
                        text_result.appendText("Le credenziali sono corrette\n");
                        clientUtil.closeall();
                    }
                    else if(result==-1) {
                        text_result.appendText("Username o password errata\n");
                        clientUtil.closeall();
                    }
                }else {
                    text_result.appendText("Errore nel ricevere la public key\n");
                    clientUtil.closeall();
                }
            else
                text_result.appendText("Connessione non riuscita\n");
        }else{
            text_result.appendText("Porta non valida");
        }


    }

}
