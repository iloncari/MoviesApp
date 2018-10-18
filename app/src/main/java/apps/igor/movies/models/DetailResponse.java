package apps.igor.movies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailResponse {
    @SerializedName("genres")
    private List<Genres> genresList;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("populatity")
    private Double popularity;
    @SerializedName("production_countries")
    private List<ProductionCountries> countries;
    @SerializedName("vote_average")
    private Double vote_average;
    @SerializedName("vote_count")
    private Integer voteCount;

    public DetailResponse( List<Genres> genresList, String homepage, Double popularity, List<ProductionCountries> countries, Double vote_average, Integer voteCount) {
        this.genresList = genresList;
        this.homepage = homepage;
        this.popularity = popularity;
        this.countries = countries;
        this.vote_average = vote_average;
        this.voteCount = voteCount;
    }



    public List<Genres> getGenresList() {
        return genresList;
    }

    public List<String> getGenresNames(){
        List<String> genresListName = new ArrayList<>();

        for(Genres g: genresList)
            genresListName.add(g.getName());

        return genresListName;
    }

    public void setGenresList(List<Genres> genresList) {
        this.genresList = genresList;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public List<ProductionCountries> getCountries() {
        return countries;
    }
    public List<String> getProductionCountriesName(){
        List<String> countryNames = new ArrayList<>();

        for(ProductionCountries c: getCountries())
            countryNames.add(c.getCountryName());

        return countryNames;
    }

    public void setCountries(List<ProductionCountries> countries) {
        this.countries = countries;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }
}
