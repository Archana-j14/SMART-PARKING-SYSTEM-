import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ParkingSystem {
    private static Map<String, ParkingSpace> parkingSpaces = new HashMap<>();
    private static JFrame frame;
    private static JTextArea textArea;
    private static JTextField dateField;
    private static JTextField timeSlotField;
    private static JTextField vacateField;
    private static JComboBox<String> vehicleTypeBox;
    private static JComboBox<String> spaceSelectionBox;
    private static JButton reserveButton;
    private static JButton vacateButton;

    public static void main(String[] args) {
        initializeParkingSpaces();
        createUI();
    }

    // Parking space class to hold additional data
    static class ParkingSpace {
        boolean isAvailable;
        double price;
        String reservedFor;
        String vehicleType;

        ParkingSpace(boolean isAvailable, double price) {
            this.isAvailable = isAvailable;
            this.price = price;
            this.reservedFor = "";
            this.vehicleType = "";
        }
    }

    private static void initializeParkingSpaces() {
        for (int i = 1; i <= 10; i++) {
            parkingSpaces.put("Space " + i, new ParkingSpace(true, 50 + i * 5)); // Base prices vary slightly
        }
    }

    private static void createUI() {
        frame = new JFrame("Parking Space Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new FlowLayout());

        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));

        dateField = new JTextField(10);
        frame.add(new JLabel("Enter Date (yyyy-MM-dd):"));
        frame.add(dateField);

        timeSlotField = new JTextField(10);
        frame.add(new JLabel("Enter Time Slot (e.g., 10:00-11:00):"));
        frame.add(timeSlotField);

        String[] vehicleTypes = { "Car", "Two-Wheeler", "Lorry", "Bus", "Auto" };
        vehicleTypeBox = new JComboBox<>(vehicleTypes);
        frame.add(new JLabel("Select Vehicle Type:"));
        frame.add(vehicleTypeBox);

        spaceSelectionBox = new JComboBox<>(parkingSpaces.keySet().toArray(new String[0]));
        frame.add(new JLabel("Select Parking Space:"));
        frame.add(spaceSelectionBox);

        reserveButton = new JButton("Reserve Parking Space");
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reserveParkingSpace();
            }
        });
        frame.add(reserveButton);

        vacateField = new JTextField(10);
        frame.add(new JLabel("Enter Space to Vacate (e.g., Space 1):"));
        frame.add(vacateField);

        vacateButton = new JButton("Vacate Parking Space");
        vacateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vacateParkingSpace();
            }
        });
        frame.add(vacateButton);

        frame.setVisible(true);
        updateParkingStatus();
    }

    private static void reserveParkingSpace() {
        String date = dateField.getText().trim();
        String timeSlot = timeSlotField.getText().trim();
        String vehicleType = (String) vehicleTypeBox.getSelectedItem();
        String selectedSpace = (String) spaceSelectionBox.getSelectedItem();

        if (date.isEmpty() || timeSlot.isEmpty() || vehicleType.isEmpty() || selectedSpace.isEmpty()) {
            textArea.append("Please enter valid date, time slot, vehicle type, and select a parking space.\n");
            return;
        }

        ParkingSpace space = parkingSpaces.get(selectedSpace);
        if (!space.isAvailable) {
            textArea.append("Selected space is already occupied.\n");
            return;
        }

        double vehicleTypeMultiplier = getVehicleTypeMultiplier(vehicleType);
        double finalPrice = space.price * vehicleTypeMultiplier;

        space.isAvailable = false;
        space.reservedFor = date + " " + timeSlot;
        space.vehicleType = vehicleType;

        textArea.append("Reserved " + selectedSpace + " for " + date + " " + timeSlot +
                " for a " + vehicleType + " at $" + finalPrice + "\n");
        updateParkingStatus();
    }

    private static double getVehicleTypeMultiplier(String vehicleType) {
        switch (vehicleType) {
            case "Two-Wheeler":
                return 0.5;
            case "Lorry":
                return 2.0;
            case "Bus":
                return 2.5;
            case "Auto":
                return 0.75;
            case "Car":
            default:
                return 1.0;
        }
    }

    private static void vacateParkingSpace() {
        String spaceToVacate = vacateField.getText().trim();
        if (spaceToVacate.isEmpty() || !parkingSpaces.containsKey(spaceToVacate)) {
            textArea.append("Invalid parking space: " + spaceToVacate + "\n");
            return;
        }

        ParkingSpace space = parkingSpaces.get(spaceToVacate);
        if (!space.isAvailable) {
            space.isAvailable = true;
            space.reservedFor = "";
            space.vehicleType = "";
            textArea.append(spaceToVacate + " has been vacated.\n");
            updateParkingStatus();
        } else {
            textArea.append(spaceToVacate + " is already available.\n");
        }
    }

    private static void updateParkingStatus() {
        textArea.setText(""); // Clear the text area before updating
        textArea.append("Current Parking Status:\n");
        for (Map.Entry<String, ParkingSpace> entry : parkingSpaces.entrySet()) {
            textArea.append(entry.getKey() + ": " +
                    (entry.getValue().isAvailable ? "Available ($" + entry.getValue().price + ")" :
                            "Occupied (" + entry.getValue().reservedFor + " by " + entry.getValue().vehicleType + ")") + "\n");
        }
        textArea.append("\n");
    }
}
