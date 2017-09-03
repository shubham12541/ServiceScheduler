package com.entra.barbers.models;

import java.io.Serializable;
import java.security.Timestamp;

/**
 * Created by shubham on 02/09/17.
 */

public class Review implements Serializable{
    String id, text;
    Long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
