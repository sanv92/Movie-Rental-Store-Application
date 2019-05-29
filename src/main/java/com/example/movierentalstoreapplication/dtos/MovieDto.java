package com.example.movierentalstoreapplication.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class MovieDto {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String title;

    @NotBlank
    @Size(min = 160)
    private String description;

    @NotNull
    private Integer year;

    @NotNull
    private Boolean published;

    @NotBlank
    private String type;

    @Min(0)
    private Double price = 0.0;

    @Min(0)
    private Integer points = 0;

    public MovieDto() {
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

    public Integer getYear() {
        return year;
    }

    public Boolean getPublished() {
        return published;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getPoints() {
        return points;
    }

    public MovieDto setId(Long id) {
        this.id = id;
        return this;
    }

    public MovieDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public MovieDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public MovieDto setYear(Integer year) {
        this.year = year;
        return this;
    }

    public MovieDto setPublished(Boolean published) {
        this.published = published;
        return this;
    }

    public MovieDto setType(String type) {
        this.type = type;
        return this;
    }

    public MovieDto setPrice(Double price) {
        this.price = price;
        return this;
    }

    public MovieDto setPoints(Integer points) {
        this.points = points;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDto)) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(id, movieDto.id) &&
                Objects.equals(title, movieDto.title) &&
                Objects.equals(year, movieDto.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", published=" + published +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", points=" + points +
                '}';
    }
}