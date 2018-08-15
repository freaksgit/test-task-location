package vasyl.v.stoliarchuk.addresstracker.data.datasource.remote.retrofit.entity;

import com.google.gson.annotations.SerializedName;

public class RetrofitAddress {

    @SerializedName("house_number")
    private String houseNumber;

    @SerializedName("road")
    private String road;

    @SerializedName("neighbourhood")
    private String neighbourhood;

    @SerializedName("suburb")
    private String suburb;

    @SerializedName("county")
    private String county;

    @SerializedName("city")
    private String city;

    @SerializedName("postcode")
    private String postcode;

    @SerializedName("country")
    private String country;

    @SerializedName("country_code")
    private String countryCode;

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
