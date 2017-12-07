package trust.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class DBBase {
  public String connString;
  protected String DBHost, DBUser, DBPass, DBSchema;
  protected int DBPort;
    
  public void SetConnection(String DBHost, String DBUser, String DBPass, String DBSchema, int DBPort) {
    this.DBHost = DBHost;
    this.DBUser = DBUser;
    this.DBPass = DBPass;
    this.DBSchema = DBSchema;
    this.DBPort = DBPort;
    this.SetConnection();
  };
  protected abstract int DefaultPort();
  public void SetConnection(String DBHost, String DBUser, String DBPass, String DBSchema) {
    SetConnection(DBHost, DBUser, DBPass, DBSchema, DefaultPort());
  }
  protected abstract void SetConnection();
  public boolean BackupDatabase(String filename) throws SQLException { return this.BackupDatabase(filename, null); }
  public abstract boolean BackupDatabase(String filename, List<String> tables) throws SQLException;
  public abstract boolean RestoreDatabase(String filename) throws SQLException;
  
  public ArrayList<String> DbList() throws SQLException { //Tested
    ArrayList<String> res = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(connString); ) {
      DatabaseMetaData dbmd = conn.getMetaData();
      try (ResultSet rs = dbmd.getCatalogs()) {
        while (rs.next()) res.add(rs.getString(1));
      }
    }
    return res;
  }
  public ArrayList<String> TablesList() throws SQLException { //Tested
    ArrayList<String> res = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(connString); ) {
      DatabaseMetaData dbmd = conn.getMetaData();
      try (ResultSet rs = dbmd.getTables(null, "public", "%", new String[] {"TABLE"})) {
        while (rs.next()) res.add(rs.getString(3));
      }
    }
    return res;
  }
  public long InsertGetId(String command) throws SQLException { //Tested
    long res;
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement()) {
      st.executeUpdate(command, Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = st.getGeneratedKeys();
      if (rs.next()) res = rs.getLong(1); else res = 0;
    }
    return res;
  };
  public long TransExecWithAutoInc(List<String> commands) throws SQLException { //Tested
    long newid;
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement()) {
      conn.setAutoCommit(false);
      //Execute first and get newid
      st.executeUpdate(commands.get(0), Statement.RETURN_GENERATED_KEYS);
      ResultSet rs = st.getGeneratedKeys();
      rs.next();
      newid = rs.getLong(1);
      
      for (int i=1; i<commands.size(); i++) {
        String cmd = commands.get(i);
        String newcmd = cmd.replace("<NEWID>", Long.toString(newid));
        st.executeUpdate(newcmd);
      }
      conn.commit();
      conn.setAutoCommit(true);
    }
    return newid;
  }
  
  
  public boolean RowExists(String query) throws SQLException { //Tested
    boolean hasRow;
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      hasRow = rs.next();
    }
    return hasRow;
  }
  public <T> T GetOneData(String query, Class<T> type) throws SQLException{ //Tested
    T res;
    try (Connection conn = DriverManager.getConnection(connString);Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      if (rs.next()) res = rs.getObject(1, type); else res = null;
    }
    return res;
  }
  
  
  
  public DataRow GetOneRow(String query) throws SQLException { //Tested
    DataRow res = null;
    try (Connection conn = DriverManager.getConnection(connString);Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      if (rs.next()) res = new DataRow(rs, rs.getMetaData());
    }
    return res;
  }
  public DataTable GetToDataTable(String query) throws SQLException {
    DataTable res;
    try (Connection conn = DriverManager.getConnection(connString);Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      res = new DataTable(rs);
    }
    return res;
  }
  public void AppendToDataTable(String query, DataTable targetDataTable) throws SQLException {
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      targetDataTable.Append(rs);
    }
  }
  
  
  public <T> ArrayList<T> GetToList(String query, Class<T> type) throws SQLException { //Tested
    ArrayList<T> res = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(connString);Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
      while (rs.next()) res.add(rs.getObject(1, type));
    }
    return res;
  }
  public int Execute(String command) throws SQLException { //Tested
    int rowsAffectedCount;
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement()) {
      rowsAffectedCount = st.executeUpdate(command);
    }
    return rowsAffectedCount;
  }
  public void TransactionExecute(List<String> commands) throws SQLException { //Change to executebatch, 
    //Tested. Middleway error causes rollback and throws SQLException.
    try (Connection conn = DriverManager.getConnection(connString); Statement st = conn.createStatement()) {
      conn.setAutoCommit(false);
      for(String s: commands) st.addBatch(s);
      st.executeBatch();
      conn.commit();
      conn.setAutoCommit(true);
    }
  }
}
