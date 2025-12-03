package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! Search for movies using various criteria like a treasure hunt!
     * This method filters the movie treasure chest based on name, id, and genre.
     * 
     * @param name Movie name to search for (partial match, case-insensitive)
     * @param id Specific movie ID to find
     * @param genre Genre to filter by (case-insensitive)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Starting treasure hunt for movies with criteria - name: {}, id: {}, genre: {}", 
                   name, id, genre);
        
        List<Movie> searchResults = new ArrayList<>(movies);
        
        // Filter by movie name if provided - like searching for a specific treasure map!
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            searchResults = searchResults.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
            logger.debug("Arrr! After name filter '{}', found {} movies", name, searchResults.size());
        }
        
        // Filter by ID if provided - hunting for a specific piece of treasure!
        if (id != null && id > 0) {
            searchResults = searchResults.stream()
                .filter(movie -> movie.getId() == id)
                .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
            logger.debug("Shiver me timbers! After ID filter '{}', found {} movies", id, searchResults.size());
        }
        
        // Filter by genre if provided - sorting treasure by type!
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            searchResults = searchResults.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
            logger.debug("Batten down the hatches! After genre filter '{}', found {} movies", genre, searchResults.size());
        }
        
        logger.info("Treasure hunt complete! Found {} movies matching the search criteria", searchResults.size());
        return searchResults;
    }

    /**
     * Validates search parameters to avoid scurvy bugs in our treasure hunt!
     * 
     * @param name Movie name parameter
     * @param id Movie ID parameter  
     * @param genre Genre parameter
     * @return true if at least one valid search parameter is provided
     */
    public boolean hasValidSearchCriteria(String name, Long id, String genre) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasId = id != null && id > 0;
        boolean hasGenre = genre != null && !genre.trim().isEmpty();
        
        return hasName || hasId || hasGenre;
    }

    /**
     * Get all unique genres from our treasure chest of movies!
     * Useful for helping landlubbers know what genres be available.
     * 
     * @return List of unique genres
     */
    public List<String> getAllGenres() {
        return movies.stream()
            .map(Movie::getGenre)
            .distinct()
            .sorted()
            .collect(ArrayList::new, (list, genre) -> list.add(genre), ArrayList::addAll);
    }
}
