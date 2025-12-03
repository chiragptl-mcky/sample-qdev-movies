package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-themed test data
        mockMovieService = new MovieService() {
            private final List<Movie> testMovies = Arrays.asList(
                new Movie(1L, "The Pirate's Adventure", "Captain Director", 2023, "Adventure", "A swashbuckling tale", 120, 4.5),
                new Movie(2L, "Treasure Island", "Seafaring Filmmaker", 2022, "Adventure", "Hunt for buried treasure", 110, 4.0),
                new Movie(3L, "The Kraken's Revenge", "Ocean Moviemaker", 2021, "Horror", "Sea monster terror", 95, 3.5),
                new Movie(4L, "Blackbeard's Legacy", "Pirate Producer", 2020, "Drama", "Historical pirate drama", 140, 4.8)
            );
            
            @Override
            public List<Movie> getAllMovies() {
                return testMovies;
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                return testMovies.stream().filter(movie -> movie.getId() == id).findFirst();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> results = new ArrayList<>(testMovies);
                
                if (name != null && !name.trim().isEmpty()) {
                    String searchName = name.trim().toLowerCase();
                    results = results.stream()
                        .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                        .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
                }
                
                if (id != null && id > 0) {
                    results = results.stream()
                        .filter(movie -> movie.getId() == id)
                        .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
                }
                
                if (genre != null && !genre.trim().isEmpty()) {
                    String searchGenre = genre.trim().toLowerCase();
                    results = results.stream()
                        .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                        .collect(ArrayList::new, (list, movie) -> list.add(movie), ArrayList::addAll);
                }
                
                return results;
            }
            
            @Override
            public boolean hasValidSearchCriteria(String name, Long id, String genre) {
                boolean hasName = name != null && !name.trim().isEmpty();
                boolean hasId = id != null && id > 0;
                boolean hasGenre = genre != null && !genre.trim().isEmpty();
                return hasName || hasId || hasGenre;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Adventure", "Drama", "Horror");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
        
        // Verify model attributes for regular movie listing
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("genres"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertFalse((Boolean) model.asMap().get("searchPerformed"));
    }

    @Test
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    // Arrr! Pirate-themed search tests begin here, matey!
    
    @Test
    public void testSearchMoviesByName_TreasureHunt() {
        // Test searching by movie name - like hunting for a specific treasure!
        String result = moviesController.searchMovies("Pirate", null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue((Boolean) model.asMap().get("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size());
        assertEquals("The Pirate's Adventure", movies.get(0).getMovieName());
    }
    
    @Test
    public void testSearchMoviesById_SpecificTreasure() {
        // Test searching by ID - finding a specific piece of treasure!
        String result = moviesController.searchMovies(null, 2L, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size());
        assertEquals("Treasure Island", movies.get(0).getMovieName());
    }
    
    @Test
    public void testSearchMoviesByGenre_SortingTreasure() {
        // Test searching by genre - sorting treasure by type!
        String result = moviesController.searchMovies(null, null, "Adventure", model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(2, movies.size()); // Should find both Adventure movies
    }
    
    @Test
    public void testSearchMoviesCombined_MultiCriteriaTreasureHunt() {
        // Test combined search criteria - advanced treasure hunting!
        String result = moviesController.searchMovies("Treasure", null, "Adventure", model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(1, movies.size());
        assertEquals("Treasure Island", movies.get(0).getMovieName());
    }
    
    @Test
    public void testSearchMoviesNoResults_EmptyTreasureChest() {
        // Test search with no results - empty treasure chest!
        String result = moviesController.searchMovies("Nonexistent Movie", null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertTrue(movies.isEmpty());
        
        String searchMessage = (String) model.asMap().get("searchMessage");
        assertNotNull(searchMessage);
        assertTrue(searchMessage.contains("No movies found"));
    }
    
    @Test
    public void testSearchMoviesNoCriteria_SailingWithoutCompass() {
        // Test search without criteria - sailing without a compass!
        String result = moviesController.searchMovies(null, null, null, model);
        
        assertNotNull(result);
        assertEquals("movies", result);
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.asMap().get("movies");
        assertEquals(4, movies.size()); // Should return all movies
        
        String searchMessage = (String) model.asMap().get("searchMessage");
        assertNotNull(searchMessage);
        assertTrue(searchMessage.contains("need to provide some search criteria"));
    }

    @Test
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(4, movies.size());
        assertEquals("The Pirate's Adventure", movies.get(0).getMovieName());
    }
}
