import java.io.BufferedReader;
import java.io.FileReader;

public class overspeedDetection {

    static final double SPEED_LIMIT = 20.0; // km/h

    // Calculate time (seconds)
    public static double calculateTime(int entryFrame, int exitFrame, int fps) {
        return (exitFrame - entryFrame) / (double) fps;
    }

    // Calculate speed (km/h)
    public static double calculateSpeed(double distance, double time) {
        return (distance / time) * 3.6;
    }

    // Check violation
    public static String checkViolation(double speed) {
        if (speed > SPEED_LIMIT) {
            return "Over Speed 🚨";
        } else {
            return "Normal ✅";
        }
    }

    public static void main(String[] args) {

        String file = "data.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            // Skip header
            br.readLine();

            System.out.println("Vehicle\tTime(s)\tSpeed(km/h)\tStatus");

            while ((line = br.readLine()) != null) {

                // Debug (optional)
                // System.out.println("Reading: " + line);

                String[] data = line.split(",");

                // Skip incomplete rows
                if (data.length < 5) {
                    System.out.println("Skipping invalid row: " + line);
                    continue;
                }

                try {
                    String vehicle = data[0].trim();
                    int entry = Integer.parseInt(data[1].trim());
                    int exit = Integer.parseInt(data[2].trim());
                    int fps = Integer.parseInt(data[3].trim());
                    double distance = Double.parseDouble(data[4].trim());

                    // Calculate time
                    double time = calculateTime(entry, exit, fps);

                    if (time <= 0) {
                        System.out.println(vehicle + "\tInvalid time data");
                        continue;
                    }

                    // Calculate speed
                    double speed = calculateSpeed(distance, time);

                    // Check violation
                    String status = checkViolation(speed);

                    // Print result
                    System.out.printf("%s\t%.2f\t%.2f\t%s\n",
                            vehicle, time, speed, status);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid row: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}