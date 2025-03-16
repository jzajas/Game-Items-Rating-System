package com.jzajas.RatingSystem.ExceptionsHandler;

import com.jzajas.RatingSystem.Exceptions.*;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//TODO split into few exception handlers?

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

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<String> handleUserEmailNotFoundException(UserEmailNotFoundException ex) {
        log.info("Caught UserEmailNotFoundException: ", ex);
        return new ResponseEntity<>(
                "Provided email: " + ex.getMessage() + " could not be found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidReceiverException.class)
    public ResponseEntity<String> handleInvalidReceiverException(InvalidReceiverException ex) {
        log.info("Caught InvalidReceiverException: ", ex);
        return new ResponseEntity<>(
                "Receiver with the id: " + ex.getMessage() + " is invalid",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        log.info("Caught EmailAlreadyInUseException: ", ex);
        return new ResponseEntity<>(
                "Provided email: " + ex.getMessage() + " is already in use",
                HttpStatus.CONFLICT
        );
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
                "Comment with the provided id: " + ex.getMessage() + " could not be found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(GameObjectNotFoundException.class)
    public ResponseEntity<String> handleGameObjectNotFoundException(GameObjectNotFoundException ex) {
        log.info("Caught GameObjectNotFoundException: ", ex);
        return new ResponseEntity<>(
                "Object with provided id: " + ex.getMessage() + " could not be found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        log.info("Caught BadRequestException: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.info("Caught MethodArgumentNotValidException: ", ex);
        return new ResponseEntity<>(
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("Method argument mismatch exception occurred: ", ex);
        return new ResponseEntity<> (
                "name: " + ex.getName() + "\nmessage: " + ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.info("Access denied exception occurred: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountNotApprovedException.class)
    public ResponseEntity<String> handleAccountNotApprovedException(AccountNotApprovedException ex) {
        log.info("Caught AccountNotApprovedException: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException ex) {
        log.info("Caught MessagingException: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUncaughtException(Exception ex) {
        log.info("Uncaught exception occurred: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
