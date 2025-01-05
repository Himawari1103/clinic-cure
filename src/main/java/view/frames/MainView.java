package view.frames;

import controller.login.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainView extends view.components.main.frames.MainView {
    public MainView() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitDialog();
            }
        });
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
}
