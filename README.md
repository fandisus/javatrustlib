# Java Trustlib
A java library for simplifying many things. Especially database operations.
# Installation
In netbeans, just include this project in your project. Or manually compile this source code to jar and include the jar file.
# Usages
Below are usage examples.
## package trust.common:
##### IntCom(String str)
For checking if string [str] is an integer string or not.
```java
import trust.common.IntCom;
...
String strNumber = someEditText.getText();
if (IntCom.StrIsInt(strNumber)) {
  JOptionPane.showMessageDialog(this, "Invalid number");
}
```
##### StrCom
For escaping SQL Injection. Note that this method is meant for PostgreSQL. For MySQL, slight modifications are required.
```
import trust.common.StrCom;
...
String username = "wendy's";
String password = "papa jack's";
String strFormat = "INSERT INTO users VALUES (%s, PASSWORD('%s'))";
String sql = String.format(
  strFormat, 
  StrCom.nqq(username),  //Add enclosing quotes automatically
  StrCom.nq(password)    //Without enclosing quote, just escape quotes.
);
System.out.println(sql);
//INSERT INTO users VALUES ('wendy''s', PASSWORD('papa jack''s'))
```

## trust.controls
just some saved up code for javafx controls. Analyze the source and use at your own risk.

## trust.db
* DataRow and DataTable comes from C# coding habit.
* DataRow is a single row with multipe columns.
* DataTable is a collection of DataRows.
* DBBase is base class of MySQLDB and PgsqlDB.
* MySQLDB and PgsqlDB are database APIs.
* TableComposer is laravel like query builder.

##### SetConnection (DBBase: PgsqlDB, MySQLDB)
Sets the connection parameters for the other method calls.
```
public static MySQLDB db = new MySQLDB();
db.SetConnection("localhost", "root", "thepass", "dbname");
//or
db.SetConnection("localhost", "root", "thepass", "dbname", 3306);
//The first SetConnection function overload will use database default port.
//3306 for MySQLDB, 5432 for PgsqlDB
```

##### ArrayList<String> DbList()
Lists available databases.
```
ArrayList<String> databases = db.DbList();
for (String database : databases) {
  System.out.println(database);
}
```
##### ArrayList<String> TablesList()
Lists available tables in current database/schema.
```
ArrayList<String> tables = db.TablesList();
for (String table : tables) {
  System.out.println(table);
}
```

##### long InsertGetId(String cmd)
Inserts an auto incremented / serial row to the database and returns the new id.
```
String sql =
  "INSERT INTO books (id,title,author) VALUES (null, 'title', 'author')";
long id = db.InsertGetId(sql);;
int newId = (int) id; //If the id actually an int instead of long
```

##### boolean RowExists(String query)
Check if query returns rows or not.
```
boolean hasRow = db.RowExists("SELECT * FROM users WHERE username='fandi'");
if (hasRow) System.out.println("Username already exists");
```

##### T GetOneData(String query, Class<T> type)
Get exactly one data with type T.
```
int age = db.GetOneData("SELECT age FROM users WHERE id=1", Integer.class);
String gender = db.GetOneData("SELECT gender FROM users WHERE id=1", String.class);
```

##### DataRow GetOneRow(String query)
Get one row from database.
```
DataRow r = db.GetOneRow("SELECT * FROM users WHERE id=1");
System.out.println("Username is: " + r.Get("username"));
System.out.println("Encrypted Pass is: " + r.Get(1));
```

##### DataTable GetToDataTable(String query)
Get a collection of DataRow from database.
```
DataTable dt = db.GetToDataTable("SELECT id,title,author FROM books");
for (DataRow r : dt.Rows) {
  System.out.println("Book id: " + r.Get("id")); //By column name
  System.out.println("Title  : " + r.Get(1)); //By column index
  System.out.println("Author : " + r.Get("author")); //By colname
}
```

##### AppendToDataTable(String query, DataTable target)
This method self explains. Just try it.

##### int Execute(String query)
Execute a query, for example INSERT, UPDATE or DELETE query. Returns affected row count.
```
db.Execute("INSERT INTO users VALUES (null, 'fandi', PASSWORD('123123'))");
```

##### TransactionExecute(List<String> commands)
Execute a batch of queries in an transaction. Error in queries will cause rollback.
```
ArrayList<String> queries = new ArrayList<>();
//sales_details columns: item_id, qty, price
queries.add("INSERT INTO sales_details VALUES (1, 2, 20)");
queries.add("UPDATE items SET stock=stock-2 WHERE id=1");
db.TransactionExecute(queries);
```

##### long TransExecWithAutoInc(List<String> commands)
Executes a batch of queries in an transaction. With first query returns an auto increment / serial id, and the remaining queries put <newid> in them when need to use the new id.
```
ArrayList<String> queries = new ArrayList<>();
//sales columns: id, sales_time, customer_name
queries.add(
  "INSERT INTO sales VALUES (NULL, '2017-12-01 09:32:12', 'Emilia')"
);
//sales_details columns: sales_id, item_id, qty, price
queries.add("INSERT INTO sales_details VALUES (<NEWID>, 1, 2, 20)");
queries.add("UPDATE items SET stock=stock-2 WHERE id=1");
db.TransactionExecute(queries);
```

# License
Use it as you like, but dont blame me if anything goes wrong.