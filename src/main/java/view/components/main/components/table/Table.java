package view.components.main.components.table;

import view.components.main.components.scrollbar.ScrollBarCustom;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

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
                    com.setBackground(new Color(239, 244, 255));
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
