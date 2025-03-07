package com.jzajas.RatingSystem.ExceptionsHandler;

import com.jzajas.RatingSystem.Exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.info("Caught UserNotFoundException: ", ex);
        return new ResponseEntity<>(
                "User with provided ID:" + ex.getMessage() + " could not be found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        log.info("Caught EmailAlreadyInUseException: ", ex);
        return new ResponseEntity<>("Provided email is already in use", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRatingValueException.class)
    public ResponseEntity<String> handleInvalidRatingValueException(InvalidRatingValueException ex) {
        log.info("Caught InvalidRatingValueException: ", ex);
        return new ResponseEntity<>(
                "Value of rating: " + ex.getMessage() + " not within bounds of 1-10",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException ex) {
        log.info("Caught CommentNotFoundException: ", ex);
        return new ResponseEntity<>(
                "Comment with the provided id:"+ ex.getMessage() + " could not be found",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(GameObjectNotFoundException.class)
    public ResponseEntity<String> handleGameObjectNotFoundException(GameObjectNotFoundException ex) {
        log.info("Caught GameObjectNotFoundException: ", ex);
        return new ResponseEntity<>("Object with provided id does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        log.info("Caught BadRequestException: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUncaughtException(Exception e) {
        log.info("Uncaught exception occurred: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
