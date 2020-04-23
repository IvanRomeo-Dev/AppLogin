package server.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {

    public HashUtil(){
    }
    /*
    * Metodo per Bytes -> String hex
    * */
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    /*
     *Metodo che data in input una string effettua l'hash aggiungendo un salt generato automaticamente di 8 char
     */
    public static String[] hashWsalt(String password) throws NoSuchAlgorithmException {
        String[] passSalt=new String[2];
        String salt=generateSalt(8); //faccio generare un salt da 8 caratteri
        String pass=password+salt;
        passSalt[1]=salt;
        byte[] encodedhash = MessageDigest.getInstance("SHA-256").digest(pass.getBytes(StandardCharsets.UTF_8));
        //System.out.print("Password con salt generato hashata: ");
        passSalt[0]=bytesToHex(encodedhash);
        //System.out.println(passSalt[0]);
        return passSalt;
    }
    /*
    *Stesso di hashWsalt ma senza la generazione del salt
    * */
    public static String hashWoutsalt(String password) throws NoSuchAlgorithmException {
        String pass;
        byte[] encodedhash = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
        pass=bytesToHex(encodedhash);
        return pass;
    }
/*
 * Metodo che genera un salt
 */
    private static String generateSalt(int size){
        String salt="";
        char[] val={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0'};
        SecureRandom secureRandom=new SecureRandom();
        for(int i=0;i<size;i++){
            salt=salt+val[secureRandom.nextInt(val.length)];
        }
        return salt;
    }
}
