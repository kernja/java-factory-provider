package com.kernja.factoryprovider.core;

import com.kernja.factoryprovider.core.interfaces.IFactoryProvider;
import com.kernja.factoryprovider.core.interfaces.IFactoryProviderModule;
import com.kernja.factoryprovider.core.interfaces.IInjectedService;
import com.kernja.factoryprovider.core.log.ILogService;
import com.kernja.factoryprovider.core.log.LogService;
import sun.rmi.runtime.Log;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.HashMap;

public class FactoryProvider implements IFactoryProvider {

    //hold our values in the hash
    private HashMap<String, Object> mServiceRegistration;
    private HashMap<String, Object> mObjectRegistration;

    //hold our logger
    private ILogService mLogService;

    //hold static instance
    private static FactoryProvider sProvider;

    //constructor
    public FactoryProvider()
    {
        //reset lookups
        mServiceRegistration = new HashMap<>();
        mObjectRegistration = new HashMap<>();

        //create a temporary instance of our log service
        mLogService = new LogService();

        //register our log service as a module
        this.registerServiceOfType(ILogService.class, LogService.class);

        //replace our log service
        mLogService = this.getInstanceOfType(ILogService.class);

        //set the log
        mLogService.d(this, "Created new factory provider.");
    }

    public static IFactoryProvider getInstance()
    {
        //see if a factory exists. if not, create a new one
        if (sProvider == null)
        {
            sProvider = new FactoryProvider();
        }

        //return
        return sProvider;
    }

    public <T extends Object> T getInstanceOfType(Class<T> pInterface)
    {
        //get the name of the interface to a string
        String name = getClassName(pInterface);
        mLogService.d("Getting class name of: " + name);

        //see if we have an instance variable
        if (mObjectRegistration.containsKey(name))
        {
            mLogService.d("Object exists in mObjectRegistration, returning.");
            return (T)mObjectRegistration.get(name);
        } else {
            //does not have an instance variable, try it as a service
            mLogService.d("Object does not exist in mObjectRegistration, trying with mServiceRegistration.");

            //ensure that we have the key to begin with.
            if (!mServiceRegistration.containsKey(name)) {
                mLogService.w("Object does not exist in mServiceRegistration. Returning null.");
                return null;
            }

            //return a new copy
            try {
                mLogService.d("Found object. Creating new instance.");

                //get the class that's registered
                Class c = (Class)mServiceRegistration.get(name);

                //create instance
                T r = (T)c.newInstance();

                //force it to be an injected service
                IInjectedService iS = (IInjectedService) r;

                //inject any potential child dependencies
                mLogService.d("Created a new instance. Attempting to inject dependencies");
                iS.injectRequiredDependencies(this);
                mLogService.d("Successfully injected dependencies. Returning");

                //return
                return (T) iS;

            } catch (Exception e) {
                mLogService.e(e.getMessage());
                return null;
            }
        }
    }

    //functons that provide the 1-1 mapping of an object in memory
    public <T extends Object> boolean registerInstanceOfType(Class<T> pClass, Object pObject)
    {
        mLogService.d("Attempting to register " + pClass.toString() + " as an instance type.");
        return registerObjectInternal(mObjectRegistration, pClass, pObject);
    }

    public <T extends Object, P extends IInjectedService> boolean registerServiceOfType(Class<T> pInterface, Class<P> pObject)
    {
        mLogService.d("Attempting to register " + pInterface.toString() + " as a service.");
        return registerObjectInternal(mServiceRegistration, pInterface, pObject);
    }

    //private helper class
    private  <T extends Object, P extends IInjectedService> boolean registerObjectInternal(HashMap pHashmap, Class pClass, Object pObject)
    {
        //ensure that we have valid values
        mLogService.d("Ensuring that all passed in values are not null");
        if (pClass == null || pObject == null || pHashmap == null)
            return false;

        //ensure that it's valid
        mLogService.d("Ensuring that all objects passed in have a relation to each other");
        if (!testObjectBelongsToClass(pClass, pObject))
            return false;

        //get the name of the interface to a string
        String name = getClassName(pClass);

        //register it
        pHashmap.put(name, pObject);

        //return our success result
        mLogService.d("Successfully added object to mapping.");
        return true;
    }

    //contains code that registers all mappings in a module
    //the internal wiring
    @Override
    public boolean registerModule(IFactoryProviderModule pModule)
    {
        return this.registerModule(pModule, true);
    }

    @Override
    public boolean registerModule(IFactoryProviderModule pModule, boolean pSetLogger) {
        return this.registerModule(pModule, pSetLogger, true);
    }

    @Override
    public boolean registerModule(IFactoryProviderModule pModule, boolean pSetLogger, boolean pClearExisting) {
        //clear existing, if we choose to do so
        if (pClearExisting)
        {
            mLogService.d("Clearing existing registrations");
            this.mServiceRegistration.clear();
            this.mObjectRegistration.clear();
        }

        //register objects in module
        mLogService.d("Registering objects in: " + pModule.toString());
        if (pModule.registerTypes(this))
        {

            //set the logger, if desired
            if (pSetLogger) {
                ILogService l = this.getInstanceOfType(ILogService.class);
                if (l != null) {
                    mLogService = l;
                }

            }

            mLogService.d("Successfully completed module registration");
            return true;
        }

        mLogService.w("Unable to fully register module.");
        return false;
    }

    //helper method to get the full name of a class, including package
    private static <T extends Object> String getClassName(Class<T> pClass)
    {
        return pClass.getName();
    }

    //helper method to ensure that an object matches up with it's passed-in class
    private boolean testObjectBelongsToClass(Class pClass, Object pObject)
    {
        //esnure we have valid values
        if (pClass == null || pObject == null)
            return false;

        //perform the test
        if (pClass.isAssignableFrom(pObject.getClass())) {
            mLogService.d(pClass.toString() + " is assignable from " + pObject.toString());
            return true;
        } else {
            //if the second object is a Class object, do a special exception case test
            if (pObject.getClass().isAssignableFrom(Class.class)) {
                //manually go through all interfaces
                for (Type t : ((Class)pObject).getGenericInterfaces())
                {
                    if (t.equals(pClass)) {
                        mLogService.d(t.getTypeName() + " is a generic interface of " + pClass.toString());
                        return true;
                    }
                }
            }

            //another test
            if (pClass.isInstance(pObject))
            {
                mLogService.d(pClass.toString() + " is an instance of " + pObject.toString());
                return true;
            }

            //final last ditch effort
            try {
                pClass.cast(((Class)pObject).newInstance());
                mLogService.d(pObject.toString() + " can be casted to " + pClass.toString());
                return true;
            } catch (Exception e)
            {
                mLogService.d(pClass.toString() + " is not related to " + pObject.toString());
                return false;
            }

        }
    }
}
