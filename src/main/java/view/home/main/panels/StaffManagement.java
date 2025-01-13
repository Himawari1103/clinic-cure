package view.home.main.panels;

import constants.AdminAction;
import constants.StaffRole;
import constants.StaffSpeciality;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.main.StaffController;
import model.base.Staff;
import util.Utils;
import view.home.components.Button;
import view.home.components.scrollbar.ScrollBarCustom;
import view.home.components.dialog.Message;
import view.home.components.dialog.MessageResultAdminAction;
import view.home.frames.MainView;
import view.home.components.table.Table;
import view.home.main.model.ModelPatient;

import static util.Utils.setComboBoxCustomDisabled;
import static util.Utils.setModelComboBox;

/**
 * @author Chi Cute
 */
public class StaffManagement extends javax.swing.JPanel {

    Staff selectedStaff = null;
    int indexSelectedStaff = -1;
    AdminAction currentAction = null;

    HashMap<String, Staff> mapStaff = new HashMap<>();
    TableRowSorter<DefaultTableModel> rowSorter;

    boolean isSelectionChanged = false;

    public StaffManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        initTable();
    }

    private void init(){
        setComboBoxCustomDisabled(searchStaffComboBox,false);
        setComboBoxCustomDisabled(searchSpecialityComboBox,false);
        staffIDTextField.setBackground(new Color(220, 223, 228));

        addEventFilter();
    }

    public void addDataTable() {
        StaffController.addRowStaffTable(staffTable, mapStaff);
    }

    public void initTable() {
        addDataTable();

        DefaultTableModel defaultTableModel = (DefaultTableModel) staffTable.getModel();
        staffTable.fixTable(jScrollPane1);
        rowSorter = new TableRowSorter<>(defaultTableModel);
        staffTable.setRowSorter(rowSorter);

        ListSelectionModel selectionModel = staffTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    indexSelectedStaff = staffTable.getSelectedRow();
                    if (indexSelectedStaff != -1) {
                        String staffId = (String) staffTable.getValueAt(indexSelectedStaff, 0);
                        String fullName = (String) staffTable.getValueAt(indexSelectedStaff, 1);
                        StaffRole role = StaffRole.getStaffRoleFromDetail((String) staffTable.getValueAt(indexSelectedStaff, 2));
                        String phoneNumber = (String) staffTable.getValueAt(indexSelectedStaff, 4);
                        String email = (String) staffTable.getValueAt(indexSelectedStaff, 5);

                        if (role.equals(StaffRole.DOCTOR)) {
                            StaffSpeciality speciality = StaffSpeciality.getStaffSpecialityFromDetail((String) staffTable.getValueAt(indexSelectedStaff, 3));
                            selectedStaff = new Staff(staffId, fullName, phoneNumber, email, role, speciality);
                        } else {
                            selectedStaff = new Staff(staffId, fullName, phoneNumber, email, role);
                        }

                        setText(selectedStaff);
                    }
                }
            }
        });

        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isSelectionChanged) {
                    int row = staffTable.rowAtPoint(e.getPoint());

                    if (row != -1 && staffTable.convertRowIndexToModel(row) == indexSelectedStaff) {
                        staffTable.clearSelection();
                    }
                } else {
                    isSelectionChanged = false;
                }
            }
        });
    }


    public void filterTable() {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String subStaffId = searchStaffIDTextField.getText().trim();
        String subStaffName = searchStaffNameTextField.getText().trim();
        String roleString;
        if (searchStaffComboBox.getSelectedIndex() == 0) {
            roleString = "";
        } else {
            roleString = (String) searchStaffComboBox.getSelectedItem();
        }
        String specialityString;
        if (searchSpecialityComboBox.getSelectedIndex() == 0) {
            specialityString = "";
        } else {
            specialityString = (String) searchSpecialityComboBox.getSelectedItem();
        }

        if (!subStaffId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + subStaffId + ".*", 0));
        }
        if (!subStaffName.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + subStaffName + ".*", 1));
        }
        if (!roleString.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + roleString + ".*", 2));
        }
        if (!specialityString.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + specialityString + ".*", 3));
        }

        SwingUtilities.invokeLater(() -> {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        });
    }



    public void addEventFilter(){
        searchStaffIDTextField.getDocument().addDocumentListener(new DocumentListener() {
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
        searchStaffNameTextField.getDocument().addDocumentListener(new DocumentListener() {
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
        searchStaffComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                filterTable();
            }
        });
        searchSpecialityComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                filterTable();
            }
        });
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        staffIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        staffFullNameTextField.setEnabled(true);

        setComboBoxCustomDisabled(roleComboBox, false);
        roleComboBox.setBackground(Color.WHITE);

        staffNumberTextField.setEnabled(true);

        emailTextField.setEnabled(true);

        setComboBoxCustomDisabled(specialityComboBox, false);
        specialityComboBox.setBackground(Color.WHITE);


    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        staffIDTextField.setEditable(false);
        staffIDTextField.setDisabledTextColor(Color.BLACK);

        staffFullNameTextField.setEnabled(false);
        staffFullNameTextField.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(roleComboBox, true);

        staffNumberTextField.setEnabled(false);
        staffNumberTextField.setDisabledTextColor(Color.BLACK);

        emailTextField.setEnabled(false);
        emailTextField.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(specialityComboBox, true);

    }

    public void setText(Staff staff) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        staffIDTextField.setText(staff.getStaffId());
        staffFullNameTextField.setText(staff.getFullName());
        staffNumberTextField.setText(staff.getPhoneNumber());
        emailTextField.setText(staff.getEmail());
        roleComboBox.setSelectedItem(staff.getRole().getDetail());
        specialityComboBox.setSelectedItem(selectedStaff.getSpeciality().getDetail());
