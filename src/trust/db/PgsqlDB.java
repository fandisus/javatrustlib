package trust.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PgsqlDB extends DBBase{
  @Override protected void SetConnection() {
    String csFormat = "jdbc:postgresql://%1s:%2d/%3s?user=%4s&password=%5s";
    this.connString = String.format(csFormat, DBHost, DBPort, DBSchema, DBUser, DBPass);
    //jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true
  }
  

  
  @Override public boolean BackupDatabase(String filename, List<String> tables) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  @Override public boolean RestoreDatabase(String filename) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected int DefaultPort() { return 5432; }
  
}
