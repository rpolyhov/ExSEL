package org.exsel.tools.allure;


import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * Created by SBT-Konovalov-GV on 04.04.2017.
 */
public class Attachments {

    @Attachment(value = "{message}")
    public static String attachMessage(String message){
        return message;
    }

    @Attachment(value = "{title}")
    public static String attachMessage(String title, String message){
        return message;
    }

    @Attachment(value = "{title}", type = "{type}")
    public static byte[] attachMessage(String title, String text, String type){
        return text.getBytes();
    }

    @Attachment(value = "png attachment", type = "image/png")
    public static byte[] savePngAttachment(String fileName) throws URISyntaxException, IOException {
        return getSampleFile(fileName);
    }

    @Attachment(value = "{title}", type = "image/png")
    public static byte[] savePngAttachment(String title, byte[] bytes){
        return bytes;
    }

    @Attachment(value = "png attachment", type = "image/png")
    public static byte[] savePngAttachment(byte[] bytes){
        return bytes;
    }

    @Attachment(value = "{title}", type = "image/png")
    public static byte[] savePngAttachment(String title, String fileName) throws URISyntaxException, IOException {
        return getSampleFile(fileName);
    }

    @Attachment(value = "png attachment", type = "image/png")
    public static byte[] savePngAttachment(URL url) throws URISyntaxException, IOException {
        return getSampleFile(url);
    }

    @Attachment(value = "csv attachment", type = "text/csv")
    public static byte[] saveCsvAttachment(String fileName) throws URISyntaxException, IOException {
        return getSampleFile(fileName);
    }

    @Attachment(value = "csv attachment", type = "text/csv")
    public static byte[] saveCsvAttachment(URL url) throws URISyntaxException, IOException {
        return getSampleFile(url);
    }

    @Attachment(value = "xml attachment", type = "image/svg+xml")
    public static byte[] saveXmlAttachment(String text){
        return text.getBytes();
    }

    @Attachment(value = "{name}", type = "image/svg+xml")
    public static byte[] saveXmlAttachment(String name, String text){
        //return text.getBytes(StandardCharsets.UTF_8);
        return text.getBytes();
    }

    @Attachment(value = "{name}", type = "text/html")
    public static byte[] saveHtmlAttachment(String name, String text){
        StringBuilder sb = new StringBuilder();
        if(!text.toLowerCase().contains("/html")) {
            sb.append("<!DOCTYPE html>\n" +
                    "<html lang=\"ru-RU\">\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "    <table width=\"100%\" height=\"100%\">\n" +
                    "        <tbody>");
            sb.append(text);
            sb.append("\n\t\t</tbody>" +
                    "\n\t</table>" +
                    "\n</html>");
        }else
            sb.append(text);
        return sb.toString().getBytes();
    }

    @Attachment(value = "xml attachment", type = "image/svg+xml")
    public static byte[] saveXmlAttachment(File file) throws URISyntaxException, IOException {
        return getSampleFile(file.getAbsolutePath());
    }

    @Attachment(value = "xml attachment", type = "image/svg+xml")
    public static byte[] saveXmlAttachment(URL url) throws URISyntaxException, IOException {
        return getSampleFile(url);
    }

    @Attachment(value = "avi attachment")
    public static byte[] saveAviAttachment(String fileName) throws URISyntaxException, IOException {
        return getSampleFile(fileName);
    }

    private static byte[] getSampleFile(String fileName) throws IOException, URISyntaxException {
        return getSampleFile(new File(fileName).toURL());
    }

    private static byte[] getSampleFile(URL resource) throws IOException, URISyntaxException {
        if (resource == null) {
            Assert.fail(format("Couldn't find resource '%s'", resource.getPath()));
        }
        return Files.readAllBytes(Paths.get(resource.toURI()));
    }

    public static void attachText(String label, String text){
        Allure.addAttachment(label, "text/plain", text);
    }

    public static void attachXml(String label, String text){
        Allure.addAttachment(label, "text/xml", text);
    }
}