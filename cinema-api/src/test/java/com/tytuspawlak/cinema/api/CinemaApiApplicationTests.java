package com.tytuspawlak.cinema.api;

import com.tytuspawlak.cinema.api.controller.movie.dto.MovieAddRatingRequestTO;
import com.tytuspawlak.cinema.api.controller.movie.dto.MovieRatingToAddTO;
import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemToSaveTO;
import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemsAddRequestTO;
import com.tytuspawlak.cinema.api.controller.schedule.dto.ScheduleItemsUpdateRequestTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieDTO;
import com.tytuspawlak.cinema.core.dto.movie.MovieIdType;
import com.tytuspawlak.cinema.core.dto.movie.MovieRatingDTO;
import com.tytuspawlak.cinema.core.dto.schedule.ScheduleItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CinemaApiApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
		registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
	}

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testBasicApiUsage() {

		MovieDTO movie = testListingAndAddingMovies();

		testFindingMovieByIds(movie);

		testWorkingWithMovieSchedule(movie);

		testAddingReviewRatingForMovie(movie);
	}

	private MovieDTO testListingAndAddingMovies() {
		// check if there are initially no movies in the DB
		{
			ResponseEntity<List<MovieDTO>> response = listMovies();

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertTrue(response.getBody().isEmpty());
		}

		// add default movies
		{
			ResponseEntity<List<MovieDTO>> response = restTemplate.exchange(
					"/internal-api/movie/default",
					HttpMethod.POST,
					null,
					new ParameterizedTypeReference<>() {}
			);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertEquals(8, response.getBody().size());
		}

		// list movies again and select one for further processing
		MovieDTO movie;
		{
			ResponseEntity<List<MovieDTO>> response = listMovies();

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertEquals(8, response.getBody().size());

			movie = response.getBody().get(2);
			assertEquals("tt0463985", movie.getImdbId());
			assertEquals("The Fast and the Furious: Tokyo Drift", movie.getTitle());
		}
		return movie;
	}

	private ResponseEntity<List<MovieDTO>> listMovies() {
		return restTemplate.exchange(
				"/api/movie/",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>(){}
		);
	}

	private void testFindingMovieByIds(MovieDTO movie) {
		// find movie by local DB ID
		{
			ResponseEntity<Optional<MovieDTO>> response = restTemplate.exchange(
					"/api/movie/" + movie.getId(),
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {}
			);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertTrue(response.getBody().isPresent());
			MovieDTO foundMovie = response.getBody().get();
			assertEquals(movie, foundMovie);
		}

		// find movie by IMDb ID
		{
			ResponseEntity<Optional<MovieDTO>> response = restTemplate.exchange(
					"/api/movie/" + movie.getImdbId() + "?idType=IMDB",
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {}
			);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertTrue(response.getBody().isPresent());
			MovieDTO foundMovie = response.getBody().get();
			assertEquals(movie, foundMovie);
		}
	}

	private void testWorkingWithMovieSchedule(MovieDTO movie) {
		// check if there are initially no schedule items for the movie
		{
			ResponseEntity<List<ScheduleItemDTO>> response = listScheduleItems(movie);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			assertTrue(response.getBody().isEmpty());
		}

		// add two schedule items
		{
			HttpEntity<ScheduleItemsAddRequestTO> request = new HttpEntity<>(
					ScheduleItemsAddRequestTO.builder()
							.items(List.of(
									new ScheduleItemToSaveTO(BigDecimal.valueOf(32), LocalDateTime.of(2021, Month.NOVEMBER, 20, 19, 30)),
									new ScheduleItemToSaveTO(BigDecimal.valueOf(15), LocalDateTime.of(2021, Month.NOVEMBER, 22, 12, 0)))
							)
							.build()
			);
			ResponseEntity<List<ScheduleItemDTO>> response = restTemplate.exchange(
					"/internal-api/schedule/" + movie.getId() + "/item",
					HttpMethod.POST,
					request,
					new ParameterizedTypeReference<>() {
					}
			);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			List<ScheduleItemDTO> body = response.getBody();
			assertEquals(2, body.size());
			assertEquals(movie.getId(), body.get(0).getMovieId());
			assertEquals(BigDecimal.valueOf(32), body.get(0).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 20, 19, 30), body.get(0).getShowTime());
			assertNotNull(body.get(0).getId());
			assertEquals(movie.getId(), body.get(1).getMovieId());
			assertEquals(BigDecimal.valueOf(15), body.get(1).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 22, 12, 0), body.get(1).getShowTime());
			assertNotNull(body.get(1).getId());
		}

		// list schedule items again and verify if addition was successful
		ScheduleItemDTO scheduleItem;
		{
			ResponseEntity<List<ScheduleItemDTO>> response = listScheduleItems(movie);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			List<ScheduleItemDTO> body = response.getBody();
			assertEquals(2, body.size());
			assertEquals(movie.getId(), body.get(0).getMovieId());
			assertEquals(BigDecimal.valueOf(32), body.get(0).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 20, 19, 30), body.get(0).getShowTime());
			assertNotNull(body.get(0).getId());
			scheduleItem = body.get(1);
			assertEquals(movie.getId(), scheduleItem.getMovieId());
			assertEquals(BigDecimal.valueOf(15), scheduleItem.getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 22, 12, 0), scheduleItem.getShowTime());
			assertNotNull(scheduleItem.getId());
		}

		// update one of the items
		{
			HttpEntity<ScheduleItemsUpdateRequestTO> request = new HttpEntity<>(
					ScheduleItemsUpdateRequestTO.builder()
							.item(new ScheduleItemToSaveTO(BigDecimal.valueOf(16.99), LocalDateTime.of(2021, Month.DECEMBER, 1, 17, 45)))
							.build()
			);
			ResponseEntity<ScheduleItemDTO> response = restTemplate.exchange(
					"/internal-api/schedule/" + movie.getId() + "/item/" + scheduleItem.getId(),
					HttpMethod.PUT,
					request,
					new ParameterizedTypeReference<>() {}
			);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			ScheduleItemDTO body = response.getBody();
			assertEquals(movie.getId(), body.getMovieId());
			assertEquals(BigDecimal.valueOf(16.99), body.getPrice());
			assertEquals(LocalDateTime.of(2021, Month.DECEMBER, 1, 17, 45), body.getShowTime());
			assertNotNull(body.getId());
		}

		// list schedule items again and verify if update was successful
		{
			ResponseEntity<List<ScheduleItemDTO>> response = listScheduleItems(movie);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			List<ScheduleItemDTO> body = response.getBody();
			assertEquals(2, body.size());
			assertEquals(movie.getId(), body.get(0).getMovieId());
			assertEquals(BigDecimal.valueOf(32), body.get(0).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 20, 19, 30), body.get(0).getShowTime());
			assertNotNull(body.get(0).getId());
			assertEquals(movie.getId(), body.get(1).getMovieId());
			assertEquals(BigDecimal.valueOf(16.99), body.get(1).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.DECEMBER, 1, 17, 45), body.get(1).getShowTime());
			assertNotNull(body.get(1).getId());
		}

		// delete one of the items
		restTemplate.delete("/internal-api/schedule/" + movie.getId() + "/item/" + scheduleItem.getId());

		// list schedule items again and verify if deletion was successful
		{
			ResponseEntity<List<ScheduleItemDTO>> response = listScheduleItems(movie);

			assertTrue(response.getStatusCode().is2xxSuccessful());
			assertTrue(response.hasBody());
			List<ScheduleItemDTO> body = response.getBody();
			assertEquals(1, body.size());
			assertEquals(movie.getId(), body.get(0).getMovieId());
			assertEquals(BigDecimal.valueOf(32), body.get(0).getPrice());
			assertEquals(LocalDateTime.of(2021, Month.NOVEMBER, 20, 19, 30), body.get(0).getShowTime());
			assertNotNull(body.get(0).getId());
		}
	}

	private ResponseEntity<List<ScheduleItemDTO>> listScheduleItems(MovieDTO movie) {
		return restTemplate.exchange(
				"/api/schedule/" + movie.getId() + "/item",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {}
		);
	}

	private void testAddingReviewRatingForMovie(MovieDTO movie) {
		HttpEntity<MovieAddRatingRequestTO> request = new HttpEntity<>(
				new MovieAddRatingRequestTO(
						MovieIdType.IMDB,
						new MovieRatingToAddTO(5, "user name", "Fantastic!"))
		);
		ResponseEntity<MovieRatingDTO> response = restTemplate.exchange(
				"/api/movie/" + movie.getImdbId() + "/rating",
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<>() {
				}
		);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		assertTrue(response.hasBody());
		MovieRatingDTO body = response.getBody();
		assertEquals(movie.getId(), body.getMovieId());
		assertEquals(5, body.getStars());
		assertEquals("user name", body.getUser());
		assertEquals("Fantastic!", body.getReview());
		assertNotNull(body.getId());
		assertNotNull(body.getCreateDate());
	}
}
