import Generators.Generator;
import Generators.HumanGenerator;
import models.Human;
import models.Sex;
import view.ConsoleUserInterface;
import view.UserInterface;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Human> humans;
    private static Generator<Human> generator;
    private static UserInterface ui;

    public static void main(String[] args) {
        Init();

        for (Human h : humans) {
            ui.print(h);
        }
    }

    private static void Init() {
        generator = new HumanGenerator();
        ui = new ConsoleUserInterface();
        initHumans(20);
    }

    private static void initHumans(int number) {
        humans = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            humans.add(generator.generate());
        }
    }

}
