package org.example.tezdrive.service;

import lombok.RequiredArgsConstructor;
import org.example.tezdrive.dto.comment.CommentRequest;
import org.example.tezdrive.dto.comment.CommentResponse;
import org.example.tezdrive.entity.BookingStatus;
import org.example.tezdrive.entity.DriverComment;
import org.example.tezdrive.entity.RideBooking;
import org.example.tezdrive.entity.Role;
import org.example.tezdrive.entity.User;
import org.example.tezdrive.exception.AccessDeniedException;
import org.example.tezdrive.repository.DriverCommentRepository;
import org.example.tezdrive.repository.RideBookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final DriverCommentRepository commentRepository;
    private final RideBookingRepository bookingRepository;

    @Transactional
    public CommentResponse addComment(User passenger, Long bookingId, CommentRequest request) {
        if (passenger.getRole() != Role.USER) {
            throw new AccessDeniedException("Only passengers can leave comments");
        }
        RideBooking booking = bookingRepository.findByIdAndPassengerId(bookingId, passenger.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.FINISHED) {
            throw new IllegalStateException("You can only comment on completed rides");
        }
        if (commentRepository.existsByBookingId(bookingId)) {
            throw new IllegalStateException("You have already commented on this ride");
        }

        DriverComment comment = DriverComment.builder()
                .driver(booking.getRide().getDriver())
                .passenger(passenger)
                .booking(booking)
                .text(request.getText())
                .build();

        return toResponse(commentRepository.save(comment));
    }

    public List<CommentResponse> getCommentsForDriver(Long driverId) {
        return commentRepository.findByDriverIdOrderByCreatedAtDesc(driverId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CommentResponse toResponse(DriverComment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .bookingId(comment.getBooking().getId())
                .passengerId(comment.getPassenger().getId())
                .passengerName(comment.getPassenger().getName())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
