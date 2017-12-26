package trust.crypto;
//https://www.mkyong.com/java/java-md5-hashing-example/

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hash {
  //SHA-1  SHA-256  MD5
  public static String hash(String algo, String instr) throws Exception {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance(algo);
    } catch (NoSuchAlgorithmException ex) {
      throw new Exception("Unrecognized algorithm: " + ex.getMessage(), ex);
    }
    try {
      md.update(instr.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      throw new Exception("UTF-8 Encoding not supported");
    }
    
    byte[] byteData = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
     sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }
  public static String SHA256(String instr) throws Exception {
    return hash("SHA-256", instr);
  }
  public static String MD5(String instr) throws Exception {
    return hash("MD5", instr);
  }
}
