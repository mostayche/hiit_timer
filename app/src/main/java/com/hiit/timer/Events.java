package com.hiit.timer;

public class Events {

    private String event, eventDay, numberOfEvents, eventMonth, eventYear;

    public Events(String event, String eventDay, String numberOfEvents, String eventMonth, String eventYear) {
        this.event = event;
        this.eventDay = eventDay;
        this.numberOfEvents = numberOfEvents;
        this.eventMonth = eventMonth;
        this.eventYear = eventYear;
    }

    public String getEventDay() {
        return eventDay;
    }

    public String getNumberOfEvents() {
        return numberOfEvents;
    }

    public String getEventMonth() {
        return eventMonth;
    }

    public String getEventYear() {
        return eventYear;
    }

}
