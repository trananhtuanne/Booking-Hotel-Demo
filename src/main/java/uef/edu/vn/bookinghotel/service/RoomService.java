package uef.edu.vn.bookinghotel.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.Room;

@Service
public class RoomService extends BaseService {

    public RoomService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public List<Room> rooms(String keyword, Integer typeId, String status) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, rt.name AS type_name, rt.price_per_night "
                + "FROM rooms r JOIN room_types rt ON rt.id = r.room_type_id WHERE 1=1");

        if (!blank(keyword)) {
            sql.append(" AND r.room_number LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (typeId != null) {
            sql.append(" AND r.room_type_id = ?");
            params.add(typeId);
        }
        if (!blank(status)) {
            sql.append(" AND r.status = ?");
            params.add(status);
        }

        sql.append(" ORDER BY r.id");
        return jdbc.query(sql.toString(), roomMapper, params.toArray());
    }

    public Room room(int id) {
        return first(jdbc.query(
                "SELECT r.*, rt.name AS type_name, rt.price_per_night "
                + "FROM rooms r JOIN room_types rt ON rt.id = r.room_type_id WHERE r.id = ?",
                roomMapper,
                id));
    }

    public void ensureRoomChangeHistoryTable() {
        jdbc.execute(
                "CREATE TABLE IF NOT EXISTS room_change_history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "room_id INT NOT NULL,"
                + "old_room_id INT,"
                + "old_room_type_id INT,"
                + "old_room_number VARCHAR(50),"
                + "old_status VARCHAR(50),"
                + "old_image_url VARCHAR(255),"
                + "old_description TEXT,"
                + "old_type_name VARCHAR(120),"
                + "old_price_per_night DECIMAL(12,2),"
                + "changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "INDEX idx_room_change_history_room_time (room_id, changed_at)"
                + ")");
        ensureColumn("room_change_history", "old_room_id", "INT");
    }

    public List<Map<String, Object>> roomChangeHistory() {
        ensureRoomChangeHistoryTable();
        return jdbc.queryForList(
                "SELECT h.*, r.room_number AS current_room_number, rt.name AS current_type_name "
                + "FROM room_change_history h "
                + "LEFT JOIN rooms r ON r.id = h.room_id "
                + "LEFT JOIN room_types rt ON rt.id = r.room_type_id "
                + "ORDER BY h.changed_at DESC, h.id DESC");
    }

    public List<Room> availableRooms(LocalDate in, LocalDate out, Integer typeId) {
        return availableRooms(in.atStartOfDay(), out.atStartOfDay(), typeId);
    }

    public List<Room> availableRooms(LocalDateTime in, LocalDateTime out, Integer typeId) {
        ensureBookingDateTimeColumns();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT r.*, rt.name AS type_name, rt.price_per_night "
                + "FROM rooms r JOIN room_types rt ON rt.id = r.room_type_id "
                + "WHERE 1=1");

        if (typeId != null) {
            sql.append(" AND r.room_type_id = ?");
            params.add(typeId);
        }

        sql.append(" AND NOT EXISTS (SELECT 1 FROM bookings b "
                + "WHERE b.room_id = r.id AND b.status IN ('BOOKED','CHECK_IN') "
                + "AND COALESCE(b.check_in_at, TIMESTAMP(b.check_in_date)) < ? "
                + "AND COALESCE(b.check_out_at, TIMESTAMP(b.check_out_date)) > ?) ORDER BY r.id");
        params.add(Timestamp.valueOf(out));
        params.add(Timestamp.valueOf(in));
        return jdbc.query(sql.toString(), roomMapper, params.toArray());
    }

    private void ensureBookingDateTimeColumns() {
        ensureColumn("bookings", "check_in_at", "DATETIME NULL");
        ensureColumn("bookings", "check_out_at", "DATETIME NULL");
        jdbc.update("UPDATE bookings SET check_in_at = TIMESTAMP(check_in_date) "
                + "WHERE check_in_at IS NULL AND check_in_date IS NOT NULL");
        jdbc.update("UPDATE bookings SET check_out_at = TIMESTAMP(check_out_date) "
                + "WHERE check_out_at IS NULL AND check_out_date IS NOT NULL");
    }

    public void saveRoom(Room room) {
        saveRoom(room.getId(), room);
    }

    public void saveRoom(int originalId, Room room) {
        ensureRoomChangeHistoryTable();
        String status = normalizeRoomStatus(room.getStatus());
        int targetId = room.getId() == 0 ? originalId : room.getId();
        Room existing = originalId == 0 ? null : room(originalId);
        String image = imageForSave(room, existing);

        if (existing == null) {
            if (targetId > 0) {
                jdbc.update(
                        "INSERT INTO rooms (id, room_type_id, room_number, status, image_url, description) "
                        + "VALUES (?, ?, ?, ?, ?, ?)",
                        targetId,
                        room.getRoomTypeId(),
                        room.getRoomNumber(),
                        status,
                        image,
                        room.getDescription());
                return;
            }
            jdbc.update(
                    "INSERT INTO rooms (room_type_id, room_number, status, image_url, description) VALUES (?, ?, ?, ?, ?)",
                    room.getRoomTypeId(),
                    room.getRoomNumber(),
                    status,
                    image,
                    room.getDescription());
            return;
        }

        if (targetId != originalId && room(targetId) != null) {
            throw new IllegalArgumentException("Room ID already exists: " + targetId);
        }

        recordRoomHistory(existing, targetId);
        if (targetId != originalId) {
            moveRoomId(originalId, targetId);
        }
        jdbc.update(
                "UPDATE rooms SET room_type_id=?, room_number=?, status=?, image_url=?, description=? WHERE id=?",
                room.getRoomTypeId(),
                room.getRoomNumber(),
                status,
                image,
                room.getDescription(),
                targetId);
    }

    public void deleteRoom(int id) {
        jdbc.update(
                "DELETE FROM rooms WHERE id = ? AND NOT EXISTS (SELECT 1 FROM bookings WHERE room_id = ? AND status <> 'CANCELLED')",
                id,
                id);
    }

    public void updateRoomStatus(int id, String status) {
        ensureRoomChangeHistoryTable();
        Room existing = room(id);
        if (existing != null) {
            recordRoomHistory(existing, id);
        }
        jdbc.update("UPDATE rooms SET status = ? WHERE id = ?", normalizeRoomStatus(status), id);
    }

    private void ensureColumn(String tableName, String columnName, String definition) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName);
        if (count == null || count == 0) {
            jdbc.execute("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + definition);
        }
    }

    private void moveRoomId(int originalId, int targetId) {
        jdbc.update("UPDATE rooms SET id = ? WHERE id = ?", targetId, originalId);
        jdbc.update("UPDATE bookings SET room_id = ? WHERE room_id = ?", targetId, originalId);
        jdbc.update("UPDATE maintenance_requests SET room_id = ? WHERE room_id = ?", targetId, originalId);
        jdbc.update("UPDATE room_change_history SET room_id = ? WHERE room_id = ?", targetId, originalId);
    }

    private String imageForSave(Room room, Room existing) {
        if (!blank(room.getImageUrl())) {
            return normalizeRoomImage(room.getImageUrl());
        }
        if (existing != null && !blank(existing.getImageUrl())) {
            return normalizeRoomImage(existing.getImageUrl());
        }
        return "/assets/img/rooms/1.png";
    }

    private String normalizeRoomImage(String imageUrl) {
        if (blank(imageUrl)) {
            return "/assets/img/rooms/1.png";
        }

        String image = imageUrl.trim().replace("\\", "/");
        if (image.startsWith("assets/img/rooms/")) {
            image = "/" + image;
        }
        if (image.startsWith("/assets/img/rooms/")) {
            return image;
        }
        return "/assets/img/rooms/1.png";
    }

    private void recordRoomHistory(Room room, int logicalRoomId) {
        jdbc.update(
                "INSERT INTO room_change_history "
                + "(room_id, old_room_id, old_room_type_id, old_room_number, old_status, old_image_url, old_description, "
                + "old_type_name, old_price_per_night, changed_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())",
                logicalRoomId,
                room.getId(),
                room.getRoomTypeId(),
                room.getRoomNumber(),
                room.getStatus(),
                room.getImageUrl(),
                room.getDescription(),
                room.getTypeName(),
                room.getPricePerNight());
    }

    private String normalizeRoomStatus(String status) {
        if ("BOOKED".equals(status) || "CHECK_IN".equals(status) || "CHECK_OUT".equals(status)) {
            return status;
        }
        return "AVAILABLE";
    }
}
