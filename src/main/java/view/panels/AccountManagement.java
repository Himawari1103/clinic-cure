/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.panels;

import constants.AccountType;
import constants.AdminAction;
import controller.main.AccountController;
import controller.main.AccountController;
import model.base.Account;
import util.Utils;
import view.components.main.components.scrollbar.ScrollBarCustom;
import view.components.main.components.table.Table;
import view.components.main.dialog.Message;
import view.components.main.dialog.MessageResultAdminAction;
import view.frames.MainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Chi Cute
 */
public class AccountManagement extends javax.swing.JPanel {

    Account selectedAccount = null;
    int indexSelectedAccount = -1;
    AdminAction currentAction = null;

    DefaultTableModel defaultTableModelMain;

    public AccountManagement() {
        initComponents();
        disableEditingText();
        disableSupportButton();

        initTable();
        setTextDate(LocalDate.now());
    }

    public void enableEditingText() {   // bật edit tất cả các textField
        accountIDTextField.setEnabled(true);
        enableEditingTextWithOutId();
    }

    public void enableEditingTextWithOutId() {  // bật edit tất cả các textField ngoại trừ id
        loginNameTextField.setEnabled(true);
        passwordTextField.setEnabled(true);
        setComboBoxCustomDisabled(accountMonth, false);
        emailTextField.setEnabled(true);
        accountDate.setEnabled(true);
        setComboBoxCustomDisabled(accountTypeComboBox, false);
        accountYear.setEnabled(true);
    }

    public void disableEditingText() {  // tắt edit tất cả các textField
        accountIDTextField.setEditable(false);
        loginNameTextField.setDisabledTextColor(Color.BLACK);

        loginNameTextField.setEnabled(false);
        loginNameTextField.setDisabledTextColor(Color.BLACK);

        passwordTextField.setEnabled(false);
        passwordTextField.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(accountMonth, true);

        emailTextField.setEnabled(false);
        emailTextField.setDisabledTextColor(Color.BLACK);

        accountDate.setEnabled(false);
        accountDate.setDisabledTextColor(Color.BLACK);

        setComboBoxCustomDisabled(accountTypeComboBox, true);

        accountYear.setEnabled(false);
        accountYear.setDisabledTextColor(Color.BLACK);
    }

    public void setTextDate(LocalDate lcDate){
        accountMonth.setSelectedItem(String.valueOf(lcDate.getMonthValue()));
        accountDate.setText(String.valueOf(lcDate.getDayOfMonth()));
        accountYear.setText(String.valueOf(lcDate.getYear()));
    }

    public void setText(Account account) {  // Thiết lập giá trị của textField thông qua đối tượng được quản lý
        accountIDTextField.setText(account.getAccountId());
        loginNameTextField.setText(account.getUsername());
        passwordTextField.setText(account.getPassword());
        accountMonth.setSelectedItem(String.valueOf(account.getCreatedAt().getMonthValue()));
        emailTextField.setText(account.getEmail());
        accountDate.setText(String.valueOf(account.getCreatedAt().getDayOfMonth()));
        accountTypeComboBox.setSelectedItem(account.getAccountType().getDetail());
        accountYear.setText(String.valueOf(account.getCreatedAt().getYear()));
    }

    public void clearText() {
        accountIDTextField.setText("");
        clearTextWithoutId();
    }

    public void clearTextWithoutId() {   // xóa hết giá trị của textField
        loginNameTextField.setText("");
        passwordTextField.setText("");
        accountMonth.setSelectedItem("1");
        emailTextField.setText("");
        accountDate.setText("");
        accountTypeComboBox.setSelectedItem(AccountType.ADMIN.getDetail());
        accountYear.setText("");
    }

