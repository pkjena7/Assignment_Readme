package com.example.assignment_readme;

public class Data {
    private String query;
    private String timezone;

    public Data(String query, String timezone) {
        this.query = query;
        this.timezone = timezone;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Data{" +
                "query='" + query + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
