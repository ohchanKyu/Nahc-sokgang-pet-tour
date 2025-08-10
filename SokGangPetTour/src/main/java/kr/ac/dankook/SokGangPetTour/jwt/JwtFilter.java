package kr.ac.dankook.SokGangPetTour.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ac.dankook.SokGangPetTour.config.SecurityPaths;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtErrorResponseHandler jwtErrorResponseHandler;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (SecurityPaths.isOpen(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = resolveToken(request);
        if (!StringUtils.hasText(authToken)) {
            jwtErrorResponseHandler.sendErrorResponse(response, ErrorCode.UNAUTHORIZED);
            return;
        }
        try{
            Authentication authentication = jwtTokenProvider.validateToken(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JWTVerificationException e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
