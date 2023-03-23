package org.exsel;

import com.automation.remarks.testng.UniversalVideoListener;
import io.qameta.allure.Allure;
import org.apache.commons.io.IOUtils;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyUniversalVideoListener extends UniversalVideoListener

{
    public void onTestFailure(ITestResult result) {
      super.onTestFailure(result);
        allureVid();
    }
    public void allureVid() {
        try {
            File dir = new File("video/"); //path указывает на директорию
            File[] arrFiles = dir.listFiles();

            List<File> lst = Arrays.asList(arrFiles);
            byte[] byteArr = IOUtils.toByteArray(new FileInputStream(lst.get(lst.size()-1)));
            //byte[] byteArr1 = IOUtils.toByteArray(new FileInputStream(Arrays.stream(arrFiles));

            Allure.addAttachment("attachment name",  new ByteArrayInputStream(byteArr));        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
