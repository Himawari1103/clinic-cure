package view.components.main.dialog;

import constants.Gender;
import controller.main.PatientController;
import model.base.Patient;
import util.Utils;
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
import java.time.LocalDate;

public class DialogPatientChooser extends javax.swing.JDialog {

    private boolean ok = false;

    DefaultTableModel defaultTableModelMain;
    String selectedPatientId = "";
    String selectedPatientFullName = "";

    private JLabel titleLabel;
    private Table patientTable;
    private JScrollPane jScrollPane;
    private JButton confirmButton;
    private JPanel panelHeader;
    private JPanel panelEventHeader;

    public DialogPatientChooser(java.awt.Frame parent, boolean modal) {
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
                patientTable.setModel(defaultTableModelMain);
            }
        });

        setSize(250,500);

        setResizable(false);
        setLayout(new BorderLayout(0,5));

        panelHeader = new JPanel(new BorderLayout());
        titleLabel = new JLabel("title",SwingConstants.CENTER);
        patientTable = new Table();
        jScrollPane= new JScrollPane(patientTable);
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

        patientTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "Họ tên", "Ngày sinh", "Giới tính", "Điện thoại"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        patientTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane.setViewportView(patientTable);
        patientTable.setColumnWidths(new int[]{20, 100, 50, 20, 50});

        add(panelHeader,BorderLayout.NORTH);
        add(jScrollPane,BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void initTable() {
        defaultTableModelMain = (DefaultTableModel) patientTable.getModel();
        patientTable.fixTable(jScrollPane);

        addDataTable();

        ListSelectionModel selectionModel = patientTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = patientTable.getSelectedRow();
                    if (index != -1) {
                        selectedPatientId = (String) patientTable.getValueAt(index, 0);
                        selectedPatientFullName = (String) patientTable.getValueAt(index, 1);
                    }
                }
            }
        });
    }

    public void addDataTable() {
        PatientController.addRowPatientTableDialog(patientTable);
    }

    public boolean isOk() {
        return ok;
    }

    public String getSelectedPatientId() {
        return selectedPatientId;
    }

    public String getSelectedPatientFullName() {
        return selectedPatientFullName;
    }
}
