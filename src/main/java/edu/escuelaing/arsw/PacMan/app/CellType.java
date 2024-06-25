package edu.escuelaing.arsw.PacMan.app;

public enum CellType {
    BLUE("0", "\u001B[34m"),     // Azul
    PURPLE("*", "\u001B[35m"),   // Morado
    BLACK("-", "\u001B[30m");    // Negro

    private final String symbol;
    private final String colorCode;

    CellType(String symbol, String colorCode) {
        this.symbol = symbol;
        this.colorCode = colorCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColorCode() {
        return colorCode;
    }
}
