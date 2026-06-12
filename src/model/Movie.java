package model;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final long serialVersionUID = 6L;

    private int movieId;
    private String title;
    private String genre;
    private String director;
    private String actors;
    private int durationMin;
    private String description;
    private String posterPath;
    private String status;

    public Movie() {}

    public Movie(int movieId, String title, String genre, String director, String actors,
                 int durationMin, String description, String posterPath, String status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.actors = actors;
        this.durationMin = durationMin;
        this.description = description;
        this.posterPath = posterPath;
        this.status = status;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors = actors; }
    public int getDurationMin() { return durationMin; }
    public void setDurationMin(int durationMin) { this.durationMin = durationMin; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() { return title; }
}
