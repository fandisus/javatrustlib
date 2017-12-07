package trust.db;

import java.util.ArrayList;

public class TableComposer {

  public String TableName;
  protected ArrayList<String> ColumnDefs = new ArrayList<>();
  protected ArrayList<String> Constraints = new ArrayList<>();
  protected ArrayList<String> Indexes = new ArrayList<>();
  protected ArrayList<String> Comments = new ArrayList<>();

  protected String lastCol;

  public TableComposer(String tname) {
    TableName = tname;
  }

  protected TableComposer returner(String colName) {
    this.lastCol = colName;
    return this;
  }

  public TableComposer Increments(String colName) {
    this.ColumnDefs.add(colName + " SERIAL");
    return this.returner(colName);
  }

  public TableComposer BigIncrements(String colName) {
    this.ColumnDefs.add(colName + " BIGSERIAL");
    return this.returner(colName);
  }

  public TableComposer String(String colName) {
    return this.String(colName, 50);
  }

  public TableComposer String(String colName, int length) {
    this.ColumnDefs.add(colName + " CHARACTER VARYING(" + Integer.toString(length) + ")");
    return this.returner(colName);
  }

  public TableComposer Text(String colName) {
    this.ColumnDefs.add(colName + " TEXT");
    return this.returner(colName);
  }

  public TableComposer Integer(String colName) {
    this.ColumnDefs.add(colName + " INTEGER");
    return this.returner(colName);
  }

  public TableComposer BigInteger(String colName) {
    this.ColumnDefs.add(colName + " BIGINT");
    return this.returner(colName);
  }

  public TableComposer Double(String colName) {
    this.ColumnDefs.add(colName + " DOUBLE PRECISION");
    return this.returner(colName);
  }
  
  public TableComposer Numeric(String colName, int precision, int scale) {
    this.ColumnDefs.add(colName + " NUMERIC(" + Integer.toString(precision) + "," + Integer.toString(scale) + ")");
    return this.returner(colName);
  }

  public TableComposer Bool(String colName) {
    this.ColumnDefs.add(colName + " BOOL");
    return this.returner(colName);
  }

  public TableComposer Timestamp(String colName) {
    this.ColumnDefs.add(colName + " TIMESTAMP");
    return this.returner(colName);
  }

  public TableComposer Date(String colName) {
    this.ColumnDefs.add(colName + " DATE");
    return this.returner(colName);
  }

  public TableComposer JSONB(String colName) {
    this.ColumnDefs.add(colName + " JSONB");
    return this.returner(colName);
  }

  public TableComposer NotNull() {
    int latestIdx = this.ColumnDefs.size() - 1;
    String latestDef = this.ColumnDefs.get(latestIdx);
    this.ColumnDefs.set(latestIdx, latestDef + " NOT NULL");
    return this;
  }

  public TableComposer Unique() {
    this.Constraints.add("CONSTRAINT uq_" + this.TableName + "_" + lastCol + " UNIQUE (" + lastCol + ")");
    return this;
  }

  public TableComposer Index() {
    this.Indexes.add("CREATE INDEX idx_" + lastCol + "_" + TableName + " ON " + TableName + " USING BTREE (" + lastCol + ");");
    return this;
  }

  public TableComposer GinPropIndex(String[] props) {
    for (String anu : props) {
      Indexes.add("CREATE INDEX idx_" + anu + "_" + lastCol + "_" + TableName + " ON " + TableName + " USING GIN ((" + lastCol + "->'" + anu + "'));");
    }
    return this;
  }

  public TableComposer GinIndex() {
    Indexes.add("CREATE INDEX idx_" + lastCol + "_" + TableName + " ON " + TableName + " USING GIN (" + lastCol + ");");
    return this;
  }

  public TableComposer Primary() {
    return this.Primary(null);
  }

  public TableComposer Primary(String strCols) {
    if (strCols == null) strCols = lastCol;
    Constraints.add("CONSTRAINT pk_" + TableName + " PRIMARY KEY (" + strCols + ")");
    return this;
  }

  public TableComposer Foreign(String RefTable, String RefCol) {
    return this.Foreign(RefTable, RefCol, "CASCADE", "CASCADE");
  }
  public TableComposer Foreign(String RefTable, String RefCol, String OnUpdate, String OnDelete) {
    OnUpdate = " ON UPDATE " + OnUpdate;
    OnDelete = " ON DELETE " + OnDelete;
    Constraints.add("CONSTRAINT fk_" + lastCol + "_" + TableName + " FOREIGN KEY (" + lastCol + ") REFERENCES " + RefTable + " (" + RefCol + ")" + OnUpdate + OnDelete);
    return this;
  }

  public TableComposer MultiForeign(String strCols, String refTable, String strRefCols) {
    return this.MultiForeign(strCols, refTable, strRefCols, "CASCADE", "CASCADE");
  }
  public TableComposer MultiForeign(String strCols, String refTable, String strRefCols, String OnUpdate, String OnDelete) {
    OnUpdate = " ON UPDATE " + OnUpdate;
    OnDelete = " ON DELETE " + OnDelete;
    Constraints.add("CONSTRAINT fk_" + refTable + "_" + TableName + " FOREIGN KEY (" + strCols + ") REFERENCES " + refTable + " (" + strRefCols + ")" + OnUpdate + OnDelete);
    return this;
  }

  public TableComposer Comment(String comment) {
    return Comment(lastCol, comment);
  }

  public TableComposer Comment(String col, String comment) {
    String newcom = comment.replace("'", "''");
    Comments.add("COMMENT ON COLUMN " + TableName + "." + col + " IS '" + newcom + "';");
    return this;
  }

  public ArrayList<String> parse(boolean withDrop) {
    String nl = System.getProperty("line.separator");
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE ").append(TableName).append(" (").append(nl);
    ArrayList<String> insides = new ArrayList<>();
    insides.addAll(ColumnDefs);
    insides.addAll(Constraints);
    String strInsides = String.join("," + nl, insides);
    String creatorSQL = "CREATE TABLE " + TableName + "(" + nl + strInsides + nl + ");";

    ArrayList<String> hasil = new ArrayList<>();
    if (withDrop) hasil.add("DROP TABLE IF EXISTS " + TableName + " CASCADE;");
    hasil.add(creatorSQL);
    hasil.addAll(Indexes);
    hasil.addAll(Comments);
    return hasil;
  }
}
