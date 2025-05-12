package src.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ShapeAreaPanel extends JPanel {
    private JFrame parentFrame;
    private Set<String> practicedShapes = new HashSet<>();

    public ShapeAreaPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));

        // 标题
        JLabel titleLabel = new JLabel("Area Calculation of Shapes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // 图形选择区
        JPanel shapeSelectPanel = new JPanel();
        shapeSelectPanel.setLayout(new GridLayout(2, 2, 30, 30));
        shapeSelectPanel.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        JButton rectBtn = new JButton("Rectangle");
        JButton paraBtn = new JButton("Parallelogram");
        JButton triBtn = new JButton("Triangle");
        JButton trapBtn = new JButton("Trapezium");
        rectBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        paraBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        triBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        trapBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        // Rectangle按钮事件
        rectBtn.addActionListener(e -> {
            parentFrame.setContentPane(new RectangleAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        shapeSelectPanel.add(rectBtn);
        shapeSelectPanel.add(paraBtn);
        shapeSelectPanel.add(triBtn);
        shapeSelectPanel.add(trapBtn);
        add(shapeSelectPanel, BorderLayout.CENTER);

        // Home按钮
        JButton homeBtn = new JButton("Home");
        homeBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
} 