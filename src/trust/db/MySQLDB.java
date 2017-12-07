package trust.db;

import java.sql.SQLException;
import java.util.List;
public class MySQLDB extends DBBase {

  @Override
  protected void SetConnection() {
    String csFormat = "jdbc:mysql://%1s:%2d/%3s?user=%4s&password=%5s";
    this.connString = String.format(csFormat, DBHost, DBPort, DBSchema, DBUser, DBPass);
  }

  @Override
  public boolean BackupDatabase(String filename, List<String> tables) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean RestoreDatabase(String filename) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected int DefaultPort() { return 3306; }
}
