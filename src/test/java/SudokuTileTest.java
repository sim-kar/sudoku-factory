import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SudokuTileTest {
    SudokuTile tile;

    @Test
    @DisplayName("Comparing the retrieved correct number with the one set")
    void compareCorrectNumber() {
        tile = new SudokuTile(1);
        assertEquals(1, tile.getCorrectValue());

        tile = new SudokuTile(9);
        assertEquals(9, tile.getCorrectValue());
    }

    @Test
    @DisplayName("Tests setting and getting the editable field")
    void SetGetEditable() {
        tile = new SudokuTile(1);

        tile.setEditable(true);
        assertTrue(tile.isEditable());

        tile.setEditable(false);
        assertFalse(tile.isEditable());
    }

    @Test
    @DisplayName("Set and get number of current value")
    void setGetCurrentVal() {
        tile = new SudokuTile(1);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        assertEquals(1, tile.getCurrentValue());

        tile.setCurrentValue(9);
        assertEquals(9, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Test that it is not possible to change current number while isEditable is false")
    void setCurrentNotEditable() {
        tile = new SudokuTile(1);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        tile.setEditable(false);
        tile.setCurrentValue(2);
        Assertions.assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Clearing an editable tile should return a current value of 0")
    void canClearEditable() {
        tile = new SudokuTile(1);
        tile.setEditable(true);
        tile.setCurrentValue(1);
        tile.clear();
        Assertions.assertEquals(0, tile.getCurrentValue());

        tile.setCurrentValue(9);
        tile.clear();
        Assertions.assertEquals(0, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Clearing an non-editable tile should not change the current value")
    void cannotClearNonEditable() {
        tile = new SudokuTile(1);
        tile.setEditable(false);
        tile.clear();
        Assertions.assertEquals(1, tile.getCurrentValue());
    }

    @Test
    @DisplayName("Same current and correct number should return true")
    void sameCurrentCorrectValueIsTrue() {
        tile = new SudokuTile(1);
        tile.setCurrentValue(1);
        assertTrue(tile.check());
    }

    @Test
    @DisplayName("Different current and correct number should return false")
    void sameCurrentCorrectValueIsFalse() {
        tile = new SudokuTile(1);
        tile.setEditable(true);
        tile.setCurrentValue(9);
        assertFalse(tile.check());
    }
}