package vn.hit.remotelogging.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

import vn.hit.remotelogging.model.LogParameters;
import vn.hit.remotelogging.model.PendingLogEntity;
import vn.hit.remotelogging.RemoteLoggerManager;

public class UploadWorker extends Worker {
    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final List<PendingLogEntity> copy = new ArrayList<>(RemoteLoggerManager.getInstance().getPendingLogs());

        if (copy.size() > 0) {
            LogParameters logParameters = RemoteLoggerManager.getInstance().getLogParams();
            logParameters.getLogPusher().push(logParameters.getPushCallback());
        }

        return Result.success();
    }

}
