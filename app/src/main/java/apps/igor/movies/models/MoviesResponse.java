package apps.igor.movies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse {
    /*
    "page": 1,
  "total_results": 6335,
  "total_pages": 317,
  "results"
     */
    @SerializedName("results")
    private List<Movie> results;

    public MoviesResponse(Integer page, Integer total_results, Integer total_pages, ArrayList<Movie> results) {
        this.results = results;
    }


    public List<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
