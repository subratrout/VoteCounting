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
        List<Map <String, Integer>> allVotes = new LinkedList<>();
        do {
            System.out.println("Enter your vote (or \"tally\" to calculate):");
            userCommand = scanner.next();
            if(userCommand.equals("tally")) {
                //calculateVoteCount();
            }else {
                //ABDC
                char[] choices = userCommand.trim().toCharArray();
                Map<String, Integer> ballot = new LinkedHashMap<>();
                for(int i=0; i<choices.length; i++){
                    Character choice = choices[i];
                    if(!optionNumberVsOptionText.containsKey(choice)){
                        System.out.println("Please enter the valid choices, invalid option="+choice);
                        return null;
                    }
                    String optionText = optionNumberVsOptionText.get(choice);
                    ballot.put(optionText, i+1);
                }
                allVotes.add(ballot);
            }
        }while(!userCommand.equals("tally"));
        return userCommand;
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
