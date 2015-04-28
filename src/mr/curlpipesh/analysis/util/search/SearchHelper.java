package mr.curlpipesh.analysis.util.search;

import mr.curlpipesh.analysis.Main;
import mr.curlpipesh.analysis.descs.FieldDesc;
import mr.curlpipesh.analysis.descs.MethodDesc;
import mr.curlpipesh.analysis.impl.BetterClassAnalyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

public class SearchHelper {
    private static final int CLASS_NAME = 0, METHOD_NAMES = 1, FIELD_NAMES = 2, METHOD_INVOKE = 3, FIELD_ACCESS = 4;

    public static List<String> getSearchResults(String query) {
        query = query.toLowerCase(); // Safety precautions
        List<String> results = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            switch (i) {
                case CLASS_NAME:
                    results.add("Matching classes:");
                    results.add("-----------------");
                    break;
                case METHOD_NAMES:
                    results.add("Matching methods:");
                    results.add("-----------------");
                    break;
                case FIELD_NAMES:
                    results.add("Matching fields:");
                    results.add("----------------");
                    break;
                case METHOD_INVOKE:
                    results.add("Matching method invocations:");
                    results.add("----------------------------");
                    break;
                case FIELD_ACCESS:
                    results.add("Matching field accesses:");
                    results.add("------------------------");
                    break;
            }
            for (Map.Entry<JarEntry, BetterClassAnalyser> e : Main.getAnalysers().entrySet()) {
                BetterClassAnalyser analyser = e.getValue();

                switch(i) {
                    case CLASS_NAME:
                        if (analyser.getClassDesc().getClassName().toLowerCase().contains(query)) {
                            results.add(analyser.getClassDesc().getClassName());
                        }
                        break;
                    case METHOD_NAMES:
                        results.addAll(getMatchingMethods(query, analyser));
                        break;
                    case FIELD_NAMES:
                        results.addAll(getMatchingFields(query, analyser));
                        break;
                    case METHOD_INVOKE:
                        results.addAll(getMatchingMethodInvocations(query, analyser));
                        break;
                    case FIELD_ACCESS:
                        results.addAll(getMatchingFieldAccesses(query, analyser));
                        break;
                    default:
                        break;
                }
            }
            switch (i) {
                case FIELD_ACCESS:
                    break;
                default:
                    results.add("\n");
                    break;
            }
        }
        return results;
    }

    private static List<String> getMatchingMethods(String query, BetterClassAnalyser analyser) {
        List<String> results = new ArrayList<>();
        results.addAll(analyser.getMethods().stream().filter(m -> m.getName().toLowerCase().contains(query))
                .map(m -> m.getOwner().name + "#" + m.getName()).collect(Collectors.toList()));
        return results;
    }

    private static List<String> getMatchingFields(String query, BetterClassAnalyser analyser) {
        List<String> results = new ArrayList<>();
        results.addAll(analyser.getFields().stream().filter(f -> f.getName().toLowerCase().contains(query))
                .map(f -> f.getOwner().name + "#" + f.getName()).collect(Collectors.toList()));
        return results;
    }

    private static List<String> getMatchingMethodInvocations(String query, BetterClassAnalyser analyser) {
        List<String> results = new ArrayList<>();
        for(MethodDesc e : analyser.getMethods()) {
            results.addAll(e.getMethodCallLocations().stream().filter(f -> f.toLowerCase().endsWith("invokes " + query))
                    .collect(Collectors.toList()));
        }
        return results;
    }

    private static List<String> getMatchingFieldAccesses(String query, BetterClassAnalyser analyser) {
        List<String> results = new ArrayList<>();
        for(FieldDesc e : analyser.getFields()) {
            results.addAll(e.getFieldAccessLocations().stream().filter(f -> f.toLowerCase().endsWith("accesses " + query)).collect(Collectors.toList()));
        }
        return results;
    }
}
