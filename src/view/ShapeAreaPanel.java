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

        // 加载图片
        ImageIcon rectIcon = new ImageIcon("src/resources/images/task3/RECTANGLE.png");
        ImageIcon paraIcon = new ImageIcon("src/resources/images/task3/PARALLELOGRAM.png");
        ImageIcon triIcon = new ImageIcon("src/resources/images/task3/TRANGLE.png");
        ImageIcon trapIcon = new ImageIcon("src/resources/images/task3/TRAPEZNM.png");
        // 缩放图片
        int maxDim = 240;
        rectIcon = scaleIconProportionally(rectIcon, maxDim);
        paraIcon = scaleIconProportionally(paraIcon, maxDim);
        triIcon = scaleIconProportionally(triIcon, maxDim);
        trapIcon = scaleIconProportionally(trapIcon, maxDim);

        JButton rectBtn = new JButton("Rectangle", rectIcon);
        JButton paraBtn = new JButton("Parallelogram", paraIcon);
        JButton triBtn = new JButton("Triangle", triIcon);
        JButton trapBtn = new JButton("Trapezium", trapIcon);
        rectBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        paraBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        triBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        trapBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        // 设置图片在文字上方
        rectBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        rectBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        paraBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        paraBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        triBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        triBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        trapBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        trapBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        // Rectangle按钮事件
        rectBtn.addActionListener(e -> {
            parentFrame.setContentPane(new RectangleAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        // Parallelogram按钮事件
        paraBtn.addActionListener(e -> {
            parentFrame.setContentPane(new ParallelogramAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        // Triangle按钮事件
        triBtn.addActionListener(e -> {
            parentFrame.setContentPane(new TriangleAreaQuestionPanel(parentFrame));
            parentFrame.revalidate();
        });
        // Trapezium按钮事件
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
        homeBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
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