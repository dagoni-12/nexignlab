package ru.anger.nexignlab.models;

public class Udr {
    private String msisdn;
    private IncomingCall incomingCall;
    private OutcomingCall outcomingCall;

    public static class IncomingCall {
        private String totalTime;

        public IncomingCall(String totalTime) {
            this.totalTime = totalTime;
        }

        public String getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(String totalTime) {
            this.totalTime = totalTime;
        }
    }

    public static class OutcomingCall {
        private String totalTime;

        public OutcomingCall(String totalTime) {
            this.totalTime = totalTime;
        }

        public String getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(String totalTime) {
            this.totalTime = totalTime;
        }
    }

    public Udr() {
    }

    public Udr(String msisdn, String incomingCallTime, String outcomingCallTime) {
        this.msisdn = msisdn;
        this.incomingCall = new IncomingCall(incomingCallTime);
        this.outcomingCall = new OutcomingCall(outcomingCallTime);
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public IncomingCall getIncomingCall() {
        return incomingCall;
    }

    public void setIncomingCall(IncomingCall incomingCall) {
        this.incomingCall = incomingCall;
    }

    public OutcomingCall getOutcomingCall() {
        return outcomingCall;
    }

    public void setOutcomingCall(OutcomingCall outcomingCall) {
        this.outcomingCall = outcomingCall;
    }
}