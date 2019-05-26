package com.kernja.factoryprovider.core;

import com.kernja.factoryprovider.core.interfaces.IFactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IFactoryProviderModule;

public class FactoryProviderModule implements IFactoryProviderModule {

    protected boolean mSuccessful = true;

    @Override
    public boolean registerTypes(IFactoryProvider provider) {
        //reset
        mSuccessful = true;

        //register a instantiated object for use
        //setResult(provider.registerInstanceOfType(String.class, "chungus"));

        //register a service for use
        //setResult(provider.registerServiceOfType(ITestInterface.class, TestInterfaceCat.class));

        return mSuccessful;
    }

    protected void setResult(boolean pBoolean)
    {
        if (pBoolean == false)
        {
            mSuccessful = false;
        }
    }
}
