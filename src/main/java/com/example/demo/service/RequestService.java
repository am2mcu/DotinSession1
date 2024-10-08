package com.example.demo.service;

import com.example.demo.model.Request;
import com.example.demo.model.Reservation;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repo.RequestRepository;
import com.example.demo.repo.ReservationRepository;
import com.example.demo.repo.RoomRepository;
import com.example.demo.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Request> getRequests() {
        logger.info("Requests selected from DB");
        return requestRepository.findAll();
    }
    public void requestRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> {
            logger.error("Room does not exists!");
            throw new IllegalStateException("Room does not exists!");
        });
        User user = userRepository.findById(1L).orElseThrow(() -> {
            logger.error("User does not exists!");
            throw new IllegalStateException("User does not exists!");
        });

        if (room.getStatus().equals("Available")) {
            logger.info("Room requested");
            requestRepository.save(new Request(room, user));
        } else {
            logger.error("Selected room is occupied");
            throw new IllegalStateException("Room occupied");
        }
    }

    public void deleteRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            logger.error("Request does not exists!");
            throw new IllegalStateException("Request does not exists!");
        });

        logger.info("Request deleted");
        requestRepository.deleteById(requestId);
    }

    public void acceptRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            logger.error("Request does not exists!");
            throw new IllegalStateException("Request does not exists!");
        });

        Room room = request.getRoom();
        room.setStatus("Reserved");
        roomRepository.save(room);

        User user = request.getUser();
        reservationRepository.save(new Reservation(room, user));

        requestRepository.deleteById(requestId);
        logger.info("Request removed from database");
    }

    public void declineRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            logger.error("Request does not exists!");
            throw new IllegalStateException("Request does not exists!");
        });

        requestRepository.deleteById(requestId);
        logger.info("Request removed");
    }
}
