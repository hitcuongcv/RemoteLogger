package vn.hit.remotelogging.model;

import vn.hit.remotelogging.worker.LogPusher;

public class LogParameters {
    public enum InterruptType {
        /**
         * Push logs every specified range of time.
         */
        TIME,
        /**
         * Push logs every time that pending logs count reach a specified number.
         */
        RECORD_COUNT
    }

    private static final int DEFAULT_TRIGGER_AMOUNT = 20;

    private Runnable storeObserver;
    private InterruptType interruptType;
    private long interval;
    private int triggerAmount;
    private Class logEntityClass;
    private LogPusher logPusher;
    private LogPusher.Callback pushCallback;

    private LogParameters() {
        // Do nothing.
    }

    public Runnable getStoreObserver() {
        return storeObserver;
    }

    public InterruptType getInterruptType() {
        return interruptType;
    }

    public long getInterval() {
        return interval;
    }

    public int getTriggerAmount() {
        return triggerAmount;
    }

    public Class getLogEntityClass() {
        return logEntityClass;
    }

    public LogPusher getLogPusher() {
        return logPusher;
    }

    public LogPusher.Callback getPushCallback() {
        return pushCallback;
    }

    public static class Builder {
        private Runnable storeObserver;
        private InterruptType interruptType = InterruptType.RECORD_COUNT;
        private long interval;
        private int triggerAmount = DEFAULT_TRIGGER_AMOUNT;
        private Class logEntityClass = PendingLogEntity.class;
        private LogPusher logPusher;
        private LogPusher.Callback pushCallback;

        public Builder setStoreObserver(Runnable storeObserver) {
            this.storeObserver = storeObserver;
            return this;
        }

        public Builder setInterruptType(InterruptType interruptType) {
            this.interruptType = interruptType;
            return this;
        }

        public Builder setInterval(long interval) {
            this.interval = interval;
            return this;
        }

        public Builder setTriggerAmount(int triggerAmount) {
            this.triggerAmount = triggerAmount;
            return this;
        }

        public Builder setLogEntityClass(Class logEntityClass) {
            this.logEntityClass = logEntityClass;
            return this;
        }

        public Builder setLogPusher(LogPusher logPusher) {
            this.logPusher = logPusher;
            return this;
        }

        public Builder setPushCallback(LogPusher.Callback pushCallback) {
            this.pushCallback = pushCallback;
            return this;
        }

        public LogParameters build() {
            LogParameters logParameters = new LogParameters();
            logParameters.storeObserver = this.storeObserver;
            logParameters.interruptType = this.interruptType;
            logParameters.interval = this.interval;
            logParameters.triggerAmount = this.triggerAmount;
            logParameters.logEntityClass = this.logEntityClass;
            logParameters.logPusher = this.logPusher;
            logParameters.pushCallback = this.pushCallback;

            return logParameters;
        }
    }
}
