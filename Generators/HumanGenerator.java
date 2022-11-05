package Generators;

import models.Human;
import models.Sex;

import java.util.Random;

public class HumanGenerator implements Generator<Human>{
    String[] manNames = new String[] {"Алексей", "Дмитрий", "Василий", "Михаил", "Иван", "Роман", "Артем", "Олег"};
    String[] womanNames = new String[] {"Наталья", "Мария", "Надежда", "Ольга", "Ирина", "Валерия", "Виктория"};
    String[] lastNames = new String[] {"Долговязов", "Варенников", "Галицын", "Меньшиков", "Романов", "Мышкин"};
    Integer[] ages = new Integer[40];
    Sex[] sexes = new Sex[] {Sex.male, Sex.female};

    public HumanGenerator() {
        for (int i = 0; i < ages.length; i++) {
            ages[i] = 20 + i;
        }
    }

    @Override
    public Human generate() {
        Sex sex = getRandomFrom(sexes);
        String firstName = "";
        String lastName = "";
        int age = getRandomFrom(ages);
        if (sex == Sex.male) {
            firstName = getRandomFrom(manNames);
            lastName = getRandomFrom(lastNames);
        }
        else if (sex == Sex.female) {
            firstName = getRandomFrom(womanNames);
            lastName = getRandomFrom(lastNames) + "а";
        }
        Human human = new Human(firstName, lastName, age, sex);
        return human;
    }

    private <T> T getRandomFrom(T[] array) {
        int index = this.rnd.nextInt(array.length);
        return array[index];
    }
}
