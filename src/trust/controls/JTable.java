package trust.controls;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class JTable {
  //https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
  public static void resizeColumnWidth(javax.swing.JTable table) {
    final TableColumnModel columnModel = table.getColumnModel();
    for (int column = 0; column < table.getColumnCount(); column++) {
      int width = 15; // Min width
      for (int row = 0; row < table.getRowCount(); row++) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component comp = table.prepareRenderer(renderer, row, column);
        width = Math.max(comp.getPreferredSize().width + 50, width);
      }
      if (width > 300) {
        width = 300;
      }
      columnModel.getColumn(column).setPreferredWidth(width);
    }
  }
  
  //https://stackoverflow.com/questions/3467052/set-right-alignment-in-jtable-column
  //Alignment ini ndak bisa dipakai di kolom date.
  public static void ColumnAlignRight(javax.swing.JTable table, int col_idx) {
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
    table.getColumnModel().getColumn(col_idx).setCellRenderer(rightRenderer);
  }
  public static void ColumnAlignCenter(javax.swing.JTable table, int col_idx) {
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(JLabel.CENTER);
    table.getColumnModel().getColumn(col_idx).setCellRenderer(rightRenderer);
  }
}
