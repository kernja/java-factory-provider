package com.kernja.factoryprovider.core.interfaces;

public interface IFactoryProvider {
    //getter
    <T extends Object> T getInstanceOfType(Class<T> pInterface);

    //setters
    <T extends Object> boolean registerInstanceOfType(Class<T> pInterface, Object pObject);
    <T extends Object, P extends IInjectedService> boolean registerServiceOfType(Class<T> pInterface, Class<P> pObject);

    //register module
    boolean registerModule(IFactoryProviderModule pModule);
    boolean registerModule(IFactoryProviderModule pModule, boolean pSetLogger);
    boolean registerModule(IFactoryProviderModule pModule, boolean pSetLogger, boolean pClearExisting);
}
