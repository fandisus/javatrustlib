package trust.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import trust.db.PgsqlDB;

public abstract class RepoBase<T extends Saveable> {    //<T extends ClassA & InterfaceB>
  public abstract List<T> GetWhere(String strWhere, String cols) throws SQLException;
  public List<T> GetWhere(String strWhere) throws SQLException{ return GetWhere(strWhere, "*"); };
  public List<T> GetAll() throws SQLException { return GetWhere(""); }
  public List<T> GetAll(String cols) throws SQLException { return GetWhere("","*"); } 
  
  public abstract boolean RowExists(String strWhere) throws SQLException;
  public abstract T Find(Object PK) throws SQLException;
  public abstract boolean NameChangeAvailable(T Obj) throws SQLException;
  
  public abstract PgsqlDB GetDB();
  
  //EventHandler
  
  public void Insert(T[] models) throws SQLException {
    ArrayList<String> sqls = new ArrayList<>();
    if (models[0].HasIncrements()) {
      for (T mod: models) mod.SetPK(GetDB().TransExecWithAutoInc(mod.InsertSQL()));
    } else {
      for (T mod: models) sqls.addAll(mod.InsertSQL());
      GetDB().TransactionExecute(sqls);
    }
    //Raise event
  }
  public void Update(T[] models) throws SQLException {
    ArrayList<String> sqls = new ArrayList<>();
    for (T mod: models) {
      sqls.addAll(mod.UpdateSQL());
    }
    GetDB().TransactionExecute(sqls);
    //Raise event
  }
  public void Delete(T[] models) throws SQLException {
    ArrayList<String> sqls = new ArrayList<>();
    for (T mod: models) {
      sqls.addAll(mod.DeleteSQL());
    }
    GetDB().TransactionExecute(sqls);
    //Raise event
  }
}
