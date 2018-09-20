package ConflictDetector;

import ConflictDetector.Detector.Detector;
import ConflictDetector.Detector.Pair4J;

// Java Launcher
public class JavaMain {
    public static void main(String[] args){
        String prefix = "./src/main/resources/PolicyExample/";
        Pair4J result = Detector.processConflictDetect(prefix + "children-fridge-policy.xml", prefix + "guest-fridge-policy.xml", true);
        System.out.println("결과 : " + result);
        System.out.println("충돌 [" + result.conflictCases().size() + "개] : " + result.conflictCases());
        System.out.println("맵퍼 : " + result.dictionary());
    }
}