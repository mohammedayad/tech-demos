package com.ayad.microservicedemo.exercises.problemsolving.battleshipgame;

import java.util.Scanner;
import java.util.stream.IntStream;

//problem details https://developer.aliyun.com/article/953019

public class BattleshipSolution {
    // static char[][] s;//the grid of size n x n
    // static int[] in;//array of size m containing the size of each ship
    // static int n, m;// n is the size of the grid && m is the number of ships
    //static int ans;// the final result
    //static int[][] vis;// array of size n x n used to mark the cells that are occupied by a battleship or visited
    //adjust the names for the variables
    private static char[][] battleshipGrid;
    private static int[] shipSizesContainer;
    private static int gridSize, numberOfShips;
    private static int totalNumOfWays;
    private static int[][] visitedCells;


//    static boolean ok() {
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                if (s[i][j] == 'O' && vis[i][j] == 0)
//                    return false; // O must place but not vis
//            }
//        }
//        return true;
//    }


    /**
     * Checks if all the cells covered by the battleships are visited at least once.
     *
     * @return {@code true} if all the cells covered by the battleships are visited at least once, {@code false} otherwise.
     */

    private static boolean checkCells() {
        return IntStream.rangeClosed(1, gridSize)
                .flatMap(i -> IntStream.rangeClosed(1, gridSize)
                        .filter(j -> battleshipGrid[i][j] == 'O')
                        .map(j -> visitedCells[i][j]))
                .allMatch(count -> count > 0);
    }

//    static boolean judge(int pos, int x, int y, int t) {
//        if (t == 0) {
//            if (y + in[pos] - 1 > n)
//                return false; // <= n
//            for (int i = y; i < y + in[pos]; i++) {
//                if (vis[x][i] != 0 || s[x][i] == 'X')
//                    return false; // X not place
//            }
//        } else {
//            if (x + in[pos] - 1 > n)
//                return false;
//            for (int i = x; i < x + in[pos]; i++) {
//                if (vis[i][y] != 0 || s[i][y] == 'X')
//                    return false;
//            }
//        }
//        return true;
//    }

    /**
     * Checks if a battleship of a given size can be placed at the specified row and column with the given orientation.
     *
     * @param pos the index of the battleship size in the shipSizesContainer array.
     * @param row the row where the battleship starts.
     * @param col the column where the battleship starts.
     * @param orientation the orientation of the battleship (0 for horizontal, 1 for vertical).
     * @return {@code true} if the battleship can be placed at the specified location and orientation, {@code false} otherwise.
     */

    static boolean judge(int pos, int row, int col, int orientation) {
        int start = orientation == 0 ? col : row;
        int end = start + shipSizesContainer[pos];
        return end <= gridSize + 1 && IntStream.range(start, end)
                .allMatch(i -> (orientation == 0 ? visitedCells[row][i] : visitedCells[i][col]) == 0 &&
                        (orientation == 0 ? battleshipGrid[row][i] : battleshipGrid[i][col]) != 'X');
    }

    /**
     * Checks if a battleship described by the given {@code ShipDto} object can be placed on the grid.
     * Checks if a battleship of a given size can be placed at the specified row and column with the given orientation.
     * @param ship the {@code ShipDto} object that describes the battleship.
     * @return {@code true} if the battleship can be placed on the grid, {@code false} otherwise.
     */

    private static boolean checkShipPlacedPossibility(ShipDto ship) {
        int start = ship.getOrientation() == 0 ? ship.getCol() : ship.getRow();
        int end = start + shipSizesContainer[ship.getPos()];
        return end <= gridSize + 1 && IntStream.range(start, end)
                .allMatch(i -> (ship.getOrientation() == 0 ? visitedCells[ship.getRow()][i] : visitedCells[i][ship.getCol()]) == 0 &&
                        (ship.getOrientation() == 0 ? battleshipGrid[ship.getRow()][i] : battleshipGrid[i][ship.getCol()]) != 'X');
    }

//    static void addBattleship(int x, int y, int pos, int id, int val) {
//        if (id == 0) {
//            for (int i = y; i < y + in[pos]; i++) {
//                vis[x][i] += val;
//            }
//        } else {
//            for (int i = x; i < x + in[pos]; i++) {
//                vis[i][y] += val;
//            }
//        }
//    }

