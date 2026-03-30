package org.algandsd;

import java.awt.*;
import java.util.Arrays;
import java.util.Deque;

public class TaskLogic {
    public static Deque<Point> solve (int[][] arr) {
        Deque<Point> pointsStack = new SimpleDeque<>();
//
//        for (int y = 0; y < arr.length; y++) {
//            for (int x = 0; x < arr[y].length; x++) {
//                if (arr[y][x] == 2) {
//                    pointsStack.push(new Point(x, y));
//                }
//            }
//        }
//
//        int x = pointsStack.peek().x;
//        int y = pointsStack.peek().y;
//
//        while (arr[y][x] != 3) {
//            if (x + 1 != arr[y].length) {
//                Point n= new Point(x + 1, y);
//                if (arr[y][x + 1] != 1 && !pointsStack.contains(n)) {
//                    pointsStack.push(n);
//                    x = pointsStack.peek().x;
//                    y = pointsStack.peek().y;
//                    continue;
//                }
//            }
//            if (x != 0) {
//                Point n = new Point(x - 1, y);
//                if (arr[y][x - 1] != 1 && !pointsStack.contains(n)) {
//                    pointsStack.push(n);
//                    x = pointsStack.peek().x;
//                    y = pointsStack.peek().y;
//                    continue;
//                }
//            }
//            if (y + 1 != arr.length) {
//                Point n = new Point(x, y + 1);
//                if (arr[y + 1][x] != 1 && !pointsStack.contains(n)) {
//                    pointsStack.push(n);
//                    x = pointsStack.peek().x;
//                    y = pointsStack.peek().y;
//                    continue;
//                }
//            }
//            if (y != 0) {
//                Point n = new Point(x, y - 1);
//                if (arr[y - 1][x] != 1 && !pointsStack.contains(n)) {
//                    pointsStack.push(n);
//                    x = pointsStack.peek().x;
//                    y = pointsStack.peek().y;
//                    continue;
//                }
//            }
//
//            x = pointsStack.peek().x;
//            y = pointsStack.peek().y;
//
//            arr[pointsStack.peek().y][pointsStack.pop().x] = 1;
//        }
//
//        System.out.println(Arrays.deepToString(arr));
//
        return pointsStack;
    }

    private static void check(int[][] arr, Deque<Point> points) {

    }
}