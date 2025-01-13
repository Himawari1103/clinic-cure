package view.login.panels;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.Objects;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import model.base.Account;
import model.dao.AccountDao;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import view.login.components.Button;
import view.login.components.EditButton;
import view.login.components.ImageAvatar;
import view.login.components.PanelTransparent;

public class PanelLoading extends javax.swing.JLayeredPane {

    public Account getData() {
        return data;
    }

    private final Animator animator;
    private boolean slideLeft;
    private float animate;
    private boolean isMessage;
    private Account data;

    public PanelLoading() {
        initComponents();
        loading.setVisible(true);
        profile.setVisible(false);
        message.setVisible(false);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                if (isMessage) {
                    message.setVisible(true);
                } else {
                    profile.setVisible(true);
                }
            }

            @Override
            public void timingEvent(float fraction) {
                if (isMessage) {
                    message.setAlpha(fraction);
                    loading.setAlpha(1f - fraction);
                } else {
                    profile.setAlpha(fraction);
                    loading.setAlpha(1f - fraction);
                }
                repaint();
            }
        };
        animator = new Animator(500, target);
        animator.setResolution(0);
    }

    public void setAnimate(boolean slideLeft, float animate) {
        this.slideLeft = slideLeft;
        this.animate = animate;
    }

    public void addEventBack(ActionListener event) {
        cmdCancelLoading.addActionListener(event);
        cmdCancelSuccess.addActionListener(event);
        cmdCancelError.addActionListener(event);
    }

    public void addEventContinue(ActionListener event) {
        cmdContinue.addActionListener(event);
    }

    public void doneLogin(Account data) {
        isMessage = false;
        this.data = data;
        Icon avt = data.getAvatar();
        pic.setIcon(
                Objects.requireNonNullElseGet(
                        avt, () -> new ImageIcon(
                                Objects.requireNonNull(getClass().getResource("/icon_login/user.png"))
                        )
                )
        );
        cmdContinue.setText("Tiếp tục với " + data.getUsername());
        animator.start();
    }

    public void showError(String ms) {
        lbMessage.setText(ms);
        isMessage = true;
        animator.start();
    }

    public void reset() {
        loading.setAlpha(1f);
        loading.setVisible(true);
        profile.setVisible(false);
        message.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        profile = new PanelTransparent();
        cmdCancelSuccess = new Button();
        pic = new ImageAvatar();
        cmdEdit = new EditButton();
        cmdContinue = new Button();
        loading = new PanelTransparent();
        cmdCancelLoading = new Button();
        jLabel1 = new javax.swing.JLabel();
        message = new PanelTransparent();
        cmdCancelError = new Button();
        lbMessage = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        cmdCancelSuccess.setBackground(new java.awt.Color(67, 99, 132));
        cmdCancelSuccess.setForeground(new java.awt.Color(255, 255, 255));
        cmdCancelSuccess.setText("Quay lại");

        pic.setBackground(new java.awt.Color(67, 99, 132));
        pic.setForeground(new java.awt.Color(188, 188, 188));
        pic.setBorderSize(3);
        pic.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon_login/user.png")))); // NOI18N
        pic.setOpaque(true);

        cmdEdit.setBackground(new java.awt.Color(55, 55, 55));
        cmdEdit.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon_login/edit.png")))); // NOI18N
        cmdEdit.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout picLayout = new javax.swing.GroupLayout(pic);
        pic.setLayout(picLayout);
        picLayout.setHorizontalGroup(
                picLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, picLayout.createSequentialGroup()
                                .addContainerGap(104, Short.MAX_VALUE)
                                .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        picLayout.setVerticalGroup(
                picLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, picLayout.createSequentialGroup()
                                .addContainerGap(104, Short.MAX_VALUE)
                                .addComponent(cmdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        cmdContinue.setBackground(new java.awt.Color(88, 130, 172));
        cmdContinue.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cmdContinue.setForeground(new java.awt.Color(255, 255, 255));
        cmdContinue.setText("Continue");
        cmdContinue.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N

        javax.swing.GroupLayout profileLayout = new javax.swing.GroupLayout(profile);
        profile.setLayout(profileLayout);
        profileLayout.setHorizontalGroup(
                profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profileLayout.createSequentialGroup()
                                .addContainerGap(125, Short.MAX_VALUE)
                                .addComponent(cmdCancelSuccess, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(125, 125, 125))
                        .addGroup(profileLayout.createSequentialGroup()
                                .addGroup(profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(profileLayout.createSequentialGroup()
                                                .addGap(100, 100, 100)
                                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(profileLayout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(cmdContinue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(50, 50, 50))
        );
        profileLayout.setVerticalGroup(
                profileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profileLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(cmdContinue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                                .addComponent(cmdCancelSuccess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
        );

        add(profile, "card2");

        cmdCancelLoading.setBackground(new java.awt.Color(67, 99, 132));
        cmdCancelLoading.setForeground(new java.awt.Color(255, 255, 255));
        cmdCancelLoading.setText("Quay lại");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon_login/loading.gif")))); // NOI18N

        javax.swing.GroupLayout loadingLayout = new javax.swing.GroupLayout(loading);
        loading.setLayout(loadingLayout);
        loadingLayout.setHorizontalGroup(
                loadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingLayout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(cmdCancelLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(125, 125, 125))
                        .addGroup(loadingLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(30, 30, 30))
        );
        loadingLayout.setVerticalGroup(
                loadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                                .addComponent(cmdCancelLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
        );

        add(loading, "card2");

        cmdCancelError.setBackground(new java.awt.Color(67, 99, 132));
        cmdCancelError.setForeground(new java.awt.Color(255, 255, 255));
        cmdCancelError.setText("Quay lại");

        lbMessage.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        lbMessage.setForeground(new java.awt.Color(235, 90, 90));
        lbMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbMessage.setText("Message");

        javax.swing.GroupLayout messageLayout = new javax.swing.GroupLayout(message);
        message.setLayout(messageLayout);
        messageLayout.setHorizontalGroup(
                messageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messageLayout.createSequentialGroup()
                                .addContainerGap(125, Short.MAX_VALUE)
                                .addComponent(cmdCancelError, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(125, 125, 125))
                        .addGroup(messageLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        messageLayout.setVerticalGroup(
                messageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messageLayout.createSequentialGroup()
                                .addContainerGap(177, Short.MAX_VALUE)
                                .addComponent(lbMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(147, 147, 147)
                                .addComponent(cmdCancelError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
        );

        add(message, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName().toLowerCase();
                return file.isDirectory() || name.endsWith(".png") || name.endsWith(".jpg");
            }

            @Override
            public String getDescription() {
                return "Image File";
            }
        });
        int option = ch.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = ch.getSelectedFile();
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            pic.setIcon(icon);
            repaint();
            data.setAvatar(icon);
            AccountDao.getInstance().update(data);
        }
    }//GEN-LAST:event_cmdEditActionPerformed

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        int width = getWidth();
        int height = getHeight();
        int x = 0;
        int y = 0;
        if (slideLeft) {
            int centerY = height / 2;
            Path2D.Float p = new Path2D.Float();
            p.moveTo(x, y);
            p.lineTo(width, y);
            p.lineTo(width, height);
            p.lineTo(x, height);
            p.curveTo(x, height, easeOutBounce(animate) * width, centerY, x, y);
            g2.fill(p);
        } else {
            g2.fillRect(x, y, width, height);
        }
        g2.dispose();
        super.paint(grphcs);
    }

    private float easeOutBounce(float x) {
        float n1 = 7.5625f;
        float d1 = 2.75f;
        double v;
        if (x < 1 / d1) {
            v = n1 * x * x;
        } else if (x < 2 / d1) {
            v = n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            v = n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            v = n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
        if (slideLeft) {
            return (float) (1f - v);
        } else {
            return (float) v;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Button cmdCancelLoading;
    private Button cmdCancelSuccess;
    private Button cmdCancelError;
    private Button cmdContinue;
    private EditButton cmdEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lbMessage;
    private PanelTransparent loading;
    private PanelTransparent message;
    private ImageAvatar pic;
    private PanelTransparent profile;
    // End of variables declaration//GEN-END:variables
}
