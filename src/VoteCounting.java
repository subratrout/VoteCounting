import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class VoteCounting {
    public static void main(String[] args) throws IOException {
        try {
            List<String> lines = readAllOption();
            Map<Character, String> optionNumberVsOptionText = optionNumberVsOptionText(lines);
            readUserCommand(optionNumberVsOptionText);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void calculateVoteCount(List<Map<String, Integer>> allBallots){
        Map<String, List<Map<String, Integer>>> preferenceVsBallots = new LinkedHashMap<>();
        System.out.println("All Ballots: " + allBallots);
        allBallots.stream().forEach(ballot -> {
            Integer minValAmongPreferences = ballot.values().stream().min(Comparator.comparingInt(Integer::intValue)).get();
            String optionText = null;
            for(Map.Entry<String, Integer> entry : ballot.entrySet()){
                if(entry.getValue().equals(minValAmongPreferences)){
                    optionText = entry.getKey();
                    break;
                }
            }
            if(!preferenceVsBallots.containsKey(optionText)){
                preferenceVsBallots.put(optionText, new LinkedList<>());
            }
            preferenceVsBallots.get(optionText).add(ballot);
        });

        evaluateResult(preferenceVsBallots, 1);
    }

    private static void evaluateResult(Map<String, List<Map<String, Integer>>> preferenceVsBallots, int round){
        System.out.println("****************  Round" + round + "  **************");
        votingCountRoundEvaluationBasedOnPref(preferenceVsBallots);

        Integer totalActiveBallots = preferenceVsBallots.values().stream().map(v-> v.size()).collect(Collectors.summingInt(Integer::intValue));
        System.out.println("Total Active Ballots: " + totalActiveBallots);
        int quota = (totalActiveBallots /2) + 1;
        System.out.println("Quota = " + quota);

        boolean isResultAnnounced = false;

        for(Map.Entry<String, List<Map<String, Integer>>> entry: preferenceVsBallots.entrySet()){
            String preferenceCategory = entry.getKey();
            int size = entry.getValue().size();
            if(size >= quota){
                System.out.println("The winner is: "+ preferenceCategory);
                isResultAnnounced = true;
                break;
            }
        }

        if(!isResultAnnounced){
            round++;
            int minSize = Integer.MAX_VALUE;
            String minSizePreferenceCategory = null;
            // getting minimum size for each category
            for(Map.Entry<String,List<Map<String, Integer>>> entry: preferenceVsBallots.entrySet()){
                String preferenceCategory = entry.getKey();
                int size = entry.getValue().size();
                if(size < minSize){
                    minSize = size;
                    minSizePreferenceCategory = preferenceCategory;
                }
            }
            List<Map<String,Integer>> minSizePreferenceCategoryBallots = preferenceVsBallots.get(minSizePreferenceCategory);
            preferenceVsBallots.remove(minSizePreferenceCategory);
            System.out.println("The candidate " + minSizePreferenceCategory + " is eliminated");

            for(Map<String, Integer> ballot: minSizePreferenceCategoryBallots){
                String optionText = ballot.entrySet().stream().filter(e-> e.getValue() >1).map(e-> e.getKey()).findFirst().get();
                if(preferenceVsBallots.containsKey(optionText)){
                    preferenceVsBallots.get(optionText).add(ballot);
                }
            }
            evaluateResult(preferenceVsBallots, round);
        }

    }

    private static void votingCountRoundEvaluationBasedOnPref(Map<String, List<Map<String, Integer>>> preferenceVsBallots){
        System.out.println("=========Printing our preferenceVsBallots =======");
        preferenceVsBallots.entrySet().stream().forEach(e->{
            System.out.println("Preference category: " + e.getKey() + " [current vote= "+ e.getValue().size() + "]");
            e.getValue().stream().forEach(System.out::println);
        });
    }

    private static String readUserCommand(Map<Character, String> optionNumberVsOptionText){
        Scanner scanner = new Scanner(System.in);
        String userCommand;
        List<Map<String, Integer>> allBallots = new LinkedList<>();
        do {
            optionNumberVsOptionText.entrySet().stream().forEach(e->System.out.println(e.getKey() + "."+ e.getValue()));
            System.out.println("Enter your vote in a single line only (or \"tally\" to calculate):");
            userCommand = scanner.next();
            if(userCommand.equals("tally")) {
                calculateVoteCount(allBallots);
            }else {
                //ABDC
               List<Map<String, Integer>> userBallots = processUserCommand(userCommand, optionNumberVsOptionText);
               if (null != userBallots){
                   allBallots.addAll(userBallots);
//                   System.out.println(allBallots);
               }
            }
        }while(!userCommand.equals("tally"));
        return userCommand;
    }

    private static List<Map<String, Integer>> processUserCommand(String userCommand, Map<Character, String> optionNumberVsOptionText) {
        List<Map<String, Integer>> ballots = new LinkedList<>();
        char[] choices = userCommand.trim().toCharArray();
        Map<String, Integer> ballot = new LinkedHashMap<>();
        for(int i=0 ; i < choices.length; i++){
            Character choice = choices[i];
            if(!optionNumberVsOptionText.containsKey(choice)){
                System.out.println("Please enter for valid choices, invalid option= " + choice);
                return null;
            }
            String optionText = optionNumberVsOptionText.get(choice);
            ballot.put(optionText, i + 1);
        }
        ballots.add(ballot);

        return ballots;
    }

    private static List<String> readAllOption() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("data" + File.separator + "input.txt"))
                .stream()
                .filter(Objects::nonNull)
                .filter(s->!s.isBlank())
                .distinct()
                .collect(Collectors.toList());
        return lines;
    }

    private static Map<Character, String> optionNumberVsOptionText(List<String> lines) {
        Map<Character, String> optionNumberVsOptionText = new LinkedHashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String optionText = lines.get(i);
            Character optionNumber = (char) (i + 65);
            optionNumberVsOptionText.put(optionNumber, optionText);
            System.out.println(optionNumber + " " + optionText);
        }
        return optionNumberVsOptionText;
    }
}
