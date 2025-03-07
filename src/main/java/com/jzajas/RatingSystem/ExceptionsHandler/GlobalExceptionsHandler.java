package com.jzajas.RatingSystem.ExceptionsHandler;

import com.jzajas.RatingSystem.Exceptions.CommentNotFoundException;
import com.jzajas.RatingSystem.Exceptions.EmailAlreadyInUseException;
import com.jzajas.RatingSystem.Exceptions.InvalidRatingValueException;
import com.jzajas.RatingSystem.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                "User with provided ID:" + ex.getMessage() + " could not be found",
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<String> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        return new ResponseEntity<>("Provided email is already in use", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRatingValueException.class)
    public ResponseEntity<String> handleInvalidRatingValueException(InvalidRatingValueException ex) {
        return new ResponseEntity<>("Value of rating not within bounds of 1-10", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> handleCommentNotFoundException(CommentNotFoundException ex) {
        return new ResponseEntity<>(
                "Comment with the provided id:"+ ex.getMessage() + " could not be found",
                HttpStatus.BAD_REQUEST
        );
    }
}
