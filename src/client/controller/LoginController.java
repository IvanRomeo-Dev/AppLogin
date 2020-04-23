package client.controller;

import client.model.ClientUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import utils.RsaUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


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
    private TextField text_pass;
    @FXML
    private javafx.scene.control.Label lbl_result;

    @FXML
    private void handle_test() throws IOException {
        int res=clientUtil.inizialize(text_host.getText(),Integer.parseInt(text_port.getText().trim()));
        if(res==1){
            lbl_result.setText("Connessione riuscita a "+text_host.getText()+":"+Integer.parseInt(text_port.getText().trim()));
            clientUtil.getWriter().close();
            clientUtil.getReader().close();
            clientUtil.getSocket().close();
        }else{
            lbl_result.setText("Connessione non riuscita");
            clientUtil.getWriter().close();
            clientUtil.getReader().close();
            clientUtil.getSocket().close();
        }


    }
    @FXML
    private void handle_login() throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IOException {
        int res=clientUtil.inizialize(text_host.getText(),Integer.parseInt(text_port.getText().trim()));
        if(res==1)
            if(clientUtil.requestPLK()){
                String userPass=text_user.getText().trim()+" "+text_pass.getText().trim();
                byte[] credential=RsaUtils.encrypt(userPass, Base64.getEncoder().encodeToString(clientUtil.getPublickeyServer().getEncoded()));
                while(clientUtil.isWaiting()){//se il reader sta aspettando un dato ripete l'istruzione
                    System.out.println("in attesa");
                    //res=clientUtil.execLogin(credential);
                }
                int result=clientUtil.execLogin(credential);
                if(result==1) {
                    lbl_result.setText("Le credenziali sono corrette");
                    clientUtil.getWriter().close();
                    clientUtil.getReader().close();
                    clientUtil.getSocket().close();
                }
                else if(result==-1) {
                    lbl_result.setText("Username o password errata");
                    clientUtil.getWriter().close();
                    clientUtil.getReader().close();
                    clientUtil.getSocket().close();
                }
            }else {
                lbl_result.setText("Errore nel ricevere la public key");
                clientUtil.getWriter().close();
                clientUtil.getReader().close();
                clientUtil.getSocket().close();
            }
        else
            lbl_result.setText("Connessione non riuscita a "+text_host.getText()+":"+Integer.parseInt(text_port.getText().trim()));


    }
}
