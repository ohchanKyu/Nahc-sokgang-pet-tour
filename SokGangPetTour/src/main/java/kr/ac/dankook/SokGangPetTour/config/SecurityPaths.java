package kr.ac.dankook.SokGangPetTour.config;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public final class SecurityPaths {

    private SecurityPaths() {}

    public static final List<String> OPEN_PREFIXES = List.of(
            "/api/v1/auth",
            "/pub",
            "/sub",
            "/ws"
    );

    public static boolean isOpen(HttpServletRequest request) {
        String path = request.getServletPath();
        return OPEN_PREFIXES.stream().anyMatch(path::startsWith);
    }
}
