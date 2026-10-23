import java.awt.Color;
import java.util.List;

class Tank extends GameCharacter {
    public Tank(String name) {
        super(name, 120, 9);
        armor = 2;
    }

    public String attack(GameCharacter t) {
        return name + " slams " + t.name + " with a tower shield for " + hit(t, 1.0) + " damage!";
    }
    // Hits the target & halves the next damage the Tank takes
    public String specialMove(GameCharacter t, List<GameCharacter> all) {
        shielded = true;
        return name + " uses Iron Wall: hits " + t.name + " for " + hit(t, 0.8) + " and braces for impact!";
    }
    public String getType()  { return "Tank"; }
    public Color getColor()  { return new Color(0x5D6D7E); }
}