package list_of_person_app;

public class Main {
    /*
    Develop an application that will allow you manage a file with information about a list of Persons
    a) load the persons from a text file (comma separated values)
    b) show a menu to the user with the following options:
        b.1) add a new person to the list
        b.2) update data from a person in the list
        b.3) remove a person from the list
        b.4) exit
    c) save the new list of persons into the same text file you loaded them from
    d) print the list of persons to the screen


     */

    public static void main(String[] args) {
        PersonService.runApplication("res/persons2.txt", ",");
    }

}
