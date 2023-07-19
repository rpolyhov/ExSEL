package org.exsel.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.List;

import static org.jsoup.parser.Parser.parseXmlFragment;

public class JsoupHelper {

    public static String getBodyHtml(WebDriver driver) {
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
    public static String getElementXpath(WebDriver driver, String css, int position){
        String htmlBody = getBodyHtml(driver);
        Document docBody = parseXml(htmlBody);
        Elements elements = docBody.select(css);
        return elements.get(position).text();
    }

    public static String getElementXpath(String url, String xpath) throws IOException {
        Document doc  = Jsoup.connect(url).get();
        Elements elements = doc.selectXpath(xpath);
        return elements.get(0).text();
    }

}
