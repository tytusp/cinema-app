package com.tytuspawlak.cinema.core.dto.movie;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class MovieSearchFilter {
    Set<String> ids;
    Set<String> imdbIds;
}
