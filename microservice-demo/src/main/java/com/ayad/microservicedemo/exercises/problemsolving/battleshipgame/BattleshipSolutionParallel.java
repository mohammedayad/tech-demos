package com.ayad.microservicedemo.exercises.problemsolving.battleshipgame;

import java.util.Scanner;
import java.util.stream.IntStream;

public class BattleshipSolutionParallel {
    private char[][] battleshipGrid;
    private int[] shipSizesContainer;
    private int gridSize, numberOfShips;
    private int totalNumOfWays;
    private int[][] visitedCells;


    /**
     * Checks if all the cells covered by the battleships are visited at least once.
     *
     * @return {@code true} if all the cells covered by the battleships are visited at least once, {@code false} otherwise.
     */

    private boolean checkCells() {
        return IntStream.rangeClosed(1, gridSize)
                .flatMap(i -> IntStream.rangeClosed(1, gridSize)
                        .filter(j -> battleshipGrid[i][j] == 'O')
                        .map(j -> visitedCells[i][j]))
                .allMatch(count -> count > 0);
    }


    /**
     * Checks if a battleship described by the given {@code ShipDto} object can be placed on the grid.
     * Checks if a battleship of a given size can be placed at the specified row and column with the given orientation.
     *
     * @param ship the {@code ShipDto} object that describes the battleship.
     * @return {@code true} if the battleship can be placed on the grid, {@code false} otherwise.
     */

    private boolean checkShipPlacedPossibility(ShipDto ship) {
        int start = ship.getOrientation() == 0 ? ship.getCol() : ship.getRow();
        int end = start + shipSizesContainer[ship.getPos()];
        return end <= gridSize + 1 && IntStream.range(start, end)
                .allMatch(i -> (ship.getOrientation() == 0 ? visitedCells[ship.getRow()][i] : visitedCells[i][ship.getCol()]) == 0 &&
                        (ship.getOrientation() == 0 ? battleshipGrid[ship.getRow()][i] : battleshipGrid[i][ship.getCol()]) != 'X');
    }


    /**
     * Adds a battleship described by the given {@code ShipDto} object to the grid.
     *
     * @param ship the {@code ShipDto} object that describes the battleship.
     */
    private void addBattleshipToGrid(ShipDto ship) {
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


    /**
     * Performs a depth-first search to place all the battleships on the grid and counts the number of valid configurations.
     * Uses a backtracking algorithm to explore all the possible configurations of battleships.
     * Calls the checkCells() method to check if all the cells covered by the battleships are visited at least once.
     * Increments the totalNumOfWays counter for each valid configuration found.
     *
     * @param shipNum the index of the battleship being placed.
     */

    private void getPossiblePlacementOfShips(int shipNum) {
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
                .parallel()
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
        BattleshipSolutionParallel battleshipSolutionParallel = new BattleshipSolutionParallel();
        Scanner input = new Scanner(System.in);
        battleshipSolutionParallel.gridSize = input.nextInt();
        battleshipSolutionParallel.numberOfShips = input.nextInt();
        input.nextLine();

        battleshipSolutionParallel.battleshipGrid = new char[battleshipSolutionParallel.gridSize + 1][battleshipSolutionParallel.gridSize + 1];
        battleshipSolutionParallel.visitedCells = new int[battleshipSolutionParallel.gridSize + 1][battleshipSolutionParallel.gridSize + 1];
        battleshipSolutionParallel.shipSizesContainer = new int[battleshipSolutionParallel.numberOfShips + 1];

        for (int i = 1; i <= battleshipSolutionParallel.gridSize; i++) {
            String line = input.nextLine();
            for (int j = 1; j <= battleshipSolutionParallel.gridSize; j++) {
                battleshipSolutionParallel.battleshipGrid[i][j] = line.charAt(j - 1);
            }
        }

        for (int i = 1; i <= battleshipSolutionParallel.numberOfShips; i++) {
            battleshipSolutionParallel.shipSizesContainer[i] = input.nextInt();
        }

        //  Arrays.sort(shipSizesContainer, 1, numberOfShips + 1);
        //  totalNumOfWays = 0;
        battleshipSolutionParallel.getPossiblePlacementOfShips(1);
        System.out.println(battleshipSolutionParallel.totalNumOfWays);
    }
}

