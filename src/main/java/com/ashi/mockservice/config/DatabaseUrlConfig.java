package com.ashi.mockservice.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

@Configuration
@ConditionalOnProperty(name = "DATABASE_URL")
public class DatabaseUrlConfig {

    @Bean
    public DataSource dataSource() {
        String raw = System.getenv("DATABASE_URL");
        ParsedDbUrl parsed = parseDatabaseUrl(raw);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl(parsed.jdbcUrl());

        if (parsed.username() != null && !parsed.username().isBlank()) {
            dataSource.setUsername(parsed.username());
        }
        if (parsed.password() != null && !parsed.password().isBlank()) {
            dataSource.setPassword(parsed.password());
        }

        return dataSource;
    }

    private ParsedDbUrl parseDatabaseUrl(String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalArgumentException("DATABASE_URL environment variable is required");
        }

        String withoutJdbc = databaseUrl.startsWith("jdbc:") ? databaseUrl.substring(5) : databaseUrl;
        if (withoutJdbc.startsWith("postgres://")) {
            withoutJdbc = "postgresql://" + withoutJdbc.substring("postgres://".length());
        }

        if (!withoutJdbc.startsWith("postgresql://")) {
            if (databaseUrl.startsWith("jdbc:postgresql://")) {
                return extractQueryCredentials(databaseUrl);
            }
            throw new IllegalArgumentException("Unsupported DATABASE_URL scheme. Use postgres://, postgresql://, or jdbc:postgresql://");
        }

        try {
            URI uri = new URI(withoutJdbc);
            String host = uri.getHost();
            int port = uri.getPort() == -1 ? 5432 : uri.getPort();
            String path = uri.getPath() == null ? "" : uri.getPath();
            String db = path.startsWith("/") ? path.substring(1) : path;

            String username = null;
            String password = null;
            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                int colonIndex = userInfo.indexOf(':');
                if (colonIndex >= 0) {
                    username = userInfo.substring(0, colonIndex);
                    password = userInfo.substring(colonIndex + 1);
                } else {
                    username = userInfo;
                }
            }

            Map<String, String> queryParams = parseQuery(uri.getQuery());
            if (queryParams.containsKey("user")) {
                username = queryParams.remove("user");
            }
            if (queryParams.containsKey("password")) {
                password = queryParams.remove("password");
            }

            StringBuilder jdbc = new StringBuilder("jdbc:postgresql://")
                    .append(host)
                    .append(":")
                    .append(port);
            if (!db.isBlank()) {
                jdbc.append("/").append(db);
            }
            String query = toQueryString(queryParams);
            if (!query.isBlank()) {
                jdbc.append("?").append(query);
            }

            return new ParsedDbUrl(jdbc.toString(), username, password);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid DATABASE_URL format", ex);
        }
    }

    private ParsedDbUrl extractQueryCredentials(String jdbcUrl) {
        int questionIndex = jdbcUrl.indexOf('?');
        if (questionIndex < 0) {
            return new ParsedDbUrl(jdbcUrl, null, null);
        }

        String base = jdbcUrl.substring(0, questionIndex);
        Map<String, String> queryParams = parseQuery(jdbcUrl.substring(questionIndex + 1));
        String username = queryParams.remove("user");
        String password = queryParams.remove("password");
        String query = toQueryString(queryParams);

        String normalized = query.isBlank() ? base : base + "?" + query;
        return new ParsedDbUrl(normalized, username, password);
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            if (pair.isBlank()) {
                continue;
            }
            int idx = pair.indexOf('=');
            if (idx < 0) {
                params.put(pair, "");
            } else {
                params.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }
        return params;
    }

    private String toQueryString(Map<String, String> queryParams) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                joiner.add(entry.getKey());
            } else {
                joiner.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        return joiner.toString();
    }

    private record ParsedDbUrl(String jdbcUrl, String username, String password) {
    }
}

