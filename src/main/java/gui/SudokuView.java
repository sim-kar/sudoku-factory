package gui;

import org.jetbrains.annotations.Nullable;
import sudoku.Position;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SudokuView extends JFrame implements BoardChangeObserver, BoardSolvedObserver {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 681;
    private final static List<Position> POSITIONS = getPositions();
    private final static Color NEUTRAL = Color.WHITE;
    private final static Color SELECTED = new Color(220, 240, 255);
    private final static Color HINT = new Color(255, 240, 240);
    private final static Color DUPLICATE = Color.RED;
    private final static Color EDITABLE = Color.BLUE;
    private final static Color SOLVED = new Color(230, 255, 230);
    private final static Color DARK = Color.DARK_GRAY;
    private final static Color LIGHT = Color.LIGHT_GRAY;
    private final static int EMPTY = 0;
    private final Model model;
    private final Controller controller;
    private final Map<Position, TileButton> tiles;
    private final Map<String, Difficulty> difficulties;
    private boolean showDuplicates;
    private boolean showHints;
    private JPanel board;
    private JComboBox<String> difficultyComboBox;

    @Nullable
    private Position selected;

    public SudokuView(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        this.model.registerObserver((BoardChangeObserver) this);
        this.model.registerObserver((BoardSolvedObserver) this);
        this.tiles = new HashMap<>();
        this.showDuplicates = false;
        this.showHints = false;
        this.difficulties = new LinkedHashMap<>();
        difficulties.put("Beginner", Difficulty.VERY_EASY);
        difficulties.put("Easy", Difficulty.EASY);
        difficulties.put("Medium", Difficulty.MEDIUM);
        difficulties.put("Hard", Difficulty.HARD);
        difficulties.put("Expert", Difficulty.VERY_HARD);
    }

    public void createView() {
        // Board
        GridLayout grid = new GridLayout(3, 3);
        board = new JPanel(grid);
        board.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(DARK, 1)
        ));
        add(board, BorderLayout.CENTER);
        board.setVisible(false);

        // Blocks
        GridLayout gridWithGaps = new GridLayout(3, 3, 1, 1);
        JPanel[] blocks = new JPanel[9];
        for (int i = 0; i < 9; i++) {
            JPanel block = new JPanel(gridWithGaps);
            block.setBackground(LIGHT);
            block.setBorder(BorderFactory.createLineBorder(DARK, 1));
            board.add(block);
            blocks[i] = block;
        }

        // Tiles
        for (Position position : POSITIONS) {
            TileButton tile = new TileButton(position);
            tile.addActionListener(this::selectTile);
            tile.addKeyListener(new TileValueListener());
            tiles.put(position, tile);

            int blockIndex = getBlockIndex(position.getX(), position.getY());
            blocks[blockIndex].add(tile);
        }

        // Panel with all controls
        JPanel controls = new JPanel();
        controls.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        controls.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        add(controls, BorderLayout.EAST);

        // Drop down menu to select difficulty
        JPanel difficultyPanel = new JPanel();
        JLabel difficultyLabel = new JLabel("Difficulty");
        difficultyPanel.add(difficultyLabel);

        difficultyComboBox = new JComboBox<>();
        for (String difficulty : difficulties.keySet()) {
            difficultyComboBox.addItem(difficulty);
        }
        difficultyPanel.add(difficultyComboBox);
        controls.add(difficultyPanel, constraints);

        // Button to create a new puzzle
        JButton newPuzzle = new JButton("New Puzzle");
        newPuzzle.addActionListener(this::createPuzzle);
        constraints.gridy = 1;
        controls.add(newPuzzle, constraints);

        // Checkbox to toggle showing duplicates
        JCheckBox duplicates = new JCheckBox("Show Duplicates");
        duplicates.setSelected(true);
        duplicates.addActionListener(this::toggleDuplicates);
        constraints.gridy = 2;
        controls.add(duplicates, constraints);
        this.showDuplicates = duplicates.isSelected();

        // Checkbox to toggle showing hints
        JCheckBox hints = new JCheckBox("Show Hints");
        hints.addActionListener(this::toggleHints);
        constraints.gridy = 3;
        constraints.weighty = 1;
        controls.add(hints, constraints);
        this.showHints = hints.isSelected();

        pack();
        setTitle("Sudoku");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggleDuplicates(ActionEvent actionEvent) {
        if (actionEvent.getSource().getClass() == JCheckBox.class) {
            JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
            this.showDuplicates = checkBox.isSelected();
            updateBoard();
        }
    }

    private void toggleHints(ActionEvent actionEvent) {
        if (actionEvent.getSource().getClass() == JCheckBox.class) {
            JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
            this.showHints = checkBox.isSelected();
            updateBoard();
        }
    }

    @Override
    public void updateBoard() {
        for (Position position : POSITIONS) {
            TileButton tile = tiles.get(position);
            String value = (model.getValueAt(position) == EMPTY)
                    ? ""
                    : Integer.toString(model.getValueAt(position));
            tile.setText(value);
            tile.setForeground(DARK);
            tile.setBackground(NEUTRAL);
            tile.hint = false;

            if (model.isEditable(position)) tile.setForeground(EDITABLE);
        }

        if (showDuplicates) {
            for (Position duplicate : model.getDuplicates()) {
                tiles.get(duplicate).setForeground(DUPLICATE);
            }
        }

        if (showHints) {
            for (Position position : model.getSectionsWithMistakes()) {
                TileButton tile = tiles.get(position);
                tile.setBackground(HINT);
                tile.hint = true;
            }
        }

        if (selected != null) {
            tiles.get(selected).setBackground(SELECTED);
        }

        board.setVisible(true);
        repaint();
    }

    @Override
    public void solved() {
        for (Position position : POSITIONS) {
            tiles.get(position).setBackground(SOLVED);
        }

        selected = null;
        repaint();
    }

    private int getBlockIndex(int x, int y) {
        /*
        Indexes of all blocks:
        | 0 | 1 | 2 |
        | 3 | 4 | 5 |
        | 6 | 7 | 8 |

        The y-value divided by 3 (floor division) gives us a starting index
        Adding the x-value divided by 3 (floor division) gives us the block index

        Ex: the tiles in block 4 have the positions {3, 3} to {5, 5}
            sudoku.Position {3, 3}:
            row starting index: (3 // 3) * 3 = 3
            column offset:      3 // 3       = 1
            block index:        3 + 1        = 4

            sudoku.Position {5, 5}:
            row starting index: (5 // 3) * 3 = 3
            column offset:      5 // 3       = 1
            block index:        3 + 1        = 4
         */
        int blockRowStartingIndex = (y / 3) * 3;
        int blockColumnOffset = x / 3;

        return blockRowStartingIndex + blockColumnOffset;
    }

    // returns an unmodifiable list
    private static List<Position> getPositions() {
        List<Position> positions = new LinkedList<>();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                positions.add(new Position(x, y));
            }
        }

        return Collections.unmodifiableList(positions);
    }

    // select a clicked tile if it is editable
    private void selectTile(ActionEvent event) {
        if (event.getSource().getClass() == TileButton.class) {
            TileButton tile = (TileButton) event.getSource();

            if (!model.isEditable(tile.position)) return;

            if (selected != null) {
                if (tiles.get(selected).hint) {
                    tiles.get(selected).setBackground(HINT);
                } else {
                    tiles.get(selected).setBackground(NEUTRAL);
                }
            }

            selected = tile.position;
            tile.setBackground(SELECTED);
        }
    }

    // uses controller to create a new puzzle
    private void createPuzzle(ActionEvent event) {
        Difficulty difficulty = difficulties.get((String) difficultyComboBox.getSelectedItem());
        controller.createPuzzle(difficulty);
        selected = null;
    }

    // uses controller to set the value of the selected tile
    private class TileValueListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
            if (selected == null) return;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_0,
                     KeyEvent.VK_BACK_SPACE,
                     KeyEvent.VK_DELETE -> controller.setValueAt(selected, 0);
                case KeyEvent.VK_1 -> controller.setValueAt(selected, 1);
                case KeyEvent.VK_2 -> controller.setValueAt(selected, 2);
                case KeyEvent.VK_3 -> controller.setValueAt(selected, 3);
                case KeyEvent.VK_4 -> controller.setValueAt(selected, 4);
                case KeyEvent.VK_5 -> controller.setValueAt(selected, 5);
                case KeyEvent.VK_6 -> controller.setValueAt(selected, 6);
                case KeyEvent.VK_7 -> controller.setValueAt(selected, 7);
                case KeyEvent.VK_8 -> controller.setValueAt(selected, 8);
                case KeyEvent.VK_9 -> controller.setValueAt(selected, 9);
            }
        }


    }

    private static class TileButton extends JButton {
        Position position;
        boolean hint;

        TileButton(Position position) {
            this.position = position;
            setFont(new Font("Arial", Font.BOLD, 26));
            setBackground(NEUTRAL);
            setForeground(DARK);
            setBorder(BorderFactory.createEmptyBorder());
            setFocusPainted(false);
        }
    }
}
