package vn.hit.remotelogging.middleware;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import trikita.jedux.Action;
import trikita.jedux.Store;
import vn.hit.remotelogging.ImmutablePendingLogsState;
import vn.hit.remotelogging.PendingLogsState;
import vn.hit.remotelogging.model.LogsBody;
import vn.hit.remotelogging.model.PendingLogEntity;
import vn.hit.remotelogging.utils.Constants;

public class PersistenceController implements Store.Middleware<Action, PendingLogsState> {
    private final SharedPreferences preferences;
    private final Gson gson;

    public PersistenceController(Context context, Class entityClass) {
        preferences = context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        RuntimeTypeAdapterFactory adapter =
                RuntimeTypeAdapterFactory
                        .of(PendingLogEntity.class)
                        .registerSubtype(entityClass);
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(adapter);
        gson = gsonBuilder.create();
    }

    public PendingLogsState getSavedState() {
        if (preferences.contains(Constants.PREFERENCES_STATE_KEY)) {
            try {
                String json = preferences.getString(Constants.PREFERENCES_STATE_KEY, "");
                if (!json.isEmpty()) {
                    LogsBody body = gson.fromJson(json, LogsBody.class);
                    return ImmutablePendingLogsState.builder().pendingLogs(body.getPendingLogs()).build();
                }
            } catch (Exception e) {
                // If there are exceptions, clear the saved state.
                preferences.edit().remove(Constants.PREFERENCES_STATE_KEY).apply();
            }
        }
        return null;
    }

    @Override
    public void dispatch(Store<Action, PendingLogsState> store, Action action, Store.NextDispatcher<Action> nextDispatcher) {
        nextDispatcher.dispatch(action);

        String json = gson.toJson(store.getState());
        preferences.edit().putString(Constants.PREFERENCES_STATE_KEY, json).apply();
    }
}

