package view.home.main.panels;

import constants.AdminAction;
import constants.Gender;
import controller.main.PatientController;
import util.Utils;
import view.home.components.Button;
import view.home.components.table.Table;
import view.home.components.dialog.Message;
import view.home.components.dialog.MessageResultAdminAction;
import view.home.frames.MainView;
import view.home.main.model.ModelPatient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PatientsManagement extends javax.swing.JPanel {
    ModelPatient selectedModelPatient = null;
    int indexSelectedModelPatient = -1;
    AdminAction currentAction = null;

    HashMap<String, ModelPatient> mapModelPatient = new HashMap<>();
    TableRowSorter<DefaultTableModel> rowSorter;

    boolean isSelectionChanged = false;

    public PatientsManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        initTable();

        setTextDate(LocalDate.now());
    }

    public void init() {
        patientIDTextField.setBackground(new Color(220, 223, 228));
        addEventFilter();
    }

    public void addDataTable() {
        PatientController.addRowPatientTable(patientTable, mapModelPatient);
    }

    public void initTable() {
        addDataTable();

        DefaultTableModel defaultTableModel = (DefaultTableModel) patientTable.getModel();
        patientTable.fixTable(jScrollPane1);
        rowSorter = new TableRowSorter<>(defaultTableModel);
        patientTable.setRowSorter(rowSorter);

        ListSelectionModel selectionModel = patientTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isSelectionChanged = true;

                if (currentAction == AdminAction.SEARCH) return;

                if (!e.getValueIsAdjusting()) {
                    int indexViewRow = patientTable.getSelectedRow();

                    if (indexViewRow != -1) {
                        indexSelectedModelPatient = patientTable.convertRowIndexToModel(indexViewRow);

                        String patientId = (String) patientTable.getValueAt(indexViewRow, 0);
                        String fullName = (String) patientTable.getValueAt(indexViewRow, 1);
                        LocalDate dateOfBirth = Utils.stringToLocalDate((String) patientTable.getValueAt(indexViewRow, 3));
                        Gender gender = Gender.getGenderFromDetail((String) patientTable.getValueAt(indexViewRow, 4));
                        String phoneNumber = (String) patientTable.getValueAt(indexViewRow, 5);
                        String nation = (String) patientTable.getValueAt(indexViewRow, 6);
                        String occupation = (String) patientTable.getValueAt(indexViewRow, 7);
                        String address = (String) patientTable.getValueAt(indexViewRow, 8);

                        selectedModelPatient = new ModelPatient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);

                        setText(selectedModelPatient);
                    }
                }
            }
        });

        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isSelectionChanged) {
                    int row = patientTable.rowAtPoint(e.getPoint());

                    if (row != -1 && patientTable.convertRowIndexToModel(row) == indexSelectedModelPatient) {
                        patientTable.clearSelection();
                    }
                } else {
                    isSelectionChanged = false;
                }
            }
        });
    }

    public void filterTable() {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String patientId = searchPatientIDTextField.getText().trim();
        String fullName = searchPatientNameTextField.getText().trim();
        String phoneNumber = searchPatientPhoneNumberTextField.getText().trim();

        if (!patientId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + patientId + ".*", 0));
        }
        if (!fullName.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + fullName + ".*", 1));
        }
        if (!phoneNumber.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + phoneNumber + ".*", 5));
        }

        SwingUtilities.invokeLater(() -> {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        });
    }

    public void addEventFilter() {
        JTextField[] listDataComponents = {searchPatientIDTextField, searchPatientNameTextField, searchPatientPhoneNumberTextField};

        for (int i = 0; i < listDataComponents.length; i++) {
            listDataComponents[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterTable();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterTable();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterTable();
                }
            });
        }
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        patientIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        patientFullNameTextField.setEnabled(true);
        date.setEnabled(true);
        Utils.setComboBoxCustomDisabled(month, false);
        year.setEnabled(true);
        patientAddressTextField.setEnabled(true);
        Utils.setComboBoxCustomDisabled(patientGenderComboBox, false);
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

        Utils.setComboBoxCustomDisabled(month, true);

        year.setEnabled(false);
        year.setDisabledTextColor(Color.BLACK);

        patientAddressTextField.setEnabled(false);
        patientAddressTextField.setDisabledTextColor(Color.BLACK);

        Utils.setComboBoxCustomDisabled(patientGenderComboBox, true);

        patientNationTextField.setEnabled(false);
        patientNationTextField.setDisabledTextColor(Color.BLACK);

        patientOccupationTextField.setEnabled(false);
        patientOccupationTextField.setDisabledTextColor(Color.BLACK);

        patientPhoneNumberTextField.setEnabled(false);
        patientPhoneNumberTextField.setDisabledTextColor(Color.BLACK);

    }

    public void setText(ModelPatient modelPatient) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        patientIDTextField.setText(modelPatient.getPatientId());
        patientFullNameTextField.setText(modelPatient.getFullName());
        patientAgeTextField.setText(String.valueOf(modelPatient.getAge()));
        date.setText(String.valueOf(modelPatient.getDateOfBirth().getDayOfMonth()));
        month.setSelectedItem(String.valueOf(modelPatient.getDateOfBirth().getMonth()));
        year.setText(String.valueOf(modelPatient.getDateOfBirth().getYear()));
        patientAddressTextField.setText(modelPatient.getAddress());
        patientGenderComboBox.setSelectedItem(modelPatient.getGender().getDetail());
        patientNationTextField.setText(modelPatient.getNation());
        patientOccupationTextField.setText(modelPatient.getOccupation());
        patientPhoneNumberTextField.setText(modelPatient.getPhoneNumber());
    }

    public void clearText() {
        patientIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        patientFullNameTextField.setText("");
        date.setText("");
        month.setSelectedIndex(0);
        year.setText("");
        patientAddressTextField.setText("");
        patientGenderComboBox.setSelectedItem("NAM");
        patientNationTextField.setText("");
        patientOccupationTextField.setText("");
        patientPhoneNumberTextField.setText("");
    }

    public boolean hasTextFieldEmpty() {
        return checkDateEmpty()
                || patientIDTextField.getText().trim().isEmpty()
                || patientFullNameTextField.getText().trim().isEmpty()
                || patientAddressTextField.getText().trim().isEmpty()
                || patientNationTextField.getText().trim().isEmpty()
                || patientOccupationTextField.getText().trim().isEmpty()
                || patientPhoneNumberTextField.getText().trim().isEmpty();
    }

    public boolean checkDateEmpty() {
        return date.getText().trim().isEmpty()
                || month.getSelectedIndex() == 0
                || year.getText().trim().isEmpty();
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

    public void disableRemainMainButton(Button button) {  // tắt các nút chức năng ngoài nút được nhấn
        if (button == addPatientButton) {
            updatePatientButton.setEnabled(false);
            deletePatientButton.setEnabled(false);
        } else if (button == updatePatientButton) {
            addPatientButton.setEnabled(false);
            deletePatientButton.setEnabled(false);
        } else if (button == deletePatientButton) {
            addPatientButton.setEnabled(false);
            updatePatientButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        addPatientButton.setEnabled(true);
        updatePatientButton.setEnabled(true);
        deletePatientButton.setEnabled(true);
    }

    public void setTextDate(LocalDate lcDate) {
        month.setSelectedItem(String.valueOf(lcDate.getMonthValue()));
        date.setText(String.valueOf(lcDate.getDayOfMonth()));
        year.setText(String.valueOf(lcDate.getYear()));
    }

    public ModelPatient getModelPatientFromTextField() {
        String patientId = patientIDTextField.getText().trim();
        String fullName = patientFullNameTextField.getText().trim();
        LocalDate dateOfBirth = LocalDate.of(Integer.parseInt(year.getText().trim()), Integer.parseInt(Objects.requireNonNull(month.getSelectedItem()).toString()), Integer.parseInt(date.getText().trim()));
        String address = patientAddressTextField.getText().trim();
        Gender gender = Gender.getGenderFromDetail(Objects.requireNonNull(patientGenderComboBox.getSelectedItem()).toString());
        String nation = patientNationTextField.getText().trim();
        String occupation = patientOccupationTextField.getText().trim();
        String phoneNumber = patientPhoneNumberTextField.getText().trim();

        return new ModelPatient(patientId, fullName, dateOfBirth, gender, phoneNumber, nation, occupation, address);
    }

    public void computeAge() {
        if (!year.getText().trim().isEmpty()) {
            int age = LocalDate.now().getYear() - Integer.parseInt(year.getText().trim());
            if (age > 0) patientAgeTextField.setText(String.valueOf(age));
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
        addPatientButton = new Button();
        deletePatientButton = new Button();
        updatePatientButton = new Button();
        cancelButton = new Button();
        saveButton = new Button();
        undoButton = new Button();

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
        searchPatientsButton = new Button();

        clearSearchPatientsButton = new Button();
        jLabel11 = new javax.swing.JLabel();
        searchPatientIDTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        searchPatientNameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        searchPatientPhoneNumberTextField = new javax.swing.JTextField();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin bệnh nhân", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID:");

        patientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Họ và tên:");

        patientFullNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Ngày sinh:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Giới tính:");

        patientGenderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--Chọn--", "NAM", "NỮ"}));
        patientGenderComboBox.setBackground(Color.WHITE);

        patientNationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Dân tộc:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nghề nghiệp:");

        patientOccupationTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Điện thoại:");

        patientAddressTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Địa chỉ:");

        patientPhoneNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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

        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        date.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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
                ModelPatient.getColumnNames()
        ) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
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

        jLabel13.setText("Điện thoại:");

        searchPatientPhoneNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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

    private void addPatientButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentAction != AdminAction.ADD) {
            clearText();
            currentAction = AdminAction.ADD;
            disableRemainMainButton(addPatientButton);
            enableSupportButton();

            patientIDTextField.setText(Utils.genUUID().toString());
            enableEditingTextWithOutId();

            setTextDate(LocalDate.now());
        }
    }

    private void updatePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePatientButtonActionPerformed
        if (currentAction != AdminAction.UPDATE) {
            if (selectedModelPatient == null) {
                Message obj = new Message(MainView.getFrames()[0], true);
                String ms = "Hãy chọn một bệnh nhân để cập nhật!";
                obj.showMessage(ms, false);
            } else {
                currentAction = AdminAction.UPDATE;
                disableRemainMainButton(updatePatientButton);
                enableSupportButton();
                enableEditingTextWithOutId();
            }
        }
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (currentAction == AdminAction.ADD) {
            clearTextWithoutId();
            setTextDate(LocalDate.now());
        } else if (currentAction == AdminAction.UPDATE) {
            if (selectedModelPatient != null) {
                setText(selectedModelPatient);
            }
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        disableEditingText();

        if (selectedModelPatient != null) {
            setText(selectedModelPatient);
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
                    ModelPatient modelPatient = getModelPatientFromTextField();
                    rs = PatientController.addPatient(modelPatient, patientTable);
                    if (rs) {
                        mapModelPatient.put(modelPatient.getPatientId(), modelPatient);
                    }
                }
                case UPDATE -> {
                    ModelPatient modelPatient = getModelPatientFromTextField();
                    rs = PatientController.updatePatient(indexSelectedModelPatient, modelPatient, patientTable);
                    if (rs) {
                        mapModelPatient.replace(modelPatient.getPatientId(), modelPatient);
                    }
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
            ms.showMessage("Hãy nhập đầy đủ thông tin!", false);
        }
    }

    private void deletePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePatientButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedModelPatient == null) {
            ms = "Hãy chọn một bệnh nhân để xóa!";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa " + selectedModelPatient.getFullName() + " không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            boolean rs = PatientController.deletePatient(indexSelectedModelPatient, selectedModelPatient, patientTable);
            if (rs) {
                mapModelPatient.remove(selectedModelPatient.getPatientId());
            }
            indexSelectedModelPatient = -1;
            selectedModelPatient = null;
        }

        currentAction = null;
    }

    private void clearSearchPatientsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientsButtonActionPerformed
        clearTextSearch();
    }

    public void clearTextSearch() {
        searchPatientIDTextField.setText("");
        searchPatientPhoneNumberTextField.setText("");
        searchPatientNameTextField.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button addPatientButton;
    private Button cancelButton;
    private JTextField date;
    private Button deletePatientButton;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JComboBox<String> month;
    private JTextField patientAddressTextField;
    private JFormattedTextField patientAgeTextField;
    private JTextField patientFullNameTextField;
    private JComboBox<String> patientGenderComboBox;
    private JTextField patientIDTextField;
    private JFormattedTextField patientNationTextField;
    private JTextField patientOccupationTextField;
    private JTextField patientPhoneNumberTextField;
    private Table patientTable;
    private Button saveButton;
    private Button undoButton;
    private JTextField searchPatientIDTextField;
    private JTextField searchPatientNameTextField;
    private JTextField searchPatientPhoneNumberTextField;
    private Button searchPatientsButton;
    private Button clearSearchPatientsButton;
    private Button updatePatientButton;
    private JTextField year;
    // End of variables declaration//GEN-END:variables
}
