package com.kernja.factoryprovider.module;

import com.kernja.factoryprovider.core.interfaces.IFactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IFactoryProviderModule;
import com.kernja.factoryprovider.core.log.ILogService;
import com.kernja.factoryprovider.module.samplelog.SampleLogService;

public class Module implements IFactoryProviderModule
{
    @Override
    public boolean registerTypes(IFactoryProvider provider) {

        //register the sample log service, instead of the base log service.
        //this prepends "FOO" to every log written
        provider.registerServiceOfType(ILogService.class, SampleLogService.class);

        //return true.
        //if we had a complex registration process, such as using
        //instance variables, we'd have to do logic
        return true;
    }
}
