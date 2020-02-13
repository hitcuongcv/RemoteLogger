package vn.hit.remotelogging.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import vn.hit.remotelogging.utils.Constants;

public class LogsBody {
    @SerializedName(Constants.PENDING_LOGS_SERIALIZED_NAME)
    private List<PendingLogEntity> pendingLogs;

    public List<PendingLogEntity> getPendingLogs() {
        return pendingLogs;
    }
}
