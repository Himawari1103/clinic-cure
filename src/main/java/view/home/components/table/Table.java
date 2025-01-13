package view.home.components.table;

import view.home.components.scrollbar.ScrollBarCustom;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");

                RowSorter<? extends TableModel> sorter = getRowSorter();
                if (sorter != null) {
                    List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
                    if (!sortKeys.isEmpty() && sortKeys.getFirst().getColumn() == convertColumnIndexToModel(i1)) {
                        SortOrder sortOrder = sortKeys.getFirst().getSortOrder();

                        // Thêm ký hiệu mũi tên tùy chỉnh
                        if (sortOrder == SortOrder.ASCENDING) {
                            header.setText(o + " ↑");
                        } else if (sortOrder == SortOrder.DESCENDING) {
                            header.setText(o + " ↓");
                        } else {
                            header.setText(o.toString());
                        }
                    }
                }
                return header;
            }
        });
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean selected, boolean focus, int i, int i1) {
                Component com = super.getTableCellRendererComponent(jtable, o, selected, focus, i, i1);
                setBorder(noFocusBorder);
                com.setForeground(new Color(0, 0, 0));
                if (selected) {
                    com.setBackground(new Color(0, 120, 215));
                } else {
                    com.setBackground(Color.WHITE);
                }

                return com;
            }
        });

    }

    public void addRow(Object[] row) {
        DefaultTableModel mod = (DefaultTableModel) getModel();
        mod.addRow(row);
    }

    public void deleteRow(int index){
        DefaultTableModel mod = (DefaultTableModel) getModel();
        mod.removeRow(index);
    }

    public void updateRow(int index, Object[] row){
        DefaultTableModel mod = (DefaultTableModel) getModel();
        for (int i = 0; i < mod.getColumnCount(); i++) {
            mod.setValueAt(row[i],index,i);
        }
    }

    public Object[] getRow(int index) {
        DefaultTableModel mod = (DefaultTableModel) getModel();
        String[] row = new String[mod.getColumnCount()];
        for (int i = 0; i < mod.getColumnCount(); i++) {
            row[i] = (String)mod.getValueAt(index,i);
        }
        return row;
    }

    public Object[] getColumnNames() {
        DefaultTableModel mod = (DefaultTableModel) getModel();
        String[] columnNames = new String[mod.getColumnCount()];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = mod.getColumnName(i);
        }
        return columnNames;
    }

    public void fixTable(JScrollPane scroll) {
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setVerticalScrollBar(new ScrollBarCustom());
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
//        scroll.setBorder(new EmptyBorder(5, 10, 5, 10));
    }

    public void setColumnWidths(int[] widths) {
        for (int i = 0; i < this.getColumnCount(); i++) {
            if (i < widths.length) {
                this.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        }
    }
}
