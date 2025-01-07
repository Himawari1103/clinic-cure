package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import view.components.main.components.scrollbar.ScrollBarCustom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {
    public static byte[] iconToBytes(Icon icon){
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null,bufferedImage.getGraphics(),0,0);
        // Chuyển BufferedImage thành byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos); // Lưu ảnh dưới dạng PNG
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }

    public static Date localDateToSqlDate(LocalDate lcDate){
        return Date.valueOf(lcDate);
    }

    public static LocalDate sqlDateToLocalDate(Date date){
        return date.toLocalDate();
    }

    public static Timestamp localDateTimeToSqlTimestamp(LocalDateTime lcDateTime){
        return Timestamp.valueOf(lcDateTime);
    }

    public static LocalDateTime sqlTimestampToLocalDateTime(Timestamp timestamp){
        return timestamp.toLocalDateTime();
    }

    public static String localDateToString(LocalDate lcDate){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return lcDate.format(dtfDate);
    }

    public static LocalDate stringToLocalDate(String str){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(str,dtfDate);
    }

    public static String localDateTimeToString(LocalDateTime lcDateTime){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        return lcDateTime.format(dtfDate);
    }

    public static LocalDateTime stringToLocalDateTime(String str){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        return LocalDateTime.parse(str,dtfDate);
    }

    public static String localDateTimeToStringWithTime(LocalDateTime lcDateTime){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return lcDateTime.format(dtfDate);
    }

    public static LocalDateTime stringToLocalDateTimeWithTime(String str){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return LocalDateTime.parse(str,dtfDate);
    }

    public static UUID genUUID(){
        return UUID.randomUUID();
    }

    public static String encrypt(String str){
        String encryptString = "";

        Argon2 argon2 = Argon2Factory.create();

        int iterations = 3;     // Số vòng lặp
        int memory = 65536;     // Kích thước bộ nhớ (kB)
        int parallelism = 1;    // Số luồng xử lý song song

        try {
            encryptString = argon2.hash(iterations, memory, parallelism, str);
        } finally {
            argon2.wipeArray(str.toCharArray());
        }
        System.out.println(encryptString);
        System.out.println(verifyPassword(encryptString,str));
        return encryptString;
    }

    public static boolean verifyPassword(String pwDB, String pw){
        return Argon2Factory.create().verify(pwDB,pw);
    }

    public static void setComboBoxCustomDisabled(JComboBox<?> comboBox, boolean disabled) {
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