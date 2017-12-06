package kr.ac.hifly.attention.data;

/**
 * Created by CYSN on 2017-12-01.
 */

public class Call {
    private String caller;//uuid
    private String callState;
    private String callerName;
    private String ip;

    public Call(String caller, String callState, String callerName, String ip) {
        this.caller = caller;
        this.callState = callState;
        this.callerName = callerName;
        this.ip = ip;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallState() {
        return callState;
    }

    public void setCallState(String callState) {
        this.callState = callState;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
