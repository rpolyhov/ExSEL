package org.exsel.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Greg3D on 04.07.2016.
 */
public class ScreenShots {
    // делает ScreenShot с использованием WebDriver
    public static String getDriverScreenShot(WebDriver driver) {
        return getDriverScreenShot(driver, "defaultfile");
    }

    public static String getDriverScreenShot(WebDriver driver, String fileName) {
        //if(driver == null) return;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // Now you can do whatever you need to do with it, for example copy somewhere
        try {
            FileUtils.copyFile(scrFile, new File(fileName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName + ".png";
    }

    public static byte[] getDriverScreenShotToBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }


    // делает ScreenShot рабочего стола
    public static String getScreenShot(String fileName) throws Exception {
        Robot r = new Robot();
        BufferedImage screenShot = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        ImageIO.write(screenShot, "JPG", new File(fileName));
        return fileName + ".jpg";
    }

    // делает ScreenShot рабочего стола
    public static byte [] getScreenShot(){
        try {
            Robot r = new Robot();
            BufferedImage screenShot = r.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(screenShot, "JPG", bos);
            return bos.toByteArray();
        }catch (Exception e){
            return new byte[]{};
        }

    }
}
