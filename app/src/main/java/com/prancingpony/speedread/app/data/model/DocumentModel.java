package com.prancingpony.speedread.app.data.model;

import java.util.Date;

/**
 * Created by SaitSami on 17.5.2015.
 */
public class DocumentModel {

    private int id;

    private Date timestamp;

    private String uri;

    private String text;

    private int location;

    private int sum;

    public DocumentModel(int id, Date timestamp, String uri, String text, int location, int sum) {
        this.id = id;
        this.timestamp = timestamp;
        this.uri = uri;
        this.text = text;
        this.location = location;
        this.sum = sum;
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUri() {
        return uri;
    }

    public String getText() {
        return text;
    }

    public int getLocation() {
        return location;
    }

    public int getSum() {
        return sum;
    }
}
