package org.exsel.ui.helpers;

import com.codeborne.selenide.SelenideElement;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.exsel.ui.annotations.grid.TableCell;
import org.exsel.ui.annotations.grid.Tables;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.codeborne.selenide.Selenide.$;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jsoup.parser.Parser.parseXmlFragment;
import static org.testng.util.Strings.isNotNullAndNotEmpty;


public class GridHelper {
    @SneakyThrows
    public <T> List<T> toModelList(WebDriver driver, Class<T> clazz, int index) {
        int i = 0;
        List<T> list = new ArrayList<>();
        T clazz1 = clazz.newInstance();
        assertThat(String.format("%s должен быть аннотирован @Tables", clazz1.getClass().getName()), clazz1.getClass().isAnnotationPresent(Tables.class), is(true));
        TablesAnnotation tablesAnnotation = new TablesAnnotation()
                .setCssTable(clazz1.getClass().getAnnotation(Tables.class).cssTable())
                .setCssHeader(clazz1.getClass().getAnnotation(Tables.class).cssHeader())
                .setCssRow(clazz1.getClass().getAnnotation(Tables.class).cssRow())
                .setCssCell(clazz1.getClass().getAnnotation(Tables.class).cssCell());

        assertThat("В аннотации Tables не указан параметр 'cssTable'", !tablesAnnotation.getCssTable().isEmpty());
        assertThat("В аннотации Tables не указан параметр 'cssRow'", !tablesAnnotation.getCssRow().isEmpty());

        String htmlBody = getBodyHtml(driver);
        Document docBody = parseXml(htmlBody);
        Elements tables=docBody.select(tablesAnnotation.getCssTable());
        if (tables.size()==0) return list;
        Map<String, Integer> mapColumn = getMapColumnForElement(tables.get(index - 1), tablesAnnotation);
        Iterator<Element> iterator = getRowsIteratorForElement(tables.get(index - 1), tablesAnnotation);

        while (iterator.hasNext()) {
            T model = fillModel(driver, iterator.next(), clazz, ++i, mapColumn,tablesAnnotation);
            if (model != null) list.add(model);
        }
        return list;
    }

    @SneakyThrows
    public <T> List<T> toModelList(WebDriver driver, Class<T> clazz) {
        return toModelList(driver, clazz, 1);
    }

    @Data
    @Accessors(chain = true)
    public static class TablesAnnotation {
        private String cssTable;
        private String cssHeader;
        private String cssRow;
        private String cssCell;
    }

    @SneakyThrows
    private <T> Map<String, Integer> getMapColumn(Document docBody, TablesAnnotation tablesAnnotation) {
        Map<String, Integer> headerMap = new HashMap<>();
        if (tablesAnnotation.getCssHeader().isEmpty()) return headerMap;
        Elements elements = docBody.select(String.format("%s%s", tablesAnnotation.getCssTable(), tablesAnnotation.getCssHeader()));
        String text;
        for (int i = 1; i <= elements.size(); i++) {
            if (!(text = elements.get(i - 1).text()).isEmpty()) ;
            headerMap.put(text, i);
        }
        return headerMap;
    }

    private String getBodyHtml(WebDriver driver) {
        return ((JavascriptExecutor) driver).executeScript("return document.getElementsByTagName('body').item(0).innerHTML").toString();
    }

    @SneakyThrows
    protected <T> Iterator getRowsIterator(Document docBody, TablesAnnotation tablesAnnotation) {
        String locatorRow = String.format("%s%s", tablesAnnotation.getCssTable(), tablesAnnotation.getCssRow());
        Elements elements = docBody.select(locatorRow);
        return elements.iterator();
    }

    @SneakyThrows
    protected <T> Iterator getRowsIteratorForElement(Element docBody, TablesAnnotation tablesAnnotation) {
        Elements elements = docBody.select(tablesAnnotation.cssRow);
        return elements.iterator();
    }

