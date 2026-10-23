import java.awt.Color;
import java.util.List;

class Warrior extends GameCharacter {
    public Warrior(String name) { super(name, 110, 14); }

    public String attack(GameCharacter t) {
        return name + " swings a mighty sword at " + t.name + " for " + hit(t, 1.0) + " damage!";
    }
    public String specialMove(GameCharacter t, List<GameCharacter> all) {
        return name + " uses Shield Bash on " + t.name + " for " + hit(t, 1.6) + " damage!";
    }
    public String getType()  { return "Warrior"; }
    public Color getColor()  { return new Color(0xE74C3C); }
}