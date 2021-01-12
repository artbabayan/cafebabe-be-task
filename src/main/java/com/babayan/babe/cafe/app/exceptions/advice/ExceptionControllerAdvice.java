package com.babayan.babe.cafe.app.exceptions.advice;

import com.babayan.babe.cafe.app.exceptions.InputMismatchException;
import com.babayan.babe.cafe.app.exceptions.InternalServerErrorException;
import com.babayan.babe.cafe.app.exceptions.InvalidParameterException;
import com.babayan.babe.cafe.app.exceptions.ResourceAlreadyInUseException;
import com.babayan.babe.cafe.app.exceptions.UnauthorizedException;
import com.babayan.babe.cafe.app.exceptions.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author by artbabayan
 */
@Log4j2
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * @see InvalidParameterException
     *
     * <p>
     * HttpStatus.BAD_REQUEST(400, "Bad Request")
     */
    @ExceptionHandler(value = InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleInvalidParameterException(InvalidParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * @see UnauthorizedException
     *
     * <p>
     * HttpStatus.UNAUTHORIZED(401, "Unauthorized")
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleUnauthorizedExceptionException(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * @see NotFoundException
     *
     * <p>
     * HttpStatus.NOT_FOUND(404, "Not Found")
     */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * @see InputMismatchException
     *
     * <p>
     * HttpStatus.NOT_ACCEPTABLE(406, "Not Acceptable")
     */
    @ExceptionHandler(value = InputMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ResponseEntity<String> handleInputMismatchException(InputMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * @see ResourceAlreadyInUseException
     *
     * <p>
     * HttpStatus.CONFLICT(409, "Conflict")
     */
    @ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<String> handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * @see InternalServerErrorException
     *
     * <p>
     * HttpStatus.CONFLICT(500, "INTERNAL_SERVER_ERROR")
     */
    @ExceptionHandler(value = InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
