package com.thoughtworks.frankenstein.recorders;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Takes a screenshot of the desktop
 *
 * @author vivek
 */
public class DefaultScreenShot implements ScreenShot {
    private int counter = 1;

    private boolean screenshotDirectoryCreated = false;

    public String capture(String parent, Robot robot) {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image = robot.createScreenCapture(screenRect);
        String captureFileName = "screenshot-" + counter++ +".png";
        String pathname = (parent != null? parent + File.separator: "")  + "screenshots";
        createScreenshotDirectory(pathname);
        try {
            ImageIO.write(image, "png", new File(pathname + File.separator + captureFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "screenshots/" + captureFileName;
    }

    private void createScreenshotDirectory(String pathname) {
        if (!screenshotDirectoryCreated) {
            File screenshotDirectory = new File(pathname);
            if (!screenshotDirectory.exists()) screenshotDirectory.mkdir();
            screenshotDirectoryCreated = true;
        }
    }
}
