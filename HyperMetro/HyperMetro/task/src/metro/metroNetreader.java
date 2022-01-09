package metro;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

class Transit {
    Station neighbor;
    int time;

    Transit(Station station, int time) {
        this.neighbor = station;
        this.time = time;
    }

    int getTime() {
        return this.time;
    }
}

class Station implements Comparable<Station>{
    private String name;
    public ArrayList<Transfer> transfers;
    private int time;

    public String line;
    boolean visited;
    Set<Station> transit;
    Set<Station> neighbors;
    Station last;
    Set<Transit> neighbors2;
    public String[] next;
    public String[] prev;

    @Override
    public int compareTo(Station x){
        if (this.time != x.time) {
            return Integer.compare(this.time, x.time);
        } else if (!this.name.equals(x.name)) {
            return this.name.compareTo(x.name);
        } else if (!this.line.equals(x.line)) {
            return this.line.compareTo(x.line);
        } else return 0;

    }

    Station(String linename, String name, String[] transferline, String[] transferstation, int time) {
        this.name = name;
        this.line = linename;
        this.time = time;
        transit = new HashSet<>();
        neighbors = new HashSet<>();
        transfers = new ArrayList<>();
        if (transferline.length > 0 && transferline[0] != null) {
            System.out.println("tranferline: " + transferline[0]);
            for (int i = 0; i < transferline.length; i++) {
                transfers.add(new Transfer(transferline[i], transferstation[i]));
            }
        }
    }

    public void addtransfer(String line, String station) {
        this.transfers.add(new Transfer(line, station));
    }

    void setTime(int time) {
        this.time = time;
    }
    int getTime() {
        return this.time;
    }
    static class Transfer {
        final private String line;
        final private String station;

        Transfer(String line, String station) {
            this.line = line;
            this.station = station;
        }

        public String getLine() {
            return line;
        }

        public String getStation() {
            return station;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class metroNetreader {

    Map<String, Map<String, Station>> subwayStructure;

    public metroNetreader(String stationsFile)  {
        this.subwayStructure = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(stationsFile))) {

            JsonElement tree = JsonParser.parseReader(Objects.requireNonNull(br));
            JsonObject ob = tree.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : ob.entrySet()) {
                Map<String, Station> metroline = new HashMap<>();
                String lineName = entry.getKey();
                //System.out.println(lineName);
                JsonArray jarray = entry.getValue().getAsJsonArray();
                //System.out.println(jarray.toString());
                for (JsonElement jelement : jarray) {
                    Station newstation = new Gson().fromJson(jelement.getAsJsonObject(), Station.class);
                    //System.out.print(newstation.getName());
                    JsonArray jprev = jelement.getAsJsonObject().getAsJsonArray("prev");
                    newstation.prev = new String[jprev.size()];
                    //System.out.print(jprev.size());
                    for (int i = 0; i < newstation.prev.length; i++) {
                        newstation.prev[i] = jprev.get(i).getAsString();
                        //System.out.print(" " + newstation.prev[i]);
                    }

                    JsonArray jnext = jelement.getAsJsonObject().getAsJsonArray("next");
                    newstation.next = new String[jnext.size()];
                    //System.out.print(jnext.size());
                    for (int i = 0; i < newstation.next.length; i++) {
                        newstation.next[i] = jnext.get(i).getAsString();
                        //System.out.print(" " + newstation.next[i]);
                    }
                    newstation.transfers = new ArrayList<>(Arrays.asList(new Gson().fromJson(jelement.getAsJsonObject().get("transfer").getAsJsonArray(), Station.Transfer[].class)));
                    newstation.transit = new HashSet<>();
                    newstation.line = lineName;
                    newstation.neighbors = new HashSet<>();
                    newstation.neighbors2 = new HashSet<>();
                    /*for (int i = 0; i < newstation.transfers.size(); i++) {
                        System.out.print(newstation.transfers.get(i).getLine()
                                + " "  + newstation.transfers.get(i).getStation());
                    }*/
                    //System.out.print(newstation.getTime() + "\n");
                    if (newstation.getName().matches(".*Canning Town.*")) {
                        newstation.transfers.forEach(x->System.out.println(x.getLine() + " " + x.getStation()));
                    }
                    if (newstation.getName().matches(".*Pancras.*")) {
                        System.out.println(newstation.line + " " + newstation.getName());
                    }
                    metroline.put(newstation.getName(), newstation);
                }
                subwayStructure.put(lineName, metroline);
            }
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
        }
        System.out.println("file is read");
        Station stationy;
        for (String linex : subwayStructure.keySet()) {
            for (Station stationx : subwayStructure.get(linex).values()) {
                for (String neighborname : stationx.next) {
                    stationy = subwayStructure.get(linex).get(neighborname);
                    if (stationy != null) {
                        stationx.neighbors.add(stationy);
                        stationx.neighbors2.add(new Transit(stationy, stationx.getTime()));
                    } else {
                        System.out.println("Station not found: " + linex + " " + neighborname);
                    }
                }
                for (String neighborname : stationx.prev) {
                    stationy = subwayStructure.get(linex).get(neighborname);
                    if (stationy != null) {
                        stationx.neighbors.add(stationy);
                        stationx.neighbors2.add(new Transit(stationy, stationy.getTime()));
                    } else {
                        System.out.println("Station not found: " + linex + " " + neighborname);
                    }
                }
                for (int i = 0; i < stationx.transfers.size(); i++) {
                    for  (Station stationz : subwayStructure.get(stationx.transfers.get(i).getLine()).values()) {
                        if (stationz.getName().equals(stationx.transfers.get(i).getStation())) {
                            stationx.transit.add(stationz);
                        }
                    }
                }
            }
        }
    }

