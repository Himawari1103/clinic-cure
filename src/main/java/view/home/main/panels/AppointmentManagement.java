/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.home.main.panels;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import constants.AdminAction;
import controller.main.AppointmentController;
import util.Utils;
import view.home.components.Button;
import view.home.components.table.Table;
import view.home.components.dialog.DialogPatientChooser;
import view.home.components.dialog.DialogStaffIsDoctorChooser;
import view.home.components.dialog.Message;
import view.home.components.dialog.MessageResultAdminAction;
import view.home.main.model.ModelAppointment;
import view.home.frames.MainView;

/**
 * @author Chi Cute
 */
public class AppointmentManagement extends javax.swing.JPanel {

    ModelAppointment selectedModelAppointment = null;
    int indexSelectedModelAppointment = -1;
    AdminAction currentAction = null;
    HashMap<String, ModelAppointment> mapModelAppointments = new HashMap<>();
    TableRowSorter<DefaultTableModel> rowSorter;

    boolean isSelectionChanged = false;

    public AppointmentManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        disableEditingText();
        initTable();
        setTextDate(LocalDate.now());
    }

    public void init() {
        patientNameTextField.setBackground(new Color(220, 223, 228));
        doctorNameTextField.setBackground(new Color(220, 223, 228));
        addEventFilter();
    }

    public void addDataTable() {
        AppointmentController.addRowAppointmentTable(appointmentsTable, mapModelAppointments);
    }

    public void initTable() {
        addDataTable();
        DefaultTableModel defaultTableModel = (DefaultTableModel) appointmentsTable.getModel();
        appointmentsTable.fixTable(jScrollPane1);
        rowSorter = new TableRowSorter<>(defaultTableModel);
        appointmentsTable.setRowSorter(rowSorter);

        ListSelectionModel selectionModel = appointmentsTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isSelectionChanged = true;

                if (currentAction == AdminAction.SEARCH) return;

                if (!e.getValueIsAdjusting()) {
                    int indexViewRow = appointmentsTable.getSelectedRow();

                    if (indexViewRow != -1) {
                        indexSelectedModelAppointment = appointmentsTable.convertRowIndexToModel(indexViewRow);

                        String appointmentId = (String) appointmentsTable.getValueAt(indexViewRow, 0);
                        String patientId = (String) appointmentsTable.getValueAt(indexViewRow, 1);
                        String patientName = (String) appointmentsTable.getValueAt(indexViewRow, 2);
                        String staffId = (String) appointmentsTable.getValueAt(indexViewRow, 3);
                        String staffName = (String) appointmentsTable.getValueAt(indexViewRow, 4);
                        LocalDateTime time = Utils.stringToLocalDateTimeWithTime((String) appointmentsTable.getValueAt(indexViewRow, 5));

                        selectedModelAppointment = new ModelAppointment(appointmentId, patientId, staffId, time, patientName, staffName);

                        setText(selectedModelAppointment);
                    }
                }
            }
        });

        appointmentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isSelectionChanged) {
                    int row = appointmentsTable.rowAtPoint(e.getPoint());

                    if (row != -1 && appointmentsTable.convertRowIndexToModel(row) == indexSelectedModelAppointment) {
                        appointmentsTable.clearSelection();
                    }
                } else {
                    isSelectionChanged = false;
                }
            }
        });
    }

    public void filterTable() {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String appointmentId = appointmentIDTextField.getText().trim();
        String patientId = patientIDTextField.getText().trim();
        String doctorId = doctorIDTextField.getText().trim();
        String year = appointmentYear.getText().trim();
        String month = appointmentMonth.getSelectedIndex() != 0 ? (String) Objects.requireNonNull(appointmentMonth.getSelectedItem()) : "";
        String date = appointmentDate.getText().trim();

        if (!appointmentId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + appointmentId + ".*", 0));
        }
        if (!patientId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + patientId + ".*", 1));
        }
        if (!doctorId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + doctorId + ".*", 3));
        }
        String regex = Utils.getRegexDate(date, month, year);
        filters.add(RowFilter.regexFilter(regex, 5));

        SwingUtilities.invokeLater(() -> {
            // Thay đổi trạng thái giao diện ở đây
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        });
    }

    public void resetFilter() {
        rowSorter.setRowFilter(null);
    }

    public void addEventFilter() {
        JTextField[] listDataComponents = {appointmentIDTextField, patientIDTextField, doctorIDTextField, appointmentDate, appointmentYear};

        for (int i = 0; i < listDataComponents.length; i++) {
            listDataComponents[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (currentAction == AdminAction.SEARCH) {
                        filterTable();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (currentAction == AdminAction.SEARCH) {
                        filterTable();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (currentAction == AdminAction.SEARCH) {
                        filterTable();
                    }
                }
            });
        }
        appointmentMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (currentAction == AdminAction.SEARCH) {
                    filterTable();
                }
            }
        });
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        appointmentIDTextField.setEnabled(true);
        appointmentIDTextField.setBackground(Color.WHITE);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        patientIDTextField.setEnabled(true);
        patientIDTextField.setBackground(Color.WHITE);

        doctorIDTextField.setEnabled(true);
        doctorIDTextField.setBackground(Color.WHITE);

        patientNameTextField.setEnabled(true);
        doctorNameTextField.setEnabled(true);
        appointmentDate.setEnabled(true);
        Utils.setComboBoxCustomDisabled(appointmentMonth, false);
        appointmentYear.setEnabled(true);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        appointmentIDTextField.setEditable(false);
        appointmentIDTextField.setBackground(new Color(220, 223, 228));
        appointmentIDTextField.setDisabledTextColor(Color.BLACK);

        patientIDTextField.setEditable(false);
        patientIDTextField.setBackground(new Color(220, 223, 228));
        patientIDTextField.setDisabledTextColor(Color.BLACK);

        doctorIDTextField.setEditable(false);
        doctorIDTextField.setBackground(new Color(220, 223, 228));
        doctorIDTextField.setDisabledTextColor(Color.BLACK);

        patientNameTextField.setEditable(false);
        patientNameTextField.setDisabledTextColor(Color.BLACK);

        doctorNameTextField.setEditable(false);
        doctorNameTextField.setDisabledTextColor(Color.BLACK);

        appointmentDate.setEnabled(false);
        appointmentDate.setDisabledTextColor(Color.BLACK);

        Utils.setComboBoxCustomDisabled(appointmentMonth, true);

        appointmentYear.setEnabled(false);
        appointmentYear.setDisabledTextColor(Color.BLACK);

    }

    public void setTextDate(LocalDate lcDate) {
        appointmentMonth.setSelectedItem(String.valueOf(lcDate.getMonthValue()));
        appointmentDate.setText(String.valueOf(lcDate.getDayOfMonth()));
        appointmentYear.setText(String.valueOf(lcDate.getYear()));
    }

    public void setText(ModelAppointment modelAppointment) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        appointmentIDTextField.setText(modelAppointment.getAppointmentId());
        patientIDTextField.setText(modelAppointment.getPatientId());
        patientNameTextField.setText(modelAppointment.getPatientName());
        doctorIDTextField.setText(modelAppointment.getStaffId());
        doctorNameTextField.setText(modelAppointment.getStaffName());
        appointmentDate.setText(String.valueOf(modelAppointment.getTime().getDayOfMonth()));
        appointmentMonth.setSelectedItem(String.valueOf(modelAppointment.getTime().getMonthValue()));
        appointmentYear.setText(String.valueOf(modelAppointment.getTime().getYear()));
    }

    public void clearText() {   // xóa hết giá trị của textField
        appointmentIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {
        patientIDTextField.setText("");
        patientNameTextField.setText("");
        doctorIDTextField.setText("");
        doctorNameTextField.setText("");
        appointmentDate.setText("");
        appointmentMonth.setSelectedIndex(0);
        appointmentYear.setText("");
    }

    public boolean hasTextFieldEmpty() {
        return checkDateEmpty()
                || patientIDTextField.getText().trim().isEmpty()
                || doctorIDTextField.getText().trim().isEmpty();
    }


    public boolean checkDateEmpty() {
        return appointmentDate.getText().trim().isEmpty()
                || appointmentMonth.getSelectedIndex() == 0
                || appointmentYear.getText().trim().isEmpty();
    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu, chon
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        chooseDoctorbutton.setEnabled(false);
        choosePatientButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu, chon
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        chooseDoctorbutton.setEnabled(true);
        choosePatientButton.setEnabled(true);
    }

    public void disableRemainMainButton(Button button) {  // tắt các nút chức năng ngoài nút được nhấn
        if (button == createAppointmentButton) {
            updateAppointmentButton.setEnabled(false);
            deleteButton.setEnabled(false);
            searchButton.setEnabled(false);
        } else if (button == updateAppointmentButton) {
            createAppointmentButton.setEnabled(false);
            deleteButton.setEnabled(false);
            searchButton.setEnabled(false);
        } else if (button == deleteButton) {
            createAppointmentButton.setEnabled(false);
            updateAppointmentButton.setEnabled(false);
            searchButton.setEnabled(false);
        } else if (button == searchButton) {
            createAppointmentButton.setEnabled(false);
            updateAppointmentButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        createAppointmentButton.setEnabled(true);
        updateAppointmentButton.setEnabled(true);
        deleteButton.setEnabled(true);
        searchButton.setEnabled(true);
    }

    public ModelAppointment getModelAppointmentFromTextField() {
        String appointmentId = appointmentIDTextField.getText().trim();
        String patientId = patientIDTextField.getText().trim();
        String patientName = patientNameTextField.getText().trim();
        String doctorId = doctorIDTextField.getText().trim();
        String doctorName = doctorNameTextField.getText().trim();
        LocalDateTime time = LocalDateTime.of(Integer.parseInt(appointmentYear.getText().trim()),
                Integer.parseInt((String) Objects.requireNonNull(appointmentMonth.getSelectedItem())),
                Integer.parseInt(appointmentDate.getText().trim()),
                0, 0);

        return new ModelAppointment(appointmentId, patientId, doctorId, time, patientName, doctorName);
    }

//    public boolean checkCreatedAt(DefaultTableModel defaultTableModel, int index) {
//        LocalDateTime createdAt = Utils.stringToLocalDateTimeWithTime((String) defaultTableModel.getValueAt(index, 0));
//
//        return (createdAt.getDayOfMonth() == Integer.parseInt(appointmentDate.getText().trim()) || appointmentDate.getText().trim().isEmpty())
//                && (createdAt.getMonthValue() == Integer.parseInt((String) Objects.requireNonNull(appointmentMonth.getSelectedItem())) || appointmentMonth.getSelectedIndex() == 0)
//                && (createdAt.getYear() == Integer.parseInt(appointmentDate.getText().trim()) || appointmentYear.getText().trim().isEmpty());
//    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        appointmentsTable = new Table();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        doctorIDTextField = new javax.swing.JTextField();
        patientIDTextField = new javax.swing.JTextField();
        searchButton = new Button();
        jLabel4 = new javax.swing.JLabel();
        appointmentIDTextField = new javax.swing.JTextField();
        createAppointmentButton = new Button();
        updateAppointmentButton = new Button();
        deleteButton = new Button();
        jLabel9 = new javax.swing.JLabel();
        patientNameTextField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        doctorNameTextField = new javax.swing.JFormattedTextField();
        appointmentDate = new javax.swing.JTextField();
        appointmentMonth = new javax.swing.JComboBox<>();
        appointmentYear = new javax.swing.JTextField();
        saveButton = new Button();
        cancelButton = new Button();
        undoButton = new Button();
        choosePatientButton = new Button();
        chooseDoctorbutton = new Button();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý lịch hẹn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                ModelAppointment.getColumnNames()
        ) {
//            boolean[] canEdit = new boolean[]{
//                    false, false, false, false, false, false
//            };

            //            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit[columnIndex];
//            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        appointmentsTable.setFocusable(false);
        appointmentsTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(appointmentsTable);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID bệnh nhân:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("ID bác sĩ:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Thời gian:");

        doctorIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        patientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        searchButton.setBackground(new java.awt.Color(102, 255, 255));
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchButton.setText("Tìm kiếm");
        searchButton.setFocusPainted(false);
        searchButton.setFocusable(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ID lịch hẹn:");

        appointmentIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        createAppointmentButton.setBackground(new java.awt.Color(102, 255, 255));
        createAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createAppointmentButton.setText("Tạo lịch hẹn");
        createAppointmentButton.setFocusPainted(false);
        createAppointmentButton.setFocusable(false);
        createAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAppointmentButtonActionPerformed(evt);
            }
        });

        updateAppointmentButton.setBackground(new java.awt.Color(102, 255, 255));
        updateAppointmentButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateAppointmentButton.setText("Cập nhật");
        updateAppointmentButton.setFocusPainted(false);
        updateAppointmentButton.setFocusable(false);
        updateAppointmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAppointmentButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(102, 255, 255));
        deleteButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteButton.setText("Xóa lịch hẹn");
        deleteButton.setFocusPainted(false);
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Tên BN:");

        patientNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientNameTextField.setEnabled(false);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tên BS:");

        doctorNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        doctorNameTextField.setEnabled(false);

        appointmentDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        appointmentDate.setEnabled(false);

        Utils.setModelMonthComboBox(appointmentMonth);
        appointmentMonth.setEnabled(false);

        appointmentYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        appointmentYear.setEnabled(false);

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

        cancelButton.setBackground(new java.awt.Color(255, 204, 204));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.setFocusPainted(false);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        undoButton.setBackground(new java.awt.Color(204, 204, 204));
        undoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        undoButton.setText("Hoàn tác");
        undoButton.setFocusPainted(false);
        undoButton.setFocusable(false);
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        choosePatientButton.setBackground(new java.awt.Color(204, 204, 204));
        choosePatientButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        choosePatientButton.setText("Chọn");
        choosePatientButton.setFocusPainted(false);
        choosePatientButton.setFocusable(false);
        choosePatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePatientButtonActionPerformed(evt);
            }
        });

        chooseDoctorbutton.setBackground(new java.awt.Color(204, 204, 204));
        chooseDoctorbutton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        chooseDoctorbutton.setText("Chọn");
        chooseDoctorbutton.setFocusPainted(false);
        chooseDoctorbutton.setFocusable(false);
        chooseDoctorbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseDoctorButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(choosePatientButton, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                        .addComponent(chooseDoctorbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(doctorIDTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(patientIDTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(appointmentIDTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(patientNameTextField)
                                        .addComponent(doctorNameTextField)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(appointmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(appointmentMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(appointmentYear, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(1, 1, 1)
                                                                .addComponent(createAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(updateAppointmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(appointmentYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(appointmentMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(appointmentDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel4)
                                                                                .addComponent(appointmentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel3)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabel1)
                                                                        .addComponent(patientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel9)
                                                                        .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(12, 12, 12)
                                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(doctorIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabel2)
                                                                        .addComponent(jLabel10)
                                                                        .addComponent(doctorNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(choosePatientButton)
                                                                .addGap(12, 12, 12)
                                                                .addComponent(chooseDoctorbutton))))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(createAppointmentButton)
                                                        .addComponent(deleteButton))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(updateAppointmentButton)
                                                        .addComponent(searchButton))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(undoButton)
                                                        .addComponent(cancelButton)
                                                        .addComponent(saveButton))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 980, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 620, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (currentAction != AdminAction.SEARCH) {
            clearText();
            currentAction = AdminAction.SEARCH;

            disableRemainMainButton(searchButton);
            enableSupportButton();
            appointmentIDTextField.setEditable(true);

            enableEditingText();
        }
    }

    private void createAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAppointmentButtonActionPerformed
        if (currentAction != AdminAction.ADD) {
            clearText();
            currentAction = AdminAction.ADD;
            disableRemainMainButton(createAppointmentButton);
            enableSupportButton();

            appointmentIDTextField.setText(Utils.genUUID().toString());
            enableEditingTextWithOutId();

            setTextDate(LocalDate.now());
        }
    }

    private void updateAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAppointmentButtonActionPerformed
        if (currentAction != AdminAction.UPDATE) {
            if (selectedModelAppointment == null) {
                Message obj = new Message(MainView.getFrames()[0], true);
                String ms = "Hãy chọn một lịch hẹn để cập nhật!";
                obj.showMessage(ms, false);
            } else {
                currentAction = AdminAction.UPDATE;
                disableRemainMainButton(updateAppointmentButton);
                enableSupportButton();
                enableEditingTextWithOutId();
            }
        }
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (currentAction == null) {
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            clearTextWithoutId();
            setTextDate(LocalDate.now());
        } else if (currentAction == AdminAction.UPDATE) {
            setText(selectedModelAppointment);
        } else if (currentAction == AdminAction.SEARCH) {
            clearText();
        }
    }

    private void choosePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        DialogPatientChooser dialogPatientChooser = new DialogPatientChooser(MainView.getFrames()[0], true);
        dialogPatientChooser.showTable("Hãy chọn bệnh nhân!");
        if (dialogPatientChooser.isOk()) {
            patientIDTextField.setText(dialogPatientChooser.getSelectedPatientId());
            patientNameTextField.setText(dialogPatientChooser.getSelectedPatientFullName());
        }
    }

    private void chooseDoctorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        DialogStaffIsDoctorChooser dialogStaffIsDoctorChooser = new DialogStaffIsDoctorChooser(MainView.getFrames()[0], true);
        dialogStaffIsDoctorChooser.showTable("Hãy chọn nhân viên!");
        if (dialogStaffIsDoctorChooser.isOk()) {
            doctorIDTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffId());
            doctorNameTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffFullName());
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        disableEditingText();

        if (currentAction == AdminAction.SEARCH) {
            currentAction = null;

            if (selectedModelAppointment != null) {
                setText(selectedModelAppointment);
            } else {
                clearText();
            }
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            currentAction = null;

            if (selectedModelAppointment != null) {
                setText(selectedModelAppointment);
            } else {
                clearText();
            }
        } else if (currentAction == AdminAction.UPDATE) {
            currentAction = null;

            setText(selectedModelAppointment);
        }
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean rs = false;
        if (currentAction == AdminAction.SEARCH) {
            enableMainButton();
            disableSupportButton();
            disableEditingText();

            currentAction = null;
            appointmentsTable.clearSelection();
        } else {
            if (!hasTextFieldEmpty()) {
                switch (currentAction) {
                    case ADD -> {
                        ModelAppointment modelAppointment = getModelAppointmentFromTextField();
                        rs = AppointmentController.addAppointment(modelAppointment, appointmentsTable);
                        if (rs) {
                            mapModelAppointments.put(modelAppointment.getAppointmentId(), modelAppointment);
                        }
                    }
                    case UPDATE -> {
                        ModelAppointment modelAppointment = getModelAppointmentFromTextField();
                        rs = AppointmentController.updateAppointment(indexSelectedModelAppointment, modelAppointment, appointmentsTable);
                        if (rs) {
                            mapModelAppointments.replace(modelAppointment.getAppointmentId(), modelAppointment);
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

    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedModelAppointment == null) {
            ms = "Hãy chọn một lịch hẹn để xóa!";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa lịch hẹn này không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            boolean rs = AppointmentController.deleteAppointment(indexSelectedModelAppointment, selectedModelAppointment, appointmentsTable);
            if (rs) {
                mapModelAppointments.remove(selectedModelAppointment.getAppointmentId());
            }
            indexSelectedModelAppointment = -1;
            selectedModelAppointment = null;
        }

        currentAction = null;
    }//GEN-LAST:event_deleteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField appointmentDate;
    private javax.swing.JTextField appointmentIDTextField;
    private javax.swing.JComboBox<String> appointmentMonth;
    private javax.swing.JTextField appointmentYear;
    private Table appointmentsTable;
    private Button cancelButton;
    private Button chooseDoctorbutton;
    private Button choosePatientButton;
    private Button createAppointmentButton;
    private Button deleteButton;
    private javax.swing.JTextField doctorIDTextField;
    private javax.swing.JFormattedTextField doctorNameTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField patientIDTextField;
    private javax.swing.JFormattedTextField patientNameTextField;
    private Button saveButton;
    private Button searchButton;
    private Button undoButton;
    private Button updateAppointmentButton;
    // End of variables declaration//GEN-END:variables
}
