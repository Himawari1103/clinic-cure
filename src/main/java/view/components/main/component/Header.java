package view.components.main.component;

import model.model.AccountModel;
import view.components.main.swing.Button;
import view.components.main.swing.ImageAvatar;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class Header extends javax.swing.JPanel {

    public Header() {
        initComponents();
        currentTime();
    }

    public void addMenuEvent(ActionListener event) {
        cmdMenu.addActionListener(event);
    }

    public void currentTime() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        Thread clock = new Thread() {
            public void run() {
                while (true) {
                    jLabelDate.setText(LocalDateTime.now().format(dtfDate));
                    jLabelTime.setText(LocalDateTime.now().format(dtfTime));
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        clock.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelDate = new JLabel();
        jLabelTime = new JLabel();
        jLabelDate.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(0, 0, 0));

        jLabelTime.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(0, 0, 0));

        cmdMenu = new Button();
        pic = new ImageAvatar();
        lbUserName = new javax.swing.JLabel();
        lbRole = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
//        buttonBadges1 = new ButtonBadges();
//        buttonBadges2 = new ButtonBadges();

        setBackground(new java.awt.Color(255, 255, 255));

        cmdMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon_main/menu.png"))); // NOI18N

        pic.setIcon(AccountModel.getAccount().getAvatar()); // NOI18N

        lbUserName.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lbUserName.setForeground(new java.awt.Color(127, 127, 127));
        lbUserName.setText(AccountModel.getAccount().getUsername());

        lbRole.setForeground(new java.awt.Color(127, 127, 127));
        lbRole.setText(AccountModel.getAccount().getAccountType().toString());

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

//        buttonBadges1.setForeground(new java.awt.Color(250, 49, 49));
//        buttonBadges1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon_main/notification.png"))); // NOI18N
//        buttonBadges1.setBadges(12);
//
//        buttonBadges2.setForeground(new java.awt.Color(63, 178, 232));
//        buttonBadges2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon_main/message.png"))); // NOI18N
//        buttonBadges2.setBadges(5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmdMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, Short.MAX_VALUE)
//                .addComponent(buttonBadges2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
//                .addComponent(buttonBadges1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelDate, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabelTime, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbRole, javax.swing.GroupLayout.Alignment.TRAILING))

                                .addGap(18, 18, 18)
                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabelDate)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabelTime))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(lbUserName)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lbRole))
                                                .addComponent(cmdMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(pic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jSeparator1)
//                    .addComponent(buttonBadges1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    .addComponent(buttonBadges2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                )
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
//    private ButtonBadges buttonBadges1;
//    private ButtonBadges buttonBadges2;
    private Button cmdMenu;
    private JSeparator jSeparator1;
    private JLabel lbRole;
    private JLabel lbUserName;
    private ImageAvatar pic;
    private JLabel jLabelDate;
    private JLabel jLabelTime;
    // End of variables declaration//GEN-END:variables
}
