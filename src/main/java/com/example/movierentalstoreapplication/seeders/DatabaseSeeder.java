package com.example.movierentalstoreapplication.seeders;

import com.example.movierentalstoreapplication.model.Customer;
import com.example.movierentalstoreapplication.model.movie.*;
import com.example.movierentalstoreapplication.model.services.MovieRentalService;
import com.example.movierentalstoreapplication.repositories.CustomerRepository;
import com.example.movierentalstoreapplication.repositories.MovieOrderRepository;

import com.example.movierentalstoreapplication.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseSeeder {
    private MovieRentalService movieRentalService;
    private MovieRepository movieRepository;
    private CustomerRepository customerRepository;
    private MovieOrderRepository movieOrderRepository;

    @Autowired
    public DatabaseSeeder(
            MovieRentalService movieRentalService,
            MovieRepository movieRepository,
            CustomerRepository customerRepository,
            MovieOrderRepository movieOrderRepository
    ) {
        this.movieRentalService = movieRentalService;
        this.movieRepository = movieRepository;
        this.customerRepository = customerRepository;
        this.movieOrderRepository = movieOrderRepository;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void seed() {
        List<Customer> customers = this.seedCustomersTable();
        List<Movie> movies = this.seedMoviesTable();
        this.seedOrdersTable(customers, movies);
    }

    public List<Customer> seedCustomersTable() {
        List<Customer> customers = new ArrayList<>(
                Arrays.asList(
                        new Customer("Robert", "Martin", 20, 25),
                        new Customer("Eric", "Evans", 10, 5),
                        new Customer("Ervin", "Knuth", 3, 1),
                        new Customer("Martin", "Flower")
                )
        );

        return customerRepository.saveAll(customers);
    }

    public List<Movie> seedMoviesTable() {
        List<Movie> movies = new ArrayList<>(
                Arrays.asList(
                        new Movie(
                                "Avengers: Endgame",
                                "The grave course of events set in motion by Thanos that wiped out half the universe and fractured the Avengers ranks compels the remaining Avengers to take one final stand in 'Marvel Studios' grand conclusion to twenty-two films, 'Avengers: Endgame'.",
                                2019,
                                MovieType.NEW
                        ),
                        new Movie(
                                "Spider-Man: Into the Spider-Verse",
                                "Original, action-packed animated Marvel adventure that centers on Brooklyn teen Miles Morales, who becomes a new Spider-Man and ends up meeting other Spider-people from parallel universes.",
                                2019,
                                MovieType.NEW
                        ),
                        new Movie(
                                "Jumanji: Welcome to the Jungle",
                                "In a brand new Jumanji adventure, four high school kids discover an old video game console and are drawn into the game's jungle setting, literally becoming the adult avatars they chose. What they discover is that you don't just play Jumanji - you must survive it.",
                                2017,
                                MovieType.REGULAR
                        ),
                        new Movie(
                                "Batman vs Superman: Dawn of Justice",
                                "Batman v Superman: Dawn of Justice is the second film in the DC Extended Universe, following on from 2013's Man of Steel, and also directed by Zack Snyder. Henry Cavill returned as Clark Kent/Superman, as well the other principle cast members from Man of Steel. Ben Affleck, Jesse Eisenberg and Gal Gadot joined the cast as Bruce Wayne/Batman, Lex Luthor and Diana Prince/Wonder Woman respectively. The film was released on March 25, 2016.",
                                2016,
                                MovieType.REGULAR
                        ),
                        new Movie(
                                "The Matrix",
                                "The Matrix is a 1999 science fiction action film written and directed by the Wachowskis, starring Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Joe Pantoliano, and Hugo Weaving. It was first released in the USA on March 31, 1999, and is the first entry in The Matrix series of films, comics, video games and animation. The film received four Academy Awards in the technical categories.",
                                1999,
                                MovieType.OLD
                        ),
                        new Movie(
                                "Jumanji",
                                "The story centers on a supernatural board game that releases jungle-based hazards upon its players with every turn they take. As a boy in 1969, Alan Parrish became trapped inside the game itself while playing with his best friend Sarah Whittle.",
                                1995,
                                MovieType.OLD
                        ),
                        new Movie(
                                "Forrest Gump",
                                "Forrest Gump is a simple man with a low I.Q. but good intentions. He is running through childhood with his best and only friend Jenny. His 'mama' teaches him the ways of life and leaves him to choose his destiny.",
                                1994,
                                MovieType.OLD,
                                false
                        ),
                        new Movie(
                                "Back to the Future",
                                "Back to the Future is a 1985 American science fiction film directed by Robert Zemeckis and written by Zemeckis and Bob Gale. It stars Michael J. Fox as teenager Marty McFly, who accidentally travels back in time to 1955, where he meets his future parents and becomes his mother's romantic interest.",
                                1985,
                                MovieType.OLD,
                                true
                        )
                )
        );

        return movieRepository.saveAll(movies);
    }

    public void seedOrdersTable(List<Customer> customers, List<Movie> movies) {
        Customer customer = customers.get(0);
        MovieOrder movieOrder1 = new MovieOrder(customer, MovieOrder.Status.OPENED);

        movieOrder1.setRentals(
                Arrays.asList(
                        movieRentalService.rentMovie(
                                customer,
                                new MovieRental(movieOrder1, movies.get(0), 1, 0)
                        ),
                        movieRentalService.rentMovie(
                                customer,
                                new MovieRental(movieOrder1, movies.get(1), 2, 0)
                        ),
                        movieRentalService.rentMovie(
                                customer,
                                new MovieRental(movieOrder1, movies.get(3), 3, 0)
                        )
                )
        );

        movieOrderRepository.save(movieOrder1);

        Customer customer2 = customers.get(1);
        MovieOrder movieOrder2 = new MovieOrder(customer2, MovieOrder.Status.OPENED);

        movieOrder2.setRentals(
                Arrays.asList(
                        movieRentalService.rentMovie(
                                customer2,
                                new MovieRental(movieOrder2, movies.get(4), 3, 0)
                        ),
                        movieRentalService.rentMovie(
                                customer2,
                                new MovieRental(movieOrder2, movies.get(5), 2, 0)
                        ),
                        movieRentalService.rentMovie(
                                customer2,
                                new MovieRental(movieOrder2, movies.get(6), 3, 0)
                        )
                )
        );

        MovieOrder savedMovieOrder2 = movieOrderRepository.save(movieOrder2);
        movieOrder2.setStatus(MovieOrder.Status.CLOSED);
        movieOrder2.setRentals(
                savedMovieOrder2.getRentals().stream()
                        .map(movieRental -> movieRentalService.returnMovie(customer2, movieRental))
                        .collect(Collectors.toList())
        );
        movieOrderRepository.save(movieOrder2);


        Customer customer3 = customers.get(2);
        MovieOrder movieOrder3 = new MovieOrder(customer3, MovieOrder.Status.OPENED);

        movieOrder3.setRentals(
                Arrays.asList(
                        movieRentalService.rentMovie(
                                customer3,
                                new MovieRental(movieOrder3, movies.get(7), 1, 0)
                        )
                )
        );

        MovieOrder savedMovieOrder3 = movieOrderRepository.save(movieOrder3);
        movieOrder3.setStatus(MovieOrder.Status.CLOSED);
        movieOrder3.setRentals(
                savedMovieOrder3.getRentals().stream()
                        .map(movieRental -> movieRentalService.returnMovie(customer3, movieRental))
                        .collect(Collectors.toList())
        );
        movieOrderRepository.save(movieOrder3);
    }
}
