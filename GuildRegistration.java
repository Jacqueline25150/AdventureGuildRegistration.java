import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class GuildRegistration {
    private ArrayList<GuildMember> roster = new ArrayList<>();
    private RegistrationStats stats = new RegistrationStats();
    private Scanner scanner = new Scanner(System.in);

    // Step 4: Non-static inner class for tracking attempt totals
    private class RegistrationStats {
        private int successfulAttempts = 0;
        private int rejectedAttempts = 0;

        public void recordSuccess() {
            successfulAttempts++;
        }

        public void recordRejection() {
            rejectedAttempts++;
        }

        public void displaySummary() {
            System.out.println("\n--- Registration Exit Summary ---");
            System.out.println("Successful attempts: " + successfulAttempts);
            System.out.println("Rejected attempts: " + rejectedAttempts);
        }
    }

    public static void main(String[] args) {
        GuildRegistration app = new GuildRegistration();
        app.runMenu();
    }

    // Step 1 & 2: Main menu loop with separate catch blocks and finally block
    public void runMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Guild Registration Desk ===");
            System.out.println("1. Register a member");
            System.out.println("2. Display the roster");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        registerMember();
                        stats.recordSuccess();
                    } catch (NumberFormatException e) {
                        stats.recordRejection();
                        System.out.println("Conversion error: Mission count must be a valid whole number.");
                    } catch (InvalidRegistrationException e) {
                        stats.recordRejection();
                        System.out.println("Validation error: " + e.getMessage());
                    } finally {
                        System.out.println("Registration attempt ended.");
                    }
                    break;

                case "2":
                    displayRoster();
                    break;

                case "3":
                    stats.displaySummary();
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    // Step 2 & 3: Reads input, parses mission count, validates, and asserts roster growth
    public void registerMember() throws InvalidRegistrationException {
        System.out.print("Enter adventurer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter rank (Scout, Ranger, Healer): ");
        String rank = scanner.nextLine().trim();

        System.out.print("Enter completed missions: ");
        String missionsText = scanner.nextLine().trim();

        // Throws NumberFormatException if input is not an integer (e.g., "five")
        int missions = Integer.parseInt(missionsText);

        // Throws InvalidRegistrationException if business rules fail
        validateMember(name, rank, missions);

        // Capture size before addition for assertion check
        int previousSize = roster.size();
        GuildMember newMember = new GuildMember(name.trim(), formatRank(rank), missions);
        roster.add(newMember);

        // Step 3: Assert that the roster grew by exactly one
        assert roster.size() == previousSize + 1 : "Roster size did not increase by exactly 1.";

        System.out.println("Successfully registered: " + newMember);
    }

    // Step 2: Validates member attributes and throws custom checked exception
    public void validateMember(String name, String rank, int missions) throws InvalidRegistrationException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRegistrationException("Name cannot be blank.");
        }

        if (!rank.equalsIgnoreCase("Scout") &&
            !rank.equalsIgnoreCase("Ranger") &&
            !rank.equalsIgnoreCase("Healer")) {
            throw new InvalidRegistrationException("Unsupported rank. Valid ranks are Scout, Ranger, and Healer.");
        }

        if (missions < 0) {
            throw new InvalidRegistrationException("Mission count cannot be negative.");
        }
    }

    private String formatRank(String rank) {
        return rank.substring(0, 1).toUpperCase() + rank.substring(1).toLowerCase();
    }

    // Step 1 & 4: Displays full roster and filters advanced members using Predicate lambda
    public void displayRoster() {
        System.out.println("\n--- Current Guild Roster ---");
        if (roster.isEmpty()) {
            System.out.println("No members registered yet.");
        } else {
            for (GuildMember member : roster) {
                System.out.println("- " + member);
            }
        }

        System.out.println("\n--- Members Eligible for Advanced Assignments (5+ Missions) ---");
        displayFilteredMembers(member -> member.getMissionsCompleted() >= 5);
    }

    // Step 4: Accepts a Predicate<GuildMember> and prints matching members
    public void displayFilteredMembers(Predicate<GuildMember> condition) {
        boolean found = false;
        for (GuildMember member : roster) {
            if (condition.test(member)) {
                System.out.println("- " + member);
                found = true;
            }
        }
        if (!found) {
            System.out.println("None.");
        }
    }
}
