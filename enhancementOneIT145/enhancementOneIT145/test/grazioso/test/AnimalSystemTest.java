package grazioso.test;

import org.junit.jupiter.api.Test;

import grazioso.app.Driver;
import grazioso.model.Dog;
import grazioso.model.Monkey;
import grazioso.model.RescueAnimal;
import grazioso.service.AnimalService;
import grazioso.service.ReservationService;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Scanner;

public class AnimalSystemTest {

    // Create a sample dog for testing.
    private Dog createDog(String name, String status) {
        return new Dog(name, "Labrador", "female", "2 years", "30",
                "01/01/2026", "United States", status,
                false, "United States");
    }

    // Create a sample monkey for testing.
    private Monkey createMonkey(String name, String status) {
        return new Monkey(name, "Capuchin", "male", "6 months", "15",
                "01/01/2026", "United States", status,
                false, "United States", 12.0f, 18.0f, 20.0f);
    }

    // Test private input methods with simulated console input.
    private Object testInput(String methodName, String input,
            Class<?>[] parameterTypes, Object... extraArguments)
            throws Exception {

        Method method = Driver.class.getDeclaredMethod(
                methodName, parameterTypes);
        method.setAccessible(true);

        try (Scanner scanner = new Scanner(input)) {
            Object[] arguments = new Object[extraArguments.length + 1];
            arguments[0] = scanner;

            for (int i = 0; i < extraArguments.length; i++) {
                arguments[i + 1] = extraArguments[i];
            }

            return method.invoke(null, arguments);
        }
    }

    // Test that a dog can be added and found.
    @Test
    public void testAddDog() {
        AnimalService service = new AnimalService();
        service.addAnimal(createDog("Luna", "in service"));

        RescueAnimal result = service.findAnimal("Dog", "Luna");

        assertNotNull(result);
        assertEquals("Luna", result.getName());
        assertEquals("Dog", result.getAnimalType());
    }

    // Test that a monkey can be added and found.
    @Test
    public void testAddMonkey() {
        AnimalService service = new AnimalService();
        service.addAnimal(createMonkey("George", "Phase I"));

        RescueAnimal result = service.findAnimal("Monkey", "George");

        assertNotNull(result);
        assertEquals("George", result.getName());
        assertEquals("Monkey", result.getAnimalType());
    }

    // Check that searches ignore capitalization.
    @Test
    public void testCaseInsensitiveSearch() {
        AnimalService service = new AnimalService();
        service.addAnimal(createDog("Luna", "in service"));

        assertNotNull(service.findAnimal("dog", "luna"));
        assertNotNull(service.findAnimal("DOG", "LUNA"));
    }

