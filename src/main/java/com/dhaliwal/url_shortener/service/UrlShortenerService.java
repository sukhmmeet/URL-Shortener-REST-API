package com.dhaliwal.url_shortener.service;

import com.dhaliwal.url_shortener.entity.UrlMapping;
import com.dhaliwal.url_shortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UrlShortenerService {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final UrlMappingRepository repository;
    private final SecureRandom random = new SecureRandom();

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public UrlMapping shortenUrl(String originalUrl) {

        String shortCode;

        do {
            shortCode = generateCode();
        } while (repository.existsByShortCode(shortCode));

        UrlMapping mapping = UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .clickCount(0L)
                .build();

        return repository.save(mapping);
    }

    public UrlMapping getByShortCode(String shortCode) {

        return repository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found"));
    }

    private String generateCode() {

        StringBuilder code = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
