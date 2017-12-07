//Copyright: http://myjavafx.blogspot.co.id/2012/03/treeview-with-data-source.html
package trust.controls;

import javafx.collections.ObservableList;

public interface HierarchyData<T extends HierarchyData> {
  public ObservableList<T> getChildren();
}
