package apps.igor.movies.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("release_date")
    private String releaseDate;


    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;


    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String coverPath;

    @SerializedName("genre_ids")
    private List<Integer> genresId;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("popularity")
    private Double popularity;

    private DetailResponse detailResponse;

    public Movie(Double popularity, Integer id, String releaseDate, String title, String overview, String posterPath, String coverPath, List<Integer> genresId, Integer voteCount, Double voteAverage) {
        this.id = id;
        this.releaseDate = releaseDate;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.coverPath = coverPath;
        this.genresId = genresId;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.popularity = popularity;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public DetailResponse getDetailResponse() {
        return detailResponse;
    }

    public void setDetailResponse(DetailResponse detailResponse) {
        this.detailResponse = detailResponse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) { this.coverPath = coverPath; }

    public List<Integer> getGenresId() {
        return genresId;
    }

    public void setGenresId(List<Integer> genresId) {
        this.genresId = genresId;
    }
}
