package Atomicity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage
{

    // Map representing hex value and its corresponding color
    public static Map<Character, Color> hexToCharMap = new HashMap<>();

    static
    {
        // Assigning distinct colors to hexadecimal characters
        hexToCharMap.put('0', CustomColors.COLOR_0.getColor());
        hexToCharMap.put('1', CustomColors.COLOR_1.getColor());
        hexToCharMap.put('2', CustomColors.COLOR_2.getColor());
        hexToCharMap.put('3', CustomColors.COLOR_3.getColor());
        hexToCharMap.put('4', CustomColors.COLOR_4.getColor());
        hexToCharMap.put('5', CustomColors.COLOR_5.getColor());
        hexToCharMap.put('6', CustomColors.COLOR_6.getColor());
        hexToCharMap.put('7', CustomColors.COLOR_7.getColor());
        hexToCharMap.put('8', CustomColors.COLOR_8.getColor());
        hexToCharMap.put('9', CustomColors.COLOR_9.getColor());
        hexToCharMap.put('A', CustomColors.COLOR_10.getColor());
        hexToCharMap.put('B', CustomColors.COLOR_11.getColor());
        hexToCharMap.put('C', CustomColors.COLOR_12.getColor());
        hexToCharMap.put('D', CustomColors.COLOR_13.getColor());
        hexToCharMap.put('E', CustomColors.COLOR_14.getColor());
        hexToCharMap.put('F', CustomColors.COLOR_15.getColor());
    }




    public static void main(String[] args)
    {

        // Data of the file read and written in its hexadecimal form
        String zipHexadecimal = convertFileToHex("C:\\Users\\99557\\OneDrive\\Desktop\\hellozip.zip");

        // Matrix where encoded data will be stored and then will be converted into corresponding image 1920 × 1080
        List<List<Color>> encodedImageMatrix = new ArrayList<>(1080);


        List<Color> row1OfEncodedImageMatrix = new ArrayList<>(1920);
        List<Color> row2OfEncodedImageMatrix = new ArrayList<>(1920);
        List<Color> row3OfEncodedImageMatrix = new ArrayList<>(1920);
        List<Color> row4OfEncodedImageMatrix = new ArrayList<>(1920);

        for (int i = 0; i < zipHexadecimal.length(); i++)
        {
            // If size of each row exceeds 1920 - 4 (because there is 4 x 4 pixels) add them to the matrix
            // NOTE: all four rows have same length, so we only check first one
            if (row1OfEncodedImageMatrix.size() > 1916)
            {
                encodedImageMatrix.add(row1OfEncodedImageMatrix);
                encodedImageMatrix.add(row2OfEncodedImageMatrix);
                encodedImageMatrix.add(row3OfEncodedImageMatrix);
                encodedImageMatrix.add(row4OfEncodedImageMatrix);

                // Create new rows to add rest of the data
                row1OfEncodedImageMatrix = new ArrayList<>(1920);
                row2OfEncodedImageMatrix = new ArrayList<>(1920);
                row3OfEncodedImageMatrix = new ArrayList<>(1920);
                row4OfEncodedImageMatrix = new ArrayList<>(1920);
            }


            // Current character which is also a hexadecimal digit
            var hexNumberAtI = zipHexadecimal.charAt(i);
            //  n x n Pixel will be colored with corresponding color
            for (int j = 0; j < 4; j++)
            {
                var hexCorrespondingColor = hexToCharMap.get(hexNumberAtI);
                row1OfEncodedImageMatrix.add(hexCorrespondingColor);
                row2OfEncodedImageMatrix.add(hexCorrespondingColor);
                row3OfEncodedImageMatrix.add(hexCorrespondingColor);
                row4OfEncodedImageMatrix.add(hexCorrespondingColor);
            }
        }


        // Construct image with corresponding matrix
        constructImage(encodedImageMatrix);


        var color = new Color(85, 245, 41);

        JFrame f = new JFrame();
        f.setBackground(CustomColors.findClosestColor(color).getColor());
        f.setBounds(20, 20, 40, 40);
        f.setVisible(true);









    }


    public static List<List<Color>> readImage(String imagePath)
    {
        List<List<Color>> matrix = new ArrayList<>();

        try {
            File input = new File(imagePath);
            BufferedImage image = ImageIO.read(input);

            if (image == null) {
                System.out.println("Could not read the image");
                return null;
            }

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            for (int i = 0; i < height; i++)
            {
                var row = new ArrayList<Color>(1920);
                for (int j = 0; j < width; j++)
                {
                    var pixelColor = image.getRGB(j, i);
                    int red = (pixelColor >> 16) & 0xFF;
                    int green = (pixelColor >> 8) & 0xFF;
                    int blue = pixelColor & 0xFF;
                    row.add(new Color(red, green, blue));
                }
                matrix.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return matrix;
    }


    public static void constructImage(List<List<Color>> matrix)
    {

        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        var g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 1920, 1080); // give the whole image a white background

        for (int y = 0; y < matrix.size(); y++)
        {
            var currentRow = matrix.get(y);
            for (int x = 0; x < 1920; x++)
            {
                var currentCol = currentRow.get(x);
                g.setColor(currentCol);
                g.fillRect(x, y, 1, 1);
            }
        }

        g.dispose();

        // Save the constructed image as a PNG file
        try {
            File output = new File("compressedImage.png");
            ImageIO.write(image, "png", output);
            System.out.println("Image saved successfully at: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }





    private static String convertFileToHex(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            StringBuilder hexString = new StringBuilder();
            int data;
            while ((data = fis.read()) != -1) {
                hexString.append(String.format("%02X", data));
            }
            return hexString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void convertHexToFile(String hexRepresentation, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            byte[] bytes = hexStringToByteArray(hexRepresentation);
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}