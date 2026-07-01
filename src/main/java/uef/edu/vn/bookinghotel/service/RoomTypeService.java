package uef.edu.vn.bookinghotel.service;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.RoomType;

@Service
public class RoomTypeService extends BaseService {

    public RoomTypeService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public List<RoomType> roomTypes() {
        return jdbc.query("SELECT * FROM room_types ORDER BY id", roomTypeMapper);
    }

    public void saveRoomType(RoomType roomType) {
        if (roomType.getId() == 0) {
            jdbc.update(
                    "INSERT INTO room_types (name, description, price_per_night, capacity) VALUES (?, ?, ?, ?)",
                    roomType.getName(),
                    roomType.getDescription(),
                    roomType.getPricePerNight(),
                    roomType.getCapacity());
            return;
        }

        jdbc.update(
                "UPDATE room_types SET name=?, description=?, price_per_night=?, capacity=? WHERE id=?",
                roomType.getName(),
                roomType.getDescription(),
                roomType.getPricePerNight(),
                roomType.getCapacity(),
                roomType.getId());
    }

    public void deleteRoomType(int id) {
        jdbc.update(
                "DELETE FROM room_types WHERE id = ? AND NOT EXISTS (SELECT 1 FROM rooms WHERE room_type_id = ?)",
                id,
                id);
    }
}
