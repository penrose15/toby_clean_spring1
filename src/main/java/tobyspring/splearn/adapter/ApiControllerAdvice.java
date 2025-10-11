package tobyspring.splearn.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tobyspring.splearn.domain.member.DuplicateEmailException;
import tobyspring.splearn.domain.member.DuplicateProfileException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {

        return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler({DuplicateEmailException.class, DuplicateProfileException.class})
    public ProblemDetail emailExceptionHandler(DuplicateEmailException ex) {
        return getProblemDetail(HttpStatus.CONFLICT, ex);
    }

    private static ProblemDetail getProblemDetail(HttpStatus code, Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(code, ex.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", ex.getClass().getSimpleName());

        return problemDetail;
    }
}
