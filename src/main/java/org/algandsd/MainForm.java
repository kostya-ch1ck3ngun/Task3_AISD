package org.algandsd;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MainForm extends JFrame{
    private JPanel mainPanel;
    private JTextField inputField;
    private JButton inputButton;
    private JTextField outputField;
    private JButton outputButton;
    private JSpinner rowsSpinner;
    private JSpinner columnsSpinner;
    private JButton resultButton;
    private JTable table;
    private JButton clearButton;
    private JScrollPane pane;
    private JLabel pointsLabel;
    private DefaultTableModel tableModel;
    private Point bluePosition = null;  // Позиция синего элемента
    private Point redPosition = null;   // Позиция красного элемента


    public MainForm() {
        super("Таск 3   ");
        setVisible(true);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(1000, 600);
        columnsSpinner.setValue(5);
        rowsSpinner.setValue(5);

        createGrid();

        setLocationRelativeTo(null);

        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile("src/main/java/org/algandsd/" + inputField.getText());
            }
        });
        resultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Deque<Point> points = TaskLogic.solve(getTableDataAsArray());
                fillFromPointQueue(points);
            }
        });
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseAndWritePoints(pointsLabel.getText(), outputField.getText());
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllGreenCells();
            }
        });
        rowsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                createGrid();
            }
        });
        columnsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                createGrid();
            }
        });
    }

    public void parseAndWritePoints(String solutionString, String filePath) {
        List<Point> points = new ArrayList<>();

        // Удаляем префикс «Точки решения: »
        if (solutionString.startsWith("Точки решения: ")) {
            solutionString = solutionString.substring("Точки решения: ".length());
        }

        // Используем регулярное выражение для поиска всех пар (x, y)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\((\\d+),\\s*(\\d+)\\)");
        java.util.regex.Matcher matcher = pattern.matcher(solutionString);

        while (matcher.find()) {
            try {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                points.add(new Point(x, y));
            } catch (NumberFormatException e) {
                System.err.println("Некорректный формат координат: " + matcher.group());
                return;
            }
        }

        // Записываем извлечённые точки в файл
        writePointsToFile(points, filePath);
    }

    public void writePointsToFile(List<Point> points, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/org/algandsd/" + filePath))) {
            for (Point point : points) {
                // Записываем координаты точки: x и y через пробел, затем переход на новую строку
                writer.write(point.x + " " + point.y);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл " + filePath + ": " + e.getMessage());
        }
    }


    public void clearAllGreenCells() {
        if (table == null || tableModel == null) {
            return;
        }

        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object value = tableModel.getValueAt(i, j);
                int cellValue = (value == null) ? 0 : (Integer) value;

                if (cellValue == 4) { // Если ячейка зелёная
                    tableModel.setValueAt(0, i, j); // Очищаем
                }
            }
        }
    }

    public void fillFromPointQueue(Deque<Point> queue) {
        if (table == null || tableModel == null) return;
        pointsLabel.setText("Точки решения:");

        while (!queue.isEmpty()) {
            Point point = queue.pop();
            int row = point.x;
            int col = point.y;

            fillCellGreen(row, col); // Зелёный цвет (значение 4)
            pointsLabel.setText(pointsLabel.getText() + " (" + row + ", " + col + ")");
        }
    }

    public void fillCellGreen(int col, int row) {
        // Проверяем инициализацию таблицы и модели
        if (table == null || tableModel == null) {
            System.err.println("Ошибка: Таблица не инициализирована");
            return;
        }

        int maxRow = tableModel.getRowCount() - 1;
        int maxCol = tableModel.getColumnCount() - 1;

        // Проверка границ: индексы должны быть неотрицательными и не превышать размеры сетки
        if (row < 0 || row > maxRow || col < 0 || col > maxCol) {
            System.err.println("Ошибка: Координаты (" + row + "," + col + ") вне границ сетки " +
                    "(" + (maxRow + 1) + "×" + (maxCol + 1) + ")");
            return;
        }

        if (((int) tableModel.getValueAt(row, col)) == 2 || ((int) tableModel.getValueAt(row, col)) == 3) {
            return;
        }
        // Устанавливаем значение 4 (зелёный цвет) в указанную ячейку
        tableModel.setValueAt(4, row, col);
    }

    public int[][] getTableDataAsArray() {
        // Проверяем инициализацию таблицы и модели
        if (table == null || tableModel == null) {
            return new int[0][0];
        }

        int rows = tableModel.getRowCount();
        int cols = tableModel.getColumnCount();

        // Создаём массив нужного размера
        int[][] data = new int[rows][cols];

        // Заполняем массив данными из таблицы
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Object value = tableModel.getValueAt(i, j);
                // Обрабатываем случай null-значений
                data[i][j] = (value == null) ? 0 : (Integer) value;
            }
        }

        return data;
    }

    private void createGrid() {
        int rows = (Integer) rowsSpinner.getValue();
        int cols = (Integer) columnsSpinner.getValue();

        int[][] oldData = null;
        if (tableModel != null) {
            oldData = getTableDataAsArray();
        }

        bluePosition = null;
        redPosition = null;

        tableModel = new DefaultTableModel(rows, cols);

        if (oldData != null) {
            int minRows = Math.min(rows, oldData.length);
            int minCols = Math.min(cols, oldData[0].length);

            for (int i = 0; i < minRows; i++) {
                for (int j = 0; j < minCols; j++) {
                    tableModel.setValueAt(oldData[i][j], i, j);
                }
            }
        }

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ячейки не редактируются напрямую
            }
        };

        // Настройка размеров ячеек
        table.setRowHeight(30);
        for (int i = 0; i < cols; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }

        // Установка рендерера для ячеек
        TableCellRenderer renderer = new ColorCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);

        // Добавление обработчика мыши
        table.addMouseListener(new GridMouseListener());

        // Замена панели с таблицей
        if (pane == null) {
            // Первый раз — создаём JScrollPane
            pane = new JScrollPane(table);
            add(pane, BorderLayout.CENTER);
        } else {
            // Уже есть — просто заменяем таблицу внутри
            pane.setViewportView(table);
        }
        revalidate();
        repaint();
    }

    // Рендерер для отображения цветов вместо значений
    private class ColorCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            int cellValue = value == null ? 0 : (Integer) value;
            Color cellColor = getColorForValue(cellValue);
            c.setBackground(cellColor);
            setText(""); // Скрываем значения

            return c;
        }
    }

    // Получение цвета для значения ячейки
    private Color getColorForValue(int value) {
        return switch (value) {
            case 1 -> Color.BLACK;
            case 2 -> Color.BLUE;
            case 3 -> Color.RED;
            case 4 -> Color.GREEN;
            default -> Color.WHITE;
        };
    }

    private class GridMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (table == null) return;

            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());

            if (row == -1 || col == -1) return;

            Object currentValueObj = tableModel.getValueAt(row, col);
            int currentValue = currentValueObj == null ? 0 : (Integer) currentValueObj;
            int newValue;

            if (SwingUtilities.isLeftMouseButton(e)) {
                // Левая кнопка: 0→1, 1→0, 2→0, 3→0
                switch (currentValue) {
                    case 0: newValue = 1; break;
                    case 2: newValue = 0; clearBlue(); break;
                    case 3: newValue = 0; clearRed(); break;
                    default: newValue = 0; break;
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                // Правая кнопка: логика с ограничениями
                if (currentValue == 2) {
                    // Если уже синий — меняем на красный, обновляем позиции
                    newValue = 3;
                    clearBlue();
                    setRed(row, col);
                } else if (currentValue == 3) {
                    // Если уже красный — меняем на синий, обновляем позиции
                    newValue = 2;
                    clearRed();
                    setBlue(row, col);
                } else {
                    if (bluePosition == null) {
                        // Если синего ещё нет — ставим синий
                        newValue = 2;
                        setBlue(row, col);
                    } else if (redPosition == null) {
                        // Если синий есть, но красного нет — ставим красный
                        newValue = 3;
                        setRed(row, col);
                    } else {
                        // Оба цвета уже есть — просто меняем текущий на синий (заменяем красный)
                        newValue = 2;
                        setBlue(row, col);
                    }
                }
            } else {
                return; // Игнорируем другие кнопки
            }

            tableModel.setValueAt(newValue, row, col);
        }
    }

    // Устанавливаем позицию синего и очищаем старый синий, если он был
    private void setBlue(int row, int col) {
        if (bluePosition != null &&
                (bluePosition.x != row || bluePosition.y != col)) {
            // Очищаем старый синий, если это не та же ячейка
            tableModel.setValueAt(0, bluePosition.x, bluePosition.y);
        }
        bluePosition = new Point(row, col);
    }

    // Устанавливаем позицию красного и очищаем старый красный, если он был
    private void setRed(int row, int col) {
        if (redPosition != null &&
                (redPosition.x != row || redPosition.y != col)) {
            // Очищаем старый красный, если это не та же ячейка
            tableModel.setValueAt(0, redPosition.x, redPosition.y);
        }
        redPosition = new Point(row, col);
    }

    // Очищаем позицию синего
    private void clearBlue() {
        bluePosition = null;
    }

    // Очищаем позицию красного
    private void clearRed() {
        redPosition = null;
    }

    private void loadFromFile(String filePath) {
        GridData gridData = readFileData(filePath);

        if (gridData != null && gridData.getRows() > 0 && gridData.getCols() > 0) {
            // Обновляем значения спиннеров
            rowsSpinner.setValue(gridData.getRows());
            columnsSpinner.setValue(gridData.getCols());

            // Создаём сетку с новыми размерами
            createGrid();

            // Заполняем данными
            fillGridWithData(gridData);
        }
    }

    // Чтение данных из файла
    private GridData readFileData(String filePath) {
        GridData gridData = new GridData();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                List<Integer> row = new ArrayList<>();

                for (String valueStr : values) {
                    try {
                        int value = Integer.parseInt(valueStr);
                        if (value >= 0 && value <= 4) {
                            row.add(value);
                        }
                    } catch (NumberFormatException e) {
                        // Пропускаем некорректные значения
                    }
                }

                if (!row.isEmpty()) {
                    gridData.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка чтения файла '" + filePath + "': " + e.getMessage(),
                    "Ошибка загрузки", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return gridData;
    }

    private void fillGridWithData(GridData gridData) {
        bluePosition = null;
        redPosition = null;

        List<List<Integer>> data = gridData.getData();
        int maxRows = Math.min(data.size(), tableModel.getRowCount());
        int maxCols = Math.min(gridData.getCols(), tableModel.getColumnCount());

        for (int r = 0; r < maxRows; r++) {
            List<Integer> rowData = data.get(r);
            int rowSize = Math.min(rowData.size(), maxCols);

            for (int c = 0; c < rowSize; c++) {
                int value = rowData.get(c);
                tableModel.setValueAt(value, r, c);

                if (value == 2) {
                    bluePosition = new Point(r, c);
                } else if (value == 3) {
                    redPosition = new Point(r, c);
                }
            }
        }
    }

    public class GridData {
        private List<List<Integer>> data;
        private int rows;
        private int cols;

        public GridData() {
            this.data = new ArrayList<>();
            this.rows = 0;
            this.cols = 0;
        }

        public void addRow(List<Integer> row) {
            data.add(row);
            rows++;
            cols = Math.max(cols, row.size());
        }

        public List<List<Integer>> getData() { return data; }
        public int getRows() { return rows; }
        public int getCols() { return cols; }
    }
}
