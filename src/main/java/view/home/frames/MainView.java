package view.home.frames;

import constants.MenuType;
import controller.login.LogoutController;
import view.home.components.panels.Header;
import view.home.components.panels.Menu;
import view.home.components.event.EventMenuSelected;
import view.home.components.panels.MainPanel;
import view.home.components.icon.GoogleMaterialDesignIcons;
import view.home.components.icon.IconFontSwing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import view.home.main.panels.*;

import javax.swing.*;


public class MainView extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainPanel mainPanel;
    private Animator animator;

    public MainView() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents();
        init();



        setVisible(true);
    }

    private void showExitDialog() {
        JDialog exitDialog = new JDialog(this, "Bạn muốn đăng xuất hay thoát?", true);
        exitDialog.setLayout(new FlowLayout());
        exitDialog.setSize(300, 100);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Đăng xuất...");
                LogoutController.logoutFromMain(MainView.this);
                exitDialog.dispose();
            }
        });

        JButton btnExit = new JButton("Thoát");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        exitDialog.add(btnLogout);
        exitDialog.add(btnExit);

        exitDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        exitDialog.setLocationRelativeTo(this);
        exitDialog.setVisible(true);
    }

    private void init() {
        layout = new MigLayout("fill", "0[]0[100%, fill]0", "0[fill, top]0");
        bg.setLayout(layout);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitDialog();
            }
        });

        menu = new Menu();
        header = new Header();
        mainPanel = new MainPanel();
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(MenuType menuType, MenuType subMenuType) {
//                switch (menuType) {
//                    case REPORT -> mainPanel.showForm(new ReportManagement(), menuType);
//                    case PATIENT -> mainPanel.showForm(new PatientsManagement(), menuType);
//                    case STAFF -> mainPanel.showForm(new StaffManagement(), menuType);
//                    case APPOINTMENT -> mainPanel.showForm(new AppointmentManagement(), menuType);
//                    case RECORD -> mainPanel.showForm(new RecordsMagagement(), menuType);
//                    case RECEIPT -> mainPanel.showForm(new ReceiptsManagement(), menuType);
//                    case ACCOUNT -> mainPanel.showForm(new AccountManagement(), menuType);
//                }
                mainPanel.showForm(menuType);
            }
        });
        menu.initMenuItem();
        bg.add(menu, "w 250!, spany 2");    // Span Y 2cell
        bg.add(header, "h 50!, wrap");
        bg.add(mainPanel, "w 100%, h 100%");
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menu.isShowMenu()) {
                    width = 60 + (190 * (1f - fraction));
                } else {
                    width = 60 + (190 * fraction);  // 60 + 190 = width Menu
                }
                layout.setComponentConstraints(menu, "w " + width + "!, spany2");
                menu.revalidate();
            }

            @Override
            public void end() {
                menu.setShowMenu(!menu.isShowMenu());
                menu.setEnableMenu(true);
            }

        };
        animator = new Animator(500, target);
        animator.setResolution(0);
        animator.setDeceleration(0.5f);
        animator.setAcceleration(0.5f);
        header.addMenuEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
                menu.setEnableMenu(false);
            }
        });
        //  Init google icon font
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        //  Start with this form
//        mainPanel.showForm(new ReportManagement(), MenuType.REPORT);
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        setUndecorated(true);
        setResizable(false);
        bg.setBackground(new java.awt.Color(245, 245, 245));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
                bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1366, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
                bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 783, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}
