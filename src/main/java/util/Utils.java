package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import view.home.components.scrollbar.ScrollBarCustom;

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
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return lcDateTime.format(dtfDate);
    }

    public static LocalDateTime stringToLocalDateTime(String str){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return LocalDateTime.parse(str + " - 00:00:00",dtfDate);
    }

    public static String localDateTimeToStringWithTime(LocalDateTime lcDateTime){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        return lcDateTime.format(dtfDate);
    }

    public static String localDateTimeToStringWithTimeSql(LocalDateTime lcDateTime){
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
            comboBox.setBackground(Color.WHITE);
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

    public static <T extends Enum<T>> void setModelComboBox (JComboBox<String> jComboBox, Class<T> modelClass){
        T[] modelEnum = modelClass.getEnumConstants();

        String[] modelString = new String[modelEnum.length + 1];
        modelString[0] = "--Chọn--";

        for (int i = 1; i < modelString.length; i++) {
            modelString[i] = modelEnum[i-1].toString();
            System.out.println(modelEnum[i-1].toString());
        }
        jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(modelString));
    }

    public static void setModelMonthComboBox (JComboBox<String> jComboBox){
        String[] modelString = new String[13];
        modelString[0] = "--";

        for (int i = 1; i < modelString.length; i++) {
            modelString[i] = String.valueOf(i);
        }
        jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(modelString));
    }

    public static String getRegexDate(String day, String month, String year) {
        StringBuilder regex = new StringBuilder();

        if (!day.isEmpty()) {
            regex.append(String.format("^%02d/", Integer.parseInt(day)));
        } else {
            regex.append("^\\d{2}/");
        }

        if (!month.isEmpty()) {
            regex.append(String.format("%02d/", Integer.parseInt(month)));
        } else {
            regex.append("\\d{2}/");
        }

        if (!year.isEmpty()) {
            regex.append(String.format("%s.*", year));
        } else {
            regex.append(".*");
        }

        return regex.toString();
    }

    public static String getRegexDateTime(String day, String month, String year, String hour, String minute) {
        StringBuilder regex = new StringBuilder();

        if (!day.isEmpty()) {
            regex.append(String.format("^%02d/", Integer.parseInt(day)));
        } else {
            regex.append("^\\d{2}/");
        }

        if (!month.isEmpty()) {
            regex.append(String.format("%02d/", Integer.parseInt(month)));
        } else {
            regex.append("\\d{2}/");
        }

        if (!year.isEmpty()) {
            regex.append(String.format("%s.*", year));
        } else {
            regex.append("\\d{4} - ");
        }

        if (!hour.isEmpty()) {
            regex.append(String.format("%02d:", Integer.parseInt(hour)));
        } else {
            regex.append("\\d{2}:");
        }

        if (!minute.isEmpty()) {
            regex.append(String.format("%02d:.*", Integer.parseInt(minute)));
        } else {
            regex.append("\\d{2}:.*");
        }

        System.out.println(regex);

        return regex.toString();
    }
}