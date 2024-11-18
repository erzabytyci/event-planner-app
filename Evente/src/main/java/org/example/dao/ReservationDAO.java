package org.example.dao;


import org.example.events.Rezervimi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public void addReservation(Rezervimi reservation, int userId) throws SQLException {
        String sql = "INSERT INTO reservations (reserver_name, event_name, event_duration, event_date, guest_count, user_id, reservation_date) VALUES (?, ?, ?, ?, ?,?,?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservation.getEmriRezervatorit());
            stmt.setString(2, reservation.getEmriEventit());
            stmt.setDouble(3, reservation.getKohezgjatjaEventit());
            stmt.setDate(4, java.sql.Date.valueOf(reservation.getDataEventit()));
            stmt.setInt(5, reservation.getNumriPresonave());
            stmt.setInt(6, userId);
            stmt.setDate(7, java.sql.Date.valueOf(reservation.getDataRezervimit()));

            stmt.executeUpdate();


            System.out.println("Rezervimi u shtua me sukses në bazën e të dhënave.");
        } catch (SQLException e) {
            System.err.println("Gabim gjatë shtimit të rezervimit: " + e.getMessage());
            throw e;
        }
    }

    public List<Rezervimi> getReservationsByUserId(int userId) throws SQLException {
        List<Rezervimi> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rezervimi reservation = new Rezervimi();
                    reservation.setId(rs.getInt("reservation_id"));
                    reservation.setEmriRezervatorit(rs.getString("reserver_name"));
                    reservation.setEmriEventit(rs.getString("event_name"));
                    reservation.setKohezgjatjaEventit(rs.getDouble("event_duration"));
                    reservation.setDataEventit(rs.getDate("event_date").toLocalDate());
                    reservation.setNumriPresonave(rs.getInt("guest_count"));
                    reservation.setDataRezervimit(rs.getDate("reservation_date").toLocalDate());

                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public void deleteReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.executeUpdate();
            System.out.println("Rezervimi u fshi me sukses nga baza e të dhënave.");
        }
    }
}
