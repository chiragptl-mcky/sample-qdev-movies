# Movie Service - Spring Boot Demo Application 🏴‍☠️

A swashbuckling movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a pirate twist!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🆕 Pirate Movie Search**: Ahoy! Search and filter movies like a true treasure hunter with multiple criteria
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds, smooth animations, and pirate-themed search interface

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Thymeleaf** for templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Search**: http://localhost:8080/movies/search (with query parameters)
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── MovieService.java         # Business logic with search capabilities
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── MovieIconUtils.java   # Movie icon utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── application.yml               # Application configuration
│       ├── movies.json                   # Movie data treasure chest
│       ├── mock-reviews.json             # Mock review data
│       ├── templates/
│       │   ├── movies.html               # Enhanced with search form
│       │   └── movie-details.html        # Movie details page
│       └── log4j2.xml                    # Logging configuration
└── test/                                 # Comprehensive unit tests
    ├── MoviesControllerTest.java         # Controller tests with search scenarios
    └── MovieServiceTest.java             # Service layer tests
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and a pirate-themed search form.

### 🆕 Search Movies (Treasure Hunt!)
```
GET /movies/search
```
Ahoy matey! Search for movies using various criteria like a true pirate treasure hunter!

**Query Parameters (all optional):**
- `name` (string): Movie name to search for (partial match, case-insensitive)
- `id` (number): Specific movie ID to find (exact match)
- `genre` (string): Genre to filter by (partial match, case-insensitive)

**Examples:**
```bash
# Search by movie name - hunt for treasure by name!
http://localhost:8080/movies/search?name=Prison

# Search by genre - sort treasure by type!
http://localhost:8080/movies/search?genre=Drama

# Search by ID - find specific treasure!
http://localhost:8080/movies/search?id=1

# Combined search - advanced treasure hunting!
http://localhost:8080/movies/search?name=Family&genre=Crime

# Case-insensitive search - smart pirate navigation!
http://localhost:8080/movies/search?name=PIRATE&genre=ADVENTURE
```

**Response:**
Returns the same movies HTML page with filtered results and search status messages in pirate language.

**Edge Cases Handled:**
- Empty search results: "Shiver me timbers! No movies found matching yer search criteria."
- No search criteria: "Arrr! Ye need to provide some search criteria, matey!"
- Invalid parameters: Gracefully handled with appropriate pirate-themed messages

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Search Features 🔍

### Available Genres
The treasure chest contains movies in these genres:
- Action/Crime
- Action/Sci-Fi
- Adventure/Fantasy
- Adventure/Sci-Fi
- Crime/Drama
- Drama
- Drama/History
- Drama/Romance
- Drama/Thriller

### Search Capabilities
- **Name Search**: Partial, case-insensitive matching
- **Genre Search**: Partial, case-insensitive matching (finds "Drama" in "Crime/Drama")
- **ID Search**: Exact matching for specific movie lookup
- **Combined Search**: Use multiple criteria with AND logic
- **Input Validation**: Handles empty, null, and whitespace-only inputs
- **Responsive UI**: Search form adapts to mobile and desktop screens

### Pirate Language Features
- Search form labels and buttons use pirate terminology
- Success/error messages in authentic pirate speak
- Logging includes pirate-themed messages for debugging
- Comments in code reference treasure hunting and nautical terms

## Testing

Run the comprehensive test suite:

```bash
# Run all tests - check if the ship is seaworthy!
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest
```

### Test Coverage
- **MovieService**: Search functionality, validation, edge cases
- **MoviesController**: All endpoints including search with various scenarios
- **Integration Tests**: End-to-end search workflows
- **Edge Cases**: Empty results, invalid inputs, combined criteria

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check application logs for pirate-themed error messages
2. Verify search parameters are properly URL-encoded
3. Ensure at least one search criterion is provided
4. Check browser developer tools for JavaScript errors

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the treasure chest (movies.json)
- Enhance the pirate-themed UI/UX
- Add new search features (year range, rating filters, etc.)
- Improve the responsive design
- Add more pirate language and nautical terms
- Implement advanced search with fuzzy matching

### Development Guidelines
- Maintain pirate theme in user-facing messages
- Follow existing code patterns for consistency
- Add comprehensive tests for new features
- Update documentation with pirate flair
- Ensure responsive design compatibility

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye navigate this movie treasure chest! 🏴‍☠️⚓*
