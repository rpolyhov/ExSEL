package org.exsel.example;

import org.exsel.example.iframe.SmartLabManager;
import org.exsel.example.menu.Aspro.AsproManager;
import org.exsel.example.menu.Decathlon.DecathlonManager;
import org.exsel.example.menu.x5.X5Manager;
import org.exsel.example.menu.xoops.XOOPSManager;
import org.exsel.example.table.OZON.OzonManager;

import org.exsel.example.table.RBC.RBCManager;
import org.exsel.example.table.UEFA.UefaManager;
import org.exsel.example.typicals.Demoqa.DemoqaManager;
import org.exsel.example.typicals.TheInternet.TheInternetManager;

public class AppManager {
    public X5Manager x5;
    public DecathlonManager decathlonManager;
    public AsproManager aspro;
    public XOOPSManager xoops;
    public UefaManager uefa;
    public OzonManager ozon;
    public TheInternetManager theInternet;
    public DemoqaManager demoqa;
    public RBCManager rbcManager;
    public SmartLabManager smartLab;

    AppManager(){
        x5=new X5Manager();
        decathlonManager=new DecathlonManager();
        aspro = new AsproManager();
        xoops= new XOOPSManager();
        uefa =new UefaManager();
        ozon =new OzonManager();
        theInternet =new TheInternetManager();
        demoqa =new DemoqaManager();
        demoqa =new DemoqaManager();
        smartLab =new SmartLabManager();

    }
}
