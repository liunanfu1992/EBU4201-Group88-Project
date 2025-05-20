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
        btn2d.setFont(new Font("Comic Sans MS", Font.BOLD, 21));
        StyleUtil.styleButton(btn3d, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        btn3d.setFont(new Font("Comic Sans MS", Font.BOLD, 21));
        StyleUtil.styleButton(btnAngle, StyleUtil.MAIN_GREEN, Color.WHITE);
        btnAngle.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
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
        btnArea.setFont(new Font("Comic Sans MS", Font.BOLD, 19));
        StyleUtil.styleButton(btnCircle, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        btnCircle.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
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
        btnBonus1.setFont(new Font("Comic Sans MS", Font.BOLD, 19));
        StyleUtil.styleButton(btnBonus2, StyleUtil.MAIN_YELLOW, StyleUtil.MAIN_BLUE);
        btnBonus2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        btnBonus1.setEnabled(true);
        btnBonus2.setEnabled(true);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus1);
        bonusPanel.add(Box.createVerticalStrut(20));
        bonusPanel.add(btnBonus2);
        bonusPanel.add(Box.createVerticalStrut(20));

        // 主体三栏（缩小高度）
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // 上下边距缩小
        mainPanel.setBackground(StyleUtil.BG_COLOR);
        mainPanel.add(ks1Panel);
        mainPanel.add(ks2Panel);
        mainPanel.add(bonusPanel);
        add(mainPanel, BorderLayout.CENTER);

        // 进度条面板
        JPanel progressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                int barH = 28;
                int barY = 32;
                int segW = w / 6;
                // 判定各任务完成
                boolean t1_2d = src.view.ShapeIdentificationPanel.is2DCompleted();
                boolean t1_3d = src.view.ShapeIdentificationPanel.is3DCompleted();
                boolean t2 = src.view.AngleIdentificationPanel.isCompleted();
                boolean t3 = src.view.ShapeAreaPanel.isAllCompleted();
                boolean t4 = src.view.CircleCalculationPanel.isAllCompleted();
                boolean b1 = src.view.CompoundShapeSelectionPanel.isAllCompleted();
                boolean b2 = src.view.SectorSelectionPanel.isAllCompleted();
                // 画6段
                Color doneColor = StyleUtil.MAIN_GREEN;
                Color halfColor = StyleUtil.MAIN_YELLOW;
                Color notColor = new Color(220,220,220);
                for (int i = 0; i < 6; i++) {
                    int x = i * segW + 8;
                    int y = barY;
                    int w0 = segW - 16;
                    // 2D/3D特殊处理
                    if (i == 0) {
                        // 2D/3D共用一格
                        g2.setColor(notColor);
                        g2.fillRoundRect(x, y, w0, barH, 16, 16);
                        if (t1_2d && t1_3d) {
                            g2.setColor(doneColor);
                            g2.fillRoundRect(x, y, w0, barH, 16, 16);
                        } else if (t1_2d || t1_3d) {
                            g2.setColor(halfColor);
                            g2.fillRoundRect(x, y, w0/2, barH, 16, 16);
                        }
                    } else {
                        boolean done = false;
                        switch(i) {
                            case 1: done = t2; break;
                            case 2: done = t3; break;
                            case 3: done = t4; break;
                            case 4: done = b1; break;
                            case 5: done = b2; break;
                        }
                        g2.setColor(done ? doneColor : notColor);
                        g2.fillRoundRect(x, y, w0, barH, 16, 16);
                    }
                    // 边框
                    g2.setColor(StyleUtil.MAIN_PURPLE);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRoundRect(x, y, w0, barH, 16, 16);
                }
                // 英文标签
                String[] labels = {"Task1-2D/3D", "Task2", "Task3", "Task4", "Bonus1", "Bonus2"};
                for (int i = 0; i < 6; i++) {
                    int x = i * segW + 8;
                    g2.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
                    g2.setColor(StyleUtil.MAIN_PURPLE);
                    g2.drawString(labels[i], x + 8, 24);
                }
            }
        };
        progressPanel.setPreferredSize(new Dimension(700, 70));
        progressPanel.setOpaque(false);

        // 底部面板（进度条+按钮）
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(StyleUtil.BG_COLOR);
        bottomPanel.add(progressPanel);
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(StyleUtil.BG_COLOR);
        JButton endSessionButton = new JButton("End session");
        StyleUtil.styleButton(endSessionButton, StyleUtil.MAIN_PINK, StyleUtil.MAIN_PURPLE);
        btnPanel.add(endSessionButton);
        bottomPanel.add(btnPanel);
        add(bottomPanel, BorderLayout.SOUTH);
        endSessionButton.addActionListener(e -> {
            int totalScore = ScoreManager.getInstance().getTotalScore();
            JOptionPane.showMessageDialog(parentFrame, "You have achieved " + totalScore + " points in this session. Goodbye!", "Session Ended", JOptionPane.INFORMATION_MESSAGE);
            ScoreManager.getInstance().resetScore();
            System.exit(0);
        });

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
    }
} 