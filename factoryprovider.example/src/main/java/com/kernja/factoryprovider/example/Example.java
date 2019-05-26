package com.kernja.factoryprovider.example;

import com.kernja.factoryprovider.core.log.ILogService;
import com.kernja.factoryprovider.module.Module;
import com.kernja.factoryprovider.core.FactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IFactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IFactoryProviderModule;

public class Example {
    public static void main(String args[])
    {
        //get an instance of our factory provider
        IFactoryProvider fp = FactoryProvider.getInstance();

        //factory provider comes with a default logger
        ILogService l = fp.getInstanceOfType(ILogService.class);
        l.i("This is a test message.");

        //create a module to register
        //that has a specialized form of a logger attached
        //this project does NOT have a reference to "factoryprovider.samplelog"
        //even though we can still access the logger specified in that project
        //through the use of this module
        IFactoryProviderModule m = new Module();

        //register it with our factory provider
        fp.registerModule(m, true);

        //our log should now use the custom logger
        //to factoryprovider.samplelog
        l = fp.getInstanceOfType(ILogService.class);
        l.i("<--- this should have FOO prepended.");
    }
}
