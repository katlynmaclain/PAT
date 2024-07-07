import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.*;

//entry point aplikasi
public class MainApplication {

    private static final String BASE_URL = "http://localhost:8000/api/tugas3/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainMenu();
        });
    }

    //menampilkan menu utama aplikasi
    //ketika button diklik akan memanggil openPage untuk membuka frame baru sesuai yang dipilih
    public static class MainMenu extends JFrame {

        public MainMenu() {
            setTitle("Main Menu");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JButton btnRooms = new JButton("Rooms");
            JButton btnBooking = new JButton("Booking");
            JButton btnAdministrator = new JButton("Administrator");

            btnRooms.addActionListener(e -> openPage(new Rooms()));
            btnBooking.addActionListener(e -> openPage(new Booking()));
            btnAdministrator.addActionListener(e -> openPage(new Administrator()));

            setLayout(new FlowLayout());
            add(btnRooms);
            add(btnBooking);
            add(btnAdministrator);

            setVisible(true);
        }

        private void openPage(JFrame frame) {
            setVisible(false);
            frame.setVisible(true);
        }
    }

    // menjadi acuan untuk interaksi dengan api pada server.js
    //untuk metode get, post, put, delete dengan HTTP requests
    public static abstract class ApiInteractionFrame extends JFrame {

        protected JPanel mainPanel;
        protected JButton btnExecute;
        protected JComboBox<String> comboBox;
        protected JSONObject params;

        public ApiInteractionFrame(String title) {
            setTitle(title);
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(0, 2));
            addComponents();

            btnExecute = new JButton("Execute");
            btnExecute.addActionListener(e -> executeAction());

            comboBox = new JComboBox<>(getComboBoxOptions());
            comboBox.addActionListener(e -> handleComboBoxSelection());

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(comboBox, BorderLayout.NORTH);
            getContentPane().add(mainPanel, BorderLayout.CENTER);
            getContentPane().add(btnExecute, BorderLayout.SOUTH);

            setVisible(true);
        }

        protected abstract void addComponents();

        protected abstract String[] getComboBoxOptions();

        protected abstract void handleComboBoxSelection();

        protected abstract void executeAction();

        protected void showMessage(String message, String title, int messageType) {
            JOptionPane.showMessageDialog(this, message, title, messageType);
        }

        protected void setFieldsVisible(JComponent[] fields, boolean[] visibilities) {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setVisible(visibilities[i]);
                fields[i].setEnabled(visibilities[i]);
            }
        }

        protected String executePost(String endpoint, JSONObject params) throws IOException {
            return executeRequest("POST", BASE_URL + endpoint, params);
        }

        protected String executePut(String endpoint, JSONObject params) throws IOException {
            return executeRequest("PUT", BASE_URL + endpoint, params);
        }

        protected String executeDelete(String endpoint) throws IOException {
            return executeRequest("DELETE", BASE_URL + endpoint, null);
        }

        protected String executeGet(String endpoint) throws IOException {
            return executeRequest("GET", BASE_URL + endpoint, null);
        }

        private String executeRequest(String method, String url, JSONObject params) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Username", "katlyn");
            conn.setRequestProperty("Password", "katlynmaclain");
            conn.setRequestProperty("Database-Name", "tugas3pat");
            conn.setDoOutput(true);

            if (params != null) {
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = params.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        }

        protected void addReturnButton() {
            JButton btnReturn = new JButton("Return to Main Menu");
            btnReturn.addActionListener(e -> {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); //menutup halaman yg terbuka saat itu
            });
            getContentPane().add(btnReturn, BorderLayout.WEST);
        }
    }

    //mengelola kamar
    public static class Rooms extends ApiInteractionFrame {

        private JTextField txtRoomID, txtRoomNumber, txtRoomType, txtPrice, txtFacilities;
        private JLabel lblRoomID, lblRoomNumber, lblRoomType, lblPrice, lblFacilities;
    
        public Rooms() {
            super("Rooms");
        }
    
        @Override
        protected void addComponents() {
            mainPanel.setLayout(new GridLayout(0, 2, 5, 5));
    
            // Inisialisasi label
            lblRoomID = new JLabel("Room ID:");
            lblRoomNumber = new JLabel("Room Number:");
            lblRoomType = new JLabel("Room Type:");
            lblPrice = new JLabel("Price:");
            lblFacilities = new JLabel("Facilities:");
    
            // Inisialisasi text fields
            txtRoomID = new JTextField();
            txtRoomNumber = new JTextField();
            txtRoomType = new JTextField();
            txtPrice = new JTextField();
            txtFacilities = new JTextField();
    
            // Tambahkan label dan field ke panel
            mainPanel.add(lblRoomID);
            mainPanel.add(txtRoomID);
            mainPanel.add(lblRoomNumber);
            mainPanel.add(txtRoomNumber);
            mainPanel.add(lblRoomType);
            mainPanel.add(txtRoomType);
            mainPanel.add(lblPrice);
            mainPanel.add(txtPrice);
            mainPanel.add(lblFacilities);
            mainPanel.add(txtFacilities);
            
            JButton btnReturn = new JButton("Return to Main Menu");
            btnReturn.addActionListener(e -> {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // menutup frame halaman yg terbuka
            });
            mainPanel.add(btnReturn); // nambahin tombol back to mainmenu ke panel
        }
    
        //menu dropdown
        @Override
        protected String[] getComboBoxOptions() {
            return new String[]{"Add New Room", "Update Room", "Delete Room", "Get All Rooms"};
        }
    
        @Override
        protected void handleComboBoxSelection() {
            String selectedOption = (String) comboBox.getSelectedItem();
            switch (selectedOption) {
                case "Add New Room":
                    setFieldsVisible(new JComponent[]{txtRoomID, txtRoomNumber, txtRoomType, txtPrice, txtFacilities},
                            new boolean[]{false, true, true, true, true});
                    break;
                case "Update Room":
                    setFieldsVisible(new JComponent[]{txtRoomID, txtRoomNumber, txtRoomType, txtPrice, txtFacilities},
                            new boolean[]{true, true, true, true, true});
                    break;
                case "Delete Room":
                    setFieldsVisible(new JComponent[]{txtRoomID, txtRoomNumber, txtRoomType, txtPrice, txtFacilities},
                            new boolean[]{true, false, false, false, false});
                    break;
                case "Get All Rooms":
                    setFieldsVisible(new JComponent[]{txtRoomID, txtRoomNumber, txtRoomType, txtPrice, txtFacilities},
                            new boolean[]{false, false, false, false, false});
                    break;
            }
    
            // membuat label menjadi visible atau tidak
            lblRoomID.setVisible(txtRoomID.isVisible());
            lblRoomNumber.setVisible(txtRoomNumber.isVisible());
            lblRoomType.setVisible(txtRoomType.isVisible());
            lblPrice.setVisible(txtPrice.isVisible());
            lblFacilities.setVisible(txtFacilities.isVisible());
        }
    
        @Override
        protected void executeAction() {
            String selectedOption = (String) comboBox.getSelectedItem();
            JSONObject params = new JSONObject();
            params.put("roomID", txtRoomID.getText());
            params.put("roomNumber", txtRoomNumber.getText());
            params.put("roomType", txtRoomType.getText());
            params.put("price", txtPrice.getText());
            params.put("facilities", txtFacilities.getText());
    
            try {
                String response = "";
                switch (selectedOption) {
                    case "Add New Room":
                        response = executePost("rooms", params);
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Update Room":
                        response = executePut("rooms/" + params.getString("roomID"), params);
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Delete Room":
                        response = executeDelete("rooms/" + params.getString("roomID"));
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Get All Rooms":
                        response = executeGet("rooms");
                        showRoomsTable(response);
                        break;
                }
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            } finally {
                // Setelah eksekusi, reset semua nilai text field
                resetFields();
            }
        }
    
        private void showRoomsTable(String jsonResponse) {
            try {
                // Debugging: Print JSON response
                //System.out.println("JSON Response: " + jsonResponse);
    
                // Attempt to parse the JSON response
                Object json = new JSONTokener(jsonResponse).nextValue();
    
                // Check if the parsed JSON is an object with key "response"
                if (json instanceof JSONObject) {
                    JSONObject responseObject = (JSONObject) json;
    
                    // Check if the response contains "response" array
                    if (responseObject.has("response")) {
                        JSONArray roomsArray = responseObject.getJSONArray("response");
    
                        // Check if the array is empty
                        if (roomsArray.isEmpty()) {
                            showMessage("No rooms found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
    
                        // Process each room in the array
                        String[] columnNames = {"Room ID", "Room Number", "Room Type", "Price", "Facilities"};
                        Object[][] data = new Object[roomsArray.length()][columnNames.length];
    
                        for (int i = 0; i < roomsArray.length(); i++) {
                            JSONObject room = roomsArray.getJSONObject(i);
    
                            // Check if the room object has all required fields
                            if (room.has("roomID") && room.has("roomNumber") && room.has("roomType") &&
                                    room.has("price") && room.has("facilities")) {
                                data[i][0] = room.optString("roomID", "N/A");
                                data[i][1] = room.optString("roomNumber", "N/A");
                                data[i][2] = room.optString("roomType", "N/A");
                                data[i][3] = room.optDouble("price", 0.0);   
                                data[i][4] = room.optString("facilities", "N/A");
                            } else {
                                showMessage("Incomplete data room found. Missing required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Stop processing further rooms
                            }
                        }
    
                        // Display the data in a table
                        JTable table = new JTable(data, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(600, 400));
                        JOptionPane.showMessageDialog(this, scrollPane, "All Rooms", JOptionPane.INFORMATION_MESSAGE);
    
                    } else {
                        showMessage("Unexpected data format: Missing 'response' array.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                } else {
                    showMessage("Unexpected data format: Not a JSON object.", "Error", JOptionPane.ERROR_MESSAGE);
                }
    
            } catch (JSONException e) {
                //e.printStackTrace();
                showMessage("Error parsing room data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void resetFields() {
            txtRoomID.setText("");
            txtRoomNumber.setText("");
            txtRoomType.setText("");
            txtPrice.setText("");
            txtFacilities.setText("");
        }
    }

    public static class Booking extends ApiInteractionFrame {

        private JTextField txtBookingID, txtUserID, txtCheckInDate, txtCheckOutDate, txtRoomCount, txtRoomType;
        private JComboBox<String> cmbStatus;
        private JLabel lblBookingID, lblUserID, lblCheckInDate, lblCheckOutDate, lblRoomCount, lblRoomType, lblStatus;
    
        public Booking() {
            super("Booking");
        }
    
        @Override
        protected void addComponents() {
            mainPanel.setLayout(new GridLayout(0, 2, 5, 5));
    
            lblBookingID = new JLabel("Booking ID:");
            lblUserID = new JLabel("User ID:");
            lblCheckInDate = new JLabel("Check In Date:");
            lblCheckOutDate = new JLabel("Check Out Date:");
            lblRoomCount = new JLabel("Room Count:");
            lblRoomType = new JLabel("Room Type:");
            lblStatus = new JLabel("Status:");
    
            txtBookingID = new JTextField();
            txtUserID = new JTextField();
            txtCheckInDate = new JTextField();
            txtCheckOutDate = new JTextField();
            txtRoomCount = new JTextField();
            txtRoomType = new JTextField();
    
            cmbStatus = new JComboBox<>(new String[]{"Confirmed", "Pending"});
    
            mainPanel.add(lblBookingID);
            mainPanel.add(txtBookingID);
            mainPanel.add(lblUserID);
            mainPanel.add(txtUserID);
            mainPanel.add(lblCheckInDate);
            mainPanel.add(txtCheckInDate);
            mainPanel.add(lblCheckOutDate);
            mainPanel.add(txtCheckOutDate);
            mainPanel.add(lblRoomCount);
            mainPanel.add(txtRoomCount);
            mainPanel.add(lblRoomType);
            mainPanel.add(txtRoomType);
            mainPanel.add(lblStatus);
            mainPanel.add(cmbStatus);
    
            JButton btnReturn = new JButton("Return to Main Menu");
            btnReturn.addActionListener(e -> {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // Menutup frame halaman saat ini
            });
            mainPanel.add(btnReturn); // Tambahkan tombol ke mainPanel
        }
    
        @Override
        protected String[] getComboBoxOptions() {
            return new String[]{"Update Booking", "Cancel Booking", "Get All Bookings"};
        }
    
        @Override
        protected void handleComboBoxSelection() {
            String selectedOption = (String) comboBox.getSelectedItem();
            switch (selectedOption) {
                case "Update Booking":
                    setFieldsVisible(new JComponent[]{txtBookingID, txtUserID, txtCheckInDate, txtCheckOutDate, txtRoomCount, txtRoomType, cmbStatus},
                            new boolean[]{true, false, false, false, false, false, true});
                    break;
                case "Cancel Booking":
                    setFieldsVisible(new JComponent[]{txtBookingID, txtUserID, txtCheckInDate, txtCheckOutDate, txtRoomCount, txtRoomType, cmbStatus},
                            new boolean[]{true, false, false, false, false, false, false});
                    break;
                case "Get All Bookings":
                    setFieldsVisible(new JComponent[]{txtBookingID, txtUserID, txtCheckInDate, txtCheckOutDate, txtRoomCount, txtRoomType, cmbStatus},
                            new boolean[]{false, false, false, false, false, false, false});
                    break;
            }
    
            // Hide labels for which corresponding text fields are not visible
            lblBookingID.setVisible(txtBookingID.isVisible());
            lblUserID.setVisible(txtUserID.isVisible());
            lblCheckInDate.setVisible(txtCheckInDate.isVisible());
            lblCheckOutDate.setVisible(txtCheckOutDate.isVisible());
            lblRoomCount.setVisible(txtRoomCount.isVisible());
            lblRoomType.setVisible(txtRoomType.isVisible());
            lblStatus.setVisible(cmbStatus.isVisible());
        }
    
        @Override
        protected void executeAction() {
            String selectedOption = (String) comboBox.getSelectedItem();
            JSONObject params = new JSONObject();
            params.put("bookingID", txtBookingID.getText());
            params.put("userID", txtUserID.getText());
            params.put("checkInDate", txtCheckInDate.getText());
            params.put("checkOutDate", txtCheckOutDate.getText());
            params.put("roomCount", txtRoomCount.getText());
            params.put("roomType", txtRoomType.getText());
            params.put("status", cmbStatus.getSelectedItem()); // Mengambil item terpilih dari JComboBox
    
            try {
                String response = "";
                switch (selectedOption) {
                    case "Make Booking":
                        response = executePost("bookings", params);
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Update Booking":
                        response = executePut("bookings/" + params.getString("bookingID"), params);
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Cancel Booking":
                        response = executeDelete("bookings/" + params.getString("bookingID"));
                        showMessage(response, "Response", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "Get All Bookings":
                        response = executeGet("bookings");
                        showBookingsTable(response); // Menampilkan data booking dalam tabel
                        break;
                }
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            } finally {
                // Setelah eksekusi, reset semua nilai text field
                resetFields();
            }
        }
    
        private void showBookingsTable(String jsonResponse) {
            try {
                // Debugging: Print JSON response
                //System.out.println("JSON Response: " + jsonResponse);
    
                // Attempt to parse the JSON response
                Object json = new JSONTokener(jsonResponse).nextValue();
    
                // Check if the parsed JSON is an object with key "response"
                if (json instanceof JSONObject) {
                    JSONObject responseObject = (JSONObject) json;
    
                    // Check if the response contains "response" array
                    if (responseObject.has("response")) {
                        JSONArray roomsArray = responseObject.getJSONArray("response");
    
                        // Check if the array is empty
                        if (roomsArray.isEmpty()) {
                            showMessage("No bookings found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
    
                        // Process each room in the array
                        String[] columnNames = {"Booking ID", "User ID", "Check In Date", "Check Out Date", "Room Count", "Room Type", "Status"};
                        Object[][] data = new Object[roomsArray.length()][columnNames.length];
    
                        for (int i = 0; i < roomsArray.length(); i++) {
                            JSONObject room = roomsArray.getJSONObject(i);
    
                            // Check if the room object has all required fields
                            if (room.has("bookingID") && room.has("userID") && room.has("checkInDate") &&
                                    room.has("checkOutDate") && room.has("roomCount") && room.has("roomType") && 
                                    room.has("status")) {
                                data[i][0] = room.optString("bookingID", "N/A");
                                data[i][1] = room.optString("userID", "N/A");
                                data[i][2] = room.optString("checkInDate", "N/A");
                                data[i][3] = room.optString("checkOutDate", "N/A");   
                                data[i][4] = room.optString("roomCount", "N/A");
                                data[i][5] = room.optString("roomType", "N/A");
                                data[i][6] = room.optString("status", "N/A");
                            } else {
                                showMessage("Incomplete booking data found. Missing required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                                return; // Stop processing further rooms
                            }
                        }
    
                        // Display the data in a table
                        JTable table = new JTable(data, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(600, 400));
                        JOptionPane.showMessageDialog(this, scrollPane, "All Booking Data", JOptionPane.INFORMATION_MESSAGE);
    
                    } else {
                        showMessage("Unexpected data format: Missing 'response' array.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
    
                } else {
                    showMessage("Unexpected data format: Not a JSON object.", "Error", JOptionPane.ERROR_MESSAGE);
                }
    
            } catch (JSONException e) {
                //e.printStackTrace();
                showMessage("Error parsing room data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void resetFields() {
            txtBookingID.setText("");
            txtUserID.setText("");
            txtCheckInDate.setText("");
            txtCheckOutDate.setText("");
            txtRoomCount.setText("");
            txtRoomType.setText("");
        }
    }
    
    

    public static class Administrator extends ApiInteractionFrame {

        private JTextField txtUserID, txtUsername;
        private JPasswordField txtPassword;
        private JLabel lblUserID, lblUsername, lblPassword;
    
        public Administrator() {
            super("Administrator");
        }
    
        @Override
        protected void addComponents() {
            mainPanel.setLayout(new GridLayout(0, 2, 5, 5));
    
            lblUserID = new JLabel("User ID:");
            lblUsername = new JLabel("Username:");
            lblPassword = new JLabel("Password:");
            
            txtUserID = new JTextField();
            txtUsername = new JTextField();
            txtPassword = new JPasswordField();
    
            mainPanel.add(lblUserID);
            mainPanel.add(txtUserID);
            mainPanel.add(lblUsername);
            mainPanel.add(txtUsername);
            mainPanel.add(lblPassword);
            mainPanel.add(txtPassword);
    
            JButton btnReturn = new JButton("Return to Main Menu");
            btnReturn.addActionListener(e -> {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose(); // Menutup frame halaman saat ini
            });
            mainPanel.add(btnReturn); // Tambahkan tombol ke mainPanel
        }
    
        @Override
        protected String[] getComboBoxOptions() {
            return new String[]{"Add Account", "Update Password", "Delete Account", "View All Accounts"};
        }
    
        @Override
        protected void handleComboBoxSelection() {
            String selectedOption = (String) comboBox.getSelectedItem();
            switch (selectedOption) {
                case "Add Account":
                    setFieldsVisible(new JComponent[]{txtUserID, txtUsername, txtPassword},
                            new boolean[]{false, true, true});
                    break;
                case "Update Password":
                    setFieldsVisible(new JComponent[]{txtUserID, txtUsername, txtPassword},
                            new boolean[]{true, true, true});
                    break;
                case "Delete Account":
                    setFieldsVisible(new JComponent[]{txtUserID, txtUsername, txtPassword},
                            new boolean[]{true, false, false});
                    break;
                case "View All Accounts":
                    setFieldsVisible(new JComponent[]{txtUserID, txtUsername, txtPassword},
                            new boolean[]{false, false, false});
                    viewAllAccounts(); // Automatically show all accounts when selected
                    break;
            }
    
            lblUserID.setVisible(txtUserID.isVisible());
            lblUsername.setVisible(txtUsername.isVisible());
            lblPassword.setVisible(txtPassword.isVisible());
        }
    
        @Override
        protected void executeAction() {
            String selectedOption = (String) comboBox.getSelectedItem();
            String userID = txtUserID.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
    
            switch (selectedOption) {
                case "Add Account":
                    addAccount(username, password);
                    break;
                case "Update Password":
                    updatePassword(userID, username, password);
                    break;
                case "Delete Account":
                    deleteAccount(userID);
                    break;
                case "View All Accounts":
                    // No action required here as viewAllAccounts is called in handleComboBoxSelection
                    break;
            }
            
            // Clear text fields after execution
            txtUserID.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
        }
    
        private void addAccount(String username, String password) {
            if (username.isEmpty() || password.isEmpty()) {
                showMessage("Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            JSONObject params = new JSONObject();
            params.put("username", username);
            params.put("password", password);
    
            try {
                String response = executePost("users/register", params);
                showMessage(response, "Add Account", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
            
            // Clear text fields after execution
            txtUserID.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
        }
    
        private void updatePassword(String userID, String username, String password) {
            if (userID.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showMessage("User ID, username, and new password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            JSONObject params = new JSONObject();
            params.put("userID", userID);
            params.put("username", username);
            params.put("password", password);
    
            try {
                String response = executePut("users/" + userID, params); // Assuming userID is the identifier for updating password
                showMessage("Password berhasil diubah.", "Update Password", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
            }
            
            // Clear text fields after execution
            txtUserID.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
        }
    
        private void deleteAccount(String userID) {
            if (userID.isEmpty()) {
                showMessage("User ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                String response = executeDelete("users/" + userID); // Assuming userID is the identifier for deleting account
                showMessage(response, "Delete Account", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            // Clear text fields after execution
            txtUserID.setText("");
            txtUsername.setText("");
            txtPassword.setText("");
        }
    
        private void viewAllAccounts() {
            try {
                String response = executeGet("users");
                showAccountsTable(response);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    
        private void showAccountsTable(String jsonResponse) {
            try {
                Object json = new JSONTokener(jsonResponse).nextValue();
    
                if (json instanceof JSONObject) {
                    JSONObject responseObject = (JSONObject) json;
    
                    if (responseObject.has("response")) {
                        JSONArray accountsArray = responseObject.getJSONArray("response");
    
                        if (accountsArray.isEmpty()) {
                            showMessage("No accounts found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
    
                        String[] columnNames = {"User ID", "Username", "Password"};
                        Object[][] data = new Object[accountsArray.length()][columnNames.length];
    
                        for (int i = 0; i < accountsArray.length(); i++) {
                            JSONObject account = accountsArray.getJSONObject(i);
    
                            if (account.has("userID") && account.has("username") && account.has("password")) {
                                data[i][0] = account.optInt("userID", 0);
                                data[i][1] = account.optString("username", "N/A");
                                data[i][2] = account.optString("password", "N/A");
                            } else {
                                showMessage("Incomplete account data found. Missing required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
    
                        JTable table = new JTable(data, columnNames);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(600, 400));
                        JOptionPane.showMessageDialog(this, scrollPane, "All Accounts", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showMessage("Unexpected data format: Missing 'response' array.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    showMessage("Unexpected data format: Not a JSON object.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (JSONException e) {
                showMessage("Error parsing account data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
}