package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Thiết lập frame
        setTitle("Nhấn Enter hoặc Nút");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo nút và thiết lập hành động
        JButton button = new JButton("Nhấn vào tôi");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAction();
            }
        });

        // Thiết lập KeyListener để lắng nghe phím Enter
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Gọi doClick để mô phỏng nhấn nút
                    System.out.println("debug!");
                    button.doClick();
                }
            }
        });

        // Chú ý để có thể lắng nghe sự kiện phím
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        // Thêm nút vào frame
        setLayout(new FlowLayout());
        add(button);
    }

    // Phương thức thực hiện hành động
    private void performAction() {
        JOptionPane.showMessageDialog(this, "Hành động đã được thực hiện!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}

