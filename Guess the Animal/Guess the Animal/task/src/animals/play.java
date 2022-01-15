package animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Random;

public class play {
    protected String filetype;
    protected static final LocalTime now = LocalTime.now();
    protected static final Random random = new Random();
    protected static final java.util.Scanner scanner = new java.util.Scanner(System.in);
    protected Animal top;
    play(String filetype) {
        this.filetype = filetype;
        top = loadtree();
    }

    protected void savetree(Animal root) {
        String fileName = ".\\animals"  + (System.getProperty("user.language").equals("eo") ? "_eo." : ".")  +  this.filetype;
        ObjectMapper objectMapper = new JsonMapper();
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected Animal loadtree() {
        String fileName = ".\\animals" + (System.getProperty("user.language").equals("eo") ? "_eo." : ".")  + this.filetype;
        ObjectMapper objectMapper = new JsonMapper();
        File file;
        try {
            file = new File(fileName);
            if (!file.exists()) { return null;}
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return objectMapper.readValue(file, Animal.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    protected static String capitalize(String str)
    {
        if (str == null) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    protected final LinkedList<String> knownanimals = new LinkedList<>();

    protected void list(Animal node) {
        if (node.yes == null) {
            knownanimals.add(node.node);
        } else {
            list(node.yes);
            list(node.no);
        }
    }

    protected boolean search(Animal node, String target) {
        if (target.equals(node.node)) {
            return true;
        } else if (node.yes == null) {
            return false;
        }
        else {
            if (search(node.yes, target)) {
                knownanimals.addFirst(node.fact);
                return true;
            } else if (search(node.no, target)){
                knownanimals.addFirst(node.notfact);
                return true;
            } else {
                return false;
            }
        }
    }

    protected void statistics(Animal node, int[] stats, int level) {
        stats[3] = Math.max(stats[3], level + 1);
        if(node.yes != null) {
            stats[1]++;
            //System.out.print(node.node);
            statistics(node.yes, stats, level + 1);
            statistics(node.no, stats, level + 1);
        } else {
            stats[0]++;
            //System.out.print(node.node);
            stats[2] += level;

            stats[4] = stats[4] < 0 ? level:  Math.min(stats[3],level);
        }
    }
}
