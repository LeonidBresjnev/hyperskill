import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class main{
    static java.util.Scanner myReader = new java.util.Scanner(System.in);

    public static void main(String[] args) {
        String myString = "This is the front page of the Simple English Wikipedia. Wikipedias are places where people work together to write encyclopedias in different languages. We use Simple English words and grammar here. The Simple English Wikipedia is for everyone! That includes children and adults who are learning English. There are 142,262 articles on the Simple English Wikipedia. All of the pages are free to use. They have all been published under both the Creative Commons License and the GNU Free Documentation License. You can help here! You may change these pages and make new pages. Read the help pages and other good pages to learn how to write pages here. If you need help, you may ask questions at Simple talk. Use Basic English vocabulary and shorter sentences. This allows people to understand normally complex terms or phrases.";
        List<Integer> Mylist  = Arrays.stream(myString.split("\\s")).mapToInt(x -> Math.max(x
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "")
                .replaceAll("e$","")
                .replaceAll("[aeiouy]","a")
                .replaceAll("a+","a")
                .replaceAll("[^a]","").length(),1)).collect(ArrayList::new,
                ArrayList::add,
                ArrayList::addAll);
        int syllables =0; //= words0.sum();
        int polySyllables =0; //= words0.filter(x -> (x > 2)).sum();

        for (int aInt : Mylist) {

            syllables += aInt;
            if (aInt > 2) {
                polySyllables += 1;
            }
        }
        /*
while (words0.iterator().hasNext()) {
 System.out.println(words0.iterator().nextInt());
}*/
        System.out.println(syllables + " " + polySyllables);
        //System.out.println(words0.sum());
       /* try {
            myString = new java.util.Scanner(new java.io.File(args[0])).nextLine();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
        //System.out.println(myString);
        int words = myString.split("\\s").length;
        int sentences = myString.split("[\\.\\?\\!\\:\\;]").length;
        int characters = myString.replaceAll("\\s","").length();
        //int syllables = words.;
       // double score = (4.71 * characters) / words + (0.5 * words) / sentences  - 21.43;
        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polySyllables);
      //  System.out.printf("the score is: %.2f\n", score);
     //   System.out.printf("This text should be understood by %.0f-%.0f-year-olds", Math.ceil(score) + 5d, Math.ceil(score) + (score > 12d ? 11d : 6d) );

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");

        String input;
        float score;
        float sum = 0f;
        short count = 0;
        input = myReader.next();
        if ("ARI".equals(input.toUpperCase()) || "ALL".equals(input.toUpperCase())) {
            count++;
            score = (4.71f * (float) characters) / (float) words + (0.5f * (float) words) / (float) sentences  - 21.43f;
            sum += (int) Math.ceil(score) + (score > 12d ? 11 : 5);
            System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", score, (int) Math.ceil(score) + (score > 12f ? 11 : 5));
        }
        if ("FK".equals(input.toUpperCase()) || "ALL".equals(input.toUpperCase())) {
            count++;
            score = 0.39f * (float) words / (float) sentences + 11.8f * (float) syllables / (float) words - 15.59f;
            sum += (int) Math.ceil(score) + (score > 12d ? 11 : 5);
            System.out.printf("Flesch–Kincaid Index: %.2f (about %d-year-olds).\n", score, (int) Math.ceil(score) + (score > 12f ? 11 : 5));
        }
        if ("SMOG".equals(input.toUpperCase()) || "ALL".equals(input.toUpperCase())) {
            count++;
            score = 1.043f * (float) Math.sqrt( (float) polySyllables * 30f / (float) sentences) + 3.1291f;
            sum += (int) Math.ceil(score) + (score > 12d ? 11 : 5);
            System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, (int) Math.ceil(score) + (score > 12f ? 11 : 5));
        }
        if ("CL".equals(input.toUpperCase()) || "ALL".equals(input.toUpperCase())) {
            count++;
            score = 5.88f * (float) characters / (float) words - 29.6f * (float) sentences / (float) words - 15.8f ;
            sum += (int) Math.ceil(score) + (score > 12d ? 11 : 6);
            System.out.printf("Coleman–Liau k: %.2f (about %d-year-olds).\n", score, (int) Math.ceil(score) + (score > 12f ? 11 : 6));
        }



        if (count > 0) {
            System.out.printf("\nThis text should be understood in average by %.0f-year-olds.", sum / count);
        }
    }
}
