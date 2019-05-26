package com.kernja.factoryprovider.core.log;

public interface ILogService {

    void setLogLevel(int pLevel);

    //fatal
    void f(String pMessage);
    void f(Class pClass, String pMessage);
    void f(Object pObject, String pMessage);

    //error
    void e(String pMessage);
    void e(Class pClass, String pMessage);
    void e(Object pObject, String pMessage);

    //warning
    void w(String pMessage);
    void w(Class pClass, String pMessage);
    void w(Object pObject, String pMessage);

    //info
    void i(String pMessage);
    void i(Class pClass, String pMessage);
    void i(Object pObject, String pMessage);

    //debug
    void d(String pMessage);
    void d(Class pClass, String pMessage);
    void d(Object pObject, String pMessage);

    //verbose
    void v(String pMessage);
    void v(Class pClass, String pMessage);
    void v(Object pObject, String pMessage);
}
