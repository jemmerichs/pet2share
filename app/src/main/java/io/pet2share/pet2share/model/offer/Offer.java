package io.pet2share.pet2share.model.offer;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.pet2share.pet2share.data.offer.OfferLoader;

/**
 * Created by Bausch on 17.10.2016.
 */

public class Offer {
    private String name;
    private long startdate;
    private long enddate;
    private Double latitude, longitude;
    private ArrayList<Long> timeSlots;
    private String description;


    public Offer(String name, long startdate, long enddate, Double latitude, Double longitude, ArrayList<Long> timeSlots, String description) {
        this.name = name;
        this.startdate = startdate;
        this.enddate = enddate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeSlots = timeSlots;
        this.description = description;
    }

    public void setTimeSlots(ArrayList<Long> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Offer() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public Date getStartdate() {
        return new Date(startdate);
    }

    public Date getEnddate() {
        return new Date(enddate);
    }

    public ArrayList<Date> getTimeSlots() {
        ArrayList<Date> dates = new ArrayList<>();
        for (long dateStamp : timeSlots) {
            dates.add(new Date(dateStamp));
        }
        return dates;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("startdate", startdate);
        map.put("enddate", enddate);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("description", description);
        ArrayList<Long> timeslots = new ArrayList<>();
        for (long date : timeSlots) {
            timeslots.add(date);
        }
        map.put("timeSlots", timeslots);

        return map;
    }

    public double calculateDistanceToOfferinMeters(double latitude, double longitude) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(latitude - this.latitude);
        double dLng = Math.toRadians(longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(this.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}

