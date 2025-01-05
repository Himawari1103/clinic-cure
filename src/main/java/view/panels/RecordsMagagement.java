/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import java.awt.Color;
import java.awt.Component;
import java.time.LocalDateTime;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import view.components.main.components.scrollbar.ScrollBarCustom;

/**
 *
 * @author Chi Cute
 */
public class RecordsMagagement extends javax.swing.JPanel {

    Record selectedRecord = null;
    int indexSelectedRecord = -1;

    /**
     * Creates new form RecordsMagagement
     */
    public RecordsMagagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        recordIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        date.setEnabled(true);
        setComboBoxCustomDisabled(month, false);
        year.setEnabled(true);
        recordIDTextField.setEnabled(true);
        setComboBoxCustomDisabled(statusCombobox, false);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        recordIDTextField.setEditable(false);
        recordIDTextField.setDisabledTextColor(Color.BLACK);

        recordDoctorNameTextField.setEditable(false);
        recordDoctorNameTextField.setDisabledTextColor(Color.BLACK);

        recordPatientNameTextField.setEditable(false);
        recordDoctorNameTextField.setDisabledTextColor(Color.BLACK);

        date.setEnabled(false);
        date.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(month, true);

        year.setEnabled(false);
        year.setDisabledTextColor(Color.BLACK);

    }

    public void setText(model.base.Record record) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        recordIDTextField.setText(record.getRecordId());

