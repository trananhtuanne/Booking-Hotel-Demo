package uef.edu.vn.bookinghotel.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.MaintenanceRequest;
import uef.edu.vn.bookinghotel.model.Room;

@Service
public class MaintenanceRequestService extends BaseService {

    private final RoomService roomService;

    public MaintenanceRequestService(JdbcTemplate jdbc, RoomService roomService) {
        super(jdbc);
        this.roomService = roomService;
    }

    public List<MaintenanceRequest> maintenanceRequests() {
        return jdbc.query(
                "SELECT * FROM maintenance_requests ORDER BY id DESC",
                maintenanceRequestMapper);
    }

    public void saveMaintenance(MaintenanceRequest request) {
        Room room = roomService.room(request.getRoomId());
        String roomNumber = room == null ? null : room.getRoomNumber();

        if (request.getId() == 0) {
            jdbc.update(
                    "INSERT INTO maintenance_requests (room_id, room_number, title, description, status, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    request.getRoomId(),
                    roomNumber,
                    request.getTitle(),
                    request.getDescription(),
                    blank(request.getStatus()) ? "OPEN" : request.getStatus(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()));
            return;
        }

        jdbc.update(
                "UPDATE maintenance_requests SET room_id=?, room_number=?, title=?, description=?, status=?, updated_at=? WHERE id=?",
                request.getRoomId(),
                roomNumber,
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                Timestamp.valueOf(LocalDateTime.now()),
                request.getId());
    }

    public void updateMaintenanceStatus(int id, String status) {
        jdbc.update(
                "UPDATE maintenance_requests SET status = ?, updated_at = ? WHERE id = ?",
                status,
                Timestamp.valueOf(LocalDateTime.now()),
                id);
        if ("DONE".equals(status)) {
            jdbc.update(
                    "UPDATE rooms r JOIN maintenance_requests m ON m.room_id = r.id "
                    + "SET r.status = 'AVAILABLE' WHERE m.id = ? AND r.status = 'CHECK_OUT'",
                    id);
        }
    }
}
