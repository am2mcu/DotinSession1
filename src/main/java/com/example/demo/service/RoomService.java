package com.example.demo.service;

import com.example.demo.model.Room;
import com.example.demo.repo.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getRooms() {
        logger.info("Rooms selected from DB");
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms() {
        logger.info("Available Rooms selected from DB");
        return roomRepository.getAvailableRooms();
    }

    public List<Room> getReservedRooms() {
        logger.info("Reserved Rooms selected from DB");
        return roomRepository.getReservedRooms();
    }

    public void addRoom(Room room) {
        Optional<Room> roomOptional = roomRepository.findRoomById(room.getRoomId());
        if (roomOptional.isPresent()) {
            logger.error("Room number exists!");
            throw new IllegalStateException("Room number exists!");
        }
        logger.info("Room added");
        roomRepository.save(room);
    }

    public void deleteRoom(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            logger.error("Room id " + roomId + " not found!");
            throw new IllegalStateException("Room id " + roomId + " not found!");
        }
        logger.info("Room Deleted");
        roomRepository.deleteById(roomId);
    }

    @Transactional
    public void updateRoom(Long roomId, int capacity) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> {
            logger.error("Room does not exists!");
            throw new IllegalStateException("Room does not exists!");
        });
        logger.info("Room updated");
        room.setCapacity(capacity);
    }
}
