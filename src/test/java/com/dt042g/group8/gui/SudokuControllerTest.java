package com.dt042g.group8.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.dt042g.group8.sudoku.Position;

class SudokuControllerTest {
    @Mock Model modelMock;
    Controller controller;

    @BeforeEach
    void setup() {
        modelMock = mock(Model.class);
        controller = new SudokuController(modelMock);
    }

    @Test
    @DisplayName("Setting a value makes the correct method call to the model")
    void settingAValueMakesCorrectMethodCallToModel() {
        Position position = new Position(0, 0);
        int value = 1;

        controller.setValueAt(position, value);

        verify(modelMock).setValueAt(position, value);
    }

    @Test
    @DisplayName("Creating a very easy puzzle makes the correct method call to the model")
    void creatingVeryEasyPuzzleMakesCorrectMethodCallToModel() {
        controller.createPuzzle(Difficulty.VERY_EASY);

        verify(modelMock).createPuzzle(33);
    }

    @Test
    @DisplayName("Creating an easy puzzle makes the correct method call to the model")
    void creatingEasyPuzzleMakesCorrectMethodCallToModel() {
        controller.createPuzzle(Difficulty.EASY);

        verify(modelMock).createPuzzle(31);
    }

    @Test
    @DisplayName("Creating a medium puzzle makes the correct method call to the model")
    void creatingMediumPuzzleMakesCorrectMethodCallToModel() {
        controller.createPuzzle(Difficulty.MEDIUM);

        verify(modelMock).createPuzzle(29);
    }
    @Test
    @DisplayName("Creating a hard puzzle makes the correct method call to the model")
    void creatingHardPuzzleMakesCorrectMethodCallToModel() {
        controller.createPuzzle(Difficulty.HARD);

        verify(modelMock).createPuzzle(27);
    }

    @Test
    @DisplayName("Creating a very hard puzzle makes the correct method call to the model")
    void creatingVeryHardPuzzleMakesCorrectMethodCallToModel() {
        controller.createPuzzle(Difficulty.VERY_HARD);

        verify(modelMock).createPuzzle(25);
    }
}