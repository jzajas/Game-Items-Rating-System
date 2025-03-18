package com.jzajas.RatingSystem.Repositories;

import com.jzajas.RatingSystem.Entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryIntegrationTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnonymousUserDetailsRepository anonymousUserDetailsRepository;


    @Test
    void findAllPostedCommentsByUserIdReturnsCommentsForApprovedUser() {
        Comment comment = generateCommentWithStatus(Status.APPROVED);
        User receiver = userRepository.save(generateUserWithStatus("receiver@email.com"));
        User author = userRepository.save(generateUserWithStatus("author@email.com"));
        comment.setAuthor(author);
        comment.setReceiver(receiver);

        commentRepository.save(comment);
        List<Comment> foundComments = commentRepository.findAllPostedCommentsByUserId(author.getId());

        assertThat(foundComments).size().isEqualTo(1);
    }

    @Test
    void findAllPostedCommentsByUserIdReturnsNothingForUserWithoutComments() {
        User author = userRepository.save(generateUserWithStatus("author@email.com"));
        List<Comment> foundComments = commentRepository.findAllPostedCommentsByUserId(author.getId());

        assertThat(foundComments).size().isEqualTo(0);
    }


    @Test
    void findAllReceivedCommentsByUserIdReturnsCommentsForApprovedUser() {
        Comment comment = generateCommentWithStatus(Status.APPROVED);
        User receiver = userRepository.save(generateUserWithStatus("receiver@email.com"));
        User author = userRepository.save(generateUserWithStatus("author@email.com"));
        comment.setAuthor(author);
        comment.setReceiver(receiver);

        commentRepository.save(comment);
        List<Comment> foundComments = commentRepository.findAllReceivedCommentsByUserId(receiver.getId());

        assertThat(foundComments).size().isEqualTo(1);
    }

    @Test
    void findAllReceivedCommentsByUserIdReturnsNothingForUserWithoutReceivedComments() {
        User receiver = userRepository.save(generateUserWithStatus("receiver@email.com"));

        List<Comment> foundComments = commentRepository.findAllReceivedCommentsByUserId(receiver.getId());

        assertThat(foundComments).size().isEqualTo(0);
    }


    @Test
    void findAllCommentsWithPendingStatusReturnsAllPendingComments() {
        Comment comment = generateCommentWithStatus(Status.PENDING_ADMIN);
        User receiver = userRepository.save(generateUserWithStatus("receiver@email.com"));

        AnonymousUserDetails author = new AnonymousUserDetails();
        author.setFirstName("Anonymous");
        author.setLastName("User");
        AnonymousUserDetails savedAuthor = anonymousUserDetailsRepository.save(author);

        comment.setAnonymousUserDetails(savedAuthor);
        comment.setReceiver(receiver);

        commentRepository.save(comment);
        List<Comment> foundComments = commentRepository.findAllCommentsWithPendingStatus();

        assertThat(foundComments).size().isEqualTo(1);
    }

    @Test
    void findAllCommentsWithPendingStatusReturnsNoPendingComments() {
        List<Comment> foundComments = commentRepository.findAllCommentsWithPendingStatus();

        assertThat(foundComments).size().isEqualTo(0);
    }


    private User generateUserWithStatus(String email) {
        User user = new User();

        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setEmail(email);
        user.setStatus(Status.APPROVED);
        user.setRole(Role.SELLER);

        return user;
    }

    private Comment generateCommentWithStatus(Status status) {
        Comment comment = new Comment();

        comment.setMessage("test messgae");
        comment.setRating(7);
        comment.setStatus(status);

        return comment;
    }
}
