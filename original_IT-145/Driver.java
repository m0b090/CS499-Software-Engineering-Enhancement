import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    // ArrayList to store Dog objects
    private static ArrayList<Dog> dogList = new ArrayList<Dog>();
    // ArrayList to store Monkey objects
    private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize sample data for dogs and monkeys
        initializeDogList();
        initializeMonkeyList();

        // Main menu loop with input validation
        String userChoice;
        do {
            displayMenu();
            userChoice = scanner.nextLine().trim();

            // Process user input using switch-case
            switch (userChoice) {
                case "1":
                    intakeNewDog(scanner);
                    break;
                case "2":
                    intakeNewMonkey(scanner);
                    break;
                case "3":
                    reserveAnimal(scanner);
                    break;
                case "4":
                    printAnimals("dog");
                    break;
                case "5":
                    printAnimals("monkey");
                    break;
                case "6":
                    printAnimals("available");
                    break;
                case "q":
                case "Q":
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        while (!userChoice.equalsIgnoreCase("q"));

        scanner.close();
    }

    // Display the main menu options
    public static void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print a list of all dogs");
        System.out.println("[5] Print a list of all monkeys");
        System.out.println("[6] Print a list of all animals that are not reserved (available)");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection:");
    }

    // Initialize dog list with sample data for testing
    public static void initializeDogList() {
        Dog dog1 = new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States");
        Dog dog2 = new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "Phase I", false, "United States");
        Dog dog3 = new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", false, "Canada");

        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
    }

    // Initialize monkey list (currently empty for testing)
    public static void initializeMonkeyList() {
        // No predefined monkeys added for testing
    }

    // Intake a new dog by collecting user input and adding it to dogList
    public static void intakeNewDog(Scanner scanner) {
        System.out.println("What is the dog's name?");
        String name = scanner.nextLine().trim();
        // Check if the dog already exists
        for (Dog dog : dogList) {
            if (dog.getName().equalsIgnoreCase(name)) {
                System.out.println("This dog is already in our system.");
                return;
            }
        }
        System.out.println("Enter breed:");
        String breed = scanner.nextLine().trim();
        System.out.println("Enter gender:");
        String gender = scanner.nextLine().trim();
        System.out.println("Enter training status:");
        String trainingStatus = scanner.nextLine().trim();

        // Create and add the new Dog object to dogList
        Dog newDog = new Dog(name, breed, gender, "unknown", "unknown", "unknown", "unknown", trainingStatus, false, "unknown");
        dogList.add(newDog);
        System.out.println("Dog successfully added.");
    }

    // Intake a new monkey by collecting user input and adding it to monkeyList
    public static void intakeNewMonkey(Scanner scanner) {
        System.out.println("Enter monkey name:");
        String name = scanner.nextLine().trim();
        // Check if the monkey already exists
        for (Monkey monkey : monkeyList) {
            if (monkey.getName().equalsIgnoreCase(name)) {
                System.out.println("This monkey is already in our system.");
                return;
            }
        }
        System.out.println("Enter species (Capuchin, Guenon, Macaque, Marmoset, Squirrel monkey, Tamarin):");
        String species = scanner.nextLine().trim();
        // Validate species input
        if (!species.matches("(?i)Capuchin|Guenon|Macaque|Marmoset|Squirrel monkey|Tamarin")) {
            System.out.println("Invalid species. Monkey must be of an approved type.");
            return;
        }
        System.out.println("Enter gender:");
        String gender = scanner.nextLine().trim();
        System.out.println("Enter age:");
        String age = scanner.nextLine().trim();
        System.out.println("Enter weight:");
        String weight = scanner.nextLine().trim();
        System.out.println("Enter acquisition date:");
        String acquisitionDate = scanner.nextLine().trim();
        System.out.println("Enter acquisition country:");
        String acquisitionCountry = scanner.nextLine().trim();
        System.out.println("Enter training status:");
        String trainingStatus = scanner.nextLine().trim();
        System.out.println("Is the monkey reserved? (true/false):");
        boolean reserved = Boolean.parseBoolean(scanner.nextLine().trim());
        System.out.println("Enter in-service country:");
        String inServiceCountry = scanner.nextLine().trim();
        System.out.println("Enter tail length:");
        float tailLength = Float.parseFloat(scanner.nextLine().trim());
        System.out.println("Enter height:");
        float height = Float.parseFloat(scanner.nextLine().trim());
        System.out.println("Enter body length:");
        float bodyLength = Float.parseFloat(scanner.nextLine().trim());

        // Create and add the new Monkey object to monkeyList
        Monkey newMonkey = new Monkey(name, species, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry, tailLength, height, bodyLength);
        monkeyList.add(newMonkey);
        System.out.println("Monkey successfully added to the system.");
    }

    // Reserve an animal based on user input criteria (dog or monkey)
    public static void reserveAnimal(Scanner scanner) {
        System.out.println("Would you like to reserve a dog or a monkey?");
        String animalType = scanner.nextLine().trim().toLowerCase();

        // Validate animal type
        if (!animalType.equals("dog") && !animalType.equals("monkey")) {
            System.out.println("Invalid choice. Please enter 'dog' or 'monkey'.");
            return;
        }
        System.out.println("Enter the name of the animal:");
        String name = scanner.nextLine().trim();
        System.out.println("Enter the in-service country:");
        String country = scanner.nextLine().trim();

        // Process reservation for a dog
        if (animalType.equals("dog")) {
            for (Dog dog : dogList) {
                if (dog.getName().equalsIgnoreCase(name) && dog.getInServiceLocation().equalsIgnoreCase(country)) {
                    if (dog.getReserved()) {
                        System.out.println("This dog is already reserved.");
                        return;
                    }
                    if (!dog.getTrainingStatus().equalsIgnoreCase("in service")) {
                        System.out.println("This dog is not available for reservation.");
                        return;
                    }
                    dog.setReserved(true);
                    System.out.println(name + " has been successfully reserved.");
                    return;
                }
            }
        }
        else if (animalType.equals("monkey")) {
            // Process reservation for a monkey
            for (Monkey monkey : monkeyList) {
                if (monkey.getName().equalsIgnoreCase(name) && monkey.getInServiceLocation().equalsIgnoreCase(country)) {
                    if (monkey.getReserved()) {
                        System.out.println("This monkey is already reserved.");
                        return;
                    }
                    if (!monkey.getTrainingStatus().equalsIgnoreCase("in service")) {
                        System.out.println("This monkey is not available for reservation.");
                        return;
                    }
                    monkey.setReserved(true);
                    System.out.println(name + " has been successfully reserved.");
                    return;
                }
            }
        }
        System.out.println("Animal not found in the system.");
    }

    // Print animal details based on the specified list type ("dog", "monkey", or "available")
    public static void printAnimals(String listType) {
        if (listType.equalsIgnoreCase("dog")) {
            System.out.println("\nList of Dogs:");
            for (Dog dog : dogList) {
                System.out.println("Name: " + dog.getName() +
                                   ", Breed: " + dog.getAnimalType() +
                                   ", Gender: " + dog.getGender() +
                                   ", Age: " + dog.getAge() +
                                   ", Weight: " + dog.getWeight() +
                                   ", Training Status: " + dog.getTrainingStatus() +
                                   ", Acquisition Location: " + dog.getAcquisitionLocation() +
                                   ", Reserved: " + dog.getReserved());
            }
        }
        else if (listType.equalsIgnoreCase("monkey")) {
            System.out.println("\nList of Monkeys:");
            for (Monkey monkey : monkeyList) {
                System.out.println("Name: " + monkey.getName() +
                                   ", Species: " + monkey.getSpecies() +
                                   ", Gender: " + monkey.getGender() +
                                   ", Age: " + monkey.getAge() +
                                   ", Weight: " + monkey.getWeight() +
                                   ", Training Status: " + monkey.getTrainingStatus() +
                                   ", Acquisition Location: " + monkey.getAcquisitionLocation() +
                                   ", Reserved: " + monkey.getReserved());
            }
        }
        else if (listType.equalsIgnoreCase("available")) {
            System.out.println("\nList of Available (In-Service & Not Reserved) Animals:");
            // Print available dogs
            for (Dog dog : dogList) {
                if (dog.getTrainingStatus().equalsIgnoreCase("in service") && !dog.getReserved()) {
                    System.out.println("Name: " + dog.getName() +
                                       ", Training Status: " + dog.getTrainingStatus() +
                                       ", Acquisition Location: " + dog.getAcquisitionLocation() +
                                       ", Reserved: " + dog.getReserved());
                }
            }
            // Print available monkeys
            for (Monkey monkey : monkeyList) {
                if (monkey.getTrainingStatus().equalsIgnoreCase("in service") && !monkey.getReserved()) {
                    System.out.println("Name: " + monkey.getName() +
                                       ", Training Status: " + monkey.getTrainingStatus() +
                                       ", Acquisition Location: " + monkey.getAcquisitionLocation() +
                                       ", Reserved: " + monkey.getReserved());
                }
            }
        }
        else {
            System.out.println("Invalid list type.");
        }
    }
}