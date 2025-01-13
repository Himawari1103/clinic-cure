package view.login.panels;

import view.login.components.Button;
import view.login.components.PasswordField;
import view.login.components.TextField;

import java.awt.event.ActionListener;

public class PanelLogin extends javax.swing.JPanel {

    public PanelLogin() {
        initComponents();
        setOpaque(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUser = new TextField();
        jLabel1 = new javax.swing.JLabel();
        txtPass = new PasswordField();
        cmd = new Button();
        byPass = new Button();
        byPass.setVisible(false);

        txtUser.setLabelText("Tên người dùng");

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(79, 79, 79));
        jLabel1.setText("Đăng nhập");

        txtPass.setLabelText("Mật khẩu");

        cmd.setBackground(new java.awt.Color(25, 182, 247));
        cmd.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cmd.setForeground(new java.awt.Color(255, 255, 255));
        cmd.setText("Đăng nhập");
        cmd.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N


        byPass.setBackground(new java.awt.Color(255, 0, 0));
        byPass.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        byPass.setForeground(new java.awt.Color(255, 255, 255));
        byPass.setText("By Pass");
        byPass.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(byPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 192, Short.MAX_VALUE))
                    .addComponent(txtUser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(cmd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(byPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void addEventLogin(ActionListener event) {
        cmd.addActionListener(event);
    }

    public void addEventByPass(ActionListener event) {
        byPass.addActionListener(event);
    }

    public boolean checkUser() {
        boolean action = true;
        if (txtUser.getText().trim().isEmpty()) {
            txtUser.setHelperText("Hãy nhập tên người dùng");
            action = false;
        }
        if (String.valueOf(txtPass.getPassword()).trim().isEmpty()) {
            txtPass.setHelperText("Hãy nhập mật khẩu");
            action = false;
        }
        return action;
    }

    public String getUserName() {
        return txtUser.getText().trim();
    }

    public String getPassword() {
        return String.valueOf(txtPass.getPassword());
    }

    public Button getCmd() {
        return cmd;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button cmd;
    private Button byPass; //DEBUG!
    private javax.swing.JLabel jLabel1;
    private PasswordField txtPass;
    private TextField txtUser;
    // End of variables declaration//GEN-END:variables
}
