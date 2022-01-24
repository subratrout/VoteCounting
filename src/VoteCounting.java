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

    private static String readUserCommand(Map<Character, String> optionNumberVsOptionText){
        Scanner scanner = new Scanner(System.in);
        String userCommand;
        List<Map <String, Integer>> allBallots = new LinkedList<>();
        do {
            optionNumberVsOptionText.entrySet().stream().forEach(e->System.out.println(e.getKey() + "."+ e.getValue()));
            System.out.println("Enter your vote in a single line only (or \"tally\" to calculate):");
            userCommand = scanner.next();
            if(userCommand.equals("tally")) {
                //calculateVoteCount();
            }else {
                //ABDC
               List<Map<String, Integer>> userBallots = processUserCommand(userCommand, optionNumberVsOptionText);
               if (null != userBallots){
                   allBallots.addAll(userBallots);
                   System.out.println(allBallots);
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
        System.out.println(ballot);

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
