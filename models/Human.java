package models;

import java.util.ArrayList;
import java.util.List;

public class Human {
    String firstName;
    String lastName;
    int age;
    Sex sex;
    List<Kinship> kinshipList;

    public Human(String firstName, String lastName, int age, Sex sex, List<Kinship> kinshipList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.kinshipList = kinshipList;
    }

    public Human(String firstName, String lastName, int age, Sex sex) {
        this(firstName, lastName, age, sex, new ArrayList<>());
    }

    @Override
    public String toString() {
        return  "firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", age: " + age +
                ", sex: " + sex;
    }


    // Getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getAge() {
        return age;
    }
    public List<Kinship> getKinshipList() {
        return kinshipList;
    }

    // Methods
    public void addKinship(KinshipType type, Human human) {
        this.kinshipList.add(new Kinship(type, human));
    }
}
