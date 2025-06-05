package com.example.quickstart.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QrCodeGenerator {

    public static byte[] generateQrCode(String text, String fileName) throws IOException, WriterException {
        String basePath = "src/main/resources/static/qrcodes";
        Path dirPath = Paths.get(basePath);

        // Create directory if it doesn't exist
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(fileName + ".png");
        int width = 300;
        int height = 300;

        // Generate QR code
        BitMatrix matrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height);

        // Write to file
        MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);

        // Return the file bytes
        return Files.readAllBytes(filePath);
    }
}