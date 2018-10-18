package apps.igor.movies.models;

import com.google.gson.annotations.SerializedName;

class ProductionCountries {
    @SerializedName("name")
    private String countryName;

    public ProductionCountries(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
