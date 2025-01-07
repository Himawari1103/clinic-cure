/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import constants.AdminAction;
import constants.StaffRole;
import constants.StaffSpeciality;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controller.main.PatientController;
import controller.main.StaffController;
import model.base.Staff;
import util.Utils;
import view.components.main.components.scrollbar.ScrollBarCustom;
import view.components.main.dialog.Message;
import view.components.main.dialog.MessageResultAdminAction;
import view.frames.MainView;
import view.components.main.components.table.Table;

/**
 * @author Chi Cute
 */
public class StaffManagement extends javax.swing.JPanel {

    Staff selectedStaff = null;
    int indexSelectedStaff = -1;
    AdminAction currentAction = null;

    DefaultTableModel defaultTableModelMain;

    public StaffManagement() {
        initComponents();
        init();
        disableEditingText();
        disableSupportButton();

        initTable();
        defaultTableModelMain = (DefaultTableModel) staffTable.getModel();
        staffTable.fixTable(jScrollPane1);
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
        roleComboBox.repaint();
        specialityComboBox.setSelectedItem(selectedStaff.getSpeciality().getDetail());
        specialityComboBox.repaint();
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
        staffNumberTextField.setText("");
        emailTextField.setText("");
        specialityComboBox.setSelectedIndex(0);
    }

    public boolean hasTextFieldEmpty() {
        return staffIDTextField.getText().trim().isEmpty()
                || staffFullNameTextField.getText().trim().isEmpty()
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

    public void disableRemainMainButton(JButton jButton) {  // tắt các nút chức năng ngoài nút được nhấn
        if (jButton == addButton) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == updateButton) {
            addButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == deleteButton) {
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
                    if (isSelected) {
                        label.setBackground(Color.LIGHT_GRAY);
                    }
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

    public void initTable() {
        addDataTable();

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
    }

    public void addDataTable() {
        StaffController.addRowStaffTable(staffTable);
    }

    ArrayList<Object[]> listRemovedRows = new ArrayList<>();

    public void searchByStaffIdInsert() {
        String subPatientId = searchStaffIDTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) staffTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if (!((String) defaultTableModel.getValueAt(i, 0)).contains(subPatientId)) {
                listRemovedRows.add(staffTable.getRow(i));
                staffTable.deleteRow(i);
                i--;
            }
        }
    }

