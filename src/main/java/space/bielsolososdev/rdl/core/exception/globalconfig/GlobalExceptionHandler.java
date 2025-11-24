package space.bielsolososdev.rdl.core.exception.globalconfig;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletResponse;
import space.bielsolososdev.rdl.api.model.MessageResponse;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.core.exception.RedirectException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de negócio - retorna MessageResponse com 400
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageResponse> handleBusinessException(
            BusinessException ex, 
            WebRequest request) {
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }
    
    /**
     * Trata exceções de redirect - redireciona para página 404
     */
    @ExceptionHandler(RedirectException.class)
    public void handleRedirectException(
            RedirectException ex, 
            HttpServletResponse response) throws IOException {
        
        response.sendRedirect("/error/404?slug=" + ex.getSlug());
    }
    
    /**
     * Trata rotas não encontradas - retorna 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MessageResponse> handleNotFound(
            NoHandlerFoundException ex,
            WebRequest request) {
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse("Rota não encontrada: " + ex.getRequestURL()));
    }
    
    /**
     * Trata qualquer outra exceção não mapeada - retorna 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(
            Exception ex, 
            WebRequest request) {
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Ocorreu um erro inesperado no servidor"));
    }
}
