package space.bielsolososdev.rdl.core.exception.globalconfig;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.core.exception.RedirectException;

@RestControllerAdvice
@Slf4j
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
         * Trata credenciais inválidas - retorna 401
         */
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<MessageResponse> handleBadCredentials(
                        BadCredentialsException ex,
                        WebRequest request) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new MessageResponse("Usuário ou senha incorretos"));
        }

        /**
         * Trata conta desabilitada - retorna 403
         */
        @ExceptionHandler(DisabledException.class)
        public ResponseEntity<MessageResponse> handleDisabled(
                        DisabledException ex,
                        WebRequest request) {

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(new MessageResponse("Conta desabilitada"));
        }

        /**
         * Trata conta bloqueada - retorna 403
         */
        @ExceptionHandler(LockedException.class)
        public ResponseEntity<MessageResponse> handleLocked(
                        LockedException ex,
                        WebRequest request) {

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(new MessageResponse("Conta bloqueada"));
        }

        /**
         * Trata outras exceções de autenticação - retorna 401
         */
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<MessageResponse> handleAuthenticationException(
                        AuthenticationException ex,
                        WebRequest request) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new MessageResponse("Falha na autenticação"));
        }

        /**
         * Trata argumentos inválidos (ex: refresh token inválido) - retorna 400
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<MessageResponse> handleIllegalArgument(
                        IllegalArgumentException ex,
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
         * Exception exclusiva para o caso de usuário sem permissão
         * 
         * @param ex
         * @param request
         * @return
         */
        @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public MessageResponse handleAuthorizationDenied(
                        org.springframework.security.authorization.AuthorizationDeniedException ex,
                        HttpServletRequest request) {

                log.warn("Acesso negado para {}: {}", request.getRequestURI(), ex.getMessage());
                return new MessageResponse("Acesso negado: você não tem permissão para acessar este recurso");
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

record MessageResponse(String message) {
}
