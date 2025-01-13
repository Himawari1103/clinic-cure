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
import controller.main.PatientController;
import controller.main.ReceiptController;
import model.base.Receipt;
import util.Utils;
import view.home.components.Button;
import view.home.components.table.Table;
import view.home.components.dialog.DialogRecordChooser;
import view.home.components.dialog.Message;
import view.home.components.dialog.MessageResultAdminAction;
import view.home.frames.MainView;
import view.home.main.model.ModelAppointment;

import static util.Utils.setComboBoxCustomDisabled;

/**
 * @author Chi Cute
 */
public class ReceiptsManagement extends javax.swing.JPanel {

    Receipt selectedReceipt = null;
    int indexSelectedReceipt = -1;
    AdminAction currentAction = null;

    HashMap<String, Receipt> mapReceipts = new HashMap<>();
    TableRowSorter<DefaultTableModel> rowSorter;

    boolean isSelectionChanged = false;

    public ReceiptsManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        init();
        disableEditingText();
        initTable();

        setTextDateTime(LocalDateTime.now());
    }

    public void init() {
        printButton.setVisible(false);
        addEventFilter();
    }

    public void addDataTable() {
        ReceiptController.addRowReceiptTable(receiptTable, mapReceipts);
    }

    public void initTable() {
        addDataTable();

        DefaultTableModel defaultTableModel = (DefaultTableModel) receiptTable.getModel();
        receiptTable.fixTable(jScrollPane2);
        rowSorter = new TableRowSorter<>(defaultTableModel);
        receiptTable.setRowSorter(rowSorter);

        ListSelectionModel selectionModel = receiptTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isSelectionChanged = true;

                if (currentAction == AdminAction.SEARCH) return;

                if (!e.getValueIsAdjusting()) {
                    int indexViewRow = receiptTable.getSelectedRow();

                    if (indexViewRow != -1) {
                        indexSelectedReceipt = receiptTable.convertRowIndexToModel(indexViewRow);

                        String receiptId = (String) receiptTable.getValueAt(indexViewRow, 0);
                        String recordId = (String) receiptTable.getValueAt(indexViewRow, 1);
                        LocalDateTime createAt = Utils.stringToLocalDateTimeWithTime((String) receiptTable.getValueAt(indexViewRow, 2));
                        double amount = Double.parseDouble((String) receiptTable.getValueAt(indexViewRow, 3));

                        selectedReceipt = new Receipt(receiptId, recordId, amount, createAt);

                        setText(selectedReceipt);
                    }
                }
            }
        });

        receiptTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isSelectionChanged) {
                    int row = receiptTable.rowAtPoint(e.getPoint());

                    if (row != -1 && receiptTable.convertRowIndexToModel(row) == indexSelectedReceipt) {
                        receiptTable.clearSelection();
                    }
                } else {
                    isSelectionChanged = false;
                }
            }
        });
    }

    public void filterTable() {
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        String receiptId = receiptIDTextField.getText().trim();
        String recordId = recordIDTextField.getText().trim();
        String amount = amountTextField.getText().trim();
        String year = receiptYear.getText().trim();
        String month = receiptMonth.getSelectedIndex() != 0 ? (String) Objects.requireNonNull(receiptMonth.getSelectedItem()) : "";
        String date = receiptDate.getText().trim();
        String hour = receiptHour.getText().trim();
        String minute = receiptMinute.getText().trim();

        if (!receiptId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + receiptId + ".*", 0));
        }
        if (!recordId.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + recordId + ".*", 1));
        }
        if (!amount.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i).*" + amount + ".*", 3));
        }

        String regex = Utils.getRegexDateTime(date, month, year, hour, minute);
        filters.add(RowFilter.regexFilter(regex, 2));

        SwingUtilities.invokeLater(() -> {
            // Thay đổi trạng thái giao diện ở đây
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        });
    }

    public void resetFilter() {
        rowSorter.setRowFilter(null);
    }

    public void addEventFilter() {
        JTextField[] listDataComponents = {receiptIDTextField, recordIDTextField, amountTextField, receiptYear, receiptDate, receiptHour, receiptMinute};

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
        receiptMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (currentAction == AdminAction.SEARCH) {
                    filterTable();
                }
            }
        });
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        receiptIDTextField.setEnabled(true);
        receiptIDTextField.setBackground(Color.WHITE);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        receiptDate.setEnabled(true);
        setComboBoxCustomDisabled(receiptMonth, false);
        receiptYear.setEnabled(true);
        receiptHour.setEnabled(true);
        receiptMinute.setEnabled(true);

        recordIDTextField.setEnabled(true);
        recordIDTextField.setBackground(Color.WHITE);

        amountTextField.setEnabled(true);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        receiptIDTextField.setEditable(false);
        receiptIDTextField.setBackground(new Color(220, 223, 228));
        receiptIDTextField.setDisabledTextColor(Color.BLACK);

        recordIDTextField.setEditable(false);
        recordIDTextField.setBackground(new Color(220, 223, 228));
        recordIDTextField.setDisabledTextColor(Color.BLACK);

        amountTextField.setEnabled(false);
        amountTextField.setDisabledTextColor(Color.BLACK);

        receiptDate.setEnabled(false);
        receiptDate.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(receiptMonth, true);

        receiptYear.setEnabled(false);
        receiptYear.setDisabledTextColor(Color.BLACK);
        receiptHour.setEnabled(false);
        receiptHour.setDisabledTextColor(Color.BLACK);
        receiptMinute.setEnabled(false);
        receiptYear.setDisabledTextColor(Color.BLACK);

    }

    public void setText(Receipt receipt) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        receiptIDTextField.setText(receipt.getReceiptId());
        recordIDTextField.setText(receipt.getRecordId());
        amountTextField.setText(String.valueOf(receipt.getAmount()));
        receiptDate.setText(String.format("%02d", receipt.getCreatedAt().getDayOfMonth()));
        receiptMonth.setSelectedItem(String.valueOf(receipt.getCreatedAt().getMonthValue()));
        receiptYear.setText(String.valueOf(receipt.getCreatedAt().getYear()));
        receiptHour.setText(String.format("%02d", receipt.getCreatedAt().getHour()));
        receiptMinute.setText(String.format("%02d", receipt.getCreatedAt().getMinute()));
    }

    public void clearText() {   // xóa hết giá trị của textField
        receiptIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        recordIDTextField.setText("");
        amountTextField.setText("");
        receiptDate.setText("");
        receiptMonth.setSelectedIndex(0);
        receiptYear.setText("");
        receiptHour.setText("");
        receiptMinute.setText("");
    }

    public void setTextDateTime(LocalDateTime lcDateTime) {
        receiptMonth.setSelectedItem(String.valueOf(lcDateTime.getMonthValue()));
        receiptDate.setText(String.valueOf(lcDateTime.getDayOfMonth()));
        receiptYear.setText(String.valueOf(lcDateTime.getYear()));
        receiptHour.setText(String.valueOf(lcDateTime.getHour()));
        receiptMinute.setText(String.valueOf(lcDateTime.getMinute()));
    }

    public boolean hasTextFieldEmpty() {
        return receiptIDTextField.getText().trim().isEmpty()
                || recordIDTextField.getText().trim().isEmpty()
                || amountTextField.getText().trim().isEmpty()
                || receiptDate.getText().trim().isEmpty()
                || receiptMonth.getSelectedIndex() == 0
                || receiptYear.getText().trim().isEmpty()
                || receiptHour.getText().trim().isEmpty()
                || receiptMinute.getText().trim().isEmpty();
    }

    public void disableSupportButton() {    // disable các nút hoàn tác, hủy, lưu, chon
//        undoButton.setEnabled(false);
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        chooseButton.setEnabled(false);
    }

    public void enableSupportButton() { // enable các nút hoàn tác, hủy, lưu, chon
//        undoButton.setEnabled(true);
        cancelButton.setEnabled(true);
        saveButton.setEnabled(true);
        chooseButton.setEnabled(true);
    }

    public void disableRemainMainButton(Button button) {  // tắt các nút chức năng ngoài nút được nhấn
        if (button == createReceiptButton) {
            updateButton.setEnabled(false);
            putOffReceiptButton.setEnabled(false);
            searchReceiptButton.setEnabled(false);
//            printButton.setEnabled(false);
        } else if (button == updateButton) {
            createReceiptButton.setEnabled(false);
            putOffReceiptButton.setEnabled(false);
            searchReceiptButton.setEnabled(false);
//            printButton.setEnabled(false);
        } else if (button == putOffReceiptButton) {
            createReceiptButton.setEnabled(false);
            updateButton.setEnabled(false);
            searchReceiptButton.setEnabled(false);
//            printButton.setEnabled(false);
        }
        else if (button == searchReceiptButton) {
            createReceiptButton.setEnabled(false);
            updateButton.setEnabled(false);
            putOffReceiptButton.setEnabled(false);
//            printButton.setEnabled(false);
        }
//        else if (jButton == printButton) {
//            createReceiptButton.setEnabled(false);
//            updateButton.setEnabled(false);
//            putOffReceiptButton.setEnabled(false);
//        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        createReceiptButton.setEnabled(true);
        updateButton.setEnabled(true);
        putOffReceiptButton.setEnabled(true);
        chooseButton.setEnabled(true);
        searchReceiptButton.setEnabled(true);
//        printButton.setEnabled(true);
    }


    public Receipt getReceiptFromTextField() {
        String receiptId = receiptIDTextField.getText().trim();
        String recordId = recordIDTextField.getText().trim();
        double amount = Double.parseDouble(amountTextField.getText().trim());
        LocalDateTime createAt = LocalDateTime.of(
                Integer.parseInt(receiptYear.getText().trim()),
                Integer.parseInt((String) Objects.requireNonNull(receiptMonth.getSelectedItem())),
                Integer.parseInt(receiptDate.getText().trim()),
                Integer.parseInt(receiptHour.getText().trim()),
                Integer.parseInt(receiptMinute.getText().trim())
        );

        return new Receipt(receiptId, recordId, amount, createAt);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        receiptTable = new Table();
        jLabel5 = new javax.swing.JLabel();
        receiptIDTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        recordIDTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        amountTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        createReceiptButton = new Button();
        putOffReceiptButton = new Button();
        searchReceiptButton = new Button();
        updateButton = new Button();
        receiptDate = new javax.swing.JTextField();
        receiptMonth = new javax.swing.JComboBox<>();
        receiptYear = new javax.swing.JTextField();
        receiptHour = new javax.swing.JTextField();
        receiptMinute = new javax.swing.JTextField();
        chooseButton = new Button();
        saveButton = new Button();
        cancelButton = new Button();
        undoButton = new Button();
        printButton = new Button();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel2.setBackground(new java.awt.Color(229, 245, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý hóa đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        receiptTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID hóa đơn", "ID hồ sơ", "Ngày tạo", "Số tiền"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        receiptTable.setFocusable(false);
        receiptTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(receiptTable);

        jLabel5.setText("ID hóa đơn:");

        receiptIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        receiptIDTextField.setEnabled(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("ID hồ sơ:");

        recordIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        recordIDTextField.setEnabled(false);
        recordIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordIDTextFieldActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Số tiền:");

        amountTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Ngày tạo:");

        createReceiptButton.setBackground(new java.awt.Color(102, 255, 255));
        createReceiptButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createReceiptButton.setText("Thanh toán");
        createReceiptButton.setFocusPainted(false);
        createReceiptButton.setFocusable(false);
        createReceiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createReceiptButtonActionPerformed(evt);
            }
        });

        putOffReceiptButton.setBackground(new java.awt.Color(102, 255, 255));
        putOffReceiptButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        putOffReceiptButton.setText("Hủy hóa đơn");
        putOffReceiptButton.setFocusPainted(false);
        putOffReceiptButton.setFocusable(false);
        putOffReceiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                putOffReceiptButtonActionPerformed(evt);
            }
        });

        searchReceiptButton.setBackground(new java.awt.Color(102, 255, 255));
        searchReceiptButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchReceiptButton.setText("Tìm kiếm");
        searchReceiptButton.setFocusPainted(false);
        searchReceiptButton.setFocusable(false);
        searchReceiptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchReceiptButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new java.awt.Color(102, 255, 255));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Sửa hóa đơn");
        updateButton.setFocusPainted(false);
        updateButton.setFocusable(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        receiptDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        receiptMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        receiptYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        receiptYear.setText("2025");
        receiptHour.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        receiptMinute.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        chooseButton.setBackground(new java.awt.Color(204, 204, 204));
        chooseButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        chooseButton.setText("Chọn");
        chooseButton.setFocusPainted(false);
        chooseButton.setFocusable(false);
        chooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonActionPerformed(evt);
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

        printButton.setBackground(new java.awt.Color(102, 255, 255));
        printButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        printButton.setText("In hóa đơn");
        printButton.setFocusPainted(false);
        printButton.setFocusable(false);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(107, 107, 107)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel5)))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(createReceiptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(receiptIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(recordIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(chooseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jLabel8)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(receiptDate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(receiptMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(receiptYear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(receiptHour, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(receiptMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(55, Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(putOffReceiptButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(searchReceiptButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20))))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane2))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(receiptIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8)
                                        .addComponent(receiptMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(receiptHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(receiptYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(receiptMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(receiptDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6)
                                        .addComponent(recordIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chooseButton))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(updateButton)
                                        .addComponent(createReceiptButton)
                                        .addComponent(putOffReceiptButton)
                                        .addComponent(cancelButton)
                                        .addComponent(saveButton)
                                        .addComponent(undoButton)
                                        .addComponent(searchReceiptButton)
                                        .addComponent(printButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 980, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 620, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void recordIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recordIDTextFieldActionPerformed

    private void searchReceiptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchReceiptButtonActionPerformed
        if (currentAction != AdminAction.SEARCH) {
            clearText();
            currentAction = AdminAction.SEARCH;

            disableRemainMainButton(searchReceiptButton);
            enableSupportButton();
            receiptIDTextField.setEditable(true);

            enableEditingText();
        }
    }

    private void createReceiptButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentAction != AdminAction.ADD) {
            clearText();
            currentAction = AdminAction.ADD;
            disableRemainMainButton(createReceiptButton);
            enableSupportButton();

            receiptIDTextField.setText(Utils.genUUID().toString());
            enableEditingText();

            setTextDateTime(LocalDateTime.now());
        }
    }

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentAction != AdminAction.UPDATE) {
            if (selectedReceipt == null) {
                Message obj = new Message(MainView.getFrames()[0], true);
                String ms = "Hãy chọn một hóa đơn để chỉnh sửa!";
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
        if (selectedReceipt == null) {
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            clearTextWithoutId();
            setTextDateTime(LocalDateTime.now());
        } else if (currentAction == AdminAction.UPDATE) {
            setText(selectedReceipt);
        } else if (currentAction == AdminAction.SEARCH) {
            clearText();
        }
    }

    private void chooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonActionPerformed
        DialogRecordChooser dialogRecordChooser;
        if(currentAction == AdminAction.SEARCH){
            dialogRecordChooser = new DialogRecordChooser(MainView.getFrames()[0], true, true);
        } else {
            dialogRecordChooser = new DialogRecordChooser(MainView.getFrames()[0], true, false);
        }
        dialogRecordChooser.showTable("Hãy chọn hồ sơ!");
        if (dialogRecordChooser.isOk()) {
            recordIDTextField.setText(dialogRecordChooser.getSelectedRecordId());
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        enableMainButton();
        disableSupportButton();
        disableEditingText();

        if (currentAction == AdminAction.SEARCH) {
            currentAction = null;

            if (selectedReceipt != null) {
                setText(selectedReceipt);
            } else {
                clearText();
            }
            resetFilter();
        } else if (currentAction == AdminAction.ADD) {
            currentAction = null;

            if (selectedReceipt != null) {
                setText(selectedReceipt);
            } else {
                clearText();
            }
        } else if (currentAction == AdminAction.UPDATE) {
            currentAction = null;

            setText(selectedReceipt);
        }
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        boolean rs = false;
        if (currentAction == AdminAction.SEARCH) {
            enableMainButton();
            disableSupportButton();
            disableEditingText();

            currentAction = null;
            receiptTable.clearSelection();
        } else {
            if (!hasTextFieldEmpty()) {
                switch (currentAction) {
                    case ADD -> {
                        Receipt receipt = getReceiptFromTextField();
                        rs = ReceiptController.addReceipt(receipt, receiptTable);
                        if(rs){
                            mapReceipts.put(receipt.getReceiptId(), receipt);
                        }
                    }
                    case UPDATE -> {
                        Receipt receipt = getReceiptFromTextField();
                        rs = ReceiptController.updateReceipt(indexSelectedReceipt, receipt, receiptTable);
                        if (rs) {
                            mapReceipts.replace(receipt.getReceiptId(), receipt);
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

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed

    }

    private void putOffReceiptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedReceipt == null) {
            ms = "Hãy chọn một hóa đơn để hủy!";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn hủy hóa đơn này không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            boolean rs = ReceiptController.deleteReceipt(indexSelectedReceipt, selectedReceipt, receiptTable);
            if (rs) {
                mapReceipts.remove(selectedReceipt.getReceiptId());
            }
            indexSelectedReceipt = -1;
            selectedReceipt = null;
        }

        currentAction = null;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTextField amountTextField;
    private Button cancelButton;
    private Button chooseButton;
    private Button createReceiptButton;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel2;
    private JScrollPane jScrollPane2;
    private Button printButton;
    private Button putOffReceiptButton;
    private JTextField receiptDate;
    private JTextField receiptHour;
    private JTextField receiptMinute;
    private JTextField receiptIDTextField;
    private JComboBox<String> receiptMonth;
    private Table receiptTable;
    private JTextField receiptYear;
    private JTextField recordIDTextField;
    private Button saveButton;
    private Button searchReceiptButton;
    private Button undoButton;
    private Button updateButton;
    // End of variables declaration//GEN-END:variables
}
