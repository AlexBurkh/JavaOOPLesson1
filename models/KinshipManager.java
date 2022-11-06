package models;

import java.util.ArrayList;
import java.util.List;

public class KinshipManager {
    boolean flag = false;
    int ageOfSexualConsent = 16;
    List<Human> humans;
    private KinshipType[][] kinshipMatrix;

    public KinshipManager(List<Human> humans) {
        this.humans = humans;
        this.kinshipMatrix = new KinshipType[humans.size()][humans.size()];
        flag = true;
    }

    public  KinshipManager() {
    }

    // Override
    @Override
    public String toString() {
        if (!flag) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kinshipMatrix.length; i++) {
            for (int j = 0; j < kinshipMatrix[0].length; j++) {
                System.out.print(kinshipMatrix[i][j] + " ");
            }
            System.out.println();
        }
        return sb.toString();
    }

    // Getters
    public KinshipType[][] getKinshipMatrix() {
        if (!flag) {
            return null;
        }
        return kinshipMatrix;
    }

    // Setters
    public void setHumans(List<Human> humans) {
        this.humans = humans;
        this.kinshipMatrix = new KinshipType[humans.size()][humans.size()];
        this.flag = true;
    }

    //Methods
    public void addKinship(Human who, Human forWhom, KinshipType type) {
        if (!flag) {
            return;
        }
        if (! checkKinshipCorretness(new Kinship(who, forWhom, type))) {
            return;
        }
        var kinship = new Kinship(who, forWhom, type);
        var reverted = getRevertedKinship(kinship);
        saveKinshipToMatrix(kinship);
        saveKinshipToMatrix(reverted);
        addAdditionalKinships(kinship);
    }

    private boolean checkKinshipCorretness(Kinship kinship) {
        Human who = kinship.getWho();
        Human forWhom = kinship.getForWhom();
        KinshipType type = kinship.getType();

        if (type == KinshipType.father || type == KinshipType.mother) {
            if (type == KinshipType.father && who.getSex() == Sex.male) {
                var f = findFather(forWhom);
                if (f != null) {
                    return false;
                }
                if (who.getAge() - forWhom.getAge() > ageOfSexualConsent + 1) {
                    return true;
                }
            }
            if (type == KinshipType.mother && who.getSex() == Sex.female) {
                var m = findMother(forWhom);
                if (m != null) {
                    return false;
                }
                if (who.getAge() - forWhom.getAge() > ageOfSexualConsent + 1) {
                    return true;
                }
            }
        }
        if (type == KinshipType.brother || type == KinshipType.sister) {
            if (type == KinshipType.brother && who.getSex() == Sex.male ||
                    type == KinshipType.sister && who.getSex() == Sex.female) {
                return true;
            }
        }
        if (type == KinshipType.husband || type == KinshipType.wife) {
            if (type == KinshipType.husband) {
                if (who.getSex() == Sex.male && forWhom.getSex() == Sex.female) {
                    return true;
                }
            }
            if (type == KinshipType.wife) {
                if (who.getSex() == Sex.female && forWhom.getSex() == Sex.male) {
                    return true;
                }
            }
        }
        if (type == KinshipType.son || type == KinshipType.daughter) {
            if (type == KinshipType.son) {
                if ((who.getSex() == Sex.male) && (forWhom.getAge() - who.getAge() > ageOfSexualConsent + 1)) {
                    return true;
                }
            }
            if (type == KinshipType.daughter) {
                if ((who.getSex() == Sex.female) && (forWhom.getAge() - who.getAge() > ageOfSexualConsent + 1)) {
                    return true;
                }
            }
        }
        return false;
    }
    private Kinship getRevertedKinship(Kinship kinship) {
        Human who = kinship.getForWhom();
        Human forWhom = kinship.getWho();
        KinshipType type = KinshipType.none;

        if (kinship.getType() == KinshipType.father || kinship.getType() == KinshipType.mother) {
            if (forWhom.getSex() == Sex.male) {
                type = KinshipType.son;
            }
            else {
                type = KinshipType.daughter;
            }
        } else if (kinship.getType() == KinshipType.brother || kinship.getType() == KinshipType.sister) {
            if (who.getSex() == Sex.male) {
                type = KinshipType.brother;
            }
            else {
                type = KinshipType.sister;
            }
        }
        else if (kinship.getType() == KinshipType.son || kinship.getType() == KinshipType.daughter) {
            if (forWhom.getSex() == Sex.male) {
                type = KinshipType.father;
            }
            else {
                type = KinshipType.mother;
            }
        } else if (kinship.getType() == KinshipType.husband || kinship.getType() == KinshipType.wife) {
            if (kinship.getType() == KinshipType.husband) {
                type = KinshipType.wife;
            }
            else {
                type = KinshipType.husband;
            }
        }
        return new Kinship(who, forWhom, type);
    }
    private void saveKinshipToMatrix(Kinship kinship) {
        int i = humans.indexOf(kinship.getWho());
        int j = humans.indexOf(kinship.getForWhom());
        kinshipMatrix[i][j] = kinship.getType();
    }
    private void addAdditionalKinships(Kinship kinship) {
        if (kinship.getType() == KinshipType.husband || kinship.getType() == KinshipType.wife) {
            List<Human> children = findChildren(kinship.getForWhom());
            if (kinship.getType() == KinshipType.husband) {
                if (children != null) {
                    for (Human child : children) {
                        checkAndAddKinship(new Kinship(kinship.getWho(), child, KinshipType.father));
                    }
                }
            } else {
                if (children != null) {
                    for (Human child : children) {
                        checkAndAddKinship(new Kinship(kinship.getWho(), child, KinshipType.mother));
                    }
                }
            }
        }
        else if (kinship.getType() == KinshipType.father || kinship.getType() == KinshipType.mother) {
            List<Human> brothersAndSisters = findBrothersAndSisters(kinship.getForWhom(), kinship.getWho());
            Human spouse = findMother(kinship.getForWhom());
            if (kinship.getType() == KinshipType.father) {
                checkAndAddKinship(new Kinship(kinship.getWho(), spouse, KinshipType.husband));
                assert brothersAndSisters != null;
                for (Human brothersAndSister : brothersAndSisters) {
                    checkAndAddKinship(new Kinship(kinship.getWho(), brothersAndSister, KinshipType.father));
                }
            } else {
                checkAndAddKinship(new Kinship(kinship.getWho(), spouse, KinshipType.wife));
                assert brothersAndSisters != null;
                for (Human brothersAndSister : brothersAndSisters) {
                    checkAndAddKinship(new Kinship(kinship.getWho(), brothersAndSister, KinshipType.mother));
                }
            }
        }
        else if (kinship.getType() == KinshipType.brother || kinship.getType() == KinshipType.sister) {
            List<Human> brothersAndSisters = findBrothersAndSisters(kinship.getForWhom(), kinship.getWho());
            Human mother = findMother(kinship.getForWhom());
            Human father = findFather(kinship.getForWhom());

            // Mother
            if (mother != null) {
                if (kinship.getWho().getSex() == Sex.male) {
                    checkAndAddKinship(new Kinship(kinship.getWho(), mother, KinshipType.son));
                }
                else {
                    checkAndAddKinship(new Kinship(kinship.getWho(), mother, KinshipType.daughter));
                }
            }

            // Father
            if (father != null) {
                if (kinship.getWho().getSex() == Sex.male) {
                    checkAndAddKinship(new Kinship(kinship.getWho(), father, KinshipType.son));
                }
                else {
                    checkAndAddKinship(new Kinship(kinship.getWho(), father, KinshipType.daughter));
                }
            }

            // Brothers and sisters
            if (brothersAndSisters != null && brothersAndSisters.size() > 0) {
                for (Human brothersAndSister : brothersAndSisters) {
                    if (kinship.getWho().getSex() == Sex.male) {
                        checkAndAddKinship(new Kinship(kinship.getWho(), brothersAndSister, KinshipType.brother));
                    } else {
                        checkAndAddKinship(new Kinship(kinship.getWho(), brothersAndSister, KinshipType.sister));
                    }
                }
            }
        }
    }

    private List<Human> findChildren(Human human) {
        List<Human> children = new ArrayList<>();
        int i = humans.indexOf(human);
        for (int j = 0; j < kinshipMatrix[i].length; j++) {
            if (kinshipMatrix[i][j] != null) {
                if ((kinshipMatrix[i][j] == KinshipType.son) || (kinshipMatrix[i][j] == KinshipType.daughter)) {
                    children.add(humans.get(j));
                }
            }
        }
        if (children.size() > 0) {
            return children;
        }
        return null;
    }
    private List<Human> findBrothersAndSisters(Human human, Human exception) {
        List<Human> bs = new ArrayList<>();
        int i = humans.indexOf(human);
        int e = humans.indexOf(exception);
        for (int j = 0; j < kinshipMatrix[i].length; j++) {
            if (kinshipMatrix[i][j] != null) {
                if ((kinshipMatrix[i][j] == KinshipType.brother) || (kinshipMatrix[i][j] == KinshipType.sister) && j != e) {
                    bs.add(humans.get(j));
                }
            }
        }
        if (bs.size() > 0) {
            return bs;
        }
        return null;
    }
    private Human findMother(Human human) {
        if (! (human.getSex() == Sex.female)) {
            return null;
        }
        List<Human> m = new ArrayList<>();
        int i = humans.indexOf(human);
        for (int j = 0; j < kinshipMatrix[i].length; j++) {
            if (kinshipMatrix[i][j] != null) {
                if ((kinshipMatrix[i][j] == KinshipType.son) || (kinshipMatrix[i][j] == KinshipType.daughter)) {
                    m.add(humans.get(j));
                }
            }
        }
        if (m.size() > 0) {
            return m.get(0);
        }
        return null;
    }
    private Human findFather(Human human) {
        if (! (human.getSex() == Sex.male)) {
            return null;
        }
        List<Human> m = new ArrayList<>();
        int i = humans.indexOf(human);
        for (int j = 0; j < kinshipMatrix[i].length; j++) {
            if (kinshipMatrix[i][j] != null) {
                if ((kinshipMatrix[i][j] == KinshipType.son) || (kinshipMatrix[i][j] == KinshipType.daughter)) {
                    m.add(humans.get(j));
                }
            }
        }
        if (m.size() > 0) {
            return m.get(0);
        }
        return null;
    }
    private boolean isKinshipPresent(Kinship kinship) {
        Human who = kinship.getWho();
        Human forWhom = kinship.getForWhom();
        KinshipType type = kinship.getType();

        int i = humans.indexOf(who);
        int j = humans.indexOf(forWhom);

        if (i > 0 && j > 0) {
            return kinshipMatrix[i][j] == type;
        }
        else {
            return false;
        }
    }
    private void checkAndAddKinship(Kinship k) {
        if (! isKinshipPresent(k)) {
            addKinship(k.getWho(), k.getForWhom(), k.getType());
        }
    }
}
