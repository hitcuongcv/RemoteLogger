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
        private int triggerNumber = DEFAULT_TRIGGER_AMOUNT;
        private Class logEntityClass = PendingLogEntity.class;
        private LogPusher logPusher;
        private LogPusher.Callback pushCallback;

        /**
         * @param storeObserver a Runnable that will be triggered when the State changes.
         *                      Only using this setter if you want to use your own way to
         *                      interact with the changes of State. The default observer
         *                      will push log every time logs count reach the limit while
         *                      using Record Count Interruption. Modifying the default
         *                      observer will disable that feature.
         */
        public Builder setStoreObserver(Runnable storeObserver) {
            this.storeObserver = storeObserver;
            return this;
        }

        /**
         * @param interruptType push logs by range of time or by logs count.
         */
        public Builder setInterruptType(InterruptType interruptType) {
            this.interruptType = interruptType;
            return this;
        }

        /**
         * @param interval specified range of time for pushing logs (in minutes).
         */
        public Builder setInterval(long interval) {
            this.interval = interval;
            return this;
        }

        /**
         * @param triggerNumber specified the limitation of logs count for pushing logs.
         */
        public Builder setTriggerNumber(int triggerNumber) {
            this.triggerNumber = triggerNumber;
            return this;
        }

        /**
         * @param logEntityClass The log entity class. It have to implement the
         *                       base PendingLogEntity class.
         */
        public Builder setLogEntityClass(Class logEntityClass) {
            this.logEntityClass = logEntityClass;
            return this;
        }

        /**
         * @param logPusher The worker class for pushing logs.
         */
        public Builder setLogPusher(LogPusher logPusher) {
            this.logPusher = logPusher;
            return this;
        }

        /**
         * @param pushCallback The callback for handle the results of pushing logs.
         */
        public Builder setPushCallback(LogPusher.Callback pushCallback) {
            this.pushCallback = pushCallback;
            return this;
        }

        public LogParameters build() {
            LogParameters logParameters = new LogParameters();
            logParameters.storeObserver = this.storeObserver;
            logParameters.interruptType = this.interruptType;
            logParameters.interval = this.interval;
            logParameters.triggerAmount = this.triggerNumber;
            logParameters.logEntityClass = this.logEntityClass;
            logParameters.logPusher = this.logPusher;
            logParameters.pushCallback = this.pushCallback;

            return logParameters;
        }
    }
}
