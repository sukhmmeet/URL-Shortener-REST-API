package com.dhaliwal.url_shortener.controller;

import com.dhaliwal.url_shortener.dto.ShortenUrlRequest;
import com.dhaliwal.url_shortener.dto.ShortenUrlResponse;
import com.dhaliwal.url_shortener.entity.UrlMapping;
import com.dhaliwal.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlShortenerService service;

    public UrlController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ShortenUrlResponse> shorten(
            @Valid @RequestBody ShortenUrlRequest request) {

        UrlMapping mapping = service.shortenUrl(request.url());

        return ResponseEntity.ok(
                new ShortenUrlResponse(
                        mapping.getOriginalUrl(),
                        mapping.getShortCode(),
                        "http://localhost:8080/" + mapping.getShortCode()
                )
        );
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        UrlMapping mapping = service.getByShortCode(shortCode);

        mapping.setClickCount(mapping.getClickCount() + 1);

        return ResponseEntity
                .status(302)
                .header("Location", mapping.getOriginalUrl())
                .build();
    }
}
