package space.bielsolososdev.rdl.core.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedirectLoggingInterceptor implements HandlerInterceptor {

    @SuppressWarnings("null")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) throws Exception {
        
        int status = response.getStatus();
        
        // Verifica se é um redirect (3xx)
        if (status >= 300 && status < 400) {
            String location = response.getHeader("Location");
            String uri = request.getRequestURI();
            String clientIp = getClientIp(request);
            
            String redirectType = getRedirectType(status);
            
            log.info("🔀 REDIRECT [{}] {} | De: {} | Para: {} | IP: {}", 
                    status,
                    redirectType,
                    uri,
                    location != null ? location : "N/A",
                    clientIp);
            
            // Log adicional para redirects permanentes (importantes para SEO)
            if (status == 301 || status == 308) {
                log.warn("REDIRECT PERMANENTE detectado: {} -> {}", uri, location);
            }
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    private String getRedirectType(int status) {
        return switch (status) {
            case 301 -> "Moved Permanently";
            case 302 -> "Found (Temporary)";
            case 303 -> "See Other";
            case 304 -> "Not Modified";
            case 307 -> "Temporary Redirect";
            case 308 -> "Permanent Redirect";
            default -> "Unknown Redirect";
        };
    }
}
