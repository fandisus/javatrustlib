package trust.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataRow {
  public int ColumnCount() {
    return getColumnNames().size();
  }
  private ArrayList<String> columnsNames;
  private ArrayList<String> getColumnNames() {
    if (columnsNames == null) return DataTable.ColumnNames;
    return columnsNames;
  }
  
  public DataTable DataTable;
  public final ArrayList<Object> Items = new ArrayList<>();
  public DataRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
    int colcount = rsmd.getColumnCount();
    columnsNames = new ArrayList<>();
    for (int i=1; i<=colcount; i++) {
      Items.add(rs.getObject(i));
      columnsNames.add(rsmd.getColumnName(i));
    }
  }
  public DataRow(DataTable dt, ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
    DataTable = dt;
    int colcount = rsmd.getColumnCount();
    for (int i=1; i<=colcount; i++) Items.add(rs.getObject(i));
  }
  
  public String ColumnName(int index) { return getColumnNames().get(index); }
  public Object Get(int index) { return Items.get(index); }
  public Object Get(String ColumnName) throws SQLException {
    int idx = getColumnNames().indexOf(ColumnName);
    if (idx > -1) return Items.get(idx);
    else {
      throw new SQLException("DataRow has no columns with that name");
    }
  }
//  public static DataRow GetFrom(ResultSet rs) throws SQLException {
//    if (!rs.next()) return null;
//    
//    
//  }
}
