package se.liu.jenca01.adventofcode.y2022;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import se.liu.jenca01.adventofcode.Christmas;

public class Dec08 extends Christmas {

    long sampleAnswer1 = 21;
    long sampleAnswer2 = 8;

    public static void main(String[] args) throws Exception {
        var christmas = new Dec08();
        christmas.solveSample();
        christmas.solveMy();
    }

    @Override
    public void solveSample() throws Exception {
        long start = System.currentTimeMillis();
        long solve1 = solve1(sampleData());
        long stop = System.currentTimeMillis();
        System.out.println("Example solve1 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer1, solve1, "solve1 is expected to return " + sampleAnswer1);

        start = System.currentTimeMillis();
        long solve2 = solve2(sampleData());
        stop = System.currentTimeMillis();
        System.out.println("Example solve2 timing: " + (stop-start) + "ms");
        assertEquals(sampleAnswer2, solve2, "solve2 is expected to return " + sampleAnswer2);
    }

    @Override
    public void solveMy() throws Exception {
        System.out.println(simpleClassName() + " solve1: " + solve1(myData()));
        System.out.println(simpleClassName() + " solve2: " + solve2(myData()));
    }

    private int[][] convertData(Stream<String> data) {
        var lines = data.toList();
        var dimY = lines.size();
        var dimX = lines.get(0).length();
        var grid = new int[dimX][dimY];
        for (var y=0; y<dimY; y++)
            for (var x=0; x<dimX; x++)
                grid[x][y] = lines.get(y).charAt(x) - '0';
        return grid;
    }

    public long solve1(Stream<String> stream) {
        var grid = convertData(stream);
        int visibleCount = 0;
        for (var y=0; y<grid.length; y++)
            for (var x=0; x<grid[0].length; x++)
                if (isVisible(grid, x, y)) visibleCount++;;
        return visibleCount;
    }

    private boolean isVisible(int[][] grid, int x, int y) {
        boolean isVisible = isVisibleLeft(grid, x, y) ||
                isVisibleUp(grid, x, y) ||
                isVisibleRight(grid, x, y) ||
                isVisibleDown(grid, x, y);
        return isVisible;
    }

    private boolean isVisibleLeft(int[][] grid, int x, int y) {
        if (x == 0) return true;
        var treeHeight = grid[x][y];
        for(var i=x-1; i>=0; i--)
            if (grid[i][y] >= treeHeight)
                return false;
        return true;
    }

    private boolean isVisibleRight(int[][] grid, int x, int y) {
        if (x == grid[0].length - 1) return true;
        var treeHeight = grid[x][y];
        for(var i=x+1; i<grid[0].length; i++)
            if (grid[i][y] >= treeHeight)
                return false;
        return true;
    }
    
    private boolean isVisibleUp(int[][] grid, int x, int y) {
        if (y == 0) return true;
        var treeHeight = grid[x][y];
        for(var i=y-1; i>=0; i--)
            if (grid[x][i] >= treeHeight)
                return false;
        return true;
    }

    private boolean isVisibleDown(int[][] grid, int x, int y) {
        if (y == grid.length - 1) return true;
        var treeHeight = grid[x][y];
        for(var i=y+1; i<grid.length; i++)
            if (grid[x][i] >= treeHeight)
                return false;
        return true;
    }

    public long solve2(Stream<String> stream) {
        var grid = convertData(stream);
        long bestViewScore = 0;
        for (var y=0; y<grid.length; y++)
            for (var x=0; x<grid[0].length; x++) {
                var viewScore = calcViewScore(grid, x, y);
                if (viewScore > bestViewScore)
                    bestViewScore = viewScore;
            }
        return bestViewScore;
    }

    private long calcViewScore(int[][] grid, int x, int y) {
        var left = calcLeftScore(grid, x, y);
        var right = calcRightScore(grid, x, y);
        var up = calcUpScore(grid, x, y);
        var down = calcDownScore(grid, x, y);
        // TODO Auto-generated method stub
        return 1L * left * right * up * down;
    }

    private int calcLeftScore(int[][] grid, int x, int y) {
        if (x == 0) return 0;
        var treeCount = 0;
        var treeHeight = grid[x][y];
        for(var i=x-1; i>=0; i--)
            if (grid[i][y] >= treeHeight)
                return ++treeCount;
            else
                treeCount++;
        return treeCount;
    }

    private int calcRightScore(int[][] grid, int x, int y) {
        if (x == grid[0].length - 1) return 0;
        var treeCount = 0;
        var treeHeight = grid[x][y];
        for(var i=x+1; i<grid[0].length; i++)
            if (grid[i][y] >= treeHeight)
                return ++treeCount;
            else
                treeCount++;
        return treeCount;
    }

    private int calcUpScore(int[][] grid, int x, int y) {
        if (y == 0) return 0;
        var treeCount = 0;
        var treeHeight = grid[x][y];
        for(var i=y-1; i>=0; i--)
            if (grid[x][i] >= treeHeight)
                return ++treeCount;
            else
                treeCount++;
        return treeCount;
    }

    private int calcDownScore(int[][] grid, int x, int y) {
        if (y == grid.length - 1) return 0;
        var treeCount = 0;
        var treeHeight = grid[x][y];
        for(var i=y+1; i<grid.length; i++)
            if (grid[x][i] >= treeHeight)
                return ++treeCount;
            else
                treeCount++;
        return treeCount;
    }
}

/*



*/