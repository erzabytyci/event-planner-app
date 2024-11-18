package org.example.dao;

import org.example.datas.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, first_name, last_name, contact_number, email, address) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getID());
            pstmt.setString(2, user.getEmri());
            pstmt.setString(3, user.getMbiemri());
            pstmt.setString(4, user.getNrTel());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getAdresa());
            pstmt.executeUpdate();
        }  catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                if (e.getMessage().contains("users_contact_number_key")) {
                    throw new SQLException("Contact number already registered.");
                } else if (e.getMessage().contains("users_email_key")) {
                    throw new SQLException("Email already registered.");
                } else if (e.getMessage().contains("users_pkey")) {
                    throw new SQLException("ID already registered.");
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setID(rs.getInt("user_id"));
                user.setEmri(rs.getString("first_name"));
                user.setMbiemri(rs.getString("last_name"));
                user.setNrTel(rs.getString("contact_number"));
                user.setEmail(rs.getString("email"));
                user.setAdresa(rs.getString("address"));
                return user;
            }
        }
        return null;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setEmri(rs.getString("first_name"));
                user.setMbiemri(rs.getString("last_name"));
                user.setNrTel(rs.getString("contact_number"));
                user.setEmail(rs.getString("email"));
                user.setAdresa(rs.getString("address"));
                user.setID(rs.getInt("user_id"));
                return user;
            }
        }
        return null;
    }
    public boolean isContactNumberRegistered(String contactNumber) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE contact_number = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contactNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean isEmailRegistered(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean isIdRegistered(int userId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