    public void searchByStaffIdRemove() {
        String subPatientId = searchStaffIDTextField.getText().trim();
        String subPatientName = searchStaffNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) staffTable.getModel();
        if (!listRemovedRows.isEmpty()) {
            for (int i = 0; i < listRemovedRows.size(); i++) {
                if (((String) listRemovedRows.get(i)[0]).contains(subPatientId)
                        && ((String) listRemovedRows.get(i)[1]).contains(subPatientName)) {
                    defaultTableModel.addRow(listRemovedRows.remove(i));
                    i--;
                }
            }
        }
    }

    public void searchByNameInsert() {
        String subPatientId = searchStaffNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) staffTable.getModel();
        for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
            if (!((String) defaultTableModel.getValueAt(i, 1)).contains(subPatientId)) {
                listRemovedRows.add(staffTable.getRow(i));
                staffTable.deleteRow(i);
                i--;
            }
        }
    }

    public void searchByNameRemove() {
        String subPatientId = searchStaffIDTextField.getText().trim();
        String subPatientName = searchStaffNameTextField.getText().trim();
        DefaultTableModel defaultTableModel = (DefaultTableModel) staffTable.getModel();
        if (!listRemovedRows.isEmpty()) {
            for (int i = 0; i < listRemovedRows.size(); i++) {
                if (((String) listRemovedRows.get(i)[0]).contains(subPatientId)
                        && ((String) listRemovedRows.get(i)[1]).contains(subPatientName)) {
                    defaultTableModel.addRow(listRemovedRows.remove(i));
                    i--;
                }
            }
        }
    }

    public void filter() {
        System.out.println("debug!out " + searchSpecialityComboBox.getSelectedIndex());
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

        String[] columnNames = new String[defaultTableModelMain.getColumnCount()];
        for (int j = 0; j < defaultTableModelMain.getColumnCount(); j++) {
            columnNames[j] = defaultTableModelMain.getColumnName(j);
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(columnNames,0);
        for (int i = 0; i < defaultTableModelMain.getRowCount(); i++) {
            if (((String) defaultTableModelMain.getValueAt(i, 0)).contains(subStaffId)
                    && ((String) defaultTableModelMain.getValueAt(i, 1)).contains(subStaffName)
                    && ((String) defaultTableModelMain.getValueAt(i, 2)).contains(roleString)
                    && ((String) defaultTableModelMain.getValueAt(i, 3)).contains(specialityString)) {

                String[] row = new String[defaultTableModelMain.getColumnCount()];
                for (int j = 0; j < defaultTableModelMain.getColumnCount(); j++) {
                    row[j] = (String) defaultTableModelMain.getValueAt(i, j);
                }
                defaultTableModel.addRow(row);
            }
        }
        System.out.println(defaultTableModel.getColumnCount() + "---" + defaultTableModel.getRowCount());
        staffTable.setModel(defaultTableModel);
        staffTable.repaint();
    }

    private void init(){
        searchStaffIDTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchByStaffIdInsert();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchByStaffIdRemove();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        searchStaffNameTextField.getDocument().addDocumentListener(new DocumentListener() {
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
        searchStaffComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    String selectedItem = (String) e.getItem();
//                    System.out.println("Selected: " + selectedItem);
//                }
                filter();
            }
        });
        searchSpecialityComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    String selectedItem = (String) e.getItem();
//                    System.out.println("Selected: " + selectedItem);
//                }
                filter();
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        staffIDTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        staffFullNameTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        roleComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        staffNumberTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        specialityComboBox = new javax.swing.JComboBox<>();
        saveButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        staffTable = new Table();
        searchButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        searchStaffIDTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        searchStaffNameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        searchStaffComboBox = new javax.swing.JComboBox<>();
        searchSpecialityComboBox = new javax.swing.JComboBox<>();
        clearButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID:");

        staffIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Họ tên:");

        staffFullNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        staffFullNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffFullNameTextFieldActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Vị trí:");

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--Chọn--", "Bác sĩ"}));
        roleComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleComboBoxActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Chuyên khoa:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Điện thoại:");

        staffNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        staffNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffNumberTextFieldActionPerformed(evt);
            }
        });

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

        specialityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Không có", "Tim mạch", "Thần kinh", "Da liễu", "Nhãn khoa", "Tai mũi họng", "Nội tiết", "Huyết học"}));

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
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });

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
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel11.setText("ID:");

        searchStaffIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setText("Tên:");

        searchStaffNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchStaffNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchStaffNameTextFieldActionPerformed(evt);
            }
        });

        jLabel13.setText("Vị trí:");

        jLabel14.setText("Chuyên khoa:");

        searchStaffComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--Chọn--", "Bác sĩ"}));

        searchSpecialityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--Chọn--", "Không có", "Tim mạch", "Thần kinh", "Da liễu", "Nhãn khoa", "Tai mũi họng", "Nội tiết", "Huyết học"}));

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

    private void staffFullNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffFullNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffFullNameTextFieldActionPerformed

    private void roleComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roleComboBoxActionPerformed

    private void staffNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffNumberTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffNumberTextFieldActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (currentAction != AdminAction.ADD) clearText();
        currentAction = AdminAction.ADD;
        disableRemainMainButton(addButton);
        enableSupportButton();

        staffIDTextField.setText(Utils.genUUID().toString());

        enableEditingText();
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedStaff == null) {
            ms = "Không có đối tượng để xóa";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa" + selectedStaff.getFullName() + " không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            StaffController.deleteStaff(indexSelectedStaff,selectedStaff,staffTable);
            StaffController.addRowStaffTable(defaultTableModelMain);
            indexSelectedStaff = -1;
            selectedStaff = null;
        }

        currentAction = null;
    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        currentAction = AdminAction.UPDATE;
        disableRemainMainButton(updateButton);
        enableSupportButton();
        enableEditingText();
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
                    rs = StaffController.addStaff(getStaffFromTextField(), staffTable);
                    StaffController.addRowStaffTable(defaultTableModelMain);
                }
                case UPDATE -> {
                    rs = StaffController.updateStaff(indexSelectedStaff, getStaffFromTextField(), staffTable);
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
    }//GEN-LAST:event_saveButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (selectedStaff != null) {
            setText(selectedStaff);
        } else {
            if(currentAction == AdminAction.ADD){
                clearTextWithoutId();
            } else {
                clearText();
            }

        }
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchStaffNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchStaffNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchStaffNameTextFieldActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButtonActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<String> searchSpecialityComboBox;
    private javax.swing.JComboBox<String> searchStaffComboBox;
    private javax.swing.JTextField searchStaffIDTextField;
    private javax.swing.JTextField searchStaffNameTextField;
    private javax.swing.JComboBox<String> specialityComboBox;
    private javax.swing.JTextField staffFullNameTextField;
    private javax.swing.JTextField staffIDTextField;
    private javax.swing.JTextField staffNumberTextField;
    private Table staffTable;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
