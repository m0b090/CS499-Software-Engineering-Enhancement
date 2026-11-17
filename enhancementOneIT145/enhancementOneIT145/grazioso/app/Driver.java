package grazioso.app;
import java.util.List;
import java.util.Scanner;

import grazioso.model.Dog;
import grazioso.model.Monkey;
import grazioso.model.RescueAnimal;
import grazioso.service.AnimalService;
import grazioso.service.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Driver {

    // Services manage animal records and reservations.
    private static final AnimalService animals = new AnimalService();
    private static final ReservationService reservations =
            new ReservationService(animals);

    public static void main(String[] args) {
        initializeSampleAnimals();

        // Keep the program running until the user quits.
        try (Scanner scanner = new Scanner(System.in)) {
            String choice;

            do {
                displayMenu();
                choice = scanner.nextLine().trim().toLowerCase();

                switch (choice) {
                    case "1":
                        intakeDog(scanner);
                        break;
                    case "2":
                        intakeMonkey(scanner);
                        break;
                    case "3":
                        reserveAnimal(scanner);
                        break;
                    case "4":
                        printAnimals(animals.getAnimals("Dog"));
                        returnToMenu(scanner);
                        break;
                    case "5":
                        printAnimals(animals.getAnimals("Monkey"));
                        returnToMenu(scanner);
                        break;
                    case "6":
                        printAnimals(animals.getAvailableAnimals());
                        returnToMenu(scanner);
                        break;
                    case "q":
                        System.out.println("Exiting application...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (!choice.equals("q"));
        }
    }

    // Display the main menu.
    private static void displayMenu() {
        System.out.println("\nRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print all dogs");
        System.out.println("[5] Print all monkeys");
        System.out.println("[6] Print available animals");
        System.out.println("[q] Quit application");
        System.out.print("Enter a menu selection: ");
    }

    // Wait for the user before returning to the main menu.
    private static void returnToMenu(Scanner scanner) {
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Add sample animals when the program starts.
    private static void initializeSampleAnimals() {
        animals.addAnimal(new Dog("Spot", "German Shepherd", "male",
                "1 year", "25.6", "05/12/2019", "United States",
                "intake", false, "United States"));

        animals.addAnimal(new Dog("Rex", "Great Dane", "male",
                "3 years", "35.2", "02/03/2020", "United States",
                "Phase I", false, "United States"));

        animals.addAnimal(new Dog("Bella", "Chihuahua", "female",
                "4 years", "25.6", "12/12/2019", "Canada",
                "in service", false, "Canada"));
    }

    // Collect and validate information for a new dog.
    private static void intakeDog(Scanner scanner) {
        System.out.println("\nNew dog intake:");

        String name = readName(scanner);
        String breed = readRequired(scanner, "Enter breed");
        String gender = readGender(scanner);
        String age = readAge(scanner);
        String weight = readWeight(scanner);
        String date = readDate(scanner);
        String country = readRequired(scanner, "Enter acquisition country");
        String status = readTrainingStatus(scanner);
        String serviceCountry =
                readRequired(scanner, "Enter in-service country");

        try {
            animals.addAnimal(new Dog(name, breed, gender, age,
                    weight, date, country, status, false,
                    serviceCountry));

            System.out.println("Dog successfully added.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Collect and validate information for a new monkey.
    private static void intakeMonkey(Scanner scanner) {
        System.out.println("\nNew monkey intake:");

        String name = readName(scanner);
        String species = readSpecies(scanner);
        String gender = readGender(scanner);
        String age = readAge(scanner);
        String weight = readWeight(scanner);
        String date = readDate(scanner);
        String country = readRequired(scanner, "Enter acquisition country");
        String status = readTrainingStatus(scanner);
        boolean reserved = readBoolean(scanner,
                "Already reserved (true/false)");
        String serviceCountry =
                readRequired(scanner, "Enter in-service country");

        // Monkey measurements are entered in inches.
        float tail = readNonNegativeFloat(scanner,
                "Enter tail length in inches (in)");
        float height = readNonNegativeFloat(scanner,
                "Enter height in inches (in)");
        float body = readNonNegativeFloat(scanner,
                "Enter body length in inches (in)");

        try {
            animals.addAnimal(new Monkey(name, species, gender,
                    age, weight, date, country, status, reserved,
                    serviceCountry, tail, height, body));

            System.out.println("Monkey successfully added.");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Show available animals and allow a numbered selection.
    private static void reserveAnimal(Scanner scanner) {
        List<RescueAnimal> available = animals.getAvailableAnimals();

        if (available.isEmpty()) {
            System.out.println(
                    "No animals are currently available for reservation.");
            return;
        }

        System.out.println("\nAvailable Animals for Reservation:");

        for (int i = 0; i < available.size(); i++) {
            RescueAnimal animal = available.get(i);

            System.out.println((i + 1) + ". "
                    + animal.getName()
                    + " - " + animal.getAnimalType()
                    + " - " + animal.getInServiceLocation());
        }

        // Reject invalid selections without ending the program.
        while (true) {
            System.out.print(
                    "Select an animal to reserve (0 to cancel): ");

            String input = scanner.nextLine().trim();

            try {
                int selection = Integer.parseInt(input);

                if (selection == 0) {
                    System.out.println("Reservation cancelled.");
                    return;
                }

                if (selection >= 1 && selection <= available.size()) {
                    RescueAnimal selected = available.get(selection - 1);

                    System.out.println(reservations.reserve(
                            selected.getAnimalType(),
                            selected.getName(),
                            selected.getInServiceLocation()));
                    return;
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    // Display animal records with readable ages and weights.
    private static void printAnimals(List<RescueAnimal> list) {
        if (list.isEmpty()) {
            System.out.println("No matching animals found.");
            return;
        }

        for (RescueAnimal animal : list) {
            String detail;

            if (animal instanceof Dog) {
                detail = "Breed: " + ((Dog) animal).getBreed();
            } else if (animal instanceof Monkey) {
                detail = "Species: " + ((Monkey) animal).getSpecies();
            } else {
                detail = "Animal information";
            }

            System.out.println("\nName: " + animal.getName());
            System.out.println("Type: " + animal.getAnimalType());
            System.out.println(detail);
            System.out.println("Gender: " + animal.getGender());

            // Age already includes months or years.
            System.out.println("Age: " + animal.getAge());

            System.out.println("Weight: "
                    + formatMeasurement(animal.getWeight(),
                            "lb", "lbs"));

            System.out.println("Training: "
                    + animal.getTrainingStatus());

            System.out.println("In-service country: "
                    + animal.getInServiceLocation());

            System.out.println("Reserved: " + animal.getReserved());
        }
    }

    // Add the correct singular or plural measurement unit.
    private static String formatMeasurement(
            String value, String singular, String plural) {

        try {
            double number = Double.parseDouble(value);

            if (number == 1.0) {
                return value + " " + singular;
            }

            return value + " " + plural;
        } catch (NumberFormatException ex) {
            // Return the original value if it is not numeric.
            return value;
        }
    }

    // Do not allow empty fields.
    private static String readRequired(Scanner scanner, String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Animal names must contain at least one letter.
    private static String readName(Scanner scanner) {
        while (true) {
            String name = readRequired(scanner, "Enter name");

            if (name.matches(".*\\p{L}.*")) {
                return name;
            }

            System.out.println(
                    "Name must contain at least one letter.");
        }
    }

    // Use a numbered menu to prevent invalid gender entries.
    private static String readGender(Scanner scanner) {
        while (true) {
            System.out.println("\nGender:");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.print("Select a gender: ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                return "male";
            }

            if (input.equals("2")) {
                return "female";
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    // Ask for age in months or years based on the animal's age.
    private static String readAge(Scanner scanner) {
        while (true) {
            System.out.println("\nIs the animal less than 1 year old?");
            System.out.println("1. Yes - Enter age in months");
            System.out.println("2. No - Enter age in years");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                return readAgeValue(scanner, "months", 0, 11);
            }

            if (choice.equals("2")) {
                return readAgeValue(scanner, "years", 1,
                        Integer.MAX_VALUE);
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    // Check the age range and add the correct unit.
    private static String readAgeValue(Scanner scanner,
            String unit, int minimum, int maximum) {

        while (true) {
            String input = readRequired(scanner,
                    "Enter age in " + unit);

            try {
                int age = Integer.parseInt(input);

                if (age >= minimum && age <= maximum) {
                    if (unit.equals("months")) {
                        return age + (age == 1 ? " month" : " months");
                    }

                    return age + (age == 1 ? " year" : " years");
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            if (unit.equals("months")) {
                System.out.println(
                        "Enter a whole number between 0 and 11 months.");
            } else {
                System.out.println(
                        "Enter a whole number of 1 year or greater.");
            }
        }
    }

    // Weight must be a positive number.
    private static String readWeight(Scanner scanner) {
        while (true) {
            String input = readRequired(
                    scanner, "Enter weight in pounds (lbs)");

            try {
                double weight = Double.parseDouble(input);

                if (Double.isFinite(weight) && weight > 0) {
                    return input;
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            System.out.println(
                    "Weight must be a number greater than zero.");
        }
    }

    // Check that the date is real and follows MM/DD/YYYY.
    private static String readDate(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("MM/dd/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        while (true) {
            String input = readRequired(
                    scanner, "Acquisition date (MM/DD/YYYY)");

            try {
                LocalDate date = LocalDate.parse(input, formatter);

                if (!date.isAfter(LocalDate.now())) {
                    return date.format(formatter);
                }

                System.out.println(
                        "Acquisition date cannot be in the future.");
            } catch (DateTimeParseException ex) {
                System.out.println(
                        "Enter a valid date in MM/DD/YYYY format.");
            }
        }
    }

    // Only accept true or false for reservation status.
    private static boolean readBoolean(Scanner scanner, String label) {
        while (true) {
            String value = readRequired(scanner, label);

            if (value.equalsIgnoreCase("true")) {
                return true;
            }

            if (value.equalsIgnoreCase("false")) {
                return false;
            }

            System.out.println("Enter true or false.");
        }
    }

    // Reject negative or invalid monkey measurements.
    private static float readNonNegativeFloat(
            Scanner scanner, String label) {

        while (true) {
            String value = readRequired(scanner, label);

            try {
                float number = Float.parseFloat(value);

                if (Float.isFinite(number) && number >= 0) {
                    return number;
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            System.out.println(
                    "Enter a valid number that is zero or greater.");
        }
    }

    // Only allow the six supported monkey species.
    private static String readSpecies(Scanner scanner) {
        String[] species = {
                "Capuchin", "Guenon", "Macaque",
                "Marmoset", "Squirrel monkey", "Tamarin"
        };

        while (true) {
            System.out.println("\nMonkey Species:");

            for (int i = 0; i < species.length; i++) {
                System.out.println((i + 1) + ". " + species[i]);
            }

            System.out.print("Select a species: ");
            String input = scanner.nextLine().trim();

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= species.length) {
                    return species[selection - 1];
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }

    // Display the five training stages.
    private static String readTrainingStatus(Scanner scanner) {
        String[] statuses = {
                "intake",
                "Phase I",
                "Phase II",
                "Phase III",
                "in service"
        };

        while (true) {
            System.out.println("\nTraining Status:");

            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i]);
            }

            System.out.print("Select a training status: ");
            String input = scanner.nextLine().trim();

            try {
                int selection = Integer.parseInt(input);

                if (selection >= 1 && selection <= statuses.length) {
                    return statuses[selection - 1];
                }
            } catch (NumberFormatException ex) {
                // Invalid input is handled below.
            }

            System.out.println("Invalid selection. Please try again.");
        }
    }
}
