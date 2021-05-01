package list_of_person_app;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonService {
    public static List<Person> loadPersonsFromFile(String fileRelativePath,
                                                   String separator) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileRelativePath));
        List<Person> persons = new ArrayList<>();
        for (String line :
                lines) {
            persons.add(Person.parse(line, separator));
        }
        return persons;
    }

    public static void savePersonsToFile(List<Person> persons, String fileRelativePath,
                                         String separator) throws IOException {
        List<String> lines = new ArrayList<>();
        Path filePath = Paths.get(fileRelativePath);
        for (Person p :
                persons) {
            lines.add(p.toString(separator));
        }
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        Files.write(filePath, lines, StandardOpenOption.CREATE_NEW);
    }

    private static void showMenu() {
        System.out.println("Chose an option:");
        System.out.println("1 -> List persons");
        System.out.println("2 -> Add person");
        System.out.println("3 -> Update person");
        System.out.println("4 -> Remove person");
        System.out.println("5 -> Exit & save");
        System.out.println("6 -> Exit without saving");
    }

    private static void listPersons(List<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            System.out.println((i + 1) + ": " + persons.get(i));
        }
    }

    private static Person readPersonData(boolean forUpdate) {
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName, dateOfBirthString, country, city, address;
        LocalDate dateOfBirth = null;
        System.out.print("First name: ");
        firstName = scanner.nextLine();
        System.out.print("Last name: ");
        lastName = scanner.nextLine();
        do {
            System.out.print("Date of birth (yyyy-mm-dd): ");
            dateOfBirthString = scanner.nextLine();
            if (forUpdate && dateOfBirthString.length() == 0) {
                // if readPersonData method is used in update context (forUpdate == true)
                // the an empty String for a date is acceptable because it means
                // dateOfBirth remains unchanged so we can get out of while loop
                break;
            }
            try {
                dateOfBirth = LocalDate.parse(dateOfBirthString);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date!");
                ;
            }
        } while (dateOfBirth == null);
        System.out.print("Country: ");
        country = scanner.nextLine();
        System.out.print("City: ");
        city = scanner.nextLine();
        System.out.print("Address: ");
        address = scanner.nextLine();
        return new Person(firstName, lastName, dateOfBirth, country, city, address);
    }

    private static int readLineNo(int noOfLines) {
        Scanner scanner = new Scanner(System.in);
        int lineNo = scanner.nextInt();
        if (lineNo > 0 && lineNo <= noOfLines) {
            return lineNo;
        }
        System.out.println("Invalid line.");
        return 0;
    }

    private static List<Person> addPerson(List<Person> persons) {
        persons.add(readPersonData(false));
        System.out.println("Person added succesfully!");
        return persons;
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }    /*
    Using reflection to copy fields that are not null and not empty from personWithUpdatedFields to personToUpdate.
     */

    private static Person updateFields(Person personToUpdate, Person personWithUpdatedFields) {        // getting the fields of the Person class
        Field[] fields = personToUpdate.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                // we are copying just the non-static fields/instance bound fields
                if (!Modifier.isStatic(field.getModifiers())) {
                    // getting the getter method for the current field
                    Method getter = Person.class.getMethod("get" + capitalize(field.getName()));
                    // getting the value of the private field using the getter
                    Object value = getter.invoke(personWithUpdatedFields);
                    if (value != null) {
                        // getting the setter method for the current field
                        Method setter = Person.class.getMethod("set" + capitalize(field.getName()), value.getClass());
                        if (value instanceof String) {
                            String s = (String) value;
                            if (s.length() > 0) {
                                // setting the value of a field using the setter
                                setter.invoke(personToUpdate, value);
                            }
                        } else {
                            setter.invoke(personToUpdate, value);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(personToUpdate);
        return personToUpdate;
    }

    private static List<Person> updatePerson(List<Person> persons) {
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName, dateOfBirthString, country, city, address;
        LocalDate dateOfBirth = null;
        System.out.print("Chose the person to update by entering the line number: ");
        int lineNo = readLineNo(persons.size());
        if (lineNo > 0) {
            Person personToUpdate = persons.get(lineNo - 1);
            System.out.println("Person to update: ");
            System.out.println(lineNo + ": " + personToUpdate);
            System.out.println("Input new info for the person. Press enter if the field value remains unchanged.");
            Person personWithUpdatedFields = readPersonData(true);
            updateFields(personToUpdate, personWithUpdatedFields);
            System.out.println("Person updated succesfully!");
        }
        return persons;
    }

    public static List<Person> removePerson(List<Person> persons) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Chose the person to remove by entering the line number: ");
        int lineNo = readLineNo(persons.size());
        if (lineNo > 0) {
            Person personToRemove = persons.get(lineNo - 1);
            System.out.println("Person to remove: ");
            System.out.println(lineNo + ": " + personToRemove);
            persons.remove(lineNo - 1);
            System.out.println("Person removed succesfully!");
        }
        return persons;
    }

    public static void runApplication(String fileRelativePath, String separator) {
        try {
            List<Person> persons = loadPersonsFromFile(fileRelativePath, separator);
            int option = 0;
            Scanner s = new Scanner(System.in);
            do {
                showMenu();
                option = s.nextInt();
                switch (option) {
                    case 1:
                        listPersons(persons);
                        break;
                    case 2:
                        addPerson(persons);
                        break;
                    case 3:
                        updatePerson(persons);
                        break;
                    case 4:
                        removePerson(persons);
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } while (option != 5 && option != 6);
            if (option == 5) {
                try {
                    savePersonsToFile(persons, fileRelativePath, separator);
                    System.out.println("Data saved succefully!");
                } catch (IOException e) {
                    System.out.println("Unable to save the data to the specified file: " + fileRelativePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read the specified file: " + fileRelativePath);
        }
    }
}
