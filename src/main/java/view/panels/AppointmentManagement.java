/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import java.awt.Color;
import java.awt.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import constants.AdminAction;
import controller.main.AppointmentController;
import model.base.Appointment;
import util.Utils;
import view.components.main.components.scrollbar.ScrollBarCustom;
import view.components.main.components.table.Table;
import view.components.main.dialog.DialogPatientChooser;
import view.components.main.dialog.DialogStaffIsDoctorChooser;
import view.components.main.dialog.Message;
import view.components.main.dialog.MessageResultAdminAction;
import view.frames.MainView;

/**
 *
 * @author Chi Cute
 */
public class AppointmentManagement extends javax.swing.JPanel {

    Appointment selectedAppointment = null;
    String selectedPatientName = "";
    String selectedStaffName = "";
    int indexSelectedAppointment = -1;
    AdminAction currentAction = null;

    DefaultTableModel defaultTableModelMain;

    public AppointmentManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        initTable();
        setTextDate(LocalDate.now());
    }

    public void init(){
        patientNameTextField.setBackground(new Color(220,223,228));
        doctorNameTextField.setBackground(new Color(220,223,228));
    }

    int debug = 1;
    public void addDataTable() {
        AppointmentController.addRowAppointmentTable(appointmentsTable);
        System.out.println(debug++);
    }

    public void initTable() {
        addDataTable();

        defaultTableModelMain = (DefaultTableModel) appointmentsTable.getModel();
        appointmentsTable.fixTable(jScrollPane1);

        ListSelectionModel selectionModel = appointmentsTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    indexSelectedAppointment = appointmentsTable.getSelectedRow();
                    if (indexSelectedAppointment != -1) {
                        String appointmentId = (String) appointmentsTable.getValueAt(indexSelectedAppointment, 0);
                        String patientId = (String) appointmentsTable.getValueAt(indexSelectedAppointment, 1);
                        String patientName = (String) appointmentsTable.getValueAt(indexSelectedAppointment, 2);
                        String staffId = (String) appointmentsTable.getValueAt(indexSelectedAppointment, 3);
                        String staffName = (String) appointmentsTable.getValueAt(indexSelectedAppointment, 4);
                        LocalDateTime time = Utils.stringToLocalDateTimeWithTime((String)appointmentsTable.getValueAt(indexSelectedAppointment, 5));

                        selectedAppointment = new Appointment(appointmentId, patientId, staffId, time);
                        selectedPatientName = patientName;
                        selectedStaffName = staffName;

                        setText(selectedAppointment, patientName, staffName);
                    }
                }
            }
        });
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        appointmentIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        appointmentDate.setEnabled(true);
        setComboBoxCustomDisabled(appointmentMonth, false);
        appointmentYear.setEnabled(true);
        patientIDTextField.setEnabled(true);
        patientNameTextField.setEnabled(true);
        doctorIDTextField.setEnabled(true);
        doctorNameTextField.setEnabled(true);
        appointmentIDTextField.setEnabled(true);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        appointmentIDTextField.setEditable(false);
        appointmentIDTextField.setDisabledTextColor(Color.BLACK);

        patientIDTextField.setEditable(false);
        patientIDTextField.setDisabledTextColor(Color.BLACK);

        patientNameTextField.setEditable(false);
        patientNameTextField.setDisabledTextColor(Color.BLACK);

        doctorIDTextField.setEditable(false);
        doctorIDTextField.setDisabledTextColor(Color.BLACK);

        doctorNameTextField.setEditable(false);
        doctorNameTextField.setDisabledTextColor(Color.BLACK);

        appointmentDate.setEnabled(false);
        appointmentDate.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(appointmentMonth, true);

        appointmentYear.setEnabled(false);
        appointmentYear.setDisabledTextColor(Color.BLACK);

    }

    public void setTextDate(LocalDate lcDate){
        appointmentMonth.setSelectedItem(String.valueOf(lcDate.getMonthValue()));
        appointmentDate.setText(String.valueOf(lcDate.getDayOfMonth()));
        appointmentYear.setText(String.valueOf(lcDate.getYear()));
    }

    public void setText(Appointment appointment, String patientName, String staffName) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        appointmentIDTextField.setText(appointment.getAppointmentId());

        patientIDTextField.setText(appointment.getPatientId());

        doctorIDTextField.setText(appointment.getStaffId());

        patientNameTextField.setText(patientName);

        doctorNameTextField.setText(staffName);

        appointmentDate.setText(String.valueOf(appointment.getTime().getDayOfMonth()));

        appointmentMonth.setSelectedItem(String.valueOf(appointment.getTime().getMonthValue()));

        appointmentYear.setText(String.valueOf(appointment.getTime().getYear()));
    }

    public void clearText() {   // xóa hết giá trị của textField
        appointmentIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId(){
        patientIDTextField.setText("");
        patientNameTextField.setText("");
        doctorIDTextField.setText("");
        doctorNameTextField.setText("");
        appointmentDate.setText("");
        appointmentMonth.setSelectedItem("1");
        appointmentYear.setText("");
    }

    public boolean hasTextFieldEmpty() {
        return appointmentDate.getText().trim().isEmpty()
                || appointmentYear.getText().trim().isEmpty()
                || patientIDTextField.getText().trim().isEmpty()
                || doctorIDTextField.getText().trim().isEmpty();
    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu, chon
        undoButton.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        chooseDoctorbutton.setEnabled(false);
        choosePatientButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu, chon
        undoButton.setEnabled(true);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        chooseDoctorbutton.setEnabled(true);
        choosePatientButton.setEnabled(true);
    }

    public void disableRemainMainButton(JButton jButton) {  // tắt các nút chức năng ngoài nút được nhấn
        if (jButton == createAppointmentButton) {
            updateAppointmentButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == updateAppointmentButton) {
            createAppointmentButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == deleteButton) {
            createAppointmentButton.setEnabled(false);
            updateAppointmentButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        createAppointmentButton.setEnabled(true);
        updateAppointmentButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    public Appointment getAppointmentFromTextField() {
        String appointmentId = appointmentIDTextField.getText().trim();
        String patientId = patientIDTextField.getText().trim();
        String doctorId = doctorIDTextField.getText().trim();
        LocalDateTime time = LocalDateTime.of(Integer.parseInt(appointmentYear.getText().trim()),
                Integer.parseInt((String) Objects.requireNonNull(appointmentMonth.getSelectedItem())),
                Integer.parseInt(appointmentDate.getText().trim()),
                0, 0);

        return new Appointment(appointmentId, patientId, doctorId, time);
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

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        appointmentsTable = new Table();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        doctorIDTextField = new javax.swing.JTextField();
        patientIDTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        appointmentIDTextField = new javax.swing.JTextField();
        createAppointmentButton = new javax.swing.JButton();
        updateAppointmentButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        patientNameTextField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        doctorNameTextField = new javax.swing.JFormattedTextField();
        appointmentDate = new javax.swing.JTextField();
        appointmentMonth = new javax.swing.JComboBox<>();
        appointmentYear = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        choosePatientButton = new javax.swing.JButton();
        chooseDoctorbutton = new javax.swing.JButton();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý lịch hẹn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ", "ID bệnh nhân", "Tên BN", "ID bác sĩ", "Tên BS", "Thời gian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
        doctorIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doctorIDTextFieldActionPerformed(evt);
            }
        });

        patientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        patientIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientIDTextFieldActionPerformed(evt);
            }
        });

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
        appointmentIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appointmentIDTextFieldActionPerformed(evt);
            }
        });

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
        patientNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientNameTextFieldActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tên BS:");

        doctorNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        doctorNameTextField.setEnabled(false);
        doctorNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doctorNameTextFieldActionPerformed(evt);
            }
        });

        appointmentDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        appointmentDate.setEnabled(false);

        appointmentMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        appointmentMonth.setEnabled(false);

        appointmentYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        appointmentYear.setText("2025");
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
    }// </editor-fold>//GEN-END:initComponents

    private void doctorIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doctorIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doctorIDTextFieldActionPerformed

    private void patientIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientIDTextFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void appointmentIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appointmentIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_appointmentIDTextFieldActionPerformed

    private void patientNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_patientNameTextFieldActionPerformed

    private void doctorNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doctorNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doctorNameTextFieldActionPerformed

    private void createAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAppointmentButtonActionPerformed
        if (currentAction != AdminAction.ADD) clearText();
        currentAction = AdminAction.ADD;
        disableRemainMainButton(createAppointmentButton);
        enableSupportButton();

        appointmentIDTextField.setText(Utils.genUUID().toString());
        enableEditingText();
        setTextDate(LocalDate.now());
    }//GEN-LAST:event_createAppointmentButtonActionPerformed

    private void updateAppointmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAppointmentButtonActionPerformed
        currentAction = AdminAction.UPDATE;
        disableRemainMainButton(updateAppointmentButton);
        enableSupportButton();
        enableEditingTextWithOutId();
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (selectedAppointment != null) {
            setText(selectedAppointment, selectedPatientName, selectedStaffName);
        } else {
            if(currentAction == AdminAction.ADD){
                clearTextWithoutId();
            } else {
                clearText();
            }

        }
    }

    private void choosePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        DialogPatientChooser dialogPatientChooser = new DialogPatientChooser(MainView.getFrames()[0], true);
        dialogPatientChooser.showTable("Hãy chọn bệnh nhân!");
        if(dialogPatientChooser.isOk()){
            patientIDTextField.setText(dialogPatientChooser.getSelectedPatientId());
            patientNameTextField.setText(dialogPatientChooser.getSelectedPatientFullName());
        }
    }
    private void chooseDoctorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        DialogStaffIsDoctorChooser dialogStaffIsDoctorChooser = new DialogStaffIsDoctorChooser(MainView.getFrames()[0], true);
        dialogStaffIsDoctorChooser.showTable("Hãy chọn nhân viên!");
        if(dialogStaffIsDoctorChooser.isOk()){
            doctorIDTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffId());
            doctorNameTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffFullName());
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        enableMainButton();

        if (selectedAppointment != null) {
            setText(selectedAppointment, selectedPatientName, selectedStaffName);
        } else {
            clearText();
        }

        currentAction = null;
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        boolean rs = false;
        if (!hasTextFieldEmpty()) {
            switch (currentAction) {
                case ADD -> {
                    rs = AppointmentController.addAppointment(getAppointmentFromTextField(),appointmentsTable);
                    AppointmentController.addRowAppointmentTable(defaultTableModelMain);
                }
                case UPDATE -> {
                    rs = AppointmentController.updateAppointment(indexSelectedAppointment, getAppointmentFromTextField(), appointmentsTable);
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

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedAppointment == null) {
            ms = "Không có đối tượng để xóa";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            AppointmentController.deleteAppointment(indexSelectedAppointment, selectedAppointment, appointmentsTable);
            AppointmentController.addRowAppointmentTable(defaultTableModelMain);
            indexSelectedAppointment = -1;
            selectedAppointment = null;
        }

        currentAction = null;
    }//GEN-LAST:event_deleteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField appointmentDate;
    private javax.swing.JTextField appointmentIDTextField;
    private javax.swing.JComboBox<String> appointmentMonth;
    private javax.swing.JTextField appointmentYear;
    private Table appointmentsTable;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton chooseDoctorbutton;
    private javax.swing.JButton choosePatientButton;
    private javax.swing.JButton createAppointmentButton;
    private javax.swing.JButton deleteButton;
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
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton updateAppointmentButton;
    // End of variables declaration//GEN-END:variables
}
