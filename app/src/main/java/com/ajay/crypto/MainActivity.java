package com.ajay.crypto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {


    public   String modulus = "24130287975021244042702223805688721202041521798556826651085672609155097623636349771918006235309701436638877260677191655500886975872679820355397440672922966114867081224266610192869324297514124544273216940817802300465149818663693299511097403105193694390420041695022375597863889602539348837984499566822859405785094021038882988619692110445776031330831112388738257159574572412058904373392173474311363017975036752132291687352767767085957596076340458420658040735725435536120045045686926201660857778184633632435163609220055250478625974096455522280609375267155256216043291335838965519403970406995613301546002859220118001163241";
    public   String exponent ="415029";
    public   String prefix ="00008099";
    public   String CC_NUMBER ="4111111111111111";
    public  PublicKey publicKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            publicKey = getPublicKey(new BigInteger(modulus), new BigInteger(exponent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final String ccEncrypted = encryptPAN(prefix, CC_NUMBER, publicKey);
          //  System.out.println(ccEncrypted);
            Log.d("ajaymehta",ccEncrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  PublicKey getPublicKey(final BigInteger modulus, final BigInteger exponent) throws Exception {
        final KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(new RSAPublicKeySpec(modulus, exponent));
    }

    //*************** keep in mind Base64 gives spaces in encyrption so ..please remove it if you wanna use it
    // somewhere or you are going to decode it

    public  String encryptPAN(final String prefix, final String pan, PublicKey publicKey) throws Exception {
        byte[] input = String.format("%s%s", prefix, pan).getBytes();
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey,new SecureRandom());
        byte[] cipherText = cipher.doFinal(input);
        String encodedKey = Base64.encodeToString(cipherText, Base64.DEFAULT);
       encodedKey = encodedKey.replaceAll("\\s","");  // remove white space and line breaks
        return encodedKey;
    }



}
