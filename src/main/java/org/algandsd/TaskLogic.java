package org.algandsd;

import java.awt.*;
import java.util.Deque;

public class TaskLogic {
    public static Deque<Point> solve (int[][] arr) {
        Deque<Point> pointsStack = new SimpleDeque<>();

        int rows = arr.length;
        int cols = arr[0].length;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (arr[y][x] == 2) {
                    pointsStack.push(new Point(x, y));
                }
            }
        }

        Point point = pointsStack.peek();

        while (arr[point.y][point.x] != 3) {
            point = check(arr, pointsStack);
        }

        return pointsStack;
    }

    private static Point check(int[][] arr, Deque<Point> pointsStack) {
        Point point = pointsStack.peek();
        int x = point.x;
        int y = point.y;
        int rows = arr.length;
        int cols = arr[0].length;

        if (x != cols - 1) {
            Point temp = new Point(x + 1, y);

            if (arr[y][x + 1] != 1 && !pointsStack.contains(temp)) {
                pointsStack.push(temp);
                return pointsStack.peek();
            }
        }

        if (x != 0) {
            Point temp = new Point(x - 1, y);

            if (arr[y][x - 1] != 1 && !pointsStack.contains(temp)) {
                pointsStack.push(temp);
                return pointsStack.peek();
            }
        }

        if (y + 1 != rows) {
            Point temp = new Point(x, y + 1);

            if (arr[y + 1][x] != 1 && !pointsStack.contains(temp)) {
                pointsStack.push(temp);
                return pointsStack.peek();
            }
        }

        if (y != 0) {
            Point temp = new Point(x, y - 1);

            if (arr[y - 1][x] != 1 && !pointsStack.contains(temp)) {
                pointsStack.push(temp);
                return pointsStack.peek();
            }
        }

        arr[pointsStack.peek().y][pointsStack.pop().x] = 1;

        return pointsStack.peek();
    }
}