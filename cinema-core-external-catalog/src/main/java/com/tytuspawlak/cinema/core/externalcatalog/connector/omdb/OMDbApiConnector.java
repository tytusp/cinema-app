package com.tytuspawlak.cinema.core.externalcatalog.connector.omdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tytuspawlak.cinema.core.dto.movie.MovieDetailsDTO;
import com.tytuspawlak.cinema.core.externalcatalog.connector.MovieExternalCatalogConnector;
import com.tytuspawlak.cinema.core.externalcatalog.connector.omdb.dto.OMDbApiResponseTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OMDbApiConnector implements MovieExternalCatalogConnector {
    private final OMDbApiResponseConverter responseConverter;

    @Value("${cinema.externalcatalog.omdb.apiKey}")
    private String apiKey;
    @Value("${cinema.externalcatalog.omdb.apiUrl}")
    private String apiUrl;

    public Optional<MovieDetailsDTO> findMovieDetails(String id) {
        String url = apiUrl + "/?apikey=" + apiKey + "&i=" + id;

        try {
            ResponseDeserializer deserializer = new ResponseDeserializer();

            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .header("Accept", "application/json")
                    .timeout(Duration.of(5, ChronoUnit.SECONDS))
                    .build();

            OMDbApiResponseTO response = HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(deserializer::deserialize)
                    .get();

            MovieDetailsDTO movieDetails = responseConverter.convert(response);

            return Optional.of(movieDetails);
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            throw new CompletionException(e); // TODO error handling should be improved in order to provide better feedback on what went wrong to the API user
        }
    }

    private static final class ResponseDeserializer {
        // TODO the object mapper should be instantiated as a configurable application bean as a performance improvement - same types mapping that uses reflection would be only done once then
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // do not fail on unknown properties in case the API creators extend the endpoint with some new ones

        OMDbApiResponseTO deserialize(String json) {
            try {
                return objectMapper.readValue(json, OMDbApiResponseTO.class);
            } catch (JsonProcessingException e) {
                throw new CompletionException(e);
            }
        }
    }
}
