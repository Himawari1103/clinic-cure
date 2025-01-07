package view.components.main.dialog;

import controller.main.PatientController;
import controller.main.RecordController;
import view.components.main.components.Button;
import view.components.main.components.icon.GoogleMaterialDesignIcons;
import view.components.main.components.icon.IconFontSwing;
import view.components.main.components.table.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogRecordChooser extends javax.swing.JDialog {

    private boolean ok = false;

    DefaultTableModel defaultTableModelMain;
    String selectedRecordId = "";

    private JLabel titleLabel;
    private Table recordTable;
    private JScrollPane jScrollPane;
    private JButton confirmButton;
    private JPanel panelHeader;
    private JPanel panelEventHeader;

    public DialogRecordChooser(java.awt.Frame parent, boolean modal, boolean isPaid) {
        super(parent, modal);
        initComponents();

        initTable(isPaid);
    }

    public void showTable(String message) {
        titleLabel.setText(message);
        ok = false;
        setVisible(true);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                recordTable.setModel(defaultTableModelMain);
            }
        });

        setSize(300,500);

        setResizable(false);
        setLayout(new BorderLayout(0,5));

        panelHeader = new JPanel(new BorderLayout());
        titleLabel = new JLabel("title",SwingConstants.CENTER);
        recordTable = new Table();
        jScrollPane= new JScrollPane(recordTable);
        confirmButton = new JButton("OK");
        confirmButton.setPreferredSize(new Dimension(50,50));
        panelEventHeader = new JPanel();
        panelEventHeader.add(confirmButton);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = true;
                dispose();
            }
        });

        panelHeader.add(titleLabel, BorderLayout.NORTH);
        panelHeader.add(panelEventHeader, BorderLayout.CENTER);

        recordTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "ID bệnh nhân", "Tên bệnh nhân", "Tên bác sĩ", "Ngày khám"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        recordTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane.setViewportView(recordTable);
        recordTable.setColumnWidths(new int[]{20, 20, 100, 100, 50});

        add(panelHeader,BorderLayout.NORTH);
        add(jScrollPane,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void initTable(boolean isPaid) {
        defaultTableModelMain = (DefaultTableModel) recordTable.getModel();
        recordTable.fixTable(jScrollPane);

        addDataTable(isPaid);

        ListSelectionModel selectionModel = recordTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = recordTable.getSelectedRow();
                    if (index != -1) {
                        selectedRecordId = (String) recordTable.getValueAt(index, 0);
                    }
                }
            }
        });
    }

    public void addDataTable(boolean isPaid) {
        RecordController.addRowRecordTableDialog(recordTable, isPaid);
    }

    public boolean isOk() {
        return ok;
    }

    public String getSelectedRecordId() {
        return selectedRecordId;
    }
}
