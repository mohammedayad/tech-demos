package com.ayad.microservicedemo.exercises.problemsolving.battleshipgame;

public class ShipDto {
    private int pos;
    private int row;
    private int col;
    private int orientation;
    private int cellVal;

    public ShipDto(int pos) {
        this.pos = pos;
    }

    public ShipDto(int pos, int row, int col) {
        this.pos = pos;
        this.row = row;
        this.col = col;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getCellVal() {
        return cellVal;
    }

    public void setCellVal(int cellVal) {
        this.cellVal = cellVal;
    }
}
