package org.exsel.helpers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.exsel.helpers.JsoupHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ParsingSiteHelper {
   // @Test
    public void site() throws CsvValidationException, IOException {
        String csvFile = "book1.csv";
        String file = "result.html";
        String resultText = "";
        CSVReader reader = new CSVReader(new FileReader(csvFile));
        String[] rowData;
        File f = new File(file);
        if (!f.exists()) f.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);

        while ((rowData = reader.readNext()) != null) {
            assertThat(String.format("В строке %s указаны не все параметры", reader), rowData.length, is(4));
            resultText = getElementXpath(rowData[0], rowData[1]);
            fos.write(String.format("<div class=\"block\">\n" +
                            "<a href=\"%s\" style=\"color: %s;\" title=\"Ссылка\">%s</a> - %s</div>",
                    rowData[0],
                    rowData[2].equals(resultText) ? "green" : "red",
                    rowData[0],
                    rowData[3]).getBytes());
        }
        fos.flush();
        fos.close();
    }

    @Test
    public void siteMosRu() {

        Map<String, Object> map = new HashMap<>();
        map.put("ajaxModule", "Esz");
        map.put("ajaxAction", "startSearch");
        map.put("items[searchString]", "2127 волейбол");
        map.put("items[page]", 1);

        Response responce = new RestAssuredHelpers()
                .requestPost("https://www.mos.ru/pgu/common/ajax/index.php", map);

        //  .as(RootFinam.class);

    }
}


