package org.example.dao;

import org.example.datas.ReservationDetails;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDetailsDAO {

    public void addReservationDetails(ReservationDetails details) throws SQLException {
        String sql = "INSERT INTO reservation_details (nr_ftuarve, menu_price, decoration_price, music_price, offer_price, is_offer, total_amount, isPaid, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, details.getNrFtuarve());
            stmt.setInt(2, details.getMenuPrice());
            stmt.setInt(3, details.getDecorationPrice());
            stmt.setInt(4, details.getMusicPrice());
            stmt.setInt(5, details.getOfferPrice());
            stmt.setBoolean(6, details.isOffer());
            stmt.setDouble(7, details.calculateTotal());
            stmt.setBoolean(8, false);
            stmt.setInt(9, details.getUserId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    details.setId(generatedId);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
            System.out.println("Reservation details added successfully.");
        }
    }

    public List<ReservationDetails> getUnpaidReservationDetails(int userId) throws SQLException {
        List<ReservationDetails> unpaidDetailsList = new ArrayList<>();
        String sql = "SELECT * FROM reservation_details WHERE isPaid = false AND user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationDetails details = new ReservationDetails(
                            rs.getInt("nr_ftuarve"),
                            rs.getInt("menu_price"),
                            rs.getInt("decoration_price"),
                            rs.getInt("music_price"),
                            rs.getInt("offer_price"),
                            rs.getBoolean("is_offer"),
                            rs.getInt("user_id")
                    );
                    details.setId(rs.getInt("id"));
                    details.setPaid(rs.getBoolean("isPaid"));
                    details.setTotal_amount(rs.getDouble("total_amount"));
                    unpaidDetailsList.add(details);
                }
            }
        }
        return unpaidDetailsList;
    }

    public void markInvoiceAsPaid(int reservationId) throws SQLException {
        String sql = "UPDATE reservation_details SET isPaid = true WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
            System.out.println("Invoice marked as paid.");
        }
    }
}
