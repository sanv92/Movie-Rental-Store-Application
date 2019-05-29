package com.example.movierentalstoreapplication.model.movie;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_generator")
    @SequenceGenerator(
            name = "movie_id_generator",
            sequenceName = "movie_id_seq",
            allocationSize = 1
    )
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "title", unique = true, nullable = false)
    private String title;

    @NotBlank
    @Size(min = 160)
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Min(1970)
    @Column(name = "year", columnDefinition = "smallint", nullable = false)
    private int year;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieType type;

    @NotNull
    @Column(name = "published", nullable = false)
    private boolean published = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie", fetch = FetchType.LAZY)
    private List<MovieRental> rentals;

    public Movie() {
    }

    public Movie(String title, String description, int year, MovieType type) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.type = type;
    }

    public Movie(String title, String description, int year, MovieType type, boolean published) {
        this.title = title;
        this.description = description;
        this.year = year;
        this.type = type;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public MovieType getType() {
        return type;
    }

    public boolean isPublished() {
        return published;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public Movie setDescription(String description) {
        this.description = description;
        return this;
    }

    public Movie setYear(int year) {
        this.year = year;
        return this;
    }

    public Movie setType(MovieType type) {
        this.type = type;
        return this;
    }

    public Movie setPublished(boolean published) {
        this.published = published;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
                Objects.equals(title, movie.title) &&
                Objects.equals(year, movie.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", type=" + type +
                ", published=" + published +
                '}';
    }
}