package models;

public class Kinship {
    KinshipType type;
    Human kinsman;

    @Override
    public String toString() {
        return type + ": " + kinsman.toString();
    }

    public Kinship(KinshipType type, Human kinsman) {
        this.type = type;
        this.kinsman = kinsman;
    }
}
