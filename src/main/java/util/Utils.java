package util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import javax.imageio.ImageIO;
import javax.swing.*;
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
            // Giải phóng tài nguyên của Argon2
            argon2.wipeArray(str.toCharArray());
        }

        return encryptString;
    }
}
