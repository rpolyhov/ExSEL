package org.exsel.helpers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.List;

import static org.jsoup.parser.Parser.parseXmlFragment;

public class WaitHelper {
    public static boolean uiIsReady(WebDriver driver){
        String htmlBody = getBodyHtml(driver);
        Document docBody = parseXml(htmlBody);
        handleFailDialog(docBody);
        if(isLoading(docBody)) return false;
        if(isHttpIsActive(driver)) return false;
        handleException(docBody);
        return (boolean)((JavascriptExecutor) driver).executeScript(String.format("return (typeof($) === 'undefined') ? true: $.active == 0"));
    }

    private static  void handleFailDialog(Document docBody){
        String textMessage,header;

        for(Element el: docBody.select("#prompt")) {
            header = el.select("#prompt_h").first().text();
            textMessage = el.select(".bd").first().text();

           if (Arrays.asList("Сбой","Ошибка","Internal Error")
                    .contains(header)||(textMessage.contains("Сбой")))
               throw new FailDialogError(header+"\n" + textMessage);
           else if (textMessage.contains("Failed to execute script"))
            throw new FailDialogError("Failed to execute script"+"\n" + textMessage);
        }
    }

    private static boolean isLoading(Document docBody){
        Elements els = docBody.select("#message_c[style*='visibility: visible'] .wait");
        for(Element l: els)
            if (l.text().contains("Загрузка...")) return true;

        Elements elAll = docBody.select("img[src*='loading.gif']");
        Elements elHidden = docBody.select("div[style*='visibility: hidden;'] img[src*='loading.gif']");
        Elements elNone = docBody.select("div[class*='hidden1'] img[src*='loading.gif'],div[style*='display: none;'] img[src*='loading.gif']");
        boolean isLoading = elAll.size() - (elHidden.size() + elNone.size()) > 0 ;
        if(isLoading){ /* logger.debug("isLoading !!!!");*/}
        return isLoading;
    }

    private static String getBodyHtml(WebDriver driver){
        return ((JavascriptExecutor) driver).executeScript("return document.getElementsByTagName('body').item(0).innerHTML").toString();
    }
    public static Document parseXml(String bodyHtml) {
        String baseUri = "";
        Document doc = new Document(baseUri);
        List<Node> nodeList = parseXmlFragment(bodyHtml, baseUri);
        Node[] nodes = nodeList.toArray(new Node[nodeList.size()]);
        for (int i = nodes.length - 1; i > 0; i--)
            nodes[i].remove();
        for (Node node : nodes)
            doc.appendChild(node);
        return doc;
    }

    private static boolean isHttpIsActive(WebDriver driver){
        JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
        monkeyPatchXMLHttpRequest(jsExecutor);
        int requestsCount = Integer.valueOf(jsExecutor.executeScript("return window.openHTTPs").toString());
        return requestsCount > 0;
    }
    private static void monkeyPatchXMLHttpRequest(JavascriptExecutor jsDriver) {
        try {
            Object numberOfAjaxConnections = jsDriver.executeScript("return window.openHTTPs");
            if (numberOfAjaxConnections instanceof Long) {
                return;
            }
            String script = "  (function() {" +
                    "var oldOpen = XMLHttpRequest.prototype.open;" +
                    "window.openHTTPs = 0;" +
                    "XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {" +
                    "window.openHTTPs++;" +
                    "this.addEventListener('readystatechange', function() {" +
                    "if(this.readyState == 4) {" +
                    "window.openHTTPs--;" +
                    "}" +
                    "}, false);" +
                    "oldOpen.call(this, method, url, async, user, pass);" +
                    "}" +
                    "})();";
            jsDriver.executeScript(script);
        } catch (Exception e) {
         /* //  logger.info("monkeyPatchXMLHttpRequeste" + e.toString());*/
        }
    }
    private static void handleException(Document docBody){
        Elements els = docBody.select(".yui-dt-message:not([style]) .yui-dt-liner,.yui-dt-message[style=''] .yui-dt-liner");
        for(Element l: els) {
            if (l.text().contains("xception") || l.text().contains("Error:")) {
                System.err.println("DialogError: " + l.text());
                throw new Error("DialogError: " + l.text());
            }
        }
    }

    public static class FailDialogError extends Error{
        public FailDialogError(String message){
            super(message);
            System.err.println(message);
        }

    }

}
