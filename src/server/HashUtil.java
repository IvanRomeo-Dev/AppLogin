package server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    MessageDigest digest;
    public HashUtil(){
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public String generateSalt(){
        String salt="";
        char[] val={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','1','2','3','4','5','6','7','8','9','0'};
        SecureRandom secureRandom=new SecureRandom();
        for(int i=0;i<8;i++){
            salt=salt+val[secureRandom.nextInt(val.length)];
        }
        return salt;
    }
    public String[] hash(String password){
        String[] passSalt=new String[2];
        String salt=generateSalt();
        String pass=password+salt;
        passSalt[1]=salt;
        byte[] encodedhash = digest.digest(pass.getBytes(StandardCharsets.UTF_8));
        System.out.print("Password con salt generato hashata: ");
        passSalt[0]=bytesToHex(encodedhash);
        System.out.println(passSalt[0]);
        return passSalt;
    }

}
