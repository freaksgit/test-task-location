package vasyl.v.stoliarchuk.addresstracker.data.entity;

import java.util.List;

public class Place {

    private String placeId;

    private String licence;

    private String osmType;

    private String osmId;

    private String lat;

    private String lon;

    private String displayName;

    private Address address;

    private List<String> boundingbox;

    public Place() {
    }

    public Place(String placeId, String licence, String osmType, String osmId, String lat, String lon, String displayName, Address address, List<String> boundingbox) {
        this.placeId = placeId;
        this.licence = licence;
        this.osmType = osmType;
        this.osmId = osmId;
        this.lat = lat;
        this.lon = lon;
        this.displayName = displayName;
        this.address = address;
        this.boundingbox = boundingbox;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public String getOsmId() {
        return osmId;
    }

    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }
}