    public Account getAccountFromTextField() {
        String accountID = accountIDTextField.getText().trim();
        String username = loginNameTextField.getText().trim();
        String password = passwordTextField.getText().trim();
        String email = emailTextField.getText().trim();
        AccountType accountType = AccountType.getAccountTypeFromDetail((String) accountTypeComboBox.getSelectedItem());
        LocalDateTime createdAt = LocalDateTime.of(
                Integer.parseInt(accountYear.getText().trim()),
                Integer.parseInt(Objects.requireNonNull(accountMonth.getSelectedItem()).toString()),
                Integer.parseInt(accountDate.getText().trim()),
                0, 0);

        return new Account(accountID, username, password, email, accountType, createdAt);
    }

    public boolean hasTextFieldEmpty() {
        return accountIDTextField.getText().trim().isEmpty()
                || loginNameTextField.getText().trim().isEmpty()
                || passwordTextField.getText().trim().isEmpty()
                || emailTextField.getText().trim().isEmpty()
                || accountDate.getText().trim().isEmpty()
                || accountYear.getText().trim().isEmpty();
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
        if (jButton == createAccountButton) {
            updateButton.setEnabled(false);
            deleteAccountButton.setEnabled(false);
        } else if (jButton == updateButton) {
            createAccountButton.setEnabled(false);
            deleteAccountButton.setEnabled(false);
        } else if (jButton == deleteAccountButton) {
            createAccountButton.setEnabled(false);
            updateButton.setEnabled(false);
        }
    }

    public void enableMainButton() {    // bật tất cả các nút chức năng
        createAccountButton.setEnabled(true);
        updateButton.setEnabled(true);
        deleteAccountButton.setEnabled(true);
    }


