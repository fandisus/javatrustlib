package trust.common;
public class IntCom {
  public static boolean StrIsInt(String str) {
    try {
      int a = Integer.valueOf(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
