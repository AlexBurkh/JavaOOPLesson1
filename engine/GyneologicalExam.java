package engine;

import models.Human;
import models.Kinship;

import java.util.List;

public class GyneologicalExam {
    StringBuilder resultInfo;

    public GyneologicalExam() {
        resultInfo = new StringBuilder();
    }

    public String exam(Human humanToExam) {
        this.resultInfo.append(humanToExam.toString());
        List<Kinship> kinshipList = humanToExam.getKinshipList();
        for (Kinship currentKinship : kinshipList) {
            resultInfo.append("\n\t").append(currentKinship.toString());
        }
        return resultInfo.toString();
    }
}
