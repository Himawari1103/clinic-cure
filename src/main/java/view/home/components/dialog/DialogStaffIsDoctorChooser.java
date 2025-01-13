package view.home.components.dialog;

import controller.main.StaffController;
import view.home.components.table.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogStaffIsDoctorChooser extends javax.swing.JDialog {

    private boolean ok = false;

    DefaultTableModel defaultTableModelMain;
    String selectedStaffId = "";
    String selectedStaffFullName = "";

    private JLabel titleLabel;
    private Table staffTable;
    private JScrollPane jScrollPane;
    private JButton confirmButton;
    private JPanel panelHeader;
    private JPanel panelEventHeader;

    public DialogStaffIsDoctorChooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        initTable();
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
                staffTable.setModel(defaultTableModelMain);
            }
        });

        setSize(300,500);

        setResizable(false);
        setLayout(new BorderLayout(0,5));

        panelHeader = new JPanel(new BorderLayout());
        titleLabel = new JLabel("title",SwingConstants.CENTER);
        staffTable = new Table();
        jScrollPane= new JScrollPane(staffTable);
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

        staffTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "Họ tên", "Chuyên khoa", "Điện thoại", "Email"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        staffTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane.setViewportView(staffTable);
        staffTable.setColumnWidths(new int[]{20, 100, 60, 50, 70});

        add(panelHeader,BorderLayout.NORTH);
        add(jScrollPane,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void initTable() {
        defaultTableModelMain = (DefaultTableModel) staffTable.getModel();
        staffTable.fixTable(jScrollPane);

        addDataTable();

        ListSelectionModel selectionModel = staffTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = staffTable.getSelectedRow();
                    if (index != -1) {
                        selectedStaffId = (String) staffTable.getValueAt(index, 0);
                        selectedStaffFullName = (String) staffTable.getValueAt(index, 1);
                    }
                }
            }
        });
    }

    public void addDataTable() {
        StaffController.addRowStaffIsDoctorTable(staffTable);
    }

    public boolean isOk() {
        return ok;
    }

    public String getSelectedStaffId() {
        return selectedStaffId;
    }

    public String getSelectedStaffFullName() {
        return selectedStaffFullName;
    }
}
