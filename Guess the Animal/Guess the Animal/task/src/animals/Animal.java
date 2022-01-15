package animals;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(
        scope = Animal.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "node")
class Animal {
    public String node;

    public String question;
    public String fact;
    public String notfact;
    public Animal yes;
    public Animal no;
    public Animal parent;


    public Animal() {
        this.node = "";
        this.question = "";
        this.yes = null;
        this.no = null;
        this.parent = null;

    }


    public Animal(String animal, String question) {
        this.node = animal;
        this.question = question;
        this.yes = null;
        this.no = null;
        this.parent = null;
    }
}
