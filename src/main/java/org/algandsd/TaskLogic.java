package org.algandsd;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class TaskLogic {
    private final static int[][] directions = {
            {0, 1},
            {1, 0},
            {-1, 0},
            {0, -1}
    };

    public static Deque<Point> solve (int[][] arr) {
        Deque<Point> pointsStack = new SimpleDeque<>();
        Set<Point> visitedPoints = new HashSet<>();

        findStart(arr, pointsStack, visitedPoints);

        while (!pointsStack.isEmpty()) {
            check(arr, pointsStack, visitedPoints);

            assert pointsStack.peekLast() != null;
            if (arr[pointsStack.peekLast().getY()][pointsStack.peekLast().getX()] == 3) {
                break;
            }
        }

        assert pointsStack.peekLast() != null;
        return getResult(pointsStack.peekLast());
    }

    private static void findStart (int[][] arr, Deque<Point> pointsStack, Set<Point> visitedPoints) {
        int rows = arr.length;
        int cols = arr[0].length;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (arr[y][x] == 2) {
                    Point point = new Point(x, y);
                    pointsStack.addLast(point);
                    visitedPoints.add(point);
                }
            }
        }
    }

    private static void check(int[][] arr, Deque<Point> pointsStack, Set<Point> visitedPoints) {
        Point point = pointsStack.pollFirst();

        for (int[] direction : directions) {
            Point temp = new Point(point.getX() + direction[0], point.getY() + direction[1]);
            temp.setParent(point);

            if (temp.getX() >= 0 && temp.getX() < arr[0].length && temp.getY() >= 0 && temp.getY() < arr.length) {
                if (!visitedPoints.contains(temp) && arr[temp.getY()][temp.getX()] != 1) {
                    pointsStack.addLast(temp);
                    visitedPoints.add(temp);

                    if (arr[temp.getY()][temp.getX()] == 3) {
                        break;
                    }
                }
            }
        }
    }

    private static Deque<Point> getResult (Point point) {
        Deque<Point> res = new SimpleDeque<>();

        while (point.getParent() != null) {
            res.push(point);
            point = point.getParent();
        }
        res.push(point);

        return res;
    }

}