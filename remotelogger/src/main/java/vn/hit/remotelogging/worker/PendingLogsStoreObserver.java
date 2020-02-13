package vn.hit.remotelogging.worker;

import java.util.ArrayList;
import java.util.List;

import vn.hit.remotelogging.RemoteLoggerManager;
import vn.hit.remotelogging.model.LogParameters;
import vn.hit.remotelogging.model.PendingLogEntity;

public class PendingLogsStoreObserver implements Runnable {
    @Override
    public void run() {
        LogParameters logParameters = RemoteLoggerManager.getInstance().getLogParams();

        if (logParameters.getInterruptType() == LogParameters.InterruptType.RECORD_COUNT) {
            List<PendingLogEntity> copy = new ArrayList<>(RemoteLoggerManager.getInstance().getPendingLogs());

            if (copy.size() >= logParameters.getTriggerAmount()) {
                logParameters.getLogPusher().push(logParameters.getPushCallback());
            }
        }
    }
}
