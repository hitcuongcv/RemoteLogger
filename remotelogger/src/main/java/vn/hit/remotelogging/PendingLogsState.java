package vn.hit.remotelogging;

import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;

import java.util.List;

import vn.hit.remotelogging.model.PendingLogEntity;
import vn.hit.remotelogging.utils.Constants;

@Value.Immutable
public abstract class PendingLogsState {
    @SerializedName(Constants.PENDING_LOGS_SERIALIZED_NAME)
    abstract List<PendingLogEntity> pendingLogs();

    static PendingLogsState getDefault() {
        return ImmutablePendingLogsState.builder().build();
    }
}
