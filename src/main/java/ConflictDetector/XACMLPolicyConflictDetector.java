package ConflictDetector;

import ConflictDetector.Detector.Detector;
import ConflictDetector.Detector.Pair4J;

public class XACMLPolicyConflictDetector {

    public Pair4J detectConflict(String originalPolicyPath, String modifiedPolicyPath){
        return Detector.processConflictDetect(originalPolicyPath, modifiedPolicyPath, false);
    }

}
