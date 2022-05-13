import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTileTest {
    int repeat = 20;  // Number of repeated tests for each method
    int maxNum = 9;    // Highest number to test for a tile
    Random rnd = new Random();

    @Test
    @DisplayName("Getting correct number ")
    void testGetCorrectVal() {
        int val;
        for (int i = 0; i < repeat; i++) {
            val = rnd.nextInt(maxNum) + 1;
            SudokuTile sTile = new SudokuTile(val);
            assertEquals(val, sTile.getCorrectVal());
        }
    }

    @Test
    @DisplayName("Tests setting and getting the editable field")
    void testSetGetEditable() {
        SudokuTile sTile = new SudokuTile(rnd.nextInt(maxNum) + 1);
        boolean editable;
        for (int i = 0; i < repeat; i++) {
            editable = rnd.nextBoolean();
            sTile.setEditable(editable);
            Assertions.assertEquals(editable, sTile.isEditable());
        }
    }

    @Test
    @DisplayName("Set and get number of current value")
    void testSetGetCurrentVal() {
        SudokuTile sTile = new SudokuTile(rnd.nextInt(maxNum) + 1);
        sTile.setEditable(true);
        int r;
        for (int i = 0; i < repeat; i++) {
            r = rnd.nextInt(maxNum) + 1;
            sTile.setCurrentVal(r);
            Assertions.assertEquals(r, sTile.getCurrentVal());
        }
    }

    @Test
    @DisplayName("Test set current number when not editable")
    void testSetCurrentNotEditable() {
        SudokuTile sTile = new SudokuTile(rnd.nextInt(maxNum) + 1);
        int cur, wrongCur;

        for (int i = 0; i < repeat; i++) {
            cur = rnd.nextInt(maxNum) + 1;
            sTile.setEditable(true);
            sTile.setCurrentVal(cur);
            sTile.setEditable(false);
            wrongCur = (cur + 1) % maxNum;
            sTile.setCurrentVal(wrongCur);
            Assertions.assertNotEquals(wrongCur, sTile.getCurrentVal());
        }
    }

    @Test
    @DisplayName("Clear the current number")
    void clear() {
        SudokuTile sTile = new SudokuTile(rnd.nextInt(maxNum) + 1);
        for (int i = 0; i < repeat; i++) {
            sTile.setCurrentVal(rnd.nextInt(maxNum) + 1);
            sTile.clear();
            Assertions.assertEquals(0, sTile.getCurrentVal());
        }
    }

    @Test
    @DisplayName("Check if the current number is the correct number")
    void check() {
        int cor, cur;
        boolean testTrue;

        for (int i = 0; i < maxNum; i++) {
            cor = rnd.nextInt(maxNum) + 1;
            SudokuTile sTile = new SudokuTile(cor);
            testTrue = rnd.nextBoolean();

            if (testTrue) sTile.setCurrentVal(cor);
            else {
                do {
                    cur = rnd.nextInt(maxNum) + 1;
                } while (cur == cor);
                sTile.setCurrentVal(cur);
            }

            Assertions.assertEquals(testTrue, sTile.check());
        }

    }
}