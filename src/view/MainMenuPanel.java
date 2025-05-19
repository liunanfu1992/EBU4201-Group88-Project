package src.view;

import javax.swing.*;
import java.awt.*;
import src.model.ScoreManager;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    public MainMenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Shaperville - Welcome to Geometry Learning!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        // KS1模块
        JPanel ks1Panel = new JPanel();
        ks1Panel.setLayout(new BoxLayout(ks1Panel, BoxLayout.Y_AXIS));
        ks1Panel.setBorder(BorderFactory.createTitledBorder("KS1"));
        JButton btn2d = new JButton("2D Shape Identification");
        JButton btn3d = new JButton("3D Shape Identification");
        JButton btnAngle = new JButton("Angle Identification");
        btn2d.setFont(new Font("Arial", Font.PLAIN, 18));
        btn3d.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAngle.setFont(new Font("Arial", Font.PLAIN, 18));
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
        ks2Panel.setBorder(BorderFactory.createTitledBorder("KS2"));
        JButton btnArea = new JButton("Area Calculation of Shapes");
        btnArea.setFont(new Font("Arial", Font.PLAIN, 18));
        ks2Panel.add(Box.createVerticalStrut(20));
        ks2Panel.add(btnArea);
        ks2Panel.add(Box.createVerticalStrut(20));
        // 预留Circle Calculation按钮
        JButton btnCircle = new JButton("Circle Calculation");
        btnCircle.setFont(new Font("Arial", Font.PLAIN, 18));
        btnCircle.setEnabled(true);
        ks2Panel.add(btnCircle);
        ks2Panel.add(Box.createVerticalStrut(20));

        // Bonus模块
        JPanel bonusPanel = new JPanel();
        bonusPanel.setLayout(new BoxLayout(bonusPanel, BoxLayout.Y_AXIS));
        bonusPanel.setBorder(BorderFactory.createTitledBorder("Bonus"));
        JButton btnBonus1 = new JButton("Compound Shape Challenge");
        btnBonus1.setFont(new Font("Arial", Font.PLAIN, 18));
        btnBonus1.setEnabled(true);
        JButton btnBonus2 = new JButton("Sector Area Challenge");
        btnBonus2.setFont(new Font("Arial", Font.PLAIN, 18));
        btnBonus2.setEnabled(true);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus1);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus2);
        bonusPanel.add(Box.createVerticalStrut(20));

        // 主体三栏
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
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
        JButton homeButton = new JButton("Home");
        endSessionButton.setFont(new Font("Arial", Font.PLAIN, 16));
        homeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(endSessionButton);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);
        endSessionButton.addActionListener(e -> {
            int totalScore = ScoreManager.getInstance().getTotalScore();
            JOptionPane.showMessageDialog(parentFrame, "You have achieved " + totalScore + " points in this session. Goodbye!", "Session Ended", JOptionPane.INFORMATION_MESSAGE);
            ScoreManager.getInstance().resetScore();
            System.exit(0);
        });
        homeButton.addActionListener(e -> {
            // Already at home menu, do nothing
        });
    }
} 