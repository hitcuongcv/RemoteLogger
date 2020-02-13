package vn.hit.remotelogging;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import trikita.jedux.Action;
import trikita.jedux.Store;
import vn.hit.remotelogging.middleware.PersistenceController;
import vn.hit.remotelogging.model.ActionType;
import vn.hit.remotelogging.model.LogParameters;
import vn.hit.remotelogging.model.PendingLogEntity;
import vn.hit.remotelogging.worker.PendingLogsStoreObserver;
import vn.hit.remotelogging.worker.UploadWorker;

@SuppressWarnings("WeakerAccess")
public class RemoteLoggerManager {
    private static RemoteLoggerManager sInstance;

    private Store<Action, PendingLogsState> pendingLogStore;
    private LogParameters logParams;
    private Application application;

    private RemoteLoggerManager() {
        // Do nothing. Just make the default constructor private.
    }

    public static RemoteLoggerManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("RemoteLoggerManager hasn't been initialized yet!\n" +
                    "Call init method before using this class.");
        }
        return sInstance;
    }

    /**
     * Initialize the singleton RemoteLoggerManager.
     *
     * @param application   the application object.
     * @param logParameters Configs for remote logger.
     * @return RemoteLoggerManager instance.
     */
    public static RemoteLoggerManager init(@NonNull Application application, @NonNull LogParameters logParameters) {
        sInstance = new RemoteLoggerManager();

        PendingLogsState pendingLogsState;
        PersistenceController persistenceController = new PersistenceController(application, logParameters.getLogEntityClass());

        pendingLogsState = persistenceController.getSavedState();

        if (pendingLogsState == null) {
            pendingLogsState = PendingLogsState.getDefault();
        }

        sInstance.application = application;
        sInstance.logParams = logParameters;
        sInstance.pendingLogStore = new Store<>(new PendingLogsReducer(), pendingLogsState, persistenceController);
        sInstance.pendingLogStore.subscribe(logParameters.getStoreObserver() != null ?
                logParameters.getStoreObserver() : new PendingLogsStoreObserver());

        sInstance.checkLogParams();
        return sInstance;
    }

    private void checkLogParams() {
        if (logParams.getLogPusher() == null) {
            throw new IllegalArgumentException("LogPusher must not be null.");
        } else if (logParams.getPushCallback() == null) {
            throw new IllegalArgumentException("PushCallback must not be null.");
        } else if (logParams.getInterruptType() == LogParameters.InterruptType.TIME) {
            if (logParams.getInterval() <= 0) {
                throw new IllegalArgumentException("Interval must be larger than 15."); // Cause we're using WorkManager.
            }
            scheduleUpload();
        }
    }

    private void scheduleUpload() {
        PeriodicWorkRequest uploadLogsRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class, logParams.getInterval(), TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance(application.getApplicationContext()).enqueue(uploadLogsRequest);
    }

    public void addLog(PendingLogEntity log) {
        pendingLogStore.dispatch(new Action(ActionType.ADD, log));
    }

    public void clearLogs() {
        pendingLogStore.dispatch(new Action(ActionType.CLEAR_ALL));
    }

    public List<PendingLogEntity> getPendingLogs() {
        return pendingLogStore.getState().pendingLogs();
    }

    public LogParameters getLogParams() {
        return logParams;
    }

    public void forcePushPendingLogs() {
        final List<PendingLogEntity> copy = new ArrayList<>(RemoteLoggerManager.getInstance().getPendingLogs());

        if (copy.size() > 0) {
            logParams.getLogPusher().push(logParams.getPushCallback());
        }
    }
}
