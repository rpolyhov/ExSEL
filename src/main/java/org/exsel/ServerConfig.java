package org.exsel;

import com.codeborne.selenide.FileDownloadMode;
import org.aeonbits.owner.Config;

public interface ServerConfig extends Config {

    int port();
    String hostname();

    @DefaultValue("http://192.168.1.79:4444/wd/hub")
    //@DefaultValue("http://localhost:4444/wd/hub")
    String remoteAddress();

    @DefaultValue("http://192.168.1.79:8080/video/")
    String remoteVideos();

    @DefaultValue("FOLDER")
    FileDownloadMode fileDownload();

    @DefaultValue("true")
    Boolean enableVNC();

    @DefaultValue("true")
    Boolean enableVideo();

    @DefaultValue("chrome")
    public String defaultBrowser();



}