    @SneakyThrows
    private <T> Map<String, Integer> getMapColumnForElement(Element docBody, TablesAnnotation tablesAnnotation) {
        Map<String, Integer> headerMap = new HashMap<>();
        if (tablesAnnotation.getCssHeader().isEmpty()) return headerMap;
        Elements elements = docBody.select(tablesAnnotation.getCssHeader());
        String text;
        for (int i = 1; i <= elements.size(); i++) {
            if (!(text = elements.get(i - 1).text()).isEmpty()) ;
            headerMap.put(text, i);
        }
        return headerMap;
    }


    private static Document parseXml(String bodyHtml) {
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

    @SneakyThrows
    public <T> T fillModel(WebDriver driver, Element row, Class<T> clazz, Integer currentRow, Map<String, Integer> headerMap,TablesAnnotation tableAnnotation) {
        String locatorRow, locatorCell, attribute, textInAttr, columnName;
        Integer columnCount;

        Class<?> typeField;
        T model = clazz.newInstance();
        for (Field field : model.getClass().getDeclaredFields()) {

            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> clazzGenericType = (Class<?>) listType.getActualTypeArguments()[0];
                if (clazzGenericType.isAnnotationPresent(Tables.class))
                    field.set(model, toModelList(driver, clazzGenericType, currentRow));
            }

            if (!field.isAnnotationPresent(TableCell.class)) continue;

            locatorCell = field.getAnnotation(TableCell.class).cssCell();
            columnName = field.getAnnotation(TableCell.class).columnName();
            columnCount = field.getAnnotation(TableCell.class).columnOrder();

            if (locatorCell.isEmpty())
                if (headerMap.size() == 0 || columnName.isEmpty()) {
                    if (columnCount != 0)
                        locatorCell = String.format((tableAnnotation.getCssCell().isEmpty()? ">td":tableAnnotation.getCssCell())+":nth-child(%s)", columnCount);
                    else continue;
                } else {
                    columnCount = headerMap.get(columnName);
                    locatorCell = String.format((tableAnnotation.getCssCell().isEmpty()? ">td":tableAnnotation.getCssCell())+":nth-child(%s)", columnCount);
                }

            attribute = field.getAnnotation(TableCell.class).getAttr();
            textInAttr = field.getAnnotation(TableCell.class).containsText();


            typeField = field.getType();
            Elements cells = row.select(locatorCell);
            assertThat(String.format("%s должен быть аннотирован @TableRow", model.getClass().getName()),
                    model.getClass().isAnnotationPresent(Tables.class), is(true));
            //locatorRow = model.getClass().getAnnotation(TableRow.class).css();
            locatorRow = String.format("%s%s%s",
                    model.getClass().getAnnotation(Tables.class).cssTable(),
                    model.getClass().getAnnotation(Tables.class).cssRow(),
                    ":nth-child(" + currentRow + ")");

          /*  if (HtmlElementDriverable.class.equals(typeField)) {
                HtmlElementDriverable element = new HtmlElementDriverable();
                element.setWrappedElement(new HtmlElementDriverable());
                ByUtils.setBy(new HtmlElementDriverable(), By.xpath(String.format("(%s)[%s]%s", locatorRow, currentRow, locatorCell)));
            } else*/if (SelenideElement.class.equals(typeField)) {
                SelenideElement element = $(String.format("%s%s", locatorRow, locatorCell));
                field.set(model, element);
            } else if (String.class.equals(typeField)) {
                if (isNotNullAndNotEmpty(attribute)) field.set(model, cells.attr(attribute));
                else field.set(model, cells.text());
            } else if (Boolean.class.equals(typeField)) {
                if (isNotNullAndNotEmpty(attribute) && isNotNullAndNotEmpty(textInAttr))
                    field.set(model, cells.attr(attribute).contains(textInAttr));
                else if (isNotNullAndNotEmpty(textInAttr)) field.set(model, Boolean.valueOf(cells.attr(attribute)));
                else field.set(model, Boolean.valueOf(cells.text()));
            }
        }
        return model;
    }

}
