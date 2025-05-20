package src.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ShapeAreaPanel extends JPanel {
    private JFrame parentFrame;
    private static Set<String> completedShapes = new HashSet<>();

    public ShapeAreaPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        StyleUtil.stylePanel(this);

        // 标题
        JLabel titleLabel = new JLabel("Area Calculation of Shapes", SwingConstants.CENTER);
        StyleUtil.styleLabel(titleLabel, StyleUtil.BIG_FONT, StyleUtil.MAIN_BLUE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(StyleUtil.MAIN_YELLOW);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        add(titleLabel, BorderLayout.NORTH);

        // 图形选择区
        JPanel shapeSelectPanel = new JPanel();
        shapeSelectPanel.setLayout(new GridLayout(2, 2, 30, 30));
        shapeSelectPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        StyleUtil.stylePanel(shapeSelectPanel);

        // 加载图片
        ImageIcon rectIcon = new ImageIcon("src/resources/images/task3/RECTANGLE.png");
        ImageIcon paraIcon = new ImageIcon("src/resources/images/task3/PARALLELOGRAM.png");
        ImageIcon triIcon = new ImageIcon("src/resources/images/task3/TRANGLE.png");
        ImageIcon trapIcon = new ImageIcon("src/resources/images/task3/TRAPEZNM.png");
        // 缩放图片
        int maxDim = 180;
        rectIcon = scaleIconProportionally(rectIcon, maxDim);
        paraIcon = scaleIconProportionally(paraIcon, maxDim);
        triIcon = scaleIconProportionally(triIcon, maxDim);
        trapIcon = scaleIconProportionally(trapIcon, maxDim);

        JButton rectBtn = new JButton("Rectangle", rectIcon);
        JButton paraBtn = new JButton("Parallelogram", paraIcon);
        JButton triBtn = new JButton("Triangle", triIcon);
        JButton trapBtn = new JButton("Trapezium", trapIcon);
        JButton[] btns = {rectBtn, paraBtn, triBtn, trapBtn};
        String[] names = {"Rectangle", "Parallelogram", "Triangle", "Trapezium"};
        for (int i = 0; i < btns.length; i++) {
            StyleUtil.styleButton(btns[i], StyleUtil.MAIN_GREEN, Color.BLACK);
            btns[i].setFont(StyleUtil.BIG_FONT);
            btns[i].setHorizontalTextPosition(SwingConstants.CENTER);
            btns[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            btns[i].setBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_BLUE, 3, true));
            btns[i].setFocusPainted(false);
            btns[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            btns[i].setPreferredSize(new Dimension(200, 220));
            btns[i].setBackground(StyleUtil.MAIN_YELLOW);
            btns[i].setOpaque(true);
            if (completedShapes.contains(names[i])) {
                btns[i].setEnabled(false);
                btns[i].setText(names[i] + " (Completed)");
                btns[i].setBackground(new Color(220, 220, 220));
                btns[i].setForeground(Color.GRAY);
            }
        }
        // 事件
        rectBtn.addActionListener(e -> {
            parentFrame.setContentPane(new RectangleAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        paraBtn.addActionListener(e -> {
            parentFrame.setContentPane(new ParallelogramAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        triBtn.addActionListener(e -> {
            parentFrame.setContentPane(new TriangleAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        trapBtn.addActionListener(e -> {
            parentFrame.setContentPane(new TrapeziumAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        shapeSelectPanel.add(rectBtn);
        shapeSelectPanel.add(paraBtn);
        shapeSelectPanel.add(triBtn);
        shapeSelectPanel.add(trapBtn);
        add(shapeSelectPanel, BorderLayout.CENTER);

        // Home按钮
        JButton homeBtn = new JButton("Home");
        StyleUtil.styleButton(homeBtn, StyleUtil.MAIN_YELLOW, Color.BLACK);
        homeBtn.setFont(StyleUtil.NORMAL_FONT);
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        JPanel bottomPanel = new JPanel();
        StyleUtil.stylePanel(bottomPanel);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void markShapeAsCompleted(String shapeType) {
        completedShapes.add(shapeType);
    }

    public static boolean isAllCompleted() {
        return completedShapes.contains("Rectangle") && completedShapes.contains("Parallelogram") && completedShapes.contains("Triangle") && completedShapes.contains("Trapezium");
    }

    // 等比例缩放图片，最大边为maxDim
    private ImageIcon scaleIconProportionally(ImageIcon icon, int maxDim) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        double scale = 1.0 * maxDim / Math.max(w, h);
        int newW = (int)(w * scale);
        int newH = (int)(h * scale);
        Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}