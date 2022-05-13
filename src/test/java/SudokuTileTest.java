import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SudokuTileTest {
    SudokuTile sTile;
    int maxNumber = 9;   // Highest number to test for a tile

    @Test
    @DisplayName("Comparing the retrieved correct number with the one set")
    void compareCorrectNumber() {
        int value = 1;
        sTile = new SudokuTile(value);
        assertEquals(value, sTile.getCorrectValue());

        sTile = new SudokuTile(maxNumber);
        assertEquals(maxNumber, sTile.getCorrectValue());
    }

    @Test
    @DisplayName("Tests setting and getting the editable field")
    void SetGetEditable() {
        sTile = new SudokuTile(1);

        sTile.setEditable(true);
        Assertions.assertEquals(true, sTile.isEditable());

        sTile.setEditable(false);
        Assertions.assertEquals(false, sTile.isEditable());
    }

    @Test
    @DisplayName("Set and get number of current value")
    void setGetCurrentVal() {
        SudokuTile sTile = new SudokuTile(1);
        sTile.setEditable(true);
        sTile.setCurrentValue(1);
        Assertions.assertEquals(1, sTile.getCurrentValue());

        sTile.setCurrentValue(maxNumber);
        Assertions.assertEquals(maxNumber, sTile.getCurrentValue());
    }

    @Test
    @DisplayName("Test that it is not possible to change current number while isEditable is false")
    void setCurrentNotEditable() {
        sTile = new SudokuTile(1);
        sTile.setEditable(true);
        sTile.setCurrentValue(1);
        sTile.setEditable(false);
        sTile.setCurrentValue(2);
        Assertions.assertEquals(1, sTile.getCurrentValue());
    }

    @Test
    @DisplayName("Check that a cleared tile returns the current value of 0")
    void clear() {
        sTile = new SudokuTile(1);
        sTile.setCurrentValue(1);
        sTile.clear();
        Assertions.assertEquals(0, sTile.getCurrentValue());

        sTile.setCurrentValue(9);
        sTile.clear();
        Assertions.assertEquals(0, sTile.getCurrentValue());
    }

    @Test
    @DisplayName("Check if the current number is the correct number")
    void checkCurrentCorrectValue() {
        sTile = new SudokuTile(1);
        sTile.setCurrentValue(1);
        Assertions.assertEquals(true, sTile.check());

        sTile.setCurrentValue(9);
        Assertions.assertEquals(false, sTile.check());

    }
}