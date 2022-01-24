import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    }
}
