package com.strangeone101.texturetorgb;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextureToRGB {

    public static void main(String[] args) {
        File inputFolder = new File("Input");

        //Create folder if it doesn't exist
        if (!inputFolder.exists()) {
            inputFolder.mkdir();
        }

        int option = JOptionPane.showOptionDialog(null, "Please put the textures into the Input folder and press Continue", "TextureToRGB", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {"Continue", "Cancel"}, "Continue");

        if (option != 0) {
            System.exit(0);
        }

        List<String> lines = new ArrayList<>();

        long time = System.currentTimeMillis();

        //Loop through all files in the input folder
        for (File file : inputFolder.listFiles()) {
            if (file.getName().endsWith(".png")) { //Only parse pngs
                try {
                    BufferedImage image = ImageIO.read(file);
                    //Get the average color from this image
                    int rgb = averageColor(image, 0, 0, image.getWidth(), image.getHeight());
                    String hex = Integer.toHexString(rgb);
                    lines.add(file.getName().substring(0, file.getName().length() - 4) + ": 0x" + hex + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        time = System.currentTimeMillis() - time;

        //Write all the lines to the output file
        File output = new File("Output.txt");
        try {
            output.createNewFile();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(output));
            for (String line : lines) {
                stream.write(line.getBytes());
            }
            stream.close();

            JOptionPane.showMessageDialog(null, "Parsed " + lines.size() + " textures in " + (time) + "ms! Wrote them to Output.txt", "TextureToRGB", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int averageColor(BufferedImage bi, int x0, int y0, int w, int h) {
        int x1 = x0 + w;
        int y1 = y0 + h;
        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Color pixel = new Color(bi.getRGB(x, y));
                if (pixel.getAlpha() == 0) continue;
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
            }
        }
        int num = w * h;
        return (int) ((sumr / num) << 16 | (sumg / num) << 8 | (sumb / num));
    }
}
