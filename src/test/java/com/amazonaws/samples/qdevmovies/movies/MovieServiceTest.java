package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arrr! Test class for the MovieService treasure hunting capabilities!
 * These tests ensure our movie search methods work like a well-oiled pirate ship.
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        // Initialize the service - loading our treasure chest!
        movieService = new MovieService();
    }

    @Test
    public void testGetAllMovies_FullTreasureChest() {
        // Test getting all movies - opening the full treasure chest!
        List<Movie> movies = movieService.getAllMovies();
        
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals(12, movies.size()); // Should have 12 movies from movies.json
    }

    @Test
    public void testGetMovieById_SpecificTreasure() {
        // Test getting a specific movie by ID - finding a specific treasure!
        Optional<Movie> movie = movieService.getMovieById(1L);
        
        assertTrue(movie.isPresent());
        assertEquals("The Prison Escape", movie.get().getMovieName());
        assertEquals(1L, movie.get().getId());
    }

    @Test
    public void testGetMovieById_NonexistentTreasure() {
        // Test getting a non-existent movie - searching for treasure that doesn't exist!
        Optional<Movie> movie = movieService.getMovieById(999L);
        
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieById_InvalidId() {
        // Test with invalid IDs - scurvy input handling!
        assertFalse(movieService.getMovieById(null).isPresent());
        assertFalse(movieService.getMovieById(0L).isPresent());
        assertFalse(movieService.getMovieById(-1L).isPresent());
    }

    @Test
    public void testSearchMoviesByName_TreasureHunt() {
        // Test searching by movie name - hunting for specific treasure!
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByName_CaseInsensitive() {
        // Test case-insensitive search - smart treasure hunting!
        List<Movie> results = movieService.searchMovies("PRISON", null, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByName_PartialMatch() {
        // Test partial name matching - using partial treasure maps!
        List<Movie> results = movieService.searchMovies("The", null, null);
        
        assertNotNull(results);
        assertTrue(results.size() >= 3); // Should find multiple movies starting with "The"
        
        // Verify all results contain "The" in the name
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"));
        }
    }

    @Test
    public void testSearchMoviesById_SpecificTreasure() {
        // Test searching by ID - finding a specific piece of treasure!
        List<Movie> results = movieService.searchMovies(null, 2L, null);
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());
        assertEquals(2L, results.get(0).getId());
    }

    @Test
    public void testSearchMoviesByGenre_SortingTreasure() {
        // Test searching by genre - sorting treasure by type!
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        
        assertNotNull(results);
        assertTrue(results.size() >= 2); // Should find multiple Drama movies
        
        // Verify all results contain "Drama" in the genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"));
        }
    }

    @Test
    public void testSearchMoviesByGenre_CaseInsensitive() {
        // Test case-insensitive genre search - smart treasure sorting!
        List<Movie> results = movieService.searchMovies(null, null, "DRAMA");
        
        assertNotNull(results);
        assertTrue(results.size() >= 2);
        
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"));
        }
    }

    @Test
    public void testSearchMoviesCombined_MultiCriteriaTreasureHunt() {
        // Test combined search criteria - advanced treasure hunting!
        List<Movie> results = movieService.searchMovies("Family", 2L, "Crime");
        
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());
        assertEquals(2L, results.get(0).getId());
        assertTrue(results.get(0).getGenre().toLowerCase().contains("crime"));
    }

    @Test
    public void testSearchMoviesNoResults_EmptyTreasureChest() {
        // Test search with no results - empty treasure chest!
        List<Movie> results = movieService.searchMovies("Nonexistent Movie", null, null);
        
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesAllNull_NoCompass() {
        // Test search with all null parameters - sailing without a compass!
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        assertNotNull(results);
        assertEquals(12, results.size()); // Should return all movies
    }

    @Test
    public void testSearchMoviesEmptyStrings_ScurvyInputs() {
        // Test search with empty strings - handling scurvy inputs!
        List<Movie> results = movieService.searchMovies("", null, "");
        
        assertNotNull(results);
        assertEquals(12, results.size()); // Should return all movies due to empty criteria
    }

    @Test
    public void testSearchMoviesWhitespaceOnly_TrimmedInputs() {
        // Test search with whitespace-only strings - trimmed inputs!
        List<Movie> results = movieService.searchMovies("   ", null, "   ");
        
        assertNotNull(results);
        assertEquals(12, results.size()); // Should return all movies due to whitespace-only criteria
    }

    @Test
    public void testHasValidSearchCriteria_NavigationTools() {
        // Test the validation methods - our navigation tools!
        
        // Valid criteria
        assertTrue(movieService.hasValidSearchCriteria("test", null, null));
        assertTrue(movieService.hasValidSearchCriteria(null, 1L, null));
        assertTrue(movieService.hasValidSearchCriteria(null, null, "Drama"));
        assertTrue(movieService.hasValidSearchCriteria("test", 1L, "Drama"));
        
        // Invalid criteria
        assertFalse(movieService.hasValidSearchCriteria(null, null, null));
        assertFalse(movieService.hasValidSearchCriteria("", null, ""));
        assertFalse(movieService.hasValidSearchCriteria("   ", null, "   "));
        assertFalse(movieService.hasValidSearchCriteria(null, 0L, null));
        assertFalse(movieService.hasValidSearchCriteria(null, -1L, null));
    }

    @Test
    public void testGetAllGenres_TreasureCategories() {
        // Test getting all genres - categorizing our treasure!
        List<String> genres = movieService.getAllGenres();
        
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        
        // Verify some expected genres are present
        assertTrue(genres.contains("Drama"));
        assertTrue(genres.contains("Crime/Drama"));
        assertTrue(genres.contains("Action/Crime"));
        
        // Verify genres are sorted and unique
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i).compareTo(genres.get(i - 1)) >= 0);
        }
    }

    @Test
    public void testSearchMoviesInvalidId_BarnacleOnTheHull() {
        // Test search with invalid ID - like a barnacle on the hull!
        List<Movie> results = movieService.searchMovies(null, -1L, null);
        
        assertNotNull(results);
        assertEquals(12, results.size()); // Should return all movies due to invalid ID
    }

    @Test
    public void testSearchMoviesZeroId_AnotherBarnacle() {
        // Test search with zero ID - another barnacle!
        List<Movie> results = movieService.searchMovies(null, 0L, null);
        
        assertNotNull(results);
        assertEquals(12, results.size()); // Should return all movies due to invalid ID
    }

    @Test
    public void testSearchMoviesGenrePartialMatch_FlexibleTreasureSorting() {
        // Test genre partial matching - flexible treasure sorting!
        List<Movie> results = movieService.searchMovies(null, null, "Action");
        
        assertNotNull(results);
        assertTrue(results.size() >= 1);
        
        // Should find movies with "Action/Crime" and "Action/Sci-Fi" genres
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("action"));
        }
    }

    @Test
    public void testSearchMoviesComplexScenario_MasterTreasureHunter() {
        // Test a complex search scenario - master treasure hunter level!
        
        // First, search for all Adventure movies
        List<Movie> adventureMovies = movieService.searchMovies(null, null, "Adventure");
        assertTrue(adventureMovies.size() >= 1);
        
        // Then search for a specific Adventure movie by name
        List<Movie> specificMovie = movieService.searchMovies("Quest", null, "Adventure");
        assertEquals(1, specificMovie.size());
        assertEquals("The Quest for the Ring", specificMovie.get(0).getMovieName());
        
        // Finally, verify the movie exists in the adventure collection
        assertTrue(adventureMovies.stream()
            .anyMatch(movie -> movie.getMovieName().equals("The Quest for the Ring")));
    }
}