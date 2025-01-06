/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import constants.AdminAction;
import constants.Gender;
import controller.main.PatientController;
import model.base.Patient;
import util.Utils;
import view.components.main.components.scrollbar.ScrollBarCustom;
import view.components.main.components.table.Table;
import view.components.main.dialog.Message;
import view.components.main.dialog.MessageResultAdminAction;
import view.frames.MainView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Chi Cute
 */
public class PatientsManagement extends javax.swing.JPanel {
    Patient selectedPatient = null;
    int indexSelectedPatient = -1;
    AdminAction currentAction = null;

    DefaultTableModel defaultTableModelMain;
//    DefaultTableModel defaultTableModelOrder;

    public PatientsManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        initTable();
        defaultTableModelMain = (DefaultTableModel) patientTable.getModel();
//        defaultTableModelOrder =  (DefaultTableModel) patientTable.getModel();
        patientTable.fixTable(jScrollPane1);
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        patientIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        patientFullNameTextField.setEnabled(true);

        date.setEnabled(true);

        setComboBoxCustomDisabled(month, false);

        year.setEnabled(true);

        patientAddressTextField.setEnabled(true);

        setComboBoxCustomDisabled(patientGenderComboBox, false);

        patientNationTextField.setEnabled(true);

        patientOccupationTextField.setEnabled(true);

        patientPhoneNumberTextField.setEnabled(true);

    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        patientIDTextField.setEditable(false);
        patientIDTextField.setDisabledTextColor(Color.BLACK);

        patientFullNameTextField.setEnabled(false);
        patientFullNameTextField.setDisabledTextColor(Color.BLACK);

        date.setEnabled(false);
        date.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(month, true);

        year.setEnabled(false);
        year.setDisabledTextColor(Color.BLACK);

        patientAddressTextField.setEnabled(false);
        patientAddressTextField.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(patientGenderComboBox, true);

        patientNationTextField.setEnabled(false);
        patientNationTextField.setDisabledTextColor(Color.BLACK);

        patientOccupationTextField.setEnabled(false);
        patientOccupationTextField.setDisabledTextColor(Color.BLACK);