    public void initTable() {
        addDataTable();

        defaultTableModelMain = (DefaultTableModel) accountTable.getModel();
        accountTable.fixTable(jScrollPane1);

        ListSelectionModel selectionModel = accountTable.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    indexSelectedAccount = accountTable.getSelectedRow();
                    if (indexSelectedAccount != -1) {
                        String accountID = (String) accountTable.getValueAt(indexSelectedAccount, 0);
                        String username = (String) accountTable.getValueAt(indexSelectedAccount, 1);
                        String password = (String) accountTable.getValueAt(indexSelectedAccount, 2);
                        String email = (String) accountTable.getValueAt(indexSelectedAccount, 3);
                        AccountType accountType = AccountType.getAccountTypeFromDetail((String) accountTable.getValueAt(indexSelectedAccount, 4));
                        LocalDateTime createdAt = Utils.stringToLocalDateTime((String) accountTable.getValueAt(indexSelectedAccount, 5));

                        selectedAccount = new Account(accountID, username, password, email, accountType, createdAt);

                        setText(selectedAccount);
                    }
                }
            }
        });
    }

    public void addDataTable() {
        AccountController.addRowAccountTable(accountTable);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        accountTable = new Table();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        accountIDTextField = new javax.swing.JTextField();
        loginNameTextField = new javax.swing.JTextField();
        passwordTextField = new javax.swing.JTextField();
        accountTypeComboBox = new javax.swing.JComboBox<>();
        createAccountButton = new javax.swing.JButton();
        deleteAccountButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        accountDate = new javax.swing.JTextField();
        accountMonth = new javax.swing.JComboBox<>();
        accountYear = new javax.swing.JTextField();

        setBackground(new java.awt.Color(229, 245, 255));

        jPanel2.setBackground(new java.awt.Color(229, 245, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý tài khoản", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        accountTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "ID", "Tên đăng nhập", "Mật khẩu", "Email", "Loại tài khoản", "Ngày tạo"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        accountTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(accountTable);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("ID:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Tên đăng nhập:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Loại tài khoản:");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Mật khẩu:");

        accountIDTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        accountIDTextField.setEnabled(false);

        loginNameTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        loginNameTextField.setEnabled(false);

        passwordTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordTextField.setEnabled(false);

        accountTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Nhân viên", "Quản trị viên"}));
        accountTypeComboBox.setEnabled(false);
        accountTypeComboBox.setFocusable(false);

        createAccountButton.setBackground(new java.awt.Color(102, 255, 255));
        createAccountButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createAccountButton.setText("Tạo tài khoản");
        createAccountButton.setFocusPainted(false);
        createAccountButton.setFocusable(false);
        createAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountButtonActionPerformed(evt);
            }
        });

        deleteAccountButton.setBackground(new java.awt.Color(102, 255, 255));
        deleteAccountButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteAccountButton.setText("Xóa tài khoản");
        deleteAccountButton.setFocusPainted(false);
        deleteAccountButton.setFocusable(false);
        deleteAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAccountButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new java.awt.Color(102, 255, 255));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Cập nhật");
        updateButton.setFocusPainted(false);
        updateButton.setFocusable(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
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

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Email:");

        emailTextField.setEnabled(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Ngày tạo:");

        accountDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        accountMonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));

        accountYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        accountYear.setText("2025");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addComponent(createAccountButton)
                                                .addGap(30, 30, 30)
                                                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33)
                                                .addComponent(deleteAccountButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(accountIDTextField)
                                                        .addComponent(loginNameTextField))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passwordTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                                        .addComponent(emailTextField))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(undoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(accountDate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(accountMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(accountYear, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(accountTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(73, 73, 73))))
                        .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(accountIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel5)
                                                .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel6)
                                                .addComponent(accountYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(accountMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(accountDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(loginNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(accountTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(createAccountButton)
                                        .addComponent(updateButton)
                                        .addComponent(deleteAccountButton)
                                        .addComponent(saveButton)
                                        .addComponent(searchButton)
                                        .addComponent(cancelButton)
                                        .addComponent(undoButton))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void createAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountButtonActionPerformed
        if (currentAction != AdminAction.ADD) clearText();
        currentAction = AdminAction.ADD;
        disableRemainMainButton(createAccountButton);
        enableSupportButton();

        accountIDTextField.setText(Utils.genUUID().toString());
        enableEditingText();

        setTextDate(LocalDate.now());
    }

    private void deleteAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAccountButtonActionPerformed
        currentAction = AdminAction.DELETE;
        Message obj = new Message(MainView.getFrames()[0], true);
        String ms = "";
        boolean withAction;
        if (selectedAccount == null) {
            ms = "Không có đối tượng để xóa";
            withAction = false;
        } else {
            ms = "Bạn có chắc chắn muốn xóa " + selectedAccount.getUsername() + " không?";
            withAction = true;
        }
        obj.showMessage(ms, withAction);
        if (obj.isOk()) {
            AccountController.deleteAccount(indexSelectedAccount, selectedAccount, accountTable);
            AccountController.addRowAccountTable(defaultTableModelMain);
            indexSelectedAccount = -1;
            selectedAccount = null;
        }

        currentAction = null;
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        currentAction = AdminAction.UPDATE;
        disableRemainMainButton(updateButton);
        enableSupportButton();
        enableEditingTextWithOutId();
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (selectedAccount != null) {
            setText(selectedAccount);
        } else {
            if(currentAction == AdminAction.ADD){
                clearTextWithoutId();
            } else {
                clearText();
            }
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        enableMainButton();
        disableSupportButton();
        disableEditingText();

        if (selectedAccount != null) {
            setText(selectedAccount);
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
                    rs = AccountController.addAccount(getAccountFromTextField(), accountTable);
                    AccountController.addRowAccountTable(defaultTableModelMain);
                }
                case UPDATE -> {
                    rs = AccountController.updateAccount(indexSelectedAccount, getAccountFromTextField(), accountTable);
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accountIDTextField;
    private Table accountTable;
    private javax.swing.JComboBox<String> accountTypeComboBox;
    private javax.swing.JTextField accountDate;
    private javax.swing.JComboBox<String> accountMonth;
    private javax.swing.JTextField accountYear;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton createAccountButton;
    private javax.swing.JButton deleteAccountButton;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField loginNameTextField;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton updateButton;

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
}
