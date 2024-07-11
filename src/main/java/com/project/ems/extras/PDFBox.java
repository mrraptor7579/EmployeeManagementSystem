package com.project.ems.extras;
import com.project.ems.model.Employee;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PDFBox {
    public Boolean generateIDCard(Employee employee) {
        getProfilePic(employee.getEmpPic());
        getQrPic(employee.getEmpDetailQr());
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(200, 400)); // Width x Height
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Draw Background Color
            contentStream.setNonStrokingColor(60, 89, 234); // Navy Blue
            contentStream.addRect(0, 240, 200, 200);
            contentStream.fill();

            //draw footer
            contentStream.setNonStrokingColor(60, 89, 234); // Navy Blue
            contentStream.addRect(0, 10, 200, 25);
            contentStream.fill();
            //Heading
            // Name Text Section
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.setNonStrokingColor(204, 182, 170);
            contentStream.newLineAtOffset(85, 375); // X, Y position of the text
            contentStream.showText("EMS");
            contentStream.endText();
            //sub Head
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setNonStrokingColor(204, 182, 170);
            contentStream.newLineAtOffset(10, 360); // X, Y position of the text
            contentStream.showText("(Crust To Core)");
            contentStream.endText();
            // Name Text Section
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.newLineAtOffset(50, 210); // X, Y position of the text
            contentStream.showText(employee.getFirstName()+" "+employee.getLastName());
            contentStream.endText();

            // Additional Text Sections
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(65, 195); // X, Y position of the text
            contentStream.showText("Employee Code : "+employee.getEmpId());
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setNonStrokingColor(164, 165, 170);
            contentStream.newLineAtOffset(70, 183); // X, Y position of the text
            contentStream.showText(employee.getDesignation());
            contentStream.endText();

            // Draw Circle (Transparent)
            contentStream.setNonStrokingColor(255, 255, 255); // White with default opacity
            contentStream.addRect(50, 230, 100, 100);
            contentStream.fill();
            // Load and Draw Image
            BufferedImage image = ImageIO.read(new File("./profilePic.jpg"));
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, toByteArray(image), "image");
            float imageWidth = 100;
            float imageHeight = 100;
            contentStream.drawImage(pdImage, 50, 230, imageWidth, imageHeight);



            // Add QR Code Image
            BufferedImage qrCodeImage = ImageIO.read(new File("./empQRPic.png"));
            PDImageXObject pdQRCodeImage = PDImageXObject.createFromByteArray(document, toByteArray(qrCodeImage), "qrCodeImage");
            float qrCodeWidth = 140;
            float qrCodeHeight = 140;
            contentStream.drawImage(pdQRCodeImage, 30, 38, qrCodeWidth, qrCodeHeight);

            contentStream.close();

            document.save("custom.pdf");
            System.out.println("PDF created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Convert BufferedImage to byte array
    private static byte[] toByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
    public void getProfilePic(String empPic){
        String base64Image = empPic;
        String filePath = "profilePic.jpg"; // Output file path

        try {
            // Decode Base64 string to byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Create File object
            File imageFile = new File(filePath);

            // Write byte array to file
            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                outputStream.write(imageBytes);
            }

            System.out.println("File created successfully: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getQrPic(String empQRPic){
        String base64Image = empQRPic;
        String filePath = "empQRPic.png"; // Output file path

        try {
            // Decode Base64 string to byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Create File object
            File imageFile = new File(filePath);

            // Write byte array to file
            try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                outputStream.write(imageBytes);
            }

            System.out.println("File created successfully: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
