/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trust.repository;

import com.google.gson.Gson;
import java.util.ArrayList;

public abstract class Saveable {
  //fromJSON, toJSON
  public abstract String TableName();
  public abstract void UpdateTo(Saveable s);
  public abstract boolean HasIncrements();
  public abstract void SetPK(Object pk); //long or int
  public abstract Object GetPK();//Whatever, String, long, int, date, Object
  public abstract ArrayList<String> InsertSQL();
  public abstract ArrayList<String> UpdateSQL();
  public abstract ArrayList<String> DeleteSQL();
  public Saveable Clone() {
    Gson gson = new Gson();
    String json = gson.toJson(this);
    return gson.fromJson(json, this.getClass());
  }
}
