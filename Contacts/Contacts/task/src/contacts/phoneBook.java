package contacts;

import java.util.ArrayList;

public class phoneBook<contact> extends ArrayList<contact> {
    public void print(String ifEmpty) {
        if (this.size() == 0) {
            System.out.println(ifEmpty);
        } else {
            for(int i = 0; i < this.size(); i++) {
                System.out.println((i + 1) + ". " + this.get(i).toString());
            }
        }
    }
}
