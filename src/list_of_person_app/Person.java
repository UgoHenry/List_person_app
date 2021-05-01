package list_of_person_app;

import java.time.LocalDate;

public class Person implements Comparable<Person> {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String country;
    private String city;
    private String address;
    public static final String DEFAULT_SEPARATOR = ",";

    public Person(String firstName, String lastName, LocalDate dateOfBirth,
                  String country, String city, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.city = city;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return this.toString(Person.DEFAULT_SEPARATOR);
    }

    public String toString(String separator) {
        return firstName +
                separator + lastName +
                separator + dateOfBirth +
                separator + country +
                separator + city +
                separator + address;
    }

    /*
    2 persons are considered equal (or the same person)
    if they have the same firstName, lastName and dateOfBirth
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (!firstName.equals(person.firstName)) return false;
        if (!lastName.equals(person.lastName)) return false;
        return dateOfBirth.equals(person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + dateOfBirth.hashCode();
        return result;
    }

    public static Person parse(String str, String separator) {
        String[] fields = str.split(separator);
        String firstName = fields[0];
        String lastName = fields[1];
        LocalDate dateOfBirth = LocalDate.parse(fields[2]);
        String country = fields[3];
        String city = fields[4];
        String address = fields[5];
        return new Person(firstName, lastName, dateOfBirth, country, city, address);
    }

    @Override
    public int compareTo(Person p) {
        if (this.firstName.equals(p.firstName)) {
            if (this.lastName.equals(p.lastName)) {
                if (this.dateOfBirth.equals(p.dateOfBirth)) {
                    return 0;
                } else return this.dateOfBirth.compareTo(p.dateOfBirth);
            } else return this.lastName.compareTo(p.lastName);
        } else return this.firstName.compareTo(p.firstName);
    }
}
