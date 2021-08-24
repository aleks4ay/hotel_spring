package org.aleks4ay.hotel.service;

import org.aleks4ay.hotel.model.Category;
import org.aleks4ay.hotel.model.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(locations = "classpath:test_conf/test-context.xml")
@Sql(scripts = {"classpath:test_conf/populateRoom.sql"})
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @Test
    void testGetById() {
        Room result = roomService.getById(1L);
        assertNotNull(result);
    }

    @Test
    void save() {
        Room room = roomService.save(new Room(201, Category.SUITE, 2, "description 2", 1100,
                "201.jpg"));
        assertNotEquals(0L, room.getId());
    }

    @Test
    void findAll() {
        List<Room> actual = roomService.findAll(0, 5, "number").getContent();
        assertEquals(3, actual.size());
    }
}