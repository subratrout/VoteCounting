import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class VoteCounting {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("data" + File.separator + "input.txt"))
                .stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(lines);
        Map<Character, String> optionNumberVsOptionText = new LinkedHashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            String optionText = lines.get(i);
            Character optionNumber = (char) (i + 65);
            optionNumberVsOptionText.put(optionNumber, optionText);
            System.out.println(optionNumber + " " + optionText);
        }

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
                        return;
                    }
                    String optionText = optionNumberVsOptionText.get(choice);
                    ballot.put(optionText, i+1);
                }
                allVotes.add(ballot);
            }
        }while(!userCommand.equals("tally"));

    }
}
