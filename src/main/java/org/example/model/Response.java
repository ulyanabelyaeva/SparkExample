package org.example.model;

import java.io.Serializable;

public class Response implements Serializable {

    private String fid;

    private String year;

    private String quarter;

    public Response() {
    }

    public Response(String fid, String year, String quarter) {
        this.fid = fid;
        this.year = year;
        this.quarter = quarter;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