//        if (roleComboBox.getSelectedItem().equals(StaffRole.DOCTOR)) {
//            if (selectedStaff.getSpeciality().equals(StaffSpeciality.CARDIOLOGY)) {
//
//                specialityComboBox.setSelectedIndex(1);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.DERMATOLOGY)) {
//                specialityComboBox.setSelectedIndex(3);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.ENDOCRINOLOGY)) {
//                specialityComboBox.setSelectedIndex(6);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.HEMATOLOGY)) {
//                specialityComboBox.setSelectedIndex(7);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.NEUROLOGY)) {
//                specialityComboBox.setSelectedIndex(2);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.OPHTHALMOLOGY)) {
//                specialityComboBox.setSelectedIndex(4);
//            } else if (selectedStaff.getSpeciality().equals(StaffSpeciality.OTOLARYNGOLOGY)) {
//                specialityComboBox.setSelectedIndex(5);
//            }
//        } else {
//            specialityComboBox.setSelectedIndex(0);
//        }
    }

    public void clearText() {   // xóa hết giá trị của textField
        staffIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        staffFullNameTextField.setText("");
        roleComboBox.setSelectedIndex(0);
        specialityComboBox.setSelectedIndex(0);
        staffNumberTextField.setText("");
        emailTextField.setText("");
    }

    public boolean hasTextFieldEmpty() {
        return staffIDTextField.getText().trim().isEmpty()
                || staffFullNameTextField.getText().trim().isEmpty()
                || roleComboBox.getSelectedIndex() == 0
                || specialityComboBox.getSelectedIndex() == 0
                || staffNumberTextField.getText().trim().isEmpty()
                || emailTextField.getText().trim().isEmpty();
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
        if (button == addButton) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (button == updateButton) {
            addButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (button == deleteButton) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        addButton.setEnabled(true);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    public Staff getStaffFromTextField() {
        String staffId = staffIDTextField.getText().trim();
        String fullName = staffFullNameTextField.getText().trim();
        StaffRole role = StaffRole.getStaffRoleFromDetail(Objects.requireNonNull(roleComboBox.getSelectedItem()).toString());
        StaffSpeciality speciality = StaffSpeciality.getStaffSpecialityFromDetail(Objects.requireNonNull(specialityComboBox.getSelectedItem()).toString());
        String email = emailTextField.getText().trim();
        String phoneNumber = staffNumberTextField.getText().trim();

        return new Staff(staffId, fullName, phoneNumber, email, role, speciality);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        staffIDTextField = new JTextField();
        jLabel2 = new JLabel();
        staffFullNameTextField = new JTextField();
        jLabel4 = new JLabel();
        roleComboBox = new JComboBox<>();
        jLabel5 = new JLabel();
        jLabel7 = new JLabel();
        staffNumberTextField = new JTextField();
        addButton = new Button();
        deleteButton = new Button();
        updateButton = new Button();
        cancelButton = new Button();
        specialityComboBox = new JComboBox<>();
        saveButton = new Button();
        undoButton = new Button();
        jLabel3 = new JLabel();
        emailTextField = new JTextField();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        staffTable = new Table();
        searchButton = new Button();
        jLabel11 = new JLabel();
        searchStaffIDTextField = new JTextField();
        jLabel12 = new JLabel();
        searchStaffNameTextField = new JTextField();
        jLabel13 = new JLabel();
        jLabel14 = new JLabel();
        searchStaffComboBox = new JComboBox<>();
        searchSpecialityComboBox = new JComboBox<>();
        clearButton = new Button();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID:");

        staffIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Họ tên:");

        staffFullNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Vị trí:");

        setModelComboBox(roleComboBox,StaffRole.class);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Chuyên khoa:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Điện thoại:");

        staffNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        addButton.setBackground(new java.awt.Color(102, 255, 255));
        addButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addButton.setText("Thêm");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(102, 255, 255));
        deleteButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteButton.setText("Xóa");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new java.awt.Color(102, 255, 255));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Cập nhật");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(255, 204, 204));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        setModelComboBox(specialityComboBox,StaffSpeciality.class);

        saveButton.setBackground(new java.awt.Color(153, 255, 51));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Lưu");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        undoButton.setBackground(new java.awt.Color(204, 204, 204));
        undoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        undoButton.setText("Hoàn tác");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Email:");

        emailTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(roleComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(staffNumberTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(staffIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(staffFullNameTextField))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(specialityComboBox, 0, 225, Short.MAX_VALUE)))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(emailTextField)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(staffIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(staffFullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(specialityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(12, 12, 12)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel7)
                                                        .addComponent(staffNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(14, 14, 14))
        );

        jPanel2.setBackground(new java.awt.Color(229, 245, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách nhân viên", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        staffTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "Họ tên", "Vị trí", "Chuyên khoa", "Điện thoại", "Email"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        staffTable.setFocusable(false);
        staffTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(staffTable);

        searchButton.setBackground(new java.awt.Color(102, 255, 255));
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchButton.setText("Tìm kiếm");

        jLabel11.setText("ID:");

        searchStaffIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setText("Tên:");

        searchStaffNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel13.setText("Vị trí:");

        jLabel14.setText("Chuyên khoa:");

        setModelComboBox(searchStaffComboBox,StaffRole.class);
        setModelComboBox(searchSpecialityComboBox,StaffSpeciality.class);

        clearButton.setBackground(new java.awt.Color(102, 255, 255));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearButton.setText("Xóa");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
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
                                .addComponent(searchStaffIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchStaffNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchStaffComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchSpecialityComboBox, 0, 1, Short.MAX_VALUE)
                                .addGap(12, 12, 12)
                                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchButton)
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(searchStaffIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12)
                                        .addComponent(searchStaffNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)
                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(searchStaffComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchSpecialityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE))
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

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (currentAction != AdminAction.ADD) {
            clearText();
            currentAction = AdminAction.ADD;
            disableRemainMainButton(addButton);
            enableSupportButton();

            staffIDTextField.setText(Utils.genUUID().toString());

            enableEditingText();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        if (currentAction != AdminAction.UPDATE) {
            if (selectedStaff == null) {
                Message obj = new Message(MainView.getFrames()[0], true);
                String ms = "Hãy chọn một nhân viên để cập nhật!";
                obj.showMessage(ms, false);
            } else {
                currentAction = AdminAction.UPDATE;
                disableRemainMainButton(updateButton);
                enableSupportButton();
                enableEditingTextWithOutId();
            }
        }
    }


    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (currentAction == AdminAction.ADD) {
            clearTextWithoutId();
        } else if (currentAction == AdminAction.UPDATE) {
            if (selectedStaff != null) {
                setText(selectedStaff);
            }
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        disableSupportButton();
        enableMainButton();
        disableEditingText();

        if (selectedStaff != null) {
            setText(selectedStaff);
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
                    Staff staff = getStaffFromTextField();
                    rs = StaffController.addStaff(staff, staffTable);
                    if (rs) {
                        mapStaff.put(staff.getStaffId(), staff);
                    }
                }
                case UPDATE -> {
                    Staff staff = getStaffFromTextField();
                    rs = StaffController.updateStaff(indexSelectedStaff, staff, staffTable);
                    if (rs) {
                        mapStaff.replace(staff.getStaffId(), staff);
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
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedStaff == null) {
            ms = "Hãy chọn một nhân viên để xóa!";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa" + selectedStaff.getFullName() + " không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            boolean rs = StaffController.deleteStaff(indexSelectedStaff,selectedStaff,staffTable);
            if (rs) {
                mapStaff.remove(selectedStaff.getStaffId());
            }
            indexSelectedStaff = -1;
            selectedStaff = null;
        }

        currentAction = null;
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        searchStaffIDTextField.setText("");
        searchStaffNameTextField.setText("");
        searchStaffComboBox.setSelectedIndex(0);
        searchSpecialityComboBox.setSelectedIndex(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button addButton;
    private Button cancelButton;
    private Button clearButton;
    private Button deleteButton;
    private JTextField emailTextField;
    private JLabel jLabel1;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JComboBox<String> roleComboBox;
    private Button saveButton;
    private Button searchButton;
    private JComboBox<String> searchSpecialityComboBox;
    private JComboBox<String> searchStaffComboBox;
    private JTextField searchStaffIDTextField;
    private JTextField searchStaffNameTextField;
    private JComboBox<String> specialityComboBox;
    private JTextField staffFullNameTextField;
    private JTextField staffIDTextField;
    private JTextField staffNumberTextField;
    private Table staffTable;
    private Button undoButton;
    private Button updateButton;
    // End of variables declaration//GEN-END:variables
}
