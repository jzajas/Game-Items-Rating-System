package com.jzajas.RatingSystem.Services;

import com.jzajas.RatingSystem.DTO.Input.CommentCreationDTO;
import com.jzajas.RatingSystem.Entities.Comment;
import com.jzajas.RatingSystem.Entities.Role;
import com.jzajas.RatingSystem.Entities.Status;
import com.jzajas.RatingSystem.Entities.User;
import com.jzajas.RatingSystem.Exceptions.BadRequestException;
import com.jzajas.RatingSystem.Exceptions.InvalidRatingValueException;
import com.jzajas.RatingSystem.Exceptions.InvalidReceiverException;
import com.jzajas.RatingSystem.Mappers.DTOMapper;
import com.jzajas.RatingSystem.Repositories.CommentRepository;
import com.jzajas.RatingSystem.Repositories.UserRepository;
import com.jzajas.RatingSystem.Services.Implementations.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl commentServiceImpl;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DTOMapper mapper;

    @Mock
    private Authentication mockAuthentication;

    @Test
    void InvalidRatingValueExceptionForTooBigRating() {
        Long id = 1L;
        Authentication authentication = null;

        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test messgae");
        dto.setRating(111);

        assertThrows(InvalidRatingValueException.class,
                () -> commentServiceImpl.createNewComment(dto, id, authentication)
        );
    }

    @Test
    void InvalidRatingValueExceptionForTooSmallRating() {
        Long id = 1L;
        Authentication authentication = null;

        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test messgae");
        dto.setRating(-1);

        assertThrows(InvalidRatingValueException.class,
                () -> commentServiceImpl.createNewComment(dto, id, authentication)
        );
    }

    @Test
    void UserNotFoundExceptionForInvalidReceiver() {
        Long id = 1L;
        Authentication authentication = null;

        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test messgae");
        dto.setRating(5);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvalidReceiverException.class,
                () -> commentServiceImpl.createNewComment(dto, id, authentication)
        );
    }

    @Test
    void mapperProperlyMapsCommentDTO() {
        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test message");
        dto.setRating(5);

        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());

        Mockito.when(mapper.convertFromCommentCreationDTONotAnonymous(dto)).thenReturn(comment);

        Comment mappedComment = mapper.convertFromCommentCreationDTONotAnonymous(dto);

        assertEquals(mappedComment.getMessage(), dto.getMessage());
        assertEquals(mappedComment.getRating(), dto.getRating());
    }

    @Test
    void exceptionThrownForAnonymousUserWithoutProperData() {
        Long id = 1L;
        Authentication authentication = null;

        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test messgae");
        dto.setRating(5);

        User receiver = new User();
        receiver.setStatus(Status.APPROVED);
        receiver.setRole(Role.SELLER);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(receiver));

        assertThrows(BadRequestException.class,
                () -> commentServiceImpl.createNewComment(dto, id, authentication)
        );
    }

    @Test
    void properlyCreatesSellersComment() {
        Long receiverId = 1L;
        String receiverEmail = "receiver@email.com";
        String authorEmail = "author@email.com";

        CommentCreationDTO dto = new CommentCreationDTO();
        dto.setMessage("test messgae");
        dto.setRating(5);

        Comment comment = new Comment();
        comment.setMessage(dto.getMessage());
        comment.setRating(dto.getRating());

        User receiver = new User();
        receiver.setStatus(Status.APPROVED);
        receiver.setRole(Role.SELLER);
        receiver.setEmail(receiverEmail);

        User author = new User();
        author.setId(2L);
        author.setEmail(authorEmail);
        author.setStatus(Status.APPROVED);


        Mockito.when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        Mockito.when(mapper.convertFromCommentCreationDTONotAnonymous(dto)).thenReturn(comment);
        Mockito.when(mockAuthentication.getName()).thenReturn(authorEmail);
        Mockito.when(userRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));

        commentServiceImpl.createNewComment(dto, receiverId, mockAuthentication);

        Mockito.verify(userRepository).findById(receiverId);
        Mockito.verify(userRepository).findByEmail(authorEmail);
        Mockito.verify(commentRepository).save(comment);
    }
}
