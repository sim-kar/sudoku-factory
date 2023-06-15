package com.sim_kar.sudoku_factory.gui;

import com.sim_kar.sudoku_factory.sudoku.Position;
import org.jetbrains.annotations.Nullable;
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

/**
 * Presents a GUI representation of a Sudoku Puzzle Game. It has controls to select the difficulty
 * and generate a new puzzle, and to enable or disable displaying hints and duplicates.
 * A generated puzzle is displayed as an interactive board.
 */
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

    /**
     * Create a new SudokuView with the given model and controller. Takes a model and a controller
     * as parameter. The model holds the state of the Sudoku game, which this view will display.
     * The controller is used to update the state of the model.
     *
     * @param model the model that will be used to display the Sudoku puzzle
     * @param controller the controller that will be used to update the model
     */
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

    /**
     * Generates a new view and displays it in a new window.
     */
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
            tile.addKeyListener(new SetTileValueListener());
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

    /**
     * Whether to show (user input) duplicates on the board or not. Uses a checkbox to toggle on
     * or off.
     *
     * @param actionEvent the triggering event. Should come from a JCheckBox.
     */
    private void toggleDuplicates(ActionEvent actionEvent) {
        if (actionEvent.getSource().getClass() == JCheckBox.class) {
            JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
            this.showDuplicates = checkBox.isSelected();
            updateBoard();
        }
    }

    /**
     * Whether to show hints (sections with errors) on the board or not. Uses a checkbox to toggle
     * on or off.
     *
     * @param actionEvent the triggering event. Should come from a JCheckBox.
     */
    private void toggleHints(ActionEvent actionEvent) {
        if (actionEvent.getSource().getClass() == JCheckBox.class) {
            JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
            this.showHints = checkBox.isSelected();
            updateBoard();
        }
    }

    /**
     * Displays the Sudoku game board, based on the current state of the game. Will show
     * duplicates and hints if enabled, and the selected tile if there is one.
     * <br><br>
     * {@inheritDoc}
     */
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

    /**
     * Sets the background of all tiles on the board to green, to signify that the board has been
     * solved.
     * <br><br>
     * {@inheritDoc}
     */
    @Override
    public void solved() {
        for (Position position : POSITIONS) {
            tiles.get(position).setBackground(SOLVED);
        }

        selected = null;
        repaint();
    }

    /**
     * Get the index of the block (0-8) that the given x,y belongs to.
     */
    private int getBlockIndex(int x, int y) {
        int blockRowStartingIndex = (y / 3) * 3;
        int blockColumnOffset = x / 3;

        return blockRowStartingIndex + blockColumnOffset;
    }

    /**
     * Get an unmodifiable list of all positions on a 9x9 board.
     */
    private static List<Position> getPositions() {
        List<Position> positions = new LinkedList<>();

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                positions.add(new Position(x, y));
            }
        }

        return Collections.unmodifiableList(positions);
    }

    /**
     * Select a tile. Only editable tiles can be selected. Selecting a tile changes the background
     * color as a visual cue to the user; the previously selected tile will have its background
     * color reset.
     *
     * @param event the triggering event. Should come from a TileButton
     */
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

    /**
     * Create a new puzzle using the controller. The difficulty level currently selected in the
     * difficulty combo box will be used to create the new puzzle.
     */
    private void createPuzzle(ActionEvent event) {
        Difficulty difficulty = difficulties.get((String) difficultyComboBox.getSelectedItem());
        controller.createPuzzle(difficulty);
        selected = null;
    }

    /**
     * Sets the value of the selected tile when pressing the matching key on the keyboard,
     * using the controller.
     * 1-9 sets the corresponding value.
     * 0, delete and backspace clears the value.
     */
    private class SetTileValueListener implements KeyListener {
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

    /**
     * Represent a tile on a Sudoku board. Holds a reference to its position and whether it is
     * a "hint", i.e. it is in a section that contains mistakes.
     */
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