    /**
     * Adds a battleship described by the given {@code ShipDto} object to the grid.
     *
     * @param ship the {@code ShipDto} object that describes the battleship.
     */
    private static void addBattleshipToGrid(ShipDto ship) {
        int start = ship.getOrientation() == 0 ? ship.getCol() : ship.getRow();
        int end = start + shipSizesContainer[ship.getPos()];

        IntStream.range(start, end)
                .forEach(i -> {
                    if (ship.getOrientation() == 0) {
                        visitedCells[ship.getRow()][i] += ship.getCellVal();
                    } else {
                        visitedCells[i][ship.getCol()] += ship.getCellVal();
                    }
                });
    }

    static void addBattleship(int row, int col, int pos, int orientation, int cellVal) {
        int start = orientation == 0 ? col : row;
        int end = start + shipSizesContainer[pos];

        IntStream.range(start, end)
                .forEach(i -> {
                    if (orientation == 0) {
                        visitedCells[row][i] += cellVal;
                    } else {
                        visitedCells[i][col] += cellVal;
                    }
                });
    }

//    static void dfs(int x) {
//        if (x >= m + 1) {
//            if (ok())
//                ans++;
//            return;
//        }
//        for (int i = 1; i <= n; i++) {
//            for (int j = 1; j <= n; j++) {
//                if (vis[i][j] != 0 || s[i][j] == 'X')
//                    continue; // X must not place
//                int lim = 1;
//                if (in[x] == 1)
//                    lim = 0;
//                for (int t = 0; t <= lim; t++) {
//                    if (judge(x, i, j, t)) { // can place
//                        addBattleship(i, j, x, t, 1);
//                        dfs(x + 1);
//                        addBattleship(i, j, x, t, -1);
//                    }
//                }
//            }
//        }
//    }


    /**
     * Performs a depth-first search to place all the battleships on the grid and counts the number of valid configurations.
     * Uses a backtracking algorithm to explore all the possible configurations of battleships.
     * Calls the checkCells() method to check if all the cells covered by the battleships are visited at least once.
     * Increments the totalNumOfWays counter for each valid configuration found.
     *
     * @param shipNum the index of the battleship being placed.
     */

    private static void getPossiblePlacementOfShips(int shipNum) {
        if (shipNum >= numberOfShips + 1) {
            if (checkCells())
                totalNumOfWays++;
            return;
        }
        IntStream.rangeClosed(1, gridSize)
                .boxed()
                .flatMap(i -> IntStream.rangeClosed(1, gridSize)
                        .filter(j -> visitedCells[i][j] == 0 && battleshipGrid[i][j] != 'X')
                        .mapToObj(j -> new int[]{i, j}))
                .forEach(coords -> {
                    int i = coords[0];
                    int j = coords[1];
                    int lim = shipSizesContainer[shipNum] == 1 ? 0 : 1;
                    ShipDto ship = new ShipDto(shipNum, i, j);
                    IntStream.rangeClosed(0, lim)
                            // .filter(t -> judge(shipNum, i, j, t))
                            .filter(t -> {
                                ship.setOrientation(t);
                                return checkShipPlacedPossibility(ship);
                            })
                            .forEach(t -> {
                                ship.setCellVal(1);
                                // addBattleship(i, j, shipNum, t, 1);
                                addBattleshipToGrid(ship);
                                getPossiblePlacementOfShips(shipNum + 1);
                                ship.setCellVal(-1);
                                // addBattleship(i, j, shipNum, t, -1);
                                addBattleshipToGrid(ship);
                            });
                });
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        gridSize = input.nextInt();
        numberOfShips = input.nextInt();
        input.nextLine();

        battleshipGrid = new char[gridSize + 1][gridSize + 1];
        visitedCells = new int[gridSize + 1][gridSize + 1];
        shipSizesContainer = new int[numberOfShips + 1];

        for (int i = 1; i <= gridSize; i++) {
            String line = input.nextLine();
            for (int j = 1; j <= gridSize; j++) {
                battleshipGrid[i][j] = line.charAt(j - 1);
            }
        }

        for (int i = 1; i <= numberOfShips; i++) {
            shipSizesContainer[i] = input.nextInt();
        }

      //  Arrays.sort(shipSizesContainer, 1, numberOfShips + 1);
        //  totalNumOfWays = 0;
        getPossiblePlacementOfShips(1);
        System.out.println(totalNumOfWays);
    }
}
