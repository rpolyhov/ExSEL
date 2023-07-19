package org.exsel.tools.allure;

import io.qameta.allure.Allure;
import org.aeonbits.owner.ConfigFactory;
import org.exsel.SelenoidSettingFactory;
import org.exsel.ServerSelenoidConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class AttachVideo {

    public static void attachAllureVideo(String url) {
        try {
            ServerSelenoidConfig serverSelenoidConfig = ConfigFactory.create(ServerSelenoidConfig.class);
            if (!serverSelenoidConfig.enableVideo()) return;
            URL videoUrl = new URL(url.replace(" ", "%20"));
            InputStream is = getSelenoidVideo(videoUrl);
            Allure.addAttachment("Video", "video/mp4", is, "mp4");
            deleteSelenoidVideo(videoUrl);
        } catch (Exception e) {
            System.out.println("attachAllureVideo");
            e.printStackTrace();
        }
    }

    public static void deleteSelenoidVideo(URL url) {
        try {
            HttpURLConnection deleteConn = (HttpURLConnection) url.openConnection();
            deleteConn.setDoOutput(true);
            deleteConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            deleteConn.setRequestMethod("DELETE");
            await("Здесь указывается алиас")
                    .pollInSameThread()
                    .atMost(30, SECONDS)
                    .pollInterval(5, SECONDS)
                    .ignoreException(NullPointerException.class)
                    .until(() ->
                    {
                        deleteConn.connect();
                        return deleteConn.getResponseCode() == 200 && deleteConn.getResponseMessage().equals("OK");
                    });

            deleteConn.disconnect();

        } catch (IOException e) {
            System.out.println("deleteSelenoidVideo");
            e.printStackTrace();
        }
    }

    public static InputStream getSelenoidVideo(URL url) throws IOException {
        await("Здесь указывается алиас1")
                .pollInSameThread()
                .during(10, SECONDS)
                .atMost(30, SECONDS)
                .ignoreException(NullPointerException.class)
                .until(() ->
                        Integer.parseInt(url.openConnection().getHeaderField("Content-Length")) > 0);
        await("Здесь указывается алиас2")
                .pollInSameThread()
                // .during(10, SECONDS)
                .atMost(120, SECONDS)
                .ignoreException(FileNotFoundException.class)
                .until(() ->
                        url.openStream() != null);

        return url.openStream();
    }

}
