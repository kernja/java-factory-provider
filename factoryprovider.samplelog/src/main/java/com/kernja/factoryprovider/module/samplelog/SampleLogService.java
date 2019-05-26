package com.kernja.factoryprovider.module.samplelog;

import com.kernja.factoryprovider.core.log.LogService;

public class SampleLogService extends LogService {
    public SampleLogService()
    {
        //log string formats
        //overridden through this service
        CONST_STRING_FULL = "FOO! " + CONST_STRING_FULL;
        CONST_STRING_PARTIAL = "FOO! " + CONST_STRING_PARTIAL;
    }
}
