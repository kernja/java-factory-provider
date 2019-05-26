package com.kernja.factoryprovider.core.interfaces;

public interface IInjectedService {
    void injectRequiredDependencies(IFactoryProvider pProvider) throws NoClassDefFoundError, ClassCastException;
}
