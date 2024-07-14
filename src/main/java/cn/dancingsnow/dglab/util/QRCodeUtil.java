package cn.dancingsnow.dglab.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class QRCodeUtil {

    public static InputStream generateQRCode(String string) throws IOException, WriterException {
        return generateQRCode(string, 256, 256);
    }

    public static InputStream generateQRCode(String text, int width, int height)
            throws IOException, WriterException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 创建二维码编码器
        BitMatrix bitMatrix =
                new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        // 将BitMatrix转换为BufferedImage
        BufferedImage image = toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        int onColor = 0xFF000000;
        int offColor = 0xFFFFFFFF;
        int[] rowPixels = new int[width];
        BitArray row = new BitArray(width);
        for (int y = 0; y < height; y++) {
            row = matrix.getRow(y, row);
            for (int x = 0; x < width; x++) {
                rowPixels[x] = row.get(x) ? onColor : offColor;
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }
        return image;
    }
}
