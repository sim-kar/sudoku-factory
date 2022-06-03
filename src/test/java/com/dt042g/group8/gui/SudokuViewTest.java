package com.dt042g.group8.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SudokuViewTest {
    @Mock Model modelMock;
    @Mock Controller controllerMock;

    @BeforeEach
    void setup() {
        modelMock = mock(Model.class);
        controllerMock = mock(Controller.class);
    }

    @Test
    @DisplayName("Created view registers as board change observer")
    void createdViewRegistersAsBoardChangeObserver() {
        SudokuView view = new SudokuView(modelMock, controllerMock);

        verify(modelMock).registerObserver((BoardChangeObserver) view);
    }

    @Test
    @DisplayName("Created view registers as board solved observer")
    void createdViewRegistersAsBoardSolvedObserver() {
        SudokuView view = new SudokuView(modelMock, controllerMock);

        verify(modelMock).registerObserver((BoardSolvedObserver) view);
    }
}