        patientPhoneNumberTextField.setEnabled(false);
        patientPhoneNumberTextField.setDisabledTextColor(Color.BLACK);

    }

    public void setText(Patient patient) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        patientIDTextField.setText(patient.getPatientId());

        patientFullNameTextField.setText(patient.getFullName());

        patientAgeTextField.setText(String.valueOf(LocalDate.now().getYear() - patient.getDateOfBirth().getYear()));

        date.setText(String.valueOf(patient.getDateOfBirth().getDayOfMonth()));

        month.setSelectedItem(String.valueOf(patient.getDateOfBirth().getMonth()));

        year.setText(String.valueOf(patient.getDateOfBirth().getYear()));

        patientAddressTextField.setText(patient.getAddress());

        patientGenderComboBox.setSelectedItem(patient.getGender());

        patientNationTextField.setText(patient.getNation());

        patientOccupationTextField.setText(patient.getOccupation());

        patientPhoneNumberTextField.setText(patient.getPhoneNumber());
    }

    public void clearText(){
        patientIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        patientFullNameTextField.setText("");
        date.setText("");
        month.setSelectedItem("1");
        year.setText("");
        patientAddressTextField.setText("");
        patientGenderComboBox.setSelectedItem("NAM");
        patientNationTextField.setText("");
        patientOccupationTextField.setText("");
        patientPhoneNumberTextField.setText("");
    }

    public boolean hasTextFieldEmpty() {
        return patientIDTextField.getText().trim().isEmpty()
                || patientFullNameTextField.getText().trim().isEmpty()
                || date.getText().trim().isEmpty()
                || year.getText().trim().isEmpty()
                || patientAddressTextField.getText().trim().isEmpty()
                || patientNationTextField.getText().trim().isEmpty()
                || patientOccupationTextField.getText().trim().isEmpty()
                || patientPhoneNumberTextField.getText().trim().isEmpty();
    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu
        undoButton.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu
        undoButton.setEnabled(true);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
    }

    public void disableRemainMainButton(JButton jButton) {  // tắt các nút chức năng ngoài nút được nhấn
        if (jButton == addPatientButton) {
            updatePatientButton.setEnabled(false);
            deletePatientButton.setEnabled(false);
        } else if (jButton == updatePatientButton) {
            addPatientButton.setEnabled(false);
            deletePatientButton.setEnabled(false);
        } else if (jButton == deletePatientButton) {
            addPatientButton.setEnabled(false);
            updatePatientButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        addPatientButton.setEnabled(true);
        updatePatientButton.setEnabled(true);
        deletePatientButton.setEnabled(true);
    }

    public Patient getPatientFromTextField() {
        String patientId = patientIDTextField.getText().trim();
        String fullName = patientFullNameTextField.getText().trim();
        LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year.getText().trim()), Integer.parseInt(Objects.requireNonNull(month.getSelectedItem()).toString()), Integer.parseInt(date.getText().trim()));
        String address = patientAddressTextField.getText().trim();
        Gender gender = Gender.getGenderFromDetail(Objects.requireNonNull(patientGenderComboBox.getSelectedItem()).toString());
        String nation = patientNationTextField.getText().trim();
        String occupation = patientOccupationTextField.getText().trim();
        String phoneNumber = patientPhoneNumberTextField.getText().trim();

        Patient patient = new Patient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);
        System.out.println(patient);
        return patient;
    }

    public void setComboBoxCustomDisabled(JComboBox<?> comboBox, boolean disabled) {
        if (disabled) {
            comboBox.setEnabled(false);
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBackground(Color.GRAY);
                    label.setForeground(Color.BLACK);
                    label.setOpaque(true);
                    return label;
                }
            });
        } else {
            comboBox.setEnabled(true);
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                    if (isSelected) label.setBackground(Color.LIGHT_GRAY);
                    label.setOpaque(true);
                    return label;
                }
            });
            comboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                    JComponent popup = (JComponent) comboBox.getUI().getAccessibleChild(comboBox, 0);
                    if (popup instanceof JPopupMenu) {
                        for (Component component : popup.getComponents()) {
                            if (component instanceof JScrollPane scrollPane) {

                                scrollPane.getViewport().setBackground(Color.WHITE);
                                scrollPane.setVerticalScrollBar(new ScrollBarCustom());
                                JPanel p = new JPanel();
                                p.setBackground(Color.WHITE);
                                scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
                }
            });
        }
    }

    public void computeAge() {
        if (!year.getText().trim().isEmpty()) {
            int age = LocalDate.now().getYear() - Integer.parseInt(year.getText().trim());
            if (age > 0) patientAgeTextField.setText(String.valueOf(age));
        }
    }

    public void initTable() {
        addDataTable();

        ListSelectionModel selectionModel = patientTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    indexSelectedPatient = patientTable.getSelectedRow();
                    if (indexSelectedPatient != -1) {
                        String patientId = (String) patientTable.getValueAt(indexSelectedPatient, 0);
                        String fullName = (String) patientTable.getValueAt(indexSelectedPatient, 1);
                        LocalDate dateOfBirth = Utils.stringToLocalDate((String) patientTable.getValueAt(indexSelectedPatient, 3));
                        Gender gender = Gender.getGenderFromDetail((String) patientTable.getValueAt(indexSelectedPatient, 4));
                        String nation = (String) patientTable.getValueAt(indexSelectedPatient, 5);
                        String phoneNumber = (String) patientTable.getValueAt(indexSelectedPatient, 6);
                        String occupation = (String) patientTable.getValueAt(indexSelectedPatient, 7);
                        String address = (String) patientTable.getValueAt(indexSelectedPatient, 8);

                        selectedPatient = new Patient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);

                        setText(selectedPatient);
                    }
                }
            }
        });
    }

    public void addDataTable() {
        PatientController.addRowPatientTable(patientTable);
    }

    ArrayList<Object[]> listRemovedRows = new ArrayList<>();

    public void searchByPatientIdInsert() {
        String subPatientId = searchPatientIDTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if (!((String) defaultTableModel.getValueAt(i, 0)).contains(subPatientId)) {
                listRemovedRows.add(patientTable.getRow(i));
                patientTable.deleteRow(i);
                i--;
            }
        }
    }

    public void searchByPatientIdRemove() {
        String subPatientId = searchPatientIDTextField.getText().trim();
        String subPatientPhoneNumber = searchPatientPhoneNumberTextField.getText().trim();
        String subPatientName = searchPatientNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        if (!listRemovedRows.isEmpty()) {
            for (int i = 0; i < listRemovedRows.size(); i++) {
                if (((String) listRemovedRows.get(i)[0]).contains(subPatientId)
                        && ((String) listRemovedRows.get(i)[6]).contains(subPatientPhoneNumber)
                        && ((String) listRemovedRows.get(i)[1]).contains(subPatientName)) {
                    defaultTableModel.addRow(listRemovedRows.remove(i));
                    i--;
                }
            }
        }
    }

    public void searchByPatientPhoneNumberInsert() {
        String subPatientId = searchPatientPhoneNumberTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if (!((String) defaultTableModel.getValueAt(i, 6)).contains(subPatientId)) {
                listRemovedRows.add(patientTable.getRow(i));
                patientTable.deleteRow(i);
                i--;
            }
        }
    }

    public void searchByPatientPhoneNumberRemove() {
        String subPatientId = searchPatientIDTextField.getText().trim();
        String subPatientPhoneNumber = searchPatientPhoneNumberTextField.getText().trim();
        String subPatientName = searchPatientNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        if (!listRemovedRows.isEmpty()) {
            for (int i = 0; i < listRemovedRows.size(); i++) {
                if (((String) listRemovedRows.get(i)[0]).contains(subPatientId)
                        && ((String) listRemovedRows.get(i)[6]).contains(subPatientPhoneNumber)
                        && ((String) listRemovedRows.get(i)[1]).contains(subPatientName)) {
                    defaultTableModel.addRow(listRemovedRows.remove(i));
                    i--;
                }
            }
        }
    }

    public void searchByNameInsert() {
        String subPatientId = searchPatientNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if (!((String) defaultTableModel.getValueAt(i, 1)).contains(subPatientId)) {
                listRemovedRows.add(patientTable.getRow(i));
                patientTable.deleteRow(i);
                i--;
            }
        }
    }

    public void searchByNameRemove() {
        String subPatientId = searchPatientIDTextField.getText().trim();
        String subPatientPhoneNumber = searchPatientPhoneNumberTextField.getText().trim();
        String subPatientName = searchPatientNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        if (!listRemovedRows.isEmpty()) {
            for (int i = 0; i < listRemovedRows.size(); i++) {
                if (((String) listRemovedRows.get(i)[0]).contains(subPatientId)
                        && ((String) listRemovedRows.get(i)[6]).contains(subPatientPhoneNumber)
                        && ((String) listRemovedRows.get(i)[1]).contains(subPatientName)) {
                    defaultTableModel.addRow(listRemovedRows.remove(i));
                    i--;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        patientIDTextField = new javax.swing.JTextField();
        patientIDTextField.setEditable(false);
        jLabel2 = new javax.swing.JLabel();
        patientFullNameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        patientGenderComboBox = new javax.swing.JComboBox<>();
        patientNationTextField = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        patientOccupationTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        patientAddressTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        patientPhoneNumberTextField = new javax.swing.JTextField();
        patientAgeTextField = new javax.swing.JFormattedTextField();
        patientAgeTextField.setEnabled(false);
        patientAgeTextField.setDisabledTextColor(Color.BLACK);
        jLabel10 = new javax.swing.JLabel();
        addPatientButton = new javax.swing.JButton();
        deletePatientButton = new javax.swing.JButton();
        updatePatientButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();

        year = new javax.swing.JTextField();
        year.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                computeAge();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                computeAge();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                computeAge();
            }
        });

        month = new javax.swing.JComboBox<>();
        month.setBackground(Color.WHITE);
        date = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        patientTable = new Table();
        searchPatientsButton = new javax.swing.JButton();

        clearSearchPatientsButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        searchPatientIDTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        searchPatientNameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        searchPatientPhoneNumberTextField = new javax.swing.JTextField();
        searchPatientIDTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchByPatientIdInsert();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchByPatientIdRemove();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        searchPatientNameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchByNameInsert();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchByNameRemove();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        searchPatientPhoneNumberTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchByPatientPhoneNumberInsert();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchByPatientPhoneNumberRemove();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin bệnh nhân", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID:");

        patientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Họ và tên:");

        patientFullNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientFullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientFullNameTextFieldActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Ngày sinh:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Giới tính:");

        patientGenderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"NAM", "NỮ"}));
        patientGenderComboBox.setBackground(Color.WHITE);
        patientGenderComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientGenderComboBoxActionPerformed(evt);
            }
        });

        patientNationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientNationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientNationTextFieldActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Dân tộc:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nghề nghiệp:");

        patientOccupationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientOccupationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientOccupationTextFieldActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Điện thoại:");

        patientAddressTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientAddressTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientAddressTextFieldActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Địa chỉ:");

        patientPhoneNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientPhoneNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientPhoneNumberTextFieldActionPerformed(evt);
            }
        });

        patientAgeTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tuổi:");

        addPatientButton.setBackground(new java.awt.Color(102, 255, 255));
        addPatientButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addPatientButton.setText("Thêm bệnh nhân");
        addPatientButton.setFocusPainted(false);
        addPatientButton.setFocusable(false);
        addPatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPatientButtonActionPerformed(evt);
            }
        });

        deletePatientButton.setBackground(new java.awt.Color(255, 204, 204));
        deletePatientButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletePatientButton.setText("Xóa bệnh nhân");
        deletePatientButton.setFocusPainted(false);
        deletePatientButton.setFocusable(false);
        deletePatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePatientButtonActionPerformed(evt);
            }
        });

        updatePatientButton.setBackground(new java.awt.Color(153, 255, 51));
        updatePatientButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updatePatientButton.setText("Cập nhật");
        updatePatientButton.setFocusPainted(false);
        updatePatientButton.setFocusable(false);
        updatePatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePatientButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(204, 204, 204));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.setFocusPainted(false);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setBackground(new java.awt.Color(153, 255, 51));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Lưu");
        saveButton.setFocusPainted(false);
        saveButton.setFocusable(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        undoButton.setBackground(new java.awt.Color(153, 255, 51));
        undoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        undoButton.setText("Hoàn tác");
        undoButton.setFocusPainted(false);
        undoButton.setFocusable(false);
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        year.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        date.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(patientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(patientAgeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(patientFullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(patientAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(12, 12, 12)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(patientOccupationTextField)
                                                        .addComponent(patientGenderComboBox, 0, 129, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(patientNationTextField)
                                                        .addComponent(patientPhoneNumberTextField))
                                                .addGap(33, 33, 33))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(addPatientButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deletePatientButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(updatePatientButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(undoButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveButton)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                                .addComponent(createRecordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(patientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(patientFullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(patientGenderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(patientNationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(patientAgeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel6)
                                        .addComponent(patientOccupationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7)
                                        .addComponent(patientPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel9)
                                                .addComponent(patientAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(updatePatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(deletePatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(addPatientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(229, 245, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách bệnh nhân", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        patientTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "Họ tên", "Tuổi", "Ngày sinh", "Giới tính", "Điện thoại", "Dân tộc", "Nghề nghiệp", "Địa chỉ"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        patientTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(patientTable);
        patientTable.setColumnWidths(new int[]{20, 100, 20, 100, 100, 100, 100, 100});

        searchPatientsButton.setBackground(new java.awt.Color(102, 255, 255));
        searchPatientsButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchPatientsButton.setText("Tìm kiếm");
        searchPatientsButton.setFocusPainted(false);
        searchPatientsButton.setFocusable(false);
        searchPatientsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPatientsButtonActionPerformed(evt);
            }
        });
        clearSearchPatientsButton.setBackground(new java.awt.Color(102, 255, 255));
        clearSearchPatientsButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearSearchPatientsButton.setText("Xóa");
        clearSearchPatientsButton.setFocusPainted(false);
        clearSearchPatientsButton.setFocusable(false);
        clearSearchPatientsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSearchPatientsButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("ID bệnh nhân:");

        searchPatientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setText("Tên bệnh nhân:");

        searchPatientNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchPatientNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPatientNameTextFieldActionPerformed(evt);
            }
        });

        jLabel13.setText("Điện thoại:");

        searchPatientPhoneNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchPatientPhoneNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPatientPhoneNumberTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchPatientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchPatientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchPatientPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clearSearchPatientsButton)
//                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchPatientsButton)
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(searchPatientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12)
                                        .addComponent(searchPatientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)
                                        .addComponent(searchPatientPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(clearSearchPatientsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchPatientsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void patientGenderComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientGenderComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientGenderComboBoxActionPerformed

    private void patientFullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientFullNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientFullNameTextFieldActionPerformed

    private void patientOccupationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientOccupationTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientOccupationTextFieldActionPerformed

    private void patientAddressTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientAddressTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientAddressTextFieldActionPerformed

    private void patientPhoneNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientPhoneNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientPhoneNumberTextFieldActionPerformed

    private void addPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentAction != AdminAction.ADD) clearText();
        currentAction = AdminAction.ADD;
        disableRemainMainButton(addPatientButton);
        enableSupportButton();

        patientIDTextField.setText(Utils.genUUID().toString());
        enableEditingText();
    }

    private void updatePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePatientButtonActionPerformed
        currentAction = AdminAction.UPDATE;
        disableRemainMainButton(updatePatientButton);
        enableSupportButton();
        enableEditingTextWithOutId();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        disableEditingText();

        if (selectedPatient != null) {
            setText(selectedPatient);
        } else {
            clearText();
        }

        currentAction = null;
    }


    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        boolean rs = false;
        if (!hasTextFieldEmpty()) {
            switch (currentAction) {
                case ADD -> {
                    rs = PatientController.addPatient(getPatientFromTextField(), patientTable);
                }
                case UPDATE -> {
                    rs = PatientController.updatePatient(indexSelectedPatient, getPatientFromTextField(), patientTable);
                }
            }
            enableMainButton();
            disableSupportButton();
            disableEditingText();
            MessageResultAdminAction messageResultAdminAction = new MessageResultAdminAction(MainView.getFrames()[0], true);
            if (rs) {
                messageResultAdminAction.showMessageSuccess(currentAction);
            } else {
                messageResultAdminAction.showMessageFail(currentAction);
            }
            currentAction = null;
        } else {
            Message ms = new Message(MainView.getFrames()[0], true);
            ms.showMessage("Hãy nhập đầy đủ thông tin", false);
        }

    }

    private void deletePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePatientButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedPatient == null) {
            ms = "Không có đối tượng để xóa";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa " + selectedPatient.getFullName() + " không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            PatientController.deletePatient(indexSelectedPatient, selectedPatient, patientTable);
            indexSelectedPatient = -1;
            selectedPatient = null;
        }

        currentAction = null;
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (selectedPatient != null) {
            setText(selectedPatient);
        } else {
            if(currentAction == AdminAction.ADD){
                clearTextWithoutId();
            } else {
                clearText();
            }

        }
    }

    private void patientNationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientNationTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientNationTextFieldActionPerformed

    private void searchPatientNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPatientNameTextFieldActionPerformed

    private void searchPatientPhoneNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientPhoneNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPatientPhoneNumberTextFieldActionPerformed

    private void searchPatientsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientsButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPatientsButtonActionPerformed

    private void clearSearchPatientsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientsButtonActionPerformed
        clearTextSearch();
    }

    public void clearTextSearch() {
        searchPatientIDTextField.setText("");
        searchPatientPhoneNumberTextField.setText("");
        searchPatientNameTextField.setText("");
    }

    private void createRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRecordButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_createRecordButtonActionPerformed

    private void dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPatientButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField date;
    private javax.swing.JButton deletePatientButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JTextField patientAddressTextField;
    private javax.swing.JFormattedTextField patientAgeTextField;
    private javax.swing.JTextField patientFullNameTextField;
    private javax.swing.JComboBox<String> patientGenderComboBox;
    private javax.swing.JTextField patientIDTextField;
    private javax.swing.JFormattedTextField patientNationTextField;
    private javax.swing.JTextField patientOccupationTextField;
    private javax.swing.JTextField patientPhoneNumberTextField;
    private Table patientTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JTextField searchPatientIDTextField;
    private javax.swing.JTextField searchPatientNameTextField;
    private javax.swing.JTextField searchPatientPhoneNumberTextField;
    private javax.swing.JButton searchPatientsButton;
    private javax.swing.JButton clearSearchPatientsButton;
    private javax.swing.JButton updatePatientButton;
    private javax.swing.JTextField year;
    // End of variables declaration//GEN-END:variables
}
