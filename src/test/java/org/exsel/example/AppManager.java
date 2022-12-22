package org.exsel.example;

import org.exsel.example.menu.Aspro.AsproManager;
import org.exsel.example.menu.Decathlon.DecathlonManager;
import org.exsel.example.menu.x5.X5Manager;
import org.exsel.example.menu.xoops.XOOPSManager;
import org.exsel.example.menu.xoops.XOOPSPages;
import org.exsel.example.table.UEFA.UefaManager;

public class AppManager {
    public X5Manager x5;
    public DecathlonManager decathlonManager;
    public AsproManager aspro;
    public XOOPSManager xoops;
    public UefaManager uefaManager;

    AppManager(){
        x5=new X5Manager();
        decathlonManager=new DecathlonManager();
        aspro = new AsproManager();
        xoops= new XOOPSManager();
        uefaManager=new UefaManager(this);
    }
}
