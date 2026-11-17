package grazioso.service;
import grazioso.model.RescueAnimal;

public class ReservationService {

    private final AnimalService animalService;

    // Use AnimalService to find animals before reserving them.
    public ReservationService(AnimalService animalService) {
        if (animalService == null) {
            throw new IllegalArgumentException(
                    "Animal service is required.");
        }

        this.animalService = animalService;
    }

    // Animals must be in service and not already reserved.
    public static boolean isEligible(RescueAnimal animal) {
        return animal != null && !animal.getReserved()
                && "in service".equalsIgnoreCase(
                        animal.getTrainingStatus());
    }

    // Check the animal's information before updating its reservation.
    public String reserve(String type, String name, String country) {
        RescueAnimal animal = animalService.findAnimal(type, name);

        if (animal == null || country == null ||
                !country.trim().equalsIgnoreCase(
                        animal.getInServiceLocation())) {
            return "Animal not found in the system for that country.";
        }

        if (animal.getReserved()) {
            return "This animal is already reserved.";
        }

        if (!isEligible(animal)) {
            return "This animal is not available for reservation.";
        }

        // Mark the animal as reserved once all checks pass.
        animal.setReserved(true);

        return animal.getName() + " has been successfully reserved.";
    }
}
