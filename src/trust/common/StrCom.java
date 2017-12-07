package trust.common;

public final class StrCom {
  private StrCom() {}
  public static String nq(String source) { //pgsql version
    return source.replace("'", "\\'");
  }
  public static String nqq(String source) {
    return "'" + nq(source) + "'";
  }
}
