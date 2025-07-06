package com.example.mfaella.physicsapp;

public class Coordinates {

    public static Box simulationSize;
    public static float scaleX;
    public static float scaleY;
    public static int gameWidth;
    public static int gameHeight;

    public static float pixelsToMetersLengthsX(float pixels) {
        float simX1 = Coordinates.toSimulationX(0); // Mondo a coordinate pixel 0
        float simX2 = Coordinates.toSimulationX(pixels); // Mondo a coordinate pixel X
        return simX2 - simX1; // Differenza in unità del mondo
    }

    public static float pixelsToMetersLengthsY(float pixels) {
        float simY1 = Coordinates.toSimulationY(0); // Mondo a coordinate pixel 0
        float simY2 = Coordinates.toSimulationY(pixels); // Mondo a coordinate pixel Y
        return simY2 - simY1; // Differenza in unità del mondo
    }

    public static float metersToPixelsLengthX(float meters) {
        return meters * (gameWidth / simulationSize.width);
    }

    public static float metersToPixelsLengthY(float meters) {
        return meters * (gameHeight / simulationSize.height);
    }

    public static float toPixelsX(float x) {
        return (x - simulationSize.xmin) / simulationSize.width * gameWidth;
    }

    public static float toPixelsY(float y) {
        return (y - simulationSize.ymin) / simulationSize.height * gameHeight;
    }

    public static float toSimulationX(float px) {
        return px / gameWidth * simulationSize.width + simulationSize.xmin;
    }

    public static float toSimulationY(float py) {
        return py / gameHeight * simulationSize.height + simulationSize.ymin;
    }


    public static boolean isInRadius(float x, float y, float cX, float cY, float r)
    {
        float dx = x - cX;
        float dy = y - cY;
        return dx * dx + dy * dy <= r * r;
    }



    public static int rotateX(int px, int py, int pivotX, int pivotY, double angle) {
        double radian = Math.toRadians(angle); // Conversione dell'angolo in radianti
        double cosTheta = Math.cos(radian);
        double sinTheta = Math.sin(radian);

        // Traslazione del punto rispetto al pivot (spostiamo il pivot a (0, 0))
        double relativeX = px - pivotX;
        double relativeY = py - pivotY;

        // Applicare la rotazione sulla coordinata X
        return (int) (pivotX + relativeX * cosTheta - relativeY * sinTheta);
    }

    public static int rotateY(int px, int py, int pivotX, int pivotY, double angle) {
        double radian = Math.toRadians(angle); // Conversione dell'angolo in radianti
        double cosTheta = Math.cos(radian);
        double sinTheta = Math.sin(radian);

        // Traslazione del punto rispetto al pivot (spostiamo il pivot a (0, 0))
        double relativeX = px - pivotX;
        double relativeY = py - pivotY;

        // Applicare la rotazione sulla coordinata Y
        return (int) (pivotY + relativeX * sinTheta + relativeY * cosTheta);
    }


}
