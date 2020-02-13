package vn.hit.remotelogging.worker;

public interface LogPusher {
    void push(Callback callback);

    interface Callback {
        void onSuccess();

        void onFailure();
    }
}
