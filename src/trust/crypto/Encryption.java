package trust.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

//Perlu tambah implementasi asymmetric encryption(public private key) https://www.mkyong.com/java/java-asymmetric-cryptography-example/
public class Encryption {
  //AES/CBC/NoPadding (128)
  //AES/CBC/PKCS5Padding (128)
  //AES/ECB/NoPadding (128)
  //AES/ECB/PKCS5Padding (128)
  //DES/CBC/NoPadding (56)
  //DES/CBC/PKCS5Padding (56)
  //DES/ECB/NoPadding (56)
  //DES/ECB/PKCS5Padding (56)
  //DESede/CBC/NoPadding (168)
  //DESede/CBC/PKCS5Padding (168)
  //DESede/ECB/NoPadding (168)
  //DESede/ECB/PKCS5Padding (168)
  //RSA/ECB/PKCS1Padding (1024, 2048)
  //RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
  //RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
  //Kata: https://stackoverflow.com/questions/15880901/java-aes-cipher-text-size
  //CBC lebih secure.
  //Coding di bawah adopsi sebagian dari:
  //https://www.veracode.com/blog/research/encryption-and-decryption-java-cryptography
  //Encryption key dan iv harus 16byte/karakter
  private static Cipher getAESCipherObject(String key, int mode) throws Exception {
    Cipher c;
    SecretKeySpec skey;
    try {
      c = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec("i2xYBmstJoMRBP9h".getBytes("UTF-8"));
      skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
      c.init(mode, skey, iv);
      return c;
    } catch (NoSuchAlgorithmException ex) {
      throw new Exception("Unknown algorithm", ex);
    } catch (NoSuchPaddingException ex) {
      throw new Exception("Unknown padding", ex);
    } catch (UnsupportedEncodingException ex) {
      throw new Exception("UTF-8 not supported", ex);
    } catch (InvalidKeyException ex) {
      throw new Exception("Invalid key exception", ex);
    }
  }
  public static String AESEncryptString(String key, String input) throws Exception {
    try {
      Cipher c = getAESCipherObject(key, Cipher.ENCRYPT_MODE);
      byte[] result = c.doFinal(input.getBytes("UTF-8"));
      return DatatypeConverter.printBase64Binary(result);
//      StringBuffer sb = new StringBuffer();
//      for (int i = 0; i < result.length; i++) {
//        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
//      }
//      return sb.toString();
//      return new String(result);
    } catch (UnsupportedEncodingException ex) {
      throw new Exception("UTF-8 not supported", ex);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
      throw new Exception("Bad padding exception", ex);
    } catch (Exception ex) {
      throw ex;
    }
  }
  public static String AESDecryptString(String key, String input) throws Exception {
    try {
      Cipher c = getAESCipherObject(key, Cipher.DECRYPT_MODE);
      byte[] result = c.doFinal(DatatypeConverter.parseBase64Binary(input));
      return new String(result);
    } catch (UnsupportedEncodingException ex) {
      throw new Exception("UTF-8 not supported", ex);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
      throw new Exception("Bad padding exception", ex);
    } catch (Exception ex) {
      throw ex;
    }
  }
}
