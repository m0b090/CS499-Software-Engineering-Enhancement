package grazioso.model;

public class Monkey extends RescueAnimal {
    
    // Instance variables specific to Monkey
    private String species;
    private float tailLength;
    private float height;
    private float bodyLength;

    // Constructor for Monkey class
    public Monkey(String name, String species, String gender, String age,
                  String weight, String acquisitionDate, String acquisitionCountry,
                  String trainingStatus, boolean reserved, String inServiceCountry,
                  float tailLength, float height, float bodyLength) {
        // Setting attributes inherited from RescueAnimal
        setName(name);									
        setAnimalType("Monkey");                         // Set the animal type to "Monkey"
        setGender(gender);
        setAge(age);
        setWeight(weight);
        setAcquisitionDate(acquisitionDate);
        setAcquisitionLocation(acquisitionCountry);
        setTrainingStatus(trainingStatus);
        setReserved(reserved);
        setInServiceCountry(inServiceCountry);

        // Setting attributes specific to Monkey
        setSpecies(species);
        setTailLength(tailLength);
        setHeight(height);
        setBodyLength(bodyLength);
    }

    // Setter and getter methods for species
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    // Setter and getter methods for tail length
    public float getTailLength() {
        return tailLength;
    }

    public void setTailLength(float tailLength) {
        this.tailLength = tailLength;
    }

    // Setter and getter methods for height
    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    // Setter and getter methods for body length
    public float getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(float bodyLength) {
        this.bodyLength = bodyLength;
    }
}