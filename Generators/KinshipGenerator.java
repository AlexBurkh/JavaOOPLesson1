package Generators;

import models.Human;
import models.Kinship;
import models.KinshipType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KinshipGenerator implements Generator<Kinship> {
    Map<String, List<Human>> namesakes;
    int ageOfSexualConsent = 16;

    public KinshipGenerator(List<Human> humans) {
        namesakes = filterForNamesakes(humans);
    }

    @Override
    public Kinship generate() {
        return null;
    }

    private Map<String, List<Human>> filterForNamesakes(List<Human> humans) {
        Map<String, List<Human>> namesakes = new HashMap<>();

        for (Human human : humans) {
            String ln = human.getLastName();
            if (namesakes.containsKey(ln)) {
                List<Human> ns = namesakes.get(ln);
                if (! ns.contains(human)) {
                    humans.add(human);
                }
            }
            else {
                namesakes.put(ln, new ArrayList<>());
            }
        }
        return namesakes;
    }
    private boolean defineKinship(Human h1, Human h2) {
        boolean[] res = new boolean[] {true, false};
        int index = rnd.nextInt(2);
        return res[index];
    }
    private KinshipType defineKinshipType(Human h1, Human h2) {
        if (defineKinship(h1, h2)) {

        }
        return KinshipType.none;
    }
}