    public void printSubwayLine(String key) {
        if (subwayStructure.containsKey(key)) {
            System.out.println("depot");
        }

        for (Station station : subwayStructure.get(key).values()) {
            System.out.print(station.getName());
            for (Station stationz : station.transit) {
                System.out.print(" " + stationz.line + " " + stationz.getName());
            }
            System.out.println();
        }
        if (subwayStructure.containsKey(key)) {
            System.out.println("depot");
        }
    }

    private void connecteach(String line1, String station1, String line2, String station2){
        subwayStructure.get(line1).get(station1).transit.add(
                subwayStructure.get(line2).get(station2)
        );
    }

    public void printlines() {
        subwayStructure.keySet().forEach(System.out::println);
    }

    public void printall() {
        for (String line : subwayStructure.keySet()) {
            System.out.println("Line: " + line);
            for (Station stationx: subwayStructure.get(line).values()) {
                System.out.print(stationx.getName());
                for (Transit transit : stationx.neighbors2) {
                    System.out.print(" " + transit.neighbor.getName() + "(" + transit.getTime() + ")");
                }

                for (Station transit : stationx.transit) {
                    System.out.print(" " + transit.getName() + "(" + transit.line + ")");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void connect(String line1, String station1, String line2, String station2){
        connecteach(line1, station1, line2, station2);
        connecteach(line2, station2, line1, station1);
    }

    public void append(String lineName, String stationName, int time, String transfline, String transfstation) {
        /*LinkedList<Station> linex = subwayStructure.get(lineName);
        linex.addLast(new Station(lineName, stationName, new String[]{transfline}, new String[]{transfstation}, time));
        if (linex.size() > 1) {
            linex.getLast().neighbors.add(linex.get(linex.size() - 2));
            linex.get(linex.size() - 2).neighbors.add(linex.getLast());
        }*/
    }

    public void addHead(String lineName, String stationName, int time, String transfline, String transfstation) {
        /*LinkedList<Station> linex = subwayStructure.get(lineName);
        subwayStructure.get(lineName).addFirst(new Station(lineName, stationName, new String[]{transfline}, new String[]{transfstation}, time));
        if (linex.size() > 1){
            linex.getFirst().neighbors.add(linex.get(1));
            linex.get(1).neighbors.add(linex.getFirst());
        }
         */
    }

    public void remove(String lineName, String stationName) {
        /*LinkedList<Station> linex =subwayStructure.get(lineName);
        Iterator<Station> iter = linex.iterator();
        Station stationx = null;
        while (iter.hasNext()) {
            stationx = iter.next();
            if (stationx.getName().equals(stationName)) {
                break;
            }
        }
        if (stationx != null) {
            linex.remove(stationx);
        }
        */
    }

    public boolean shortest(String linefrom, String fromstation, String toline, String tostation) {

        for (String linex : subwayStructure.keySet()) {
            for (Station stationz : subwayStructure.get(linex).values()) {
                stationz.visited = false;
                stationz.last = null;
            }
        }
        Station stationx = subwayStructure.get(toline).get(tostation);
        Iterator<Station> iter;


        Set<Station> currentset= new HashSet<>();
        Set<Station> nextcurrentset= new HashSet<>();
        nextcurrentset.add(stationx);
        stationx.visited = true;


        iter = stationx.transit.iterator();

        while (iter.hasNext()) {
            stationx = iter.next();
            stationx.visited = true;
            currentset.add(stationx);
        }

        int dist = 0;

        while (dist < 550) {
            currentset.addAll(nextcurrentset);

            nextcurrentset.clear();
            for (Station stationz: currentset) {
                for (Station stationu : stationz.neighbors) {
                    if (!stationu.visited) {

                        nextcurrentset.add(stationu);
                        stationu.last = stationz;
                        if (stationu.getName().equals(fromstation) && stationu.line.equals(linefrom)) {
                            printroute(stationu, linefrom);
                            return true;
                        }
                        stationu.visited = true;

                        for (Station transits : stationu.transit) {

                            if (!transits.visited) {
                                nextcurrentset.add(transits);
                                transits.last = stationu;
                                if (transits.getName().equals(fromstation) && transits.line.equals(linefrom)) {
                                    printroute(transits, linefrom);
                                    return true;
                                }
                                transits.visited = true;
                            }
                        }
                    }
                }
            }
            dist++;
            currentset.clear();
        }
        return false;
    }

    void fastest(String linefrom, String fromstation, String toline, String tostation) {
        Iterator<Station> iter;

        for (String linex : subwayStructure.keySet()) {
            for (Station stationz : subwayStructure.get(linex).values()) {
                stationz.visited = false;
                stationz.last = null;
                stationz.setTime(0);
            }
        }


        Station stationx = subwayStructure.get(toline).get(tostation);


        stationx.visited = true;
        TreeSet<Station> candidates = new TreeSet<>();


        Iterator<Transit> iter2;
        iter2 = stationx.neighbors2.iterator();
        Transit transitx;

        while (iter2.hasNext()) {
            transitx = iter2.next();
            transitx.neighbor.last = stationx;
            transitx.neighbor.setTime(transitx.getTime());
            candidates.add(transitx.neighbor);
        }

        for (Station station: stationx.transit) {
            station.last = stationx;
            station.setTime(5);
            candidates.add(station);
        }
        Station stationy;
        int counter = 0;
        while (counter < 450) {
            counter++;
            stationy = candidates.pollFirst();
            stationy.visited = true;

            if (stationy.getName().equals(fromstation) && stationy.line.equals(linefrom)) {
                printroute(stationy, linefrom);
                System.out.printf("Total: %d minutes in the way\n", stationy.getTime());
                break;
            }
            for (Transit transit: stationy.neighbors2) {
                if (!transit.neighbor.visited) {
                    if (candidates.contains(transit.neighbor) && transit.neighbor.getTime() > (stationy.getTime() + transit.getTime())) {
                        candidates.remove(transit.neighbor);
                        transit.neighbor.setTime(stationy.getTime() + transit.getTime());
                        transit.neighbor.last = stationy;
                        candidates.add(transit.neighbor);
                    }  else if (!candidates.contains(transit.neighbor))  {
                        transit.neighbor.last = stationy;
                        transit.neighbor.setTime(stationy.getTime() + transit.getTime());
                        candidates.add(transit.neighbor);
                    }
                }
            }
            iter = stationy.transit.iterator();
            Station stationu;
            while (iter.hasNext()) {
                stationu = iter.next();
                //System.out.println("Transit " + stationu.line + " " +  stationu.getName());
                if (!stationu.visited) {
                    if (candidates.contains(stationu) && stationu.getTime() > (stationy.getTime() + 5)) {
                        candidates.remove(stationu);
                        stationu.setTime(stationy.getTime() + 5);
                        stationu.last = stationy;
                        candidates.add(stationu);
                    }  else if (!candidates.contains(stationu))  {
                        stationu.last = stationy;
                        stationu.setTime(stationy.getTime() + 5);
                        candidates.add(stationu);
                    }
                }
            }
        }
    }



    void printroute(Station tostation, String fromline) {
        Station stationx = tostation;
        String lastline = fromline;
        while (stationx.last != null) {
            if (!stationx.line.equals(lastline)) {
                System.out.println("Transition to line " + stationx.line);
            }
            System.out.println(stationx.getName());
            lastline = stationx.line;
            stationx = stationx.last;
        }
        if (!stationx.line.equals(lastline)) {
            System.out.println("Transition to line " + stationx.line);
        }
        System.out.println(stationx.getName());
    }

}