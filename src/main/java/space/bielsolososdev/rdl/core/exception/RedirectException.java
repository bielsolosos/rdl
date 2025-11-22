package space.bielsolososdev.rdl.core.exception;

public class RedirectException extends RuntimeException {
    
    private final String slug;
    
    public RedirectException(String message, String slug) {
        super(message);
        this.slug = slug;
    }
    
    public String getSlug() {
        return slug;
    }
}
