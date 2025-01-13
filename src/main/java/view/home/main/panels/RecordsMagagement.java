package view.home.main.panels;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import constants.AdminAction;
import controller.main.RecordController;
import model.base.Record;
import util.Utils;
import view.home.components.Button;
import view.home.components.scrollbar.ScrollBarCustom;
import view.home.components.table.Table;
import view.home.components.dialog.DialogPatientChooser;
import view.home.components.dialog.DialogStaffIsDoctorChooser;
import view.home.components.dialog.Message;
import view.home.components.dialog.MessageResultAdminAction;
import view.home.main.model.ModelAppointment;
import view.home.main.model.ModelRecord;
import view.home.frames.MainView;

import static util.Utils.setComboBoxCustomDisabled;

public class RecordsMagagement extends JPanel {

    ModelRecord selectedModelRecord = null;
    int indexSelectedModelRecord = -1;
    AdminAction currentAction = null;
    HashMap<String, ModelRecord> mapModelRecord = new HashMap<>();
    TableRowSorter<DefaultTableModel> rowSorter;

    boolean isSelectionChanged = false;

    public RecordsMagagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        disableEditingText();
        initTable();
        setTextDate(LocalDate.now());
    }

    public void init() {
        recordPatientNameTextField.setBackground(new Color(220, 223, 228));
        recordPatientNameTextField.setEditable(false);
        recordDoctorNameTextField.setBackground(new Color(220, 223, 228));
        recordDoctorNameTextField.setEditable(false);
        addEventFilter();
    }

    public void addDataTable() {
        RecordController.addRowRecordTable(recordsTable, mapModelRecord);
    }

    public void initTable() {
        addDataTable();

        DefaultTableModel defaultTableModel = (DefaultTableModel) recordsTable.getModel();
        recordsTable.fixTable(jScrollPane1);
        rowSorter = new TableRowSorter<>(defaultTableModel);
        recordsTable.setRowSorter(rowSorter);

        ListSelectionModel selectionModel = recordsTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isSelectionChanged = true;

                if (currentAction == AdminAction.SEARCH) return;

                if (!e.getValueIsAdjusting()) {
                    int indexViewRow = recordsTable.getSelectedRow();

                    if (indexViewRow != -1) {
                        indexSelectedModelRecord = recordsTable.convertRowIndexToModel(indexViewRow);

                        String recordId = (String) recordsTable.getValueAt(indexViewRow, 0);

                        selectedModelRecord = mapModelRecord.get(recordId);
                        setText(selectedModelRecord);
                    }
                }
            }
        });

        recordsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isSelectionChanged) {
                    int row = recordsTable.rowAtPoint(e.getPoint());

                    if (row != -1 && recordsTable.convertRowIndexToModel(row) == indexSelectedModelRecord) {
                        recordsTable.clearSelection();
                    }
                } else {
                    isSelectionChanged = false;
                }
            }
        });
    }

    public void filterTable() {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String recordId = recordIDTextField.getText().trim();
        String patientId = recordPatientIDTextField.getText().trim();
        String staffName = recordDoctorNameTextField.getText().trim();
        String yea = year.getText().trim();
        String mont = month.getSelectedIndex() != 0 ? (String) month.getSelectedItem() : "";
        String dat = date.getText().trim();
        String status = statusCombobox.getSelectedIndex() != 0 ? (String) statusCombobox.getSelectedItem() : "";

        if (!recordId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + recordId + ".*", 0));
        }
        if (!patientId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + patientId + ".*", 1));
        }
        if (!staffName.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + staffName + ".*", 3));
        }
        if (!status.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + status + ".*", 5));
        }

        String regex = Utils.getRegexDate(dat, mont, yea);
        filters.add(RowFilter.regexFilter(regex, 4));

        SwingUtilities.invokeLater(() -> {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        });
    }

    public void resetFilter() {
        rowSorter.setRowFilter(null);
    }

    public void addEventFilter() {
        JTextField[] listDataComponents = {recordIDTextField, recordPatientIDTextField, recordDoctorNameTextField, date, year};

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
        month.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (currentAction == AdminAction.SEARCH) {
                    filterTable();
                }
            }
        });
        statusCombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (currentAction == AdminAction.SEARCH) {
                    filterTable();
                }
            }
        });
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        recordIDTextField.setEnabled(true);
        recordIDTextField.setBackground(Color.WHITE);
        enableEditingTextWithOutId();
    }
    public void enableEditingTextForSearch() {   // bật edit tất cả các textField
        recordIDTextField.setEnabled(true);
        recordIDTextField.setBackground(Color.WHITE);
        date.setEnabled(true);
        setComboBoxCustomDisabled(month, false);
        year.setEnabled(true);
        setComboBoxCustomDisabled(statusCombobox, false);

        recordPatientIDTextField.setEnabled(true);
        recordPatientIDTextField.setBackground(Color.WHITE);

        recordDoctorIDTextField.setEnabled(true);
        recordDoctorIDTextField.setBackground(Color.WHITE);
    }
    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        date.setEnabled(true);
        setComboBoxCustomDisabled(month, false);
        year.setEnabled(true);
        setComboBoxCustomDisabled(statusCombobox, false);
        symptomTextArea.setEnabled(true);
        diagnosisTextField.setEnabled(true);
        prescriptionTextArea.setEnabled(true);

        recordPatientIDTextField.setEnabled(true);
        recordPatientIDTextField.setBackground(Color.WHITE);

        recordDoctorIDTextField.setEnabled(true);
        recordDoctorIDTextField.setBackground(Color.WHITE);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        recordIDTextField.setEditable(false);
        recordIDTextField.setBackground(new Color(220, 223, 228));
        recordIDTextField.setDisabledTextColor(Color.BLACK);

        recordPatientIDTextField.setEditable(false);
        recordPatientIDTextField.setBackground(new Color(220, 223, 228));
        recordPatientIDTextField.setDisabledTextColor(Color.BLACK);

        recordDoctorIDTextField.setEditable(false);
        recordDoctorIDTextField.setBackground(new Color(220, 223, 228));
        recordDoctorIDTextField.setDisabledTextColor(Color.BLACK);

        recordDoctorNameTextField.setEnabled(false);
        recordDoctorNameTextField.setDisabledTextColor(Color.BLACK);

        recordPatientNameTextField.setEnabled(false);
        recordDoctorNameTextField.setDisabledTextColor(Color.BLACK);

        date.setEnabled(false);
        date.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(month, true);

        setComboBoxCustomDisabled(statusCombobox, true);

        year.setEnabled(false);
        year.setDisabledTextColor(Color.BLACK);

        symptomTextArea.setEnabled(false);
        symptomTextArea.setDisabledTextColor(Color.BLACK);

        diagnosisTextField.setEnabled(false);
        diagnosisTextField.setDisabledTextColor(Color.BLACK);

        prescriptionTextArea.setEnabled(false);
        prescriptionTextArea.setDisabledTextColor(Color.BLACK);

    }

    public void setText(ModelRecord modelRecord) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        recordIDTextField.setText(modelRecord.getRecordId());
        recordDoctorIDTextField.setText(modelRecord.getStaffId());
        recordDoctorNameTextField.setText(modelRecord.getStaffName());
        recordPatientNameTextField.setText(modelRecord.getPatientName());
        recordPatientIDTextField.setText(modelRecord.getPatientId());
        diagnosisTextField.setText(modelRecord.getDiagnosis());
        symptomTextArea.setText(modelRecord.getSymptom());
        prescriptionTextArea.setText(modelRecord.getPrescription());
        date.setText(String.valueOf(modelRecord.getCreatedAt().getDayOfMonth()));
        month.setSelectedItem(String.valueOf(modelRecord.getCreatedAt().getMonth()));
        year.setText(String.valueOf(modelRecord.getCreatedAt().getYear()));
        statusCombobox.setSelectedItem(modelRecord.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán");
    }

    public void setTextDate(LocalDate lcDate) {
        month.setSelectedItem(String.valueOf(lcDate.getMonthValue()));
        date.setText(String.valueOf(lcDate.getDayOfMonth()));
        year.setText(String.valueOf(lcDate.getYear()));
    }

    public void clearText() {   // xóa hết giá trị của textField
        recordIDTextField.setText("");

        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        recordDoctorIDTextField.setText("");

        recordDoctorNameTextField.setText("");

        recordPatientIDTextField.setText("");

        recordPatientNameTextField.setText("");

        diagnosisTextField.setText("");

        symptomTextArea.setText("");

        prescriptionTextArea.setText("");

        date.setText("");

        month.setSelectedIndex(0);

        year.setText("");

        statusCombobox.setSelectedIndex(0);
    }

    public boolean hasTextFieldEmpty() {
        return recordIDTextField.getText().trim().isEmpty()
                || recordPatientIDTextField.getText().trim().isEmpty()
                || recordDoctorIDTextField.getText().trim().isEmpty()
                || date.getText().trim().isEmpty()
                || month.getSelectedIndex() == 0
                || year.getText().trim().isEmpty()
                || statusCombobox.getSelectedIndex() == 0
                || diagnosisTextField.getText().trim().isEmpty()
                || symptomTextArea.getText().trim().isEmpty()
                || prescriptionTextArea.getText().trim().isEmpty();
    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu, chon
//        undoButton.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        chooseDoctorButton.setEnabled(false);
        choosePatientButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu, chon
//        undoButton.setEnabled(true);
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

    public ModelRecord getModelRecordFromTextField() {
        String recordId = recordIDTextField.getText().trim();
        String patientId = recordPatientIDTextField.getText().trim();
        String staffId = recordDoctorIDTextField.getText().trim();
        String symptom = symptomTextArea.getText().trim();
        String diagnosis = diagnosisTextField.getText().trim();
        String prescription = prescriptionTextArea.getText().trim();
        LocalDateTime createdAt = LocalDateTime.of(Integer.parseInt(year.getText().trim()),
                Integer.parseInt((String) Objects.requireNonNull(month.getSelectedItem())),
                Integer.parseInt(date.getText().trim()),
                0, 0);
        String patientName = recordPatientNameTextField.getText().trim();
        String doctorName = recordDoctorNameTextField.getText().trim();
        boolean status = false;
        if (statusCombobox.getSelectedIndex() == 1) {
            status = false;
        } else if (statusCombobox.getSelectedIndex() == 2) {
            status = true;
        }
        return new ModelRecord(recordId, patientId, staffId, symptom, diagnosis, prescription, createdAt, patientName, doctorName, status);
    }

    private void initComponents() {

        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        recordsTable = new Table();
        jPanel2 = new JPanel();
        jLabel6 = new JLabel();
        recordIDTextField = new JTextField();
        jLabel3 = new JLabel();
        jLabel11 = new JLabel();
        jScrollPane2 = new JScrollPane();
        symptomTextArea = new JTextArea();
        jLabel12 = new JLabel();
        jScrollPane4 = new JScrollPane();
        prescriptionTextArea = new JTextArea();
        jLabel13 = new JLabel();
        jLabel14 = new JLabel();
        diagnosisTextField = new JTextField();
        cancelButton = new Button();
        payButton = new Button();
        payButton.setVisible(false);
        date = new JTextField();
        month = new JComboBox<>();
        year = new JTextField();
        jLabel9 = new JLabel();
        statusCombobox = new JComboBox<>();
        saveButton = new Button();
        addRecordButton = new Button();
        deleteButton = new Button();
        undoButton = new Button();
        searchButton = new Button();
        updateButton = new Button();
        recordPatientIDTextField = new JTextField();
        jLabel10 = new JLabel();
        jLabel15 = new JLabel();
        recordPatientNameTextField = new JTextField();
        jLabel7 = new JLabel();
        recordDoctorIDTextField = new JTextField();
        recordDoctorNameTextField = new JTextField();
        jLabel8 = new JLabel();
        choosePatientButton = new Button();
        chooseDoctorButton = new Button();

        setBackground(new Color(229, 245, 255));

        jPanel1.setBackground(new Color(229, 245, 255));
        jPanel1.setBorder(BorderFactory.createTitledBorder(null, "Danh sách hồ sơ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 14))); // NOI18N

        recordsTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "ID bệnh nhân", "Tên BN", "Tên BS", "Ngày khám", "Trạng thái"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        recordsTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        recordsTable.setGridColor(new Color(255, 255, 255));
        jScrollPane1.setViewportView(recordsTable);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING)
        );

        jPanel2.setBackground(new Color(229, 245, 255));
        jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Thông tin hồ sơ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 14))); // NOI18N

        jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel6.setText("ID hồ sơ:");

        recordIDTextField.setEditable(false);
        recordIDTextField.setHorizontalAlignment(JTextField.CENTER);

        jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
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

        jLabel14.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel14.setText("----- Thông tin khám bệnh -----");

        cancelButton.setBackground(new Color(255, 204, 204));
        cancelButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        cancelButton.setText("Hủy");
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        payButton.setBackground(new Color(153, 255, 51));
        payButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        payButton.setText("Thanh toán");
        payButton.setFocusPainted(false);

        date.setHorizontalAlignment(JTextField.CENTER);

        month.setModel(new DefaultComboBoxModel<>(new String[]{"--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        year.setHorizontalAlignment(JTextField.CENTER);
        year.setText("2025");

        jLabel9.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel9.setText("Trạng thái:");

        statusCombobox.setModel(new DefaultComboBoxModel<>(new String[]{"--Chọn--", "Chưa thanh toán", "Đã thanh toán"}));
        statusCombobox.setEnabled(false);

        saveButton.setBackground(new Color(153, 255, 51));
        saveButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Lưu");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        addRecordButton.setBackground(new Color(102, 255, 255));
        addRecordButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        addRecordButton.setText("Thêm hồ sơ");
        addRecordButton.setFocusPainted(false);
        addRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addRecordButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new Color(102, 255, 255));
        deleteButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        deleteButton.setText("Xóa hồ sơ");
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        undoButton.setBackground(new Color(204, 204, 204));
        undoButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        undoButton.setText("Hoàn tác");
        undoButton.setFocusPainted(false);
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        searchButton.setBackground(new Color(102, 255, 255));
        searchButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        searchButton.setText("Tìm kiếm");
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new Color(102, 255, 255));
        updateButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Cập nhật hồ sơ");
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        recordPatientIDTextField.setEditable(false);
        recordPatientIDTextField.setHorizontalAlignment(JTextField.CENTER);

        jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel10.setText("ID:");

        jLabel15.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel15.setText("ID:");

        recordPatientNameTextField.setEditable(false);
        recordPatientNameTextField.setHorizontalAlignment(JTextField.CENTER);

        jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel7.setText("Tên BN:");

        recordDoctorIDTextField.setEditable(false);
        recordDoctorIDTextField.setHorizontalAlignment(JTextField.CENTER);

        recordDoctorNameTextField.setEditable(false);
        recordDoctorNameTextField.setHorizontalAlignment(JTextField.CENTER);

        jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel8.setText("Tên BS:");

        choosePatientButton.setBackground(new Color(204, 204, 204));
        choosePatientButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        choosePatientButton.setText("Chọn");
        choosePatientButton.setFocusPainted(false);
        choosePatientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                choosePatientButtonActionPerformed(evt);
            }
        });

        chooseDoctorButton.setBackground(new Color(204, 204, 204));
        chooseDoctorButton.setFont(new Font("Segoe UI", 1, 12)); // NOI18N
        chooseDoctorButton.setText("Chọn");
        chooseDoctorButton.setFocusPainted(false);
        chooseDoctorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chooseDoctorButtonActionPerformed(evt);
            }
        });

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(addRecordButton, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                                                        .addComponent(undoButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(updateButton, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                                                        .addComponent(cancelButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)))
                                                        .addComponent(jLabel11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(jLabel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addComponent(date, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(month, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(year)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(searchButton))
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addComponent(statusCombobox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(payButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                        .addComponent(jScrollPane2, GroupLayout.Alignment.TRAILING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(recordIDTextField))
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(recordPatientNameTextField, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                                                                        .addComponent(recordDoctorNameTextField))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(jLabel15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel10))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(recordPatientIDTextField)
                                                                                        .addComponent(recordDoctorIDTextField))))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(choosePatientButton, GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(chooseDoctorButton, GroupLayout.Alignment.TRAILING)))
                                                        .addComponent(jScrollPane4)
                                                        .addComponent(diagnosisTextField))
                                                .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recordIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recordPatientIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recordPatientNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(choosePatientButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recordDoctorNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recordDoctorIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chooseDoctorButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(year, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(month, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(statusCombobox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(payButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(diagnosisTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addRecordButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(deleteButton)
                                        .addComponent(updateButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
                                        .addComponent(saveButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cancelButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(undoButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (currentAction != AdminAction.SEARCH) {
            clearText();
            currentAction = AdminAction.SEARCH;

            disableRemainMainButton(searchButton);
            enableSupportButton();
            recordIDTextField.setEditable(true);

            enableEditingTextForSearch();
        }
    }

    private void addRecordButtonActionPerformed(ActionEvent evt) {
        if (currentAction != AdminAction.ADD) {
            clearText();
            currentAction = AdminAction.ADD;
            disableRemainMainButton(addRecordButton);
            enableSupportButton();

            recordIDTextField.setText(Utils.genUUID().toString());
            enableEditingText();

            setTextDate(LocalDate.now());
        }
    }

    private void updateButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (currentAction != AdminAction.UPDATE) {
            if (selectedModelRecord == null) {
                Message obj = new Message(MainView.getFrames()[0], true);
                String ms = "Hãy chọn một lịch hẹn để cập nhật!";
                obj.showMessage(ms, false);
            } else {
                currentAction = AdminAction.UPDATE;
                disableRemainMainButton(updateButton);
                enableSupportButton();
                enableEditingTextWithOutId();
            }
        }
    }

    private void undoButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (currentAction == null) {
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            clearTextWithoutId();
            setTextDate(LocalDate.now());
        } else if (currentAction == AdminAction.UPDATE) {
            if (selectedModelRecord != null) {
                setText(selectedModelRecord);
            }
        } else if (currentAction == AdminAction.SEARCH) {
            clearText();
        }
    }

    private void choosePatientButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_choosePatientButtonActionPerformed
        DialogPatientChooser dialogPatientChooser = new DialogPatientChooser(MainView.getFrames()[0], true);
        dialogPatientChooser.showTable("Hãy chọn bệnh nhân!");
        if (dialogPatientChooser.isOk()) {
            recordPatientIDTextField.setText(dialogPatientChooser.getSelectedPatientId());
            recordPatientNameTextField.setText(dialogPatientChooser.getSelectedPatientFullName());
        }
    }

    private void chooseDoctorButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_choosePatientButtonActionPerformed
        DialogStaffIsDoctorChooser dialogStaffIsDoctorChooser = new DialogStaffIsDoctorChooser(MainView.getFrames()[0], true);
        dialogStaffIsDoctorChooser.showTable("Hãy chọn nhân viên!");
        if (dialogStaffIsDoctorChooser.isOk()) {
            recordDoctorIDTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffId());
            recordDoctorNameTextField.setText(dialogStaffIsDoctorChooser.getSelectedStaffFullName());
        }
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        enableMainButton();
        disableEditingText();

        if (currentAction == AdminAction.SEARCH) {
            currentAction = null;

            if (selectedModelRecord != null) {
                setText(selectedModelRecord);
            } else {
                clearText();
            }
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            currentAction = null;

            if (selectedModelRecord != null) {
                setText(selectedModelRecord);
            } else {
                clearText();
            }
        } else if (currentAction == AdminAction.UPDATE) {
            currentAction = null;

            setText(selectedModelRecord);
        }
    }

    private void saveButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        boolean rs = false;
        if (currentAction == AdminAction.SEARCH) {
            enableMainButton();
            disableSupportButton();
            disableEditingText();

            currentAction = null;
            recordsTable.clearSelection();
        } else {
            if (!hasTextFieldEmpty()) {
                switch (currentAction) {
                    case ADD -> {
                        ModelRecord modelRecord = getModelRecordFromTextField();
                        rs = RecordController.addRecord(modelRecord, recordsTable);
                        if (rs) {
                            mapModelRecord.put(modelRecord.getRecordId(),modelRecord);
                        }
                    }
                    case UPDATE -> {
                        ModelRecord modelRecord = getModelRecordFromTextField();
                        rs = RecordController.updateRecord(indexSelectedModelRecord, modelRecord, recordsTable);
                        if (rs) {
                            mapModelRecord.replace(modelRecord.getRecordId(), modelRecord);
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
    }//GEN-LAST:event_saveButtonActionPerformed


    private void deleteButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedModelRecord == null) {
            ms = "Không có đối tượng để xóa!";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            boolean rs = RecordController.deleteRecord(indexSelectedModelRecord, selectedModelRecord, recordsTable);
            if (rs) {
                mapModelRecord.remove(selectedModelRecord.getRecordId());
            }
            indexSelectedModelRecord = -1;
            selectedModelRecord = null;
        }

        currentAction = null;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button addRecordButton;
    private Button cancelButton;
    private Button chooseDoctorButton;
    private Button choosePatientButton;
    private JTextField date;
    private Button deleteButton;
    private JTextField diagnosisTextField;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel3;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane4;
    private JComboBox<String> month;
    private Button payButton;
    private JTextArea prescriptionTextArea;
    private JTextField recordDoctorIDTextField;
    private JTextField recordDoctorNameTextField;
    private JTextField recordIDTextField;
    private JTextField recordPatientIDTextField;
    private JTextField recordPatientNameTextField;
    private Table recordsTable;
    private Button saveButton;
    private Button searchButton;
    private JComboBox<String> statusCombobox;
    private JTextArea symptomTextArea;
    private Button undoButton;
    private Button updateButton;
    private JTextField year;
    // End of variables declaration//GEN-END:variables
}
