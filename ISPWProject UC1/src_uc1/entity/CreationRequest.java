package entity;

import java.util.ArrayList;

public class CreationRequest {
    private String booker;
    private String date;
    private ArrayList features;
    private String event_type;
    private String session;
    private String course;
    private String begin;
    private String end;
    private ArrayList class_pref;
    private long n_seats;
    private String req_id;

    public CreationRequest(){
        begin = null;
        class_pref = null;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public String getBooker() {
        return booker;
    }

    public void setBooker(String booker) {
        this.booker = booker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList features) {
        this.features = features;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public ArrayList getClass_pref() {
        return class_pref;
    }

    public void setClass_pref(ArrayList class_pref) {
        this.class_pref = class_pref;
    }

    public long getN_seats() {
        return n_seats;
    }

    public void setN_seats(long n_seats) {
        this.n_seats = n_seats;
    }
}
