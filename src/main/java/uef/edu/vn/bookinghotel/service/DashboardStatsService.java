package uef.edu.vn.bookinghotel.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import uef.edu.vn.bookinghotel.model.DashboardStats;

@Service
public class DashboardStatsService extends BaseService {

    public DashboardStatsService(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public DashboardStats stats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalRooms(valueInt("SELECT COUNT(*) FROM rooms"));
        stats.setAvailableRooms(valueInt("SELECT COUNT(*) FROM rooms WHERE status = 'AVAILABLE'"));
        stats.setActiveBookings(valueInt("SELECT COUNT(*) FROM bookings WHERE status IN ('BOOKED','CHECK_IN')"));

        BigDecimal revenue = jdbc.queryForObject(
                "SELECT COALESCE(SUM(amount), 0) FROM invoices WHERE payment_status = 'PAID'",
                BigDecimal.class);
        stats.setMonthlyRevenue(revenue == null ? BigDecimal.ZERO : revenue);
        return stats;
    }

    public List<Map<String, Object>> revenue() {
        List<Map<String, Object>> rows = jdbc.query(
                "SELECT DATE_FORMAT(issued_at, '%Y-%m') AS label, COALESCE(SUM(amount), 0) AS value "
                + "FROM invoices WHERE payment_status = 'PAID' "
                + "GROUP BY DATE_FORMAT(issued_at, '%Y-%m') ORDER BY label",
                (rs, rowNum) -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getBigDecimal("value"));
                    return row;
                });

        if (rows.isEmpty()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("label", "Current");
            row.put("value", BigDecimal.ZERO);
            return Collections.singletonList(row);
        }
        return rows;
    }

    public List<Map<String, Object>> occupancy() {
        List<Map<String, Object>> rows = new ArrayList<>();
        int total = valueInt("SELECT COUNT(*) FROM rooms");
        int used = valueInt("SELECT COUNT(*) FROM rooms WHERE status IN ('BOOKED','CHECK_IN')");

        Map<String, Object> rate = new LinkedHashMap<>();
        rate.put("label", "Occupancy Rate");
        rate.put("value", total == 0 ? "0.0%" : String.format("%.1f%%", used * 100.0 / total));
        rows.add(rate);

        rows.addAll(jdbc.query(
                "SELECT status AS label, COUNT(*) AS value FROM rooms GROUP BY status",
                (rs, rowNum) -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getLong("value"));
                    return row;
                }));
        return rows;
    }

    public List<Map<String, Object>> topCustomers() {
        return jdbc.query(
                "SELECT c.full_name AS label, COUNT(b.id) AS value "
                + "FROM customers c LEFT JOIN bookings b ON b.customer_id = c.id "
                + "GROUP BY c.id, c.full_name ORDER BY value DESC",
                (rs, rowNum) -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getLong("value"));
                    return row;
                });
    }

    public List<Map<String, Object>> bookingStatistics() {
        return jdbc.query(
                "SELECT status AS label, COUNT(*) AS value FROM bookings GROUP BY status",
                (rs, rowNum) -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("label", rs.getString("label"));
                    row.put("value", rs.getLong("value"));
                    return row;
                });
    }

    private int valueInt(String sql) {
        Integer value = jdbc.queryForObject(sql, Integer.class);
        return value == null ? 0 : value;
    }
}
