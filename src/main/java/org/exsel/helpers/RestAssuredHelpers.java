package org.exsel.helpers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.internal.RestAssuredResponseOptionsImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.exsel.ui.config.TestConfigState;
import org.json.JSONObject;
import org.openqa.selenium.Cookie;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;

public class RestAssuredHelpers {
    Response response;
    private static RequestSpecification requestSpec;
    static final Filter FORCE_JSON_RESPONSE_BODY = (reqSpec, respSpec, ctx) -> {
        Response response = ctx.next(reqSpec, respSpec);
        ((RestAssuredResponseOptionsImpl) response).setContentType("application/json");
        return response;
    };

    /*    public RestAssuredHelpers() {
            if (requestSpec == null)
                requestSpec = new RequestSpecBuilder()
                      //  .setBaseUri(TestConfigState.getBaseUri())
                       .setAccept(ContentType.JSON)
                       .setContentType("application/json; charset=utf-8")
                        //.setContentType("text/html")
                        .addHeader("Accept-Language", "ru_RU")
                        .build();
        }*/


    public RestAssuredHelpers() {
        if (requestSpec == null)
            requestSpec = new RequestSpecBuilder()

                    //  .setBaseUri(TestConfigState.getBaseUri())
                    //.setAccept("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")

                    //.setContentType("text/html")
                    // .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                    //.addHeader("Accept-Encoding", "gzip, deflate, br")
                    //  .addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3")
                    // .addHeader("Connection", "keep-alive")
                    //  .addHeader("Host" , "www.finam.ru")
                    // .addHeader("Sec-Fetch-Dest" , "document")
                    // .addHeader("Sec-Fetch-Mode", "navigate")
                    // .addHeader("Sec-Fetch-Site", "none")
                    //.addHeader("Sec-Fetch-User", "?1")
                    //   .addHeader("Upgrade-Insecure-Requests", "1")
                   // .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0")
                    //.addHeader("Accept", "*/*")
                  // .addHeader("Host", "www.finam.ru")
                   // .addHeader("Connection", "keep-alive")
                  //  .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .build();

    }

    public Response requestGet(String url, String cookie ) {
        Map<String, Object> params = new HashMap<>();
        //params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");

        params .put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
        params.put("Accept", "text/html");
        params.put("Host", "www.finam.ru");
        params.put("Connection", "keep-alive");//
        params.put("Accept-Encoding", "gzip, deflate, br");
        params.put("Cookie",cookie);


        await("Выбор..Ожидания результатов поиска")
                .pollInSameThread()
                .atMost(60, SECONDS)
                .ignoreException(org.openqa.selenium.NoSuchElementException.class)
                .ignoreException(java.util.NoSuchElementException.class)
                .until((response = given()//.headers(params)
                      //  .spec(requestSpec)
                        .headers(params)

                       // .contentType("application/json; charset=utf-8")
                        // .params(params)
                        //.filters(FORCE_JSON_RESPONSE_BODY)
                        .when().get(url)
                        .then().extract().response())::getStatusCode, is(200));
        return response;

    }

    public Response requestGet(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
        // params.put("pageNumber",1);

        await("Выбор..Ожидания результатов поиска")
                .pollInSameThread()
                .atMost(60, SECONDS)
                .ignoreException(org.openqa.selenium.NoSuchElementException.class)
                .ignoreException(java.util.NoSuchElementException.class)
                .until((response = given()//.headers(params)
                        .spec(requestSpec)
                        .contentType("application/json; charset=utf-8")
                        // .params(params)
                        //.filters(FORCE_JSON_RESPONSE_BODY)
                        .when().get(url)
                        .then().extract().response())::getStatusCode, is(200));
        return response;


    }

    public Response requestPost(String url,Map<String, Object> bodyJson) {
     Map<String, Object> params = new HashMap<>();
        //params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");

        //params .put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
     //   params.put("Accept", "text/html");
      //  params.put("Host", "www.finam.ru");
      //  params.put("Connection", "keep-alive");//
      //  params.put("Accept-Encoding", "gzip, deflate, br");
        params.put("Cookie","_ym_uid=1632924859642291209; mos_id=Cg8qAWOthuSHdjAjTdk9AgA=; uxs_uid=c7d5f570-cc63-11ed-b363-bb6f5cacd135; das_d_tag2=69031323-cd43-4978-a2c2-4ecdd3581007; das_d_tag2_legacy=69031323-cd43-4978-a2c2-4ecdd3581007; KFP_DID=ef796a62-da5e-310c-0c44-f1bb8aa32903; _ym_d=1684331733; aupd_current_role=2:2; _ym_isad=2; sbp_sid=000000000000000000000000000000000000; PHPSESSID=d8b891acab65330c5db0fbd08cdca1ca; elk_token=null|b84a456cb4838e6e116a841f233043cb; ACS-SESSID=080kresjt8n0k0rk2inh6rv4qg; oxxfgh=50ed0ef0-7b53-4fec-9e1b-bda6052920f9#18#2592000000#5000#600000#81540; session-cookie=176a0b2313b43614afc48b4fbeb261f556bd0f6ee1fdb8be89c1258ea5fdbe4604997abdc8561c1b87ed1d18999c57ed; oxxfghcd=50ed0ef0-7b53-4fec-9e1b-bda6052920f9#19#2592000000#5000#600000#81540; oxxfghcd_legacy=50ed0ef0-7b53-4fec-9e1b-bda6052920f9#19#2592000000#5000#600000#81540");

        await("Выбор..Ожидания результатов поиска")
                .pollInSameThread()
                .atMost(60, SECONDS)
                .ignoreException(org.openqa.selenium.NoSuchElementException.class)
                .ignoreException(java.util.NoSuchElementException.class)
                .until((response = given()//.headers(params)
                        .spec(requestSpec)
                        .headers(params)
                        .body(new JSONObject(bodyJson).toString())
                        .when().post(url)
                        .then().extract().response())::getStatusCode, is(200));
        return response;


    }



}
