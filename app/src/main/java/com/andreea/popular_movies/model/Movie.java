package com.andreea.popular_movies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.andreea.popular_movies.utils.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = Constants.Database.FAVORITE_MOVIES_TABLE)
public class Movie {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w780";
    private static final String BACKDROP_SIZE = "w1280";

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private Integer id;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    private String originalTitle;

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    private String overview;

    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    private String originalLanguage;

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @SerializedName("video")
    @ColumnInfo(name = "video")
    private Boolean hasVideo;

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private Double voteAverage;

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    private Double popularity;

    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    private Boolean adult;

    @SerializedName("genre_ids")
    @Ignore
    private List<Integer> genreIDs;

    public Movie(Integer id, String title, String originalTitle, String overview, String originalLanguage, String releaseDate, String posterPath, String backdropPath, Boolean hasVideo, Integer voteCount, Double voteAverage, Double popularity, Boolean adult, List<Integer> genreIDs) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.hasVideo = hasVideo;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.adult = adult;
        this.genreIDs = genreIDs;
    }

    /**
     * Constructor used by the Room Database
     */
    public Movie(@NonNull Integer id, String title, String originalTitle, String overview, String originalLanguage, String releaseDate, String posterPath, String backdropPath, Boolean hasVideo, Integer voteCount, Double voteAverage, Double popularity, Boolean adult) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.hasVideo = hasVideo;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.adult = adult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Boolean getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(Boolean hasVideo) {
        this.hasVideo = hasVideo;
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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public List<Integer> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<Integer> genreIDs) {
        this.genreIDs = genreIDs;
    }

    /**
     * Computes the final poster URL based on the base URL, the image size and the poster path
     */
    public String computeFinalPosterUrl() {
        StringBuilder sb = new StringBuilder()
                .append(IMAGE_BASE_URL)
                .append(IMAGE_SIZE)
                .append(posterPath);
        return sb.toString();
    }

    /**
     * Computes the final poster URL based on the base URL, the image size and the poster path
     */
    public String computeFinalBackdropUrl() {
        StringBuilder sb = new StringBuilder()
                .append(IMAGE_BASE_URL)
                .append(BACKDROP_SIZE)
                .append(backdropPath);
        return sb.toString();
    }
}