    // Make sure duplicate animal records are rejected.
    @Test
    public void testDuplicateAnimal() {
        AnimalService service = new AnimalService();
        service.addAnimal(createDog("Luna", "in service"));

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAnimal(createDog("luna", "Phase I"));
        });
    }

    // Make sure an animal cannot be added without a name.
    @Test
    public void testMissingAnimalName() {
        AnimalService service = new AnimalService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAnimal(createDog("   ", "in service"));
        });
    }

    // Make sure a null animal cannot be added.
    @Test
    public void testNullAnimal() {
        AnimalService service = new AnimalService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAnimal(null);
        });
    }

    // Check that an eligible animal can be reserved.
    @Test
    public void testSuccessfulReservation() {
        AnimalService service = new AnimalService();
        Dog dog = createDog("Luna", "in service");
        service.addAnimal(dog);

        ReservationService reservations =
                new ReservationService(service);

        String result = reservations.reserve(
                "Dog", "Luna", "United States");

        assertTrue(result.contains("successfully reserved"));
        assertTrue(dog.getReserved());
    }

    // Make sure an animal cannot be reserved twice.
    @Test
    public void testDuplicateReservation() {
        AnimalService service = new AnimalService();
        Dog dog = createDog("Luna", "in service");
        service.addAnimal(dog);

        ReservationService reservations =
                new ReservationService(service);

        reservations.reserve("Dog", "Luna", "United States");

        String result = reservations.reserve(
                "Dog", "Luna", "United States");

        assertTrue(result.contains("already reserved"));
    }

    // Make sure animals still in training cannot be reserved.
    @Test
    public void testUntrainedAnimalReservation() {
        AnimalService service = new AnimalService();
        Dog dog = createDog("Luna", "Phase II");
        service.addAnimal(dog);

        ReservationService reservations =
                new ReservationService(service);

        String result = reservations.reserve(
                "Dog", "Luna", "United States");

        assertTrue(result.contains("not available"));
        assertFalse(dog.getReserved());
    }

    // Reject reservations for the wrong country.
    @Test
    public void testWrongCountryReservation() {
        AnimalService service = new AnimalService();
        Dog dog = createDog("Luna", "in service");
        service.addAnimal(dog);

        ReservationService reservations =
                new ReservationService(service);

        String result = reservations.reserve(
                "Dog", "Luna", "Canada");

        assertTrue(result.contains("not found"));
        assertFalse(dog.getReserved());
    }

    // Only show trained animals that are not reserved.
    @Test
    public void testAvailableAnimals() {
        AnimalService service = new AnimalService();

        Dog luna = createDog("Luna", "in service");
        Dog max = createDog("Max", "Phase I");
        Monkey george = createMonkey("George", "in service");

        service.addAnimal(luna);
        service.addAnimal(max);
        service.addAnimal(george);

        assertEquals(2, service.getAvailableAnimals().size());

        luna.setReserved(true);

        assertEquals(1, service.getAvailableAnimals().size());
    }

    // Check that dogs and monkeys can be listed separately.
    @Test
    public void testAnimalTypeFiltering() {
        AnimalService service = new AnimalService();

        service.addAnimal(createDog("Luna", "in service"));
        service.addAnimal(createMonkey("George", "Phase I"));

        assertEquals(1, service.getAnimals("Dog").size());
        assertEquals(1, service.getAnimals("Monkey").size());
        assertEquals(2, service.getAnimals(null).size());
    }

    // Make sure a missing animal cannot be reserved.
    @Test
    public void testAnimalNotFound() {
        AnimalService service = new AnimalService();

        ReservationService reservations =
                new ReservationService(service);

        String result = reservations.reserve(
                "Dog", "Unknown", "United States");

        assertTrue(result.contains("not found"));
    }

    // Check that reservations require an animal service.
    @Test
    public void testMissingAnimalService() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ReservationService(null);
        });
    }

    // Reject invalid choices and accept an age in months.
    @Test
    public void testAgeInMonths() throws Exception {
        Object result = testInput("readAge",
                "9\n1\n6\n",
                new Class<?>[] {Scanner.class});

        assertEquals("6 months", result);
    }

    // Reject negative values and ages of 12 months or more.
    @Test
    public void testInvalidMonthAge() throws Exception {
        Object result = testInput("readAge",
                "1\n-1\n12\n3\n",
                new Class<?>[] {Scanner.class});

        assertEquals("3 months", result);
    }

    // Allow a newborn animal to be entered as zero months.
    @Test
    public void testNewbornAge() throws Exception {
        Object result = testInput("readAge",
                "1\n0\n",
                new Class<?>[] {Scanner.class});

        assertEquals("0 months", result);
    }

    // Reject invalid years and accept a valid age.
    @Test
    public void testAgeInYears() throws Exception {
        Object result = testInput("readAge",
                "2\n-5\nabc\n4\n",
                new Class<?>[] {Scanner.class});

        assertEquals("4 years", result);
    }

    // Check that one year uses the singular form.
    @Test
    public void testOneYearAge() throws Exception {
        Object result = testInput("readAge",
                "2\n1\n",
                new Class<?>[] {Scanner.class});

        assertEquals("1 year", result);
    }

    // Reject negative and nonnumeric weights.
    @Test
    public void testWeightValidation() throws Exception {
        Object result = testInput("readWeight",
                "-20\nabc\n0.5\n",
                new Class<?>[] {Scanner.class});

        assertEquals("0.5", result);
    }

    // Reject impossible dates using MM/DD/YYYY.
    @Test
    public void testDateValidation() throws Exception {
        Object result = testInput("readDate",
                "02/30/2026\n01/15/2020\n",
                new Class<?>[] {Scanner.class});

        assertEquals("01/15/2020", result);
    }

    // Reject names made entirely of numbers.
    @Test
    public void testNameValidation() throws Exception {
        Object result = testInput("readName",
                "123\nLuna\n",
                new Class<?>[] {Scanner.class});

        assertEquals("Luna", result);
    }

    // Reject invalid gender selections.
    @Test
    public void testGenderValidation() throws Exception {
        Object result = testInput("readGender",
                "9\n2\n",
                new Class<?>[] {Scanner.class});

        assertEquals("female", result);
    }

    // Reject invalid monkey species selections.
    @Test
    public void testSpeciesValidation() throws Exception {
        Object result = testInput("readSpecies",
                "10\n1\n",
                new Class<?>[] {Scanner.class});

        assertEquals("Capuchin", result);
    }

    // Reject invalid training status selections.
    @Test
    public void testTrainingStatusValidation() throws Exception {
        Object result = testInput("readTrainingStatus",
                "8\n5\n",
                new Class<?>[] {Scanner.class});

        assertEquals("in service", result);
    }

    // Reject negative monkey measurements.
    @Test
    public void testMonkeyMeasurementValidation() throws Exception {
        Object result = testInput("readNonNegativeFloat",
                "-10\n12.5\n",
                new Class<?>[] {Scanner.class, String.class},
                "Tail length");

        assertEquals(12.5f, (Float) result, 0.001f);
    }

    // Check that weight uses the correct singular or plural unit.
    @Test
    public void testWeightFormatting() throws Exception {
        Method method = Driver.class.getDeclaredMethod(
                "formatMeasurement",
                String.class, String.class, String.class);

        method.setAccessible(true);

        assertEquals("1 lb",
                method.invoke(null, "1", "lb", "lbs"));

        assertEquals("0.5 lbs",
                method.invoke(null, "0.5", "lb", "lbs"));

        assertEquals("4 lbs",
                method.invoke(null, "4", "lb", "lbs"));
    }
}
