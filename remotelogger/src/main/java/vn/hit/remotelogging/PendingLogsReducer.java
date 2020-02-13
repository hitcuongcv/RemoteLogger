package vn.hit.remotelogging;

import java.util.ArrayList;

import trikita.jedux.Action;
import trikita.jedux.Store;
import vn.hit.remotelogging.model.ActionType;
import vn.hit.remotelogging.model.PendingLogEntity;

class PendingLogsReducer implements Store.Reducer<Action, PendingLogsState> {
    @Override
    public PendingLogsState reduce(Action action, PendingLogsState oldState) {
        ActionType actionType = (ActionType) action.type;
        switch (actionType) {
            case ADD:
                PendingLogEntity pendingLog = (PendingLogEntity) action.value;
                return ImmutablePendingLogsState.builder().from(oldState).addPendingLogs(pendingLog).build();
            case CLEAR_ALL:
                return ImmutablePendingLogsState.builder().pendingLogs(new ArrayList<PendingLogEntity>()).build();
        }
        return oldState;
    }
}
