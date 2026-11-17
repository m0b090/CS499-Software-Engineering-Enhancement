package grazioso.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import grazioso.model.RescueAnimal;

public class AnimalService {

    // Store all dogs and monkeys in one shared list.
    private final List<RescueAnimal> animals = new ArrayList<>();

    // Add a new animal after checking for missing information and duplicates.
    public void addAnimal(RescueAnimal animal) {
        if (animal == null || isBlank(animal.getName()) ||
                isBlank(animal.getAnimalType())) {
            throw new IllegalArgumentException(
                    "Animal name and type are required.");
        }

        if (findAnimal(animal.getAnimalType(), animal.getName()) != null) {
            throw new IllegalArgumentException(
                    "This animal is already in the system.");
        }

        animals.add(animal);
    }

    // Find an animal by its type and name.
    public RescueAnimal findAnimal(String type, String name) {
        if (isBlank(type) || isBlank(name)) {
            return null;
        }

        for (RescueAnimal animal : animals) {
            if (animal.getAnimalType().equalsIgnoreCase(type.trim()) &&
                    animal.getName().equalsIgnoreCase(name.trim())) {
                return animal;
            }
        }

        return null;
    }

    // Return animals matching the requested type.
    public List<RescueAnimal> getAnimals(String type) {
        List<RescueAnimal> results = new ArrayList<>();

        for (RescueAnimal animal : animals) {
            if (type == null ||
                    animal.getAnimalType().equalsIgnoreCase(type)) {
                results.add(animal);
            }
        }

        return Collections.unmodifiableList(results);
    }

    // Only return animals that are trained and not reserved.
    public List<RescueAnimal> getAvailableAnimals() {
        List<RescueAnimal> results = new ArrayList<>();

        for (RescueAnimal animal : animals) {
            if (ReservationService.isEligible(animal)) {
                results.add(animal);
            }
        }

        return Collections.unmodifiableList(results);
    }

    // Check for missing or empty text.
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