//        recordDoctorNameTextField.setText(record.);
//        
//        recordPatientNameTextField.setText(record.);
        diagnosisTextField.setText(record.getDiagnosis());

        symptomTextArea.setText(record.getSymptom());

        prescriptionTextArea.setText(record.getPrescription());

        date.setText(String.valueOf(record.getCreatedAt().getDayOfMonth()));

        month.setSelectedItem(String.valueOf(record.getCreatedAt().getMonth()));

        year.setText(String.valueOf(record.getCreatedAt().getYear()));
    }

    public void clearText() {   // xóa hết giá trị của textField
        recordIDTextField.setText("");

        recordDoctorNameTextField.setText("");

        recordPatientNameTextField.setText("");

        diagnosisTextField.setText("");

        symptomTextArea.setText("");

        prescriptionTextArea.setText("");

        date.setText("");

        month.setSelectedItem("1");

        year.setText("");

    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu, chon
        undoButton.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        chooseDoctorButton.setEnabled(false);
        choosePatientButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu, chon
        undoButton.setEnabled(true);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        chooseDoctorButton.setEnabled(true);
        choosePatientButton.setEnabled(true);
    }

    public void disableRemainMainButton(JButton jButton) {  // tắt các nút chức năng ngoài nút được nhấn
        if (jButton == addRecordButton) {
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == updateButton) {
            addRecordButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (jButton == deleteButton) {
            updateButton.setEnabled(false);
            addRecordButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        addRecordButton.setEnabled(true);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
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

    public model.base.Record getRecordFromTextField() {
        String recordId = recordIDTextField.getText().trim();
        String patientId = recordPatientIDTextField.getText().trim();
        String doctorId = recordDoctorIDTextField.getText().trim();
        String symptom = symptomTextArea.getText().trim();
        String dianogsis = diagnosisTextField.getText().trim();
        String prescription = prescriptionTextArea.getText().trim();
        LocalDateTime createAt = null;

        return new model.base.Record(recordId, patientId, doctorId, symptom, dianogsis, prescription, createAt);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recordsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        recordIDTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        symptomTextArea = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        prescriptionTextArea = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        diagnosisTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        payButton = new javax.swing.JButton();
        date = new javax.swing.JTextField();
        month = new javax.swing.JComboBox<>();
        year = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        statusCombobox = new javax.swing.JComboBox<>();
        saveButton = new javax.swing.JButton();
        addRecordButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        recordPatientIDTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        recordPatientNameTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        recordDoctorIDTextField = new javax.swing.JTextField();
        recordDoctorNameTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        choosePatientButton = new javax.swing.JButton();
        chooseDoctorButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel1.setBackground(new java.awt.Color(229, 245, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách hồ sơ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        recordsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "ID bệnh nhân", "Tên BN", "Tên BS", "Ngày khám", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        recordsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        recordsTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(recordsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel2.setBackground(new java.awt.Color(229, 245, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin hồ sơ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("ID hồ sơ:");

        recordIDTextField.setEditable(false);
        recordIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Ngày khám:");

        jLabel11.setText("Triệu chứng:");

        symptomTextArea.setColumns(20);
        symptomTextArea.setRows(5);
        jScrollPane2.setViewportView(symptomTextArea);

        jLabel12.setText("Chẩn đoán của bác sĩ:");

        prescriptionTextArea.setColumns(20);
        prescriptionTextArea.setRows(5);
        jScrollPane4.setViewportView(prescriptionTextArea);

        jLabel13.setText("Điều trị - Kê đơn:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("----- Thông tin khám bệnh -----");

        diagnosisTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosisTextFieldActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(255, 204, 204));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        payButton.setBackground(new java.awt.Color(153, 255, 51));
        payButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        payButton.setText("Thanh toán");
        payButton.setFocusPainted(false);

        date.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        year.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        year.setText("2025");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Trạng thái:");

        statusCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa thanh toán", "Đã thanh toán" }));
        statusCombobox.setEnabled(false);

        saveButton.setBackground(new java.awt.Color(153, 255, 51));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Lưu");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        addRecordButton.setBackground(new java.awt.Color(102, 255, 255));
        addRecordButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addRecordButton.setText("Thêm hồ sơ");
        addRecordButton.setFocusPainted(false);

        deleteButton.setBackground(new java.awt.Color(102, 255, 255));
        deleteButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteButton.setText("Xóa hồ sơ");
        deleteButton.setFocusPainted(false);

        undoButton.setBackground(new java.awt.Color(204, 204, 204));
        undoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        undoButton.setText("Hoàn tác");
        undoButton.setFocusPainted(false);
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(102, 255, 255));
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchButton.setText("Tìm kiếm");
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new java.awt.Color(102, 255, 255));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Cập nhật hồ sơ");
        updateButton.setFocusPainted(false);

        recordPatientIDTextField.setEditable(false);
        recordPatientIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("ID:");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("ID:");

        recordPatientNameTextField.setEditable(false);
        recordPatientNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Tên BN:");

        recordDoctorIDTextField.setEditable(false);
        recordDoctorIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        recordDoctorNameTextField.setEditable(false);
        recordDoctorNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Tên BS:");

        choosePatientButton.setBackground(new java.awt.Color(204, 204, 204));
        choosePatientButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        choosePatientButton.setText("Chọn");
        choosePatientButton.setFocusPainted(false);
        choosePatientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePatientButtonActionPerformed(evt);
            }
        });

        chooseDoctorButton.setBackground(new java.awt.Color(204, 204, 204));
        chooseDoctorButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        chooseDoctorButton.setText("Chọn");
        chooseDoctorButton.setFocusPainted(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addRecordButton, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(year)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(searchButton))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(statusCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(payButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(recordIDTextField))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(recordPatientNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                            .addComponent(recordDoctorNameTextField))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(recordPatientIDTextField)
                                            .addComponent(recordDoctorIDTextField))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(choosePatientButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(chooseDoctorButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(jScrollPane4)
                            .addComponent(diagnosisTextField))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordPatientIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordPatientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choosePatientButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordDoctorNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordDoctorIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseDoctorButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(statusCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diagnosisTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRecordButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteButton)
                    .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        disableSupportButton();
        enableMainButton();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        disableSupportButton();
        enableMainButton();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        // TODO add your handling code here:
        clearText();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void diagnosisTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosisTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_diagnosisTextFieldActionPerformed

    private void choosePatientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePatientButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_choosePatientButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRecordButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton chooseDoctorButton;
    private javax.swing.JButton choosePatientButton;
    private javax.swing.JTextField date;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField diagnosisTextField;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JButton payButton;
    private javax.swing.JTextArea prescriptionTextArea;
    private javax.swing.JTextField recordDoctorIDTextField;
    private javax.swing.JTextField recordDoctorNameTextField;
    private javax.swing.JTextField recordIDTextField;
    private javax.swing.JTextField recordPatientIDTextField;
    private javax.swing.JTextField recordPatientNameTextField;
    private javax.swing.JTable recordsTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<String> statusCombobox;
    private javax.swing.JTextArea symptomTextArea;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton updateButton;
    private javax.swing.JTextField year;
    // End of variables declaration//GEN-END:variables
}
