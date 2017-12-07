//Copied from: http://myjavafx.blogspot.co.id/2012/03/treeview-with-data-source.html
package trust.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import java.util.HashMap;
import java.util.Map;

/**
 * This class extends the {@link TreeView} to use items as a data source.
 * This allows you to treat a {@link TreeView} in a similar way as a {@link javafx.scene.control.ListView} or {@link javafx.scene.control.TableView}.
 * Each item in the list must implement the {@link HierarchyData} interface, in order to map the recursive nature of the tree data to the tree view.
 * Each change in the underlying data (adding, removing, sorting) will then be automatically reflected in the UI.
 *
 * @author Christian Schudt
 * @param <T> */
public class TreeViewDataBound<T extends HierarchyData<T>> extends TreeView<T> {
  /**
   * Keep hard references for each listener, so that they don't get garbage collected too soon.
   */
  private final Map<TreeItem<T>, ListChangeListener<T>> hardReferences = new HashMap<>();
  /**
   * Also store a reference from each tree item to its weak listeners,
   * so that the listener can be removed, when the tree item gets removed.
   */
  private final Map<TreeItem<T>, WeakListChangeListener<T>> weakListeners = new HashMap<>();
  private ObjectProperty<ObservableList<? extends T>> items = new SimpleObjectProperty<>(this, "items");
  
  public TreeViewDataBound() {
    super();
    init();
  }

  public TreeViewDataBound(TreeItem<T> root) {
    super(root);
    init();
  }

  /**
     * Initializes the tree view.
     */
  private void init() {
    rootProperty().addListener((observable, oldValue, newValue) -> {
      clear(oldValue);
      updateItems();
    });
//    rootProperty().addListener(new ChangeListener<TreeItem<T>>() {
//      @Override
//      public void changed(ObservableValue<? extends TreeItem<T>> observableValue, TreeItem<T> oldRoot, TreeItem<T> newRoot) {
//        System.out.println("wew");
//      }
//    });
    
    setItems(FXCollections.<T>observableArrayList());
    
    // Do not use ChangeListener, because it won't trigger if old list equals new list (but in fact different references).
    items.addListener((observable) -> {
      clear(getRoot());
      updateItems();
    });
  }

  /**
     * Removes all listener from a root.
     *
     * @param root The root.
     */
  private void clear(TreeItem<T> root) {
    if (root != null) {
      root.getChildren().forEach((treeItem) -> { removeRecursively(treeItem); });
      removeRecursively(root);
      root.getChildren().clear();
    }
  }

  /**
     * Updates the items.
     */
  private void updateItems() {
    
    if (getItems() != null) {
      getItems().forEach((value) -> { getRoot().getChildren().add(addRecursively(value)); });

      ListChangeListener<T> rootListener = getListChangeListener(getRoot().getChildren());
      WeakListChangeListener<T> weakListChangeListener = new WeakListChangeListener<>(rootListener);
      hardReferences.put(getRoot(), rootListener);
      weakListeners.put(getRoot(), weakListChangeListener);
      getItems().addListener(weakListChangeListener);
    }
  }

  /**
     * Gets a {@link javafx.collections.ListChangeListener} for a  {@link TreeItem}. It listens to changes on the underlying list and updates the UI accordingly.
     *
     * @param treeItemChildren The associated tree item's children list.
     * @return The listener.
     */
  private ListChangeListener<T> getListChangeListener(final ObservableList<TreeItem<T>> treeItemChildren) {
    return new ListChangeListener<T>() {
      @Override
      public void onChanged(final Change<? extends T> change) {
        while (change.next()) {
          if (change.wasUpdated()) {
            // http://javafx-jira.kenai.com/browse/RT-23434
            continue;
          }
          if (change.wasRemoved()) {
            for (int i = change.getRemovedSize() - 1; i >= 0; i--) {
              removeRecursively(treeItemChildren.remove(change.getFrom() + i));
            }
          }
          // If items have been added
          if (change.wasAdded()) {
            // Get the new items
            for (int i = change.getFrom(); i < change.getTo(); i++) {
              treeItemChildren.add(i, addRecursively(change.getList().get(i)));
            }
          }
          // If the list was sorted.
          if (change.wasPermutated()) {
            // Store the new order.
            Map<Integer, TreeItem<T>> tempMap = new HashMap<>();
            
            for (int i = change.getTo() - 1; i >= change.getFrom(); i--) {
              int a = change.getPermutation(i);
              tempMap.put(a, treeItemChildren.remove(i));
            }
            
            getSelectionModel().clearSelection();
            
            // Add the items in the new order.
            for (int i = change.getFrom(); i < change.getTo(); i++) {
              treeItemChildren.add(tempMap.remove(i));
            }
          }
        }
      }
    };
  }
  /**
     * Removes the listener recursively.
     *
     * @param item The tree item.
     */
  private TreeItem<T> removeRecursively(TreeItem<T> item) {
    if (item.getValue() != null && item.getValue().getChildren() != null) {
      
      if (weakListeners.containsKey(item)) {
        item.getValue().getChildren().removeListener(weakListeners.remove(item));
        hardReferences.remove(item);
      }
      for (TreeItem<T> treeItem : item.getChildren()) {
        removeRecursively(treeItem);
      }
    }
    return item;
  }
    /**
     * Adds the children to the tree recursively.
     *
     * @param value The initial value.
     * @return The tree item.
     */
  private TreeItem<T> addRecursively(T value) {
    
    TreeItem<T> treeItem = new TreeItem<>();
    treeItem.setValue(value);
    treeItem.setExpanded(true);

    if (value != null && value.getChildren() != null) {
      ListChangeListener<T> listChangeListener = getListChangeListener(treeItem.getChildren());
      WeakListChangeListener<T> weakListener = new WeakListChangeListener<>(listChangeListener);
      value.getChildren().addListener(weakListener);

      hardReferences.put(treeItem, listChangeListener);
      weakListeners.put(treeItem, weakListener);
      for (T child : value.getChildren()) {
        treeItem.getChildren().add(addRecursively(child));
      }
    }
    return treeItem;
  }
  
  public ObservableList<? extends T> getItems() {
    return items.get();
  }

    /**
     * Sets items for the tree.
     *
     * @param items The list.
    
   * @param items */
  public void setItems(ObservableList<? extends T> items) {
    this.items.set(items);
  }
}