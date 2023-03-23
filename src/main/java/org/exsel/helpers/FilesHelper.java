package org.exsel.helpers;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.io.File;

public class FilesHelper {

    public static String getFilePath(String name) {
        File file;
        if ((file = new File(new File("").getAbsolutePath() + "/" + name)).exists())
            return file.getAbsolutePath();
        else
            return new File(FilesHelper.class.getClassLoader().getResource("./").getPath() + name).getAbsolutePath();
    }

    public static File getFile(String name) {
        File file;
        if ((file = new File(new File("").getAbsolutePath() + "/" + name)).exists())
            return file;
        else
            return new File(FilesHelper.class.getClassLoader().getResource("./").getPath() + name);
    }
    public static void uploadFile(String filePath, WebElement target) {
        uploadFileBase(filePath,0,0,target);
    }
    private static void uploadFileBase(String filePath, int offsetX, int offsetY, WebElement target) {
        JavascriptExecutor jse = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        //"input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";

        // Поле в которое перетаскивается файл
        WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
        input.sendKeys(filePath);
    }
}