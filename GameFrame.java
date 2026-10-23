import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameFrame extends JFrame {
    // ADT #1: List stores the roster.  
	// ADT #2: Queue controls battle turn order.
    private final List<GameCharacter> roster = new ArrayList<>();
    private final Queue<GameCharacter> turnQueue = new LinkedList<>();
    private List<GameCharacter> fighters = new ArrayList<>();
    private final Map<GameCharacter, JProgressBar> bars = new HashMap<>();
    private boolean battleActive = false;

    // Roster widgets ++++++++++++++++++++++++++++
    private final DefaultListModel<GameCharacter> listModel = new DefaultListModel<>();
    private final JList<GameCharacter> list = new JList<>(listModel);
    private final JTextField nameField = new JTextField();
    private final JTextField searchField = new JTextField();
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Warrior", "Mage", "Archer", "Healer", "Tank"});

    // Battle widgets ++++++++++++++++++++++++++++
    private final JPanel barsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private final JTextArea log = new JTextArea();
    private final JButton nextBtn = new JButton("Next Turn");
    private final JToggleButton autoBtn = new JToggleButton("Auto-play");
    private final javax.swing.Timer autoTimer = new javax.swing.Timer(900, e -> nextTurn());

    GameFrame() {
        super("Array of Legends");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("  Array of Legends", JLabel.LEFT);
        title.setOpaque(true);
        title.setBackground(new Color(0x2C2A4A));
        title.setForeground(new Color(0xF1C40F));
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        add(buildRosterPanel(), BorderLayout.WEST);
        add(buildBattlePanel(), BorderLayout.CENTER);

        roster.add(new Mage("Aurea"));
        roster.add(new Warrior("Willer"));
        roster.add(new Archer("Dylan"));
        roster.add(new Healer("Divine"));
        roster.add(new Tank("Bulgi"));
        refreshList();
    }

    // roster (create / view / search / update / remove) ++++++++++++++++++++++++++++++++++++++ 
    private JPanel buildRosterPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setPreferredSize(new Dimension(330, 0));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(new JLabel("Search:"), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshList(); }
        });
        p.add(top, BorderLayout.NORTH);

        list.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean sel, boolean foc) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(l, v, i, sel, foc);
                if (!sel) lbl.setForeground(((GameCharacter) v).getColor().darker());
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
                return lbl;
            }
        });
        list.addListSelectionListener(e -> {
            GameCharacter c = list.getSelectedValue();
            if (c != null) { nameField.setText(c.name); typeBox.setSelectedItem(c.getType()); }
            if (!e.getValueIsAdjusting() && !battleActive) showFighters(list.getSelectedValuesList());
        });
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Class:"));
        form.add(typeBox);
        
        JPanel btns = new JPanel(new GridLayout(1, 3, 4, 4));
        btns.add(button("Add", new Color(0x27AE60), e -> addCharacter()));
        btns.add(button("Rename", new Color(0x2980B9), e -> renameCharacter()));
        btns.add(button("Remove", new Color(0xC0392B), e -> removeCharacter()));
        form.add(btns);
        p.add(form, BorderLayout.SOUTH);
        
        return p;
    }

    private JButton button(String text, Color color, java.awt.event.ActionListener action) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.addActionListener(action);
        return b;
    }

    // returns a valid name, or null (after showing an error)
    private String validName(GameCharacter ignore) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { error("Name cannot be empty."); return null; }
        if (name.length() > 15) { error("Name must be 15 characters or fewer."); return null; }
        if (!name.matches("[A-Za-z0-9 ]+")) { error("Use only letters, numbers and spaces."); return null; }
        for (GameCharacter c : roster) {
            if (c != ignore && c.name.equalsIgnoreCase(name)) { error("That name is already taken."); return null; }
        }
        return name;
    }

    private void addCharacter() {
        String name = validName(null);
        if (name == null) return;
        String type = (String) typeBox.getSelectedItem();
        switch (type) {
            case "Mage":
            	roster.add(new Mage(name));
            	break;
            case "Archer": 
            	roster.add(new Archer(name)); 
            	break;
            case "Healer":
            	roster.add(new Healer(name)); 
            	break;
            case "Tank":
            	roster.add(new Tank(name));
            	break;
            default:       
            	roster.add(new Warrior(name));
        }
        nameField.setText("");
        refreshList();
    }

    private void renameCharacter() {
        GameCharacter c = list.getSelectedValue();
        if (c == null) { error("Select a character first."); return; }
        String name = validName(c);
        if (name == null) return;
        c.name = name;
        refreshList();
    }

    private void removeCharacter() {
        GameCharacter c = list.getSelectedValue();
        if (c == null) { error("Select a character first."); return; }
        if (JOptionPane.showConfirmDialog(this, "Remove " + c + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            roster.remove(c);
            nameField.setText("");
            refreshList();
        }
    }

    private void refreshList() {
        String q = searchField.getText().trim().toLowerCase();
        listModel.clear();
        for (GameCharacter c : roster) {
            if (c.name.toLowerCase().contains(q) || c.getType().toLowerCase().contains(q)) listModel.addElement(c);
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
    }

    // battle ++++++++++++++++++++++++++
    private JPanel buildBattlePanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Select 2-6 characters on the left (Ctrl-click), then:"));
        top.add(button("Start Battle", new Color(0x2980B9), e -> startBattle()));
        nextBtn.setEnabled(false);
        top.add(nextBtn);
        top.add(autoBtn);
        nextBtn.addActionListener(e -> nextTurn());
        autoBtn.addActionListener(e -> { if (autoBtn.isSelected()) autoTimer.start(); else autoTimer.stop(); });
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        p.add(top, BorderLayout.NORTH);

        barsPanel.setBorder(BorderFactory.createTitledBorder("Fighters"));
        log.setEditable(false);
        log.setBackground(new Color(0x1B1B2F));
        log.setForeground(Color.WHITE);
        log.setFont(new Font("Monospaced", Font.PLAIN, 13));
        
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(barsPanel), new JScrollPane(log));
        split.setResizeWeight(0.55);
        p.add(split, BorderLayout.CENTER);
        
        return p;
    }

    private void startBattle() {
        List<GameCharacter> chosen = list.getSelectedValuesList();
        if (chosen.size() < 2 || chosen.size() > 6) { error("Select between 2 and 6 characters."); return; }
        showFighters(chosen);
        turnQueue.clear();
        turnQueue.addAll(fighters);
        log.setText("Battle start!\n");
        battleActive = true;
        nextBtn.setEnabled(true);
    }

    // rebuilds the "Fighters" panel (HP bars) for the given characters. Also used as a live preview
    private void showFighters(List<GameCharacter> chosen) {
        fighters = new ArrayList<>(chosen);
        barsPanel.removeAll();
        bars.clear();
        for (GameCharacter c : fighters) {
            c.resetForBattle();
            JProgressBar bar = new JProgressBar(0, c.maxHp);
            bar.setStringPainted(true);
            bar.setForeground(c.getColor());
            bars.put(c, bar);
            JPanel row = new JPanel(new BorderLayout(8, 0));
            JLabel lbl = new JLabel(c.toString());
            lbl.setPreferredSize(new Dimension(170, 20));
            row.add(lbl, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            barsPanel.add(row);
        }
        updateBars();
        barsPanel.revalidate();
        barsPanel.repaint();
    }

    private void nextTurn() {
        if (!battleActive) return;
        GameCharacter actor = turnQueue.poll();
        List<GameCharacter> targets = new ArrayList<>();
        for (GameCharacter c : fighters) if (c != actor && c.isAlive()) targets.add(c);
        GameCharacter target = targets.get(GameCharacter.rng.nextInt(targets.size()));

        Map<GameCharacter, Integer> before = new HashMap<>();
        for (GameCharacter c : fighters) before.put(c, c.hp);

        boolean special = actor.specialReady() && GameCharacter.rng.nextInt(100) < 40;
        log.append(special ? actor.useSpecial(target, fighters) + "\n" : actor.attack(target) + "\n");
        actor.endTurn();

        for (GameCharacter c : fighters) {
            if (c.hp < before.get(c)) flash(c);			// red flash on anyone who got hit
            if (before.get(c) > 0 && !c.isAlive()) log.append("\t" + c.name + " is DEFEATED!\n");
        }
        turnQueue.removeIf(c -> !c.isAlive());
        if (actor.isAlive()) turnQueue.offer(actor);	// back of the line
        updateBars();
        log.setCaretPosition(log.getDocument().getLength());

        if (turnQueue.size() <= 1) {
            battleActive = false;
            nextBtn.setEnabled(false);
            autoTimer.stop();
            autoBtn.setSelected(false);
            log.append("\n---> " + turnQueue.peek().name + " WINS! <---\n");
        }
    }

    private void updateBars() {
        for (GameCharacter c : fighters) {
            JProgressBar bar = bars.get(c);
            bar.setValue(c.hp);
            bar.setString(c.hp + " / " + c.maxHp + (c.isAlive() ? "" : "  (defeated)"));
        }
    }

    // the bar flashes red for a moment
    private void flash(GameCharacter c) {
        JProgressBar bar = bars.get(c);
        bar.setForeground(Color.RED);
        javax.swing.Timer t = new javax.swing.Timer(250, e -> bar.setForeground(c.getColor()));
        t.setRepeats(false);
        t.start();
    }

}
