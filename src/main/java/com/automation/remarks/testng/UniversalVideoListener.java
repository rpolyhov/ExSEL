package com.automation.remarks.testng;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.automation.remarks.testng.utils.ListenerUtils;
import com.automation.remarks.testng.utils.MethodUtils;
import com.automation.remarks.video.RecordingUtils;
import com.automation.remarks.video.recorder.VideoRecorder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.qameta.allure.Allure;
import org.apache.commons.io.IOUtils;
import org.testng.ITestResult;

public class UniversalVideoListener extends TestNgListener {
    private IVideoRecordClient videoRecordClient;

    public UniversalVideoListener() {
    }

    public void onTestStart(ITestResult result) {
        if (!this.videoDisabled(result) && !this.shouldNotIntercept(result)) {
            if (VideoRecorder.conf().isRemote()) {
                String nodeUrl = VideoRecorder.conf().remoteUrl();
                this.videoRecordClient = new RemoteVideoRecordClient(nodeUrl);
            } else {
                this.videoRecordClient = new LocalVideoRecordClient();
            }

            this.videoRecordClient.start();
        }
    }

    public void onTestSuccess(ITestResult result) {
        if (!this.videoDisabled(result) && !this.shouldNotIntercept(result)) {
            String fileName = ListenerUtils.getFileName(result);
            this.videoRecordClient.stopAndSave(fileName, true);
        }
    }

    public void onTestFailure(ITestResult result) {
        if (!this.videoDisabled(result) && !this.shouldNotIntercept(result)) {
            String fileName = ListenerUtils.getFileName(result);

            this.videoRecordClient.stopAndSave(fileName, false);
            //allureVid();
        }
    }
/*    public void allureVid() {
        try {
            File dir = new File("video/"); //path указывает на директорию
            File[] arrFiles = dir.listFiles();

            List<File> lst = Arrays.asList(arrFiles);
            byte[] byteArr = IOUtils.toByteArray(new FileInputStream(lst.get(lst.size()-1)));
            Allure.addAttachment("attachment name",  new ByteArrayInputStream(byteArr));        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private boolean videoDisabled(ITestResult result) {
        return !RecordingUtils.videoEnabled(MethodUtils.getVideoAnnotation(result));
    }

    public boolean shouldNotIntercept(ITestResult result) {
        List<String> listeners = result.getTestContext().getCurrentXmlTest().getSuite().getListeners();
        return !listeners.contains(this.getClass().getName()) && !this.shouldIntercept(result.getTestClass().getRealClass(), this.getClass());
    }
}
