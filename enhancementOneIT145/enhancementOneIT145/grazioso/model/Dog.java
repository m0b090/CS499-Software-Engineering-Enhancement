package grazioso.model;


public class Dog extends RescueAnimal {

    // Breed is specific to dogs.
    private String breed;

    // Set the dog's information using inherited methods.
    public Dog(String name, String breed, String gender, String age,
            String weight, String acquisitionDate,
            String acquisitionCountry, String trainingStatus,
            boolean reserved, String inServiceCountry) {

        setName(name);
        setAnimalType("Dog");
        setBreed(breed);
        setGender(gender);
        setAge(age);
        setWeight(weight);
        setAcquisitionDate(acquisitionDate);
        setAcquisitionLocation(acquisitionCountry);
        setTrainingStatus(trainingStatus);
        setReserved(reserved);
        setInServiceCountry(inServiceCountry);
    }

    // Return the dog's breed.
    public String getBreed() {
        return breed;
    }

    // Update the dog's breed.
    public void setBreed(String dogBreed) {
        breed = dogBreed;
    }
}
