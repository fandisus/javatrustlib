package trust.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataTable {
  public ArrayList<String> ColumnNames;
  public ArrayList<DataRow> Rows = new ArrayList<>();
  
  public DataTable(ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    //Populate ColumnNames
    int colcount = rsmd.getColumnCount();
    ColumnNames = new ArrayList<>();
    for (int i=1; i<=colcount; i++) ColumnNames.add(rsmd.getColumnName(i));
    //Populate Rows
    while (rs.next()) Rows.add(new DataRow(this, rs, rsmd));
  }
  public void Append(ResultSet rs) throws SQLException {
    //No need to populate ColumnNames
    ResultSetMetaData rsmd = rs.getMetaData();
    while (rs.next()) Rows.add(new DataRow(this, rs, rsmd));
  }
  public void Clear() {
    Rows.clear();
  }
}
