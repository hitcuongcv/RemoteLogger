package vn.hit.remotelogging.worker;

public interface LogPusher {
    void push(Callback callback);

    interface Callback {
        void onSuccess(Object logEvents);

        void onFailure(Object logEvents, String errorMessage);
    }
}
