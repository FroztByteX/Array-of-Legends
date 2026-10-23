import java.awt.Color;
import java.util.List;

class Healer extends GameCharacter {
    public Healer(String name) { super(name, 100, 10); }

    public String attack(GameCharacter t) {
        return name + " taps " + t.name + " with a holy staff for " + hit(t, 1.0) + " damage!";
    }
    // Heals itself AND damages the target
    public String specialMove(GameCharacter t, List<GameCharacter> all) {
        int before = hp;
        hp = Math.min(maxHp, hp + 30);
        return name + " uses Holy Light: heals " + (hp - before) + " HP and hits " + t.name + " for " + hit(t, 1.0) + "!";
    }
    public String getType()  { return "Healer"; }
    public Color getColor()  { return new Color(0xF39C12); }
}