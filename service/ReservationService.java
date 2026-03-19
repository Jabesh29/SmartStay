package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;
import model.Reservation;

public class ReservationService {

    // View Available Rooms
    public void viewRooms() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM rooms";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nRooms List:");

            while (rs.next()) {

                System.out.println(
                        "Room ID: " + rs.getInt("room_id") +
                        " | Type: " + rs.getString("room_type") +
                        " | Price: " + rs.getDouble("price") +
                        " | Status: " + rs.getString("status")
                );
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Book Room
    public void bookRoom(Reservation r) {

        try {

            Connection con = DBConnection.getConnection();

            // Check if room is available
            String checkQuery = "SELECT status FROM rooms WHERE room_id=?";
            PreparedStatement psCheck = con.prepareStatement(checkQuery);
            psCheck.setInt(1, r.getRoomId());
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {

                if (rs.getString("status").equalsIgnoreCase("Booked")) {
                    System.out.println("Room already booked!");
                    return;
                }
            }

            // Insert reservation
            String query = "INSERT INTO reservations(customer_name,room_id,check_in,check_out) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, r.getCustomerName());
            ps.setInt(2, r.getRoomId());
            ps.setString(3, r.getCheckIn());
            ps.setString(4, r.getCheckOut());

            ps.executeUpdate();

            // Update room status
            String updateRoom = "UPDATE rooms SET status='Booked' WHERE room_id=?";
            PreparedStatement ps2 = con.prepareStatement(updateRoom);
            ps2.setInt(1, r.getRoomId());
            ps2.executeUpdate();

            System.out.println("Room booked successfully!");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // View Reservations
    public void viewReservations() {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM reservations";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nReservations:");

            while (rs.next()) {

                System.out.println(
                        "ID: " + rs.getInt("reservation_id") +
                        " | Name: " + rs.getString("customer_name") +
                        " | Room: " + rs.getInt("room_id") +
                        " | CheckIn: " + rs.getString("check_in") +
                        " | CheckOut: " + rs.getString("check_out")
                );
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Cancel Reservation
    public void cancelReservation(int id) {

        try {

            Connection con = DBConnection.getConnection();

            // Get room id first
            String getRoom = "SELECT room_id FROM reservations WHERE reservation_id=?";
            PreparedStatement ps1 = con.prepareStatement(getRoom);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int roomId = rs.getInt("room_id");

                // Delete reservation
                String deleteQuery = "DELETE FROM reservations WHERE reservation_id=?";
                PreparedStatement ps2 = con.prepareStatement(deleteQuery);
                ps2.setInt(1, id);
                ps2.executeUpdate();

                // Update room status back to available
                String updateRoom = "UPDATE rooms SET status='Available' WHERE room_id=?";
                PreparedStatement ps3 = con.prepareStatement(updateRoom);
                ps3.setInt(1, roomId);
                ps3.executeUpdate();

                System.out.println("Reservation cancelled!");

            } else {
                System.out.println("Reservation not found!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
