package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
