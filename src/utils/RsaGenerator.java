package utils;

import java.security.*;

public class RsaGenerator {

        private PrivateKey privateKey;
        private PublicKey publicKey;

        public RsaGenerator() throws NoSuchAlgorithmException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        }


        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

        /*public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
            RsaGenerator keyPairGenerator = new RsaGenerator();
            System.out.println();
            System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
        }*/

}
