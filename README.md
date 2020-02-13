# RemoteLogger

Setting up and using the library:
1. Install lib following site: https://jitpack.io/#hitcuongcv/RemoteLogger/1.0.

2. Intergrate:
- First, init the Manager inside the Application class:
        LogParameters logParameters = new LogParameters.Builder()
                .setLogEntityClass(Log.class) // (Required) Log entity class. 
                .setInterruptType(LogParameters.InterruptType.RECORD_COUNT) // (Optional) Default RECORD_COUNT.
                                                                            // Push logs every specified range of time (#1) 
                                                                            // or every time the logs count reach the limitation (#2). 
                .setInterval(15) // (Required if using Time Interruption) #1 (In minute - Minimum 15').
                .setTriggerAmount(20) // (Optional) Default 20. #2.
                .setLogPusher(...) // (Required)
                .setPushCallback(...) // (Required)
                .build();
        RemoteLoggerManager.init(this, logParameters);
        
 - After the initialization, you can access the RemoteLoggerManager and it's functions.
