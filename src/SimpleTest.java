import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {

    MinePlanter planter = new BNBMinePlanter();

    @Test
    public void testNoGreedy2() {
        Sign[][] field = new Sign[][]{
                {new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
                {new Empty(), new Bound(5), new Empty(), new Bound(6), new Empty()},
                {new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
                {new Empty(), new Bound(7), new Empty(), new Bound(7), new Empty()},
                {new Empty(), new Empty(), new Empty(), new Empty(), new Empty()}
        };
        Sign[][] student = planter.plant(5, 5, field);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(student[i][j].toChar());
            }
            System.out.println("");
        }
    }

    @Test
    public void testAllMines() {
        Sign[][] field = new Sign[][]{
                {new Empty(), new Empty()},
                {new Empty(), new Bound(3)}
        };
        Sign[][] teacher = new Sign[][]{
                {new Mine(), new Mine()},
                {new Mine(), new Bound(3)}
        };
        Sign[][] student = planter.plant(2, 2, field);
        Assert.assertEquals(teacher[0][0], student[0][0]);
        Assert.assertEquals(teacher[0][1], student[0][1]);
        Assert.assertEquals(teacher[1][0], student[1][0]);
        Assert.assertTrue(student[1][1].isBound());
    }

    @Test
    public void testNoMines() {
        Sign[][] field = new Sign[][]{
                {new Empty(), new Empty(), new Empty()},
                {new Empty(), new Bound(0), new Empty()},
                {new Empty(), new Empty(), new Empty()}
        };
        Sign[][] output = planter.plant(3, 3, field);
        Assert.assertEquals(3, output.length);
        for (int r = 0; r < 3; r++) {
            Assert.assertEquals(3, output[r].length);
            for (int c = 0; c < 3; c++) {
                Assert.assertNotNull(output[r][c]);
                Assert.assertTrue(!output[r][c].isMine());
            }
        }
    }

    @Test
    public void testNoBounds() {
        Sign[][] field = new Sign[][]{
                {new Empty(), new Empty(), new Empty()},
                {new Empty(), new Empty(), new Empty()},
                {new Empty(), new Empty(), new Empty()}
        };
        Sign[][] output = planter.plant(3, 3, field);
        Assert.assertEquals(3, output.length);
        for (int r = 0; r < 3; r++) {
            Assert.assertEquals(3, output[r].length);
            for (int c = 0; c < 3; c++) {
                Assert.assertNotNull(output[r][c]);
                Assert.assertTrue(output[r][c].isMine());
            }
        }
    }

    @Test
    public void testExampleInput() {
        Sign[][] exampleInput = new Sign[][]{
                {new Bound(3), new Empty(), new Bound(4), new Empty(), new Empty(), new Bound(1)},
                {new Bound(2), new Empty(), new Bound(2), new Bound(1), new Empty(), new Empty()},
                {new Bound(8), new Bound(8), new Bound(8), new Bound(8), new Bound(8), new Bound(8)}
        };
        Sign[][] exampleOutput = planter.plant(3, 6, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                if (exampleInput[r][c].isBound()) Assert.assertTrue(exampleOutput[r][c].isBound());
                if (exampleOutput[r][c].isMine()) mineCount++;
            }
        }
        Assert.assertEquals(3, mineCount);
    }

    @Test
    public void testAntiGreedy() {
        Sign[][] exampleInput = new Sign[][]{
                {new Empty(), new Bound(2), new Empty(), new Bound(2), new Empty()},
                {new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
        };
        Sign[][] exampleOutput = planter.plant(2, 5, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                if (exampleInput[r][c].isBound()) Assert.assertTrue(exampleOutput[r][c].isBound());
                if (exampleOutput[r][c].isMine()) mineCount++;
            }
        }
        Assert.assertEquals(4, mineCount);
    }

    @Test
    public void testAntiGreedy2() {
        Sign[][] exampleInput = new Sign[][]{
                {new Empty(), new Empty()},
                {new Bound(2), new Empty()},
                {new Empty(), new Empty()},
                {new Bound(2), new Empty()},
                {new Empty(), new Empty()},
        };
        Sign[][] exampleOutput = planter.plant(5, 2, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                if (exampleInput[r][c].isBound()) Assert.assertTrue(exampleOutput[r][c].isBound());
                if (exampleOutput[r][c].isMine()) mineCount++;
            }
        }
        Assert.assertEquals(4, mineCount);
    }

    @Test
    public void testAntiGreedy3() {
        Sign[][] exampleInput = new Sign[][]{
                {new Bound(1), new Empty(), new Bound(6), new Bound(8),},
                {new Empty(), new Bound(5), new Bound(1), new Bound(7),},
                {new Empty(), new Empty(), new Empty(), new Bound(1),},
                {new Empty(), new Bound(8), new Bound(8), new Bound(2),},
        };
        Sign[][] exampleOutput = planter.plant(4, 4, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                if (exampleInput[r][c].isBound()) Assert.assertTrue(exampleOutput[r][c].isBound());
                if (exampleOutput[r][c].isMine()) mineCount++;
            }
        }
        Assert.assertEquals(4, mineCount);
    }

    @Test
    public void testAntiGreedy4() {
        Sign[][] exampleInput = new Sign[][]{
                {new Bound(1), new Empty(), new Empty(), new Empty(),},
                {new Empty(), new Bound(5), new Empty(), new Bound(8),},
                {new Bound(6), new Bound(1), new Empty(), new Bound(8),},
                {new Bound(8), new Bound(7), new Bound(1), new Bound(2),},
        };
        Sign[][] exampleOutput = planter.plant(4, 4, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                if (exampleInput[r][c].isBound()) Assert.assertTrue(exampleOutput[r][c].isBound());
                if (exampleOutput[r][c].isMine()) mineCount++;
            }
        }
        Assert.assertEquals(4, mineCount);
    }

    @Test
    public void testHugeEmpty() {
        Sign[][] exampleInput = new Sign[50][50];
        for (int r = 0; r < 50; r++)
            for (int c = 0; c < 50; c++) {
                exampleInput[r][c] = new Empty();
            }
        Sign[][] exampleOutput = planter.plant(50, 50, exampleInput);

        int mineCount = 0;
        Assert.assertEquals(exampleInput.length, exampleOutput.length);
        for (int r = 0; r < exampleInput.length; r++) {
            Assert.assertEquals(exampleInput[r].length, exampleOutput[r].length);
            for (int c = 0; c < exampleInput[r].length; c++) {
                Assert.assertNotNull(exampleOutput[r][c]);
                Assert.assertTrue(exampleOutput[r][c].isMine());
            }
        }
    }

}
