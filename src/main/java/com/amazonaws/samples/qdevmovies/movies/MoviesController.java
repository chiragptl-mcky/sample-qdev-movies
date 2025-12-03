package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Ahoy! Fetching all movies from the treasure chest");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenres());
        model.addAttribute("searchPerformed", false);
        return "movies";
    }

    /**
     * Arrr! Search for movies like a true pirate hunting for treasure!
     * This endpoint accepts query parameters to filter the movie collection.
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to find (optional) 
     * @param genre Genre to filter by (optional)
     * @param model Spring MVC model for the view
     * @return movies template with search results
     */
    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy matey! Starting treasure hunt with search criteria - name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        // Check if we have valid search criteria, or we'll be sailing without a compass!
        if (!movieService.hasValidSearchCriteria(name, id, genre)) {
            logger.warn("Shiver me timbers! No valid search criteria provided - showing all movies instead");
            model.addAttribute("movies", movieService.getAllMovies());
            model.addAttribute("searchMessage", "Arrr! Ye need to provide some search criteria, matey! Showing all movies instead.");
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", "");
            model.addAttribute("searchId", "");
            model.addAttribute("searchGenre", "");
        } else {
            // Perform the treasure hunt!
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            if (searchResults.isEmpty()) {
                logger.info("Batten down the hatches! No movies found matching the search criteria");
                model.addAttribute("movies", searchResults);
                model.addAttribute("searchMessage", "Shiver me timbers! No movies found matching yer search criteria. Try different terms, ye scallywag!");
            } else {
                logger.info("Treasure found! {} movies match the search criteria", searchResults.size());
                model.addAttribute("movies", searchResults);
                model.addAttribute("searchMessage", String.format("Ahoy! Found %d movie%s matching yer search, matey!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            }
            
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name != null ? name : "");
            model.addAttribute("searchId", id != null ? id.toString() : "");
            model.addAttribute("searchGenre", genre != null ? genre : "");
        }
        
        // Always provide genres for the dropdown and preserve search state
        model.addAttribute("genres", movieService.getAllGenres());
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Ahoy! Fetching details for movie treasure with ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Blimey! Movie with ID {} not found in our treasure chest", movieId);
            model.addAttribute("title", "Movie Not Found - Arrr!");
            model.addAttribute("message", "Shiver me timbers! Movie with ID " + movieId + " was not found in our treasure chest, matey!");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}