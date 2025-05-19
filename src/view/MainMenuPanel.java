package src.view;

import javax.swing.*;
import java.awt.*;
import src.model.ScoreManager;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    public MainMenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(StyleUtil.BG_COLOR);
        JLabel titleLabel = new JLabel("Shaperville - Welcome to Geometry Learning!", SwingConstants.CENTER);
        titleLabel.setFont(StyleUtil.TITLE_FONT);
        titleLabel.setForeground(StyleUtil.MAIN_PURPLE);
        add(titleLabel, BorderLayout.NORTH);

        // KS1模块
        JPanel ks1Panel = new JPanel();
        ks1Panel.setLayout(new BoxLayout(ks1Panel, BoxLayout.Y_AXIS));
        ks1Panel.setBackground(StyleUtil.BG_COLOR);
        ks1Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_BLUE, 3, true), "KS1"));
        JButton btn2d = new JButton("2D Shape Identification");
        JButton btn3d = new JButton("3D Shape Identification");
        JButton btnAngle = new JButton("Angle Identification");
        StyleUtil.styleButton(btn2d, StyleUtil.MAIN_YELLOW, StyleUtil.MAIN_BLUE);
        StyleUtil.styleButton(btn3d, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        StyleUtil.styleButton(btnAngle, StyleUtil.MAIN_GREEN, Color.WHITE);
        ks1Panel.add(Box.createVerticalStrut(10));
        ks1Panel.add(btn2d);
        ks1Panel.add(Box.createVerticalStrut(10));
        ks1Panel.add(btn3d);
        ks1Panel.add(Box.createVerticalStrut(10));
        ks1Panel.add(btnAngle);
        ks1Panel.add(Box.createVerticalStrut(10));

        // KS2模块
        JPanel ks2Panel = new JPanel();
        ks2Panel.setLayout(new BoxLayout(ks2Panel, BoxLayout.Y_AXIS));
        ks2Panel.setBackground(StyleUtil.BG_COLOR);
        ks2Panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_GREEN, 3, true), "KS2"));
        JButton btnArea = new JButton("Area Calculation of Shapes");
        JButton btnCircle = new JButton("Circle Calculation");
        StyleUtil.styleButton(btnArea, StyleUtil.MAIN_YELLOW, StyleUtil.MAIN_BLUE);
        StyleUtil.styleButton(btnCircle, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        ks2Panel.add(Box.createVerticalStrut(20));
        ks2Panel.add(btnArea);
        ks2Panel.add(Box.createVerticalStrut(20));
        btnCircle.setEnabled(true);
        ks2Panel.add(btnCircle);
        ks2Panel.add(Box.createVerticalStrut(20));

        // Bonus模块
        JPanel bonusPanel = new JPanel();
        bonusPanel.setLayout(new BoxLayout(bonusPanel, BoxLayout.Y_AXIS));
        bonusPanel.setBackground(StyleUtil.BG_COLOR);
        bonusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(StyleUtil.MAIN_PURPLE, 3, true), "Bonus"));
        JButton btnBonus1 = new JButton("Compound Shape Challenge");
        JButton btnBonus2 = new JButton("Sector Area Challenge");
        StyleUtil.styleButton(btnBonus1, StyleUtil.MAIN_GREEN, Color.WHITE);
        StyleUtil.styleButton(btnBonus2, StyleUtil.MAIN_YELLOW, StyleUtil.MAIN_BLUE);
        btnBonus1.setEnabled(true);
        btnBonus2.setEnabled(true);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus1);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus2);
        bonusPanel.add(Box.createVerticalStrut(20));

        // 主体三栏
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(StyleUtil.BG_COLOR);
        mainPanel.add(ks1Panel);
        mainPanel.add(ks2Panel);
        mainPanel.add(bonusPanel);
        add(mainPanel, BorderLayout.CENTER);

        // 按钮事件
        btn2d.addActionListener(e -> {
            parentFrame.setContentPane(new ShapeIdentificationPanel(parentFrame, true));
            parentFrame.revalidate();
        });
        btn3d.addActionListener(e -> {
            parentFrame.setContentPane(new ShapeIdentificationPanel(parentFrame, false));
            parentFrame.revalidate();
        });
        btnAngle.addActionListener(e -> {
            parentFrame.setContentPane(new AngleIdentificationPanel(parentFrame));
            parentFrame.revalidate();
        });
        btnArea.addActionListener(e -> {
            parentFrame.setContentPane(new ShapeAreaPanel(parentFrame));
            parentFrame.revalidate();
        });
        btnCircle.addActionListener(e -> {
            parentFrame.setContentPane(new CircleCalculationPanel(parentFrame));
            parentFrame.revalidate();
        });
        btnBonus1.addActionListener(e -> {
            parentFrame.setContentPane(new CompoundShapeSelectionPanel(parentFrame, null));
            parentFrame.revalidate();
        });
        btnBonus2.addActionListener(e -> {
            parentFrame.setContentPane(new SectorSelectionPanel(parentFrame, null));
            parentFrame.revalidate();
        });

        // 底部按钮
        JPanel bottomPanel = new JPanel();
        JButton endSessionButton = new JButton("End session");
        StyleUtil.styleButton(endSessionButton, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        bottomPanel.setBackground(StyleUtil.BG_COLOR);
        bottomPanel.add(endSessionButton);
        add(bottomPanel, BorderLayout.SOUTH);
        endSessionButton.addActionListener(e -> {
            int totalScore = ScoreManager.getInstance().getTotalScore();
            JOptionPane.showMessageDialog(parentFrame, "You have achieved " + totalScore + " points in this session. Goodbye!", "Session Ended", JOptionPane.INFORMATION_MESSAGE);
            ScoreManager.getInstance().resetScore();
            System.exit(0);
        });
    }
} 