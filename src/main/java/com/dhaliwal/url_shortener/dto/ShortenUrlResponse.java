package com.dhaliwal.url_shortener.dto;

public record ShortenUrlResponse(
        String originalUrl,
        String shortCode,
        String shortUrl
) {
}
