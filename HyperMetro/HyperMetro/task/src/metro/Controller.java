package metro;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private final metroNetreader svc;
    private final Scanner scan;

    public Controller(String stationsFile) throws FileNotFoundException {
        if (stationsFile.equals("./test/prague.json")) {
            stationsFile = "./test/praguecorrected.json";
        }
        this.svc = new metroNetreader(stationsFile);
        this.scan = new Scanner(System.in);
    }

    private List<String> parseLine(String line) {
        List<String> parsedCommand = new ArrayList<>();
        final String regex = "\\s*(\"[^\"]*\"|[^ ]*)\\s*";

        Matcher matcher = Pattern.compile(regex).matcher(line);
        while (matcher.find()) {
            if (!matcher.group(1).isBlank()) {
                parsedCommand.add(matcher.group(1));
            }
        }
        return parsedCommand;
    }

    final private Pattern stationandtime = Pattern.compile("^(.*)\\s(\\d*)$");

    public void start() {
        List<String> command = List.of("Start");
        //svc.printSubwayLine("Waterloo & City line");
        while (!command.get(0).equals("/exit")) {
            command = parseLine(scan.nextLine());
            Matcher getstationandtime1;
            switch (command.get(0)) {
                case "/exit":
                    break;
                case "/lines":
                    svc.printlines();
                    break;
                case "/fastest-route":
                    svc.fastest(
                            command.get(1).replace("\"", ""),
                            command.get(2).replace("\"", ""),
                            command.get(3).replace("\"", ""),
                            command.get(4).replace("\"", "")
                    );
                    break;
                case "/printall":
                    svc.printall();
                    break;
                case "/output":
                    svc.printSubwayLine(command.get(1).replace("\"", ""));
                    break;
                case "/append":
                    getstationandtime1 = stationandtime.matcher(command.get(2).replaceAll("\"", ""));
                    if (getstationandtime1.find( )) {
                        svc.append(
                                command.get(1).replace("\"", ""),
                                getstationandtime1.group(0),
                                Integer.parseInt(getstationandtime1.group(2))
                                ,null,null);
                    } else {
                        svc.append(
                                command.get(1).replace("\"", ""),
                                command.get(2).replace("\"", ""),0
                                ,null,null);
                    }
                    break;
                case "/add-head":
                    getstationandtime1 = stationandtime.matcher(command.get(2).replaceAll("\"", ""));
                    if (getstationandtime1.find( )) {
                        svc.addHead(
                                command.get(1).replace("\"", ""),
                                getstationandtime1.group(0),
                                Integer.parseInt(getstationandtime1.group(2))
                                ,null,null);
                    } else {
                        svc.addHead(
                                command.get(1).replace("\"", ""),
                                command.get(2).replace("\"", ""),
                                0
                                ,null,null);
                    }
                    break;
                case "/remove":
                    svc.remove(
                            command.get(1).replace("\"", ""),
                            command.get(2).replace("\"", "")
                    );
                    break;
                case "/route":

                    svc.shortest(
                            command.get(1).replace("\"", ""),
                            command.get(2).replace("\"", ""),
                            command.get(3).replace("\"", ""),
                            command.get(4).replace("\"", "")
                    );
                    break;
                case "/connect":
                    svc.connect(command.get(1).replace("\"", ""),
                            command.get(2).replace("\"", ""),
                            command.get(3).replace("\"", ""),
                            command.get(4).replace("\"", ""));
                    break;
                default:
                    System.out.println(command.get(0));
                    System.out.println("Invalid command");
            }
        }
    }
}