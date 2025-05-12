package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private JFrame parentFrame;
    private JProgressBar progressBar;
    private JButton endSessionButton;
    private JButton homeButton;

    public MainMenuPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Shaperville - Welcome to Geometry Learning!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        // Level selection
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(3, 1, 10, 10));
        levelPanel.setBorder(BorderFactory.createTitledBorder("Select a Level"));
        JButton ks1_2dButton = new JButton("KS1 - 2D Shape Identification");
        JButton ks1_3dButton = new JButton("KS1 - 3D Shape Identification");
        JButton ks1_angleButton = new JButton("KS1 - Angle Identification");
        JButton ks2Button = new JButton("KS2 - Area & Circle Calculation");
        JButton bonusButton = new JButton("Bonus - Advanced Challenges");
        levelPanel.add(ks1_2dButton);
        levelPanel.add(ks1_3dButton);
        levelPanel.add(ks1_angleButton);
        levelPanel.add(ks2Button);
        levelPanel.add(bonusButton);
        add(levelPanel, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        endSessionButton = new JButton("End session");
        homeButton = new JButton("Home");
        buttonPanel.add(endSessionButton);
        buttonPanel.add(homeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        endSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(parentFrame, "You have achieved 0 points in this session. Goodbye!", "Session Ended", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Already at home menu, do nothing
            }
        });

        ks1_2dButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new ShapeIdentificationPanel(parentFrame, true)); // 2D模式
                parentFrame.revalidate();
            }
        });
        ks1_3dButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new ShapeIdentificationPanel(parentFrame, false)); // 3D模式
                parentFrame.revalidate();
            }
        });
        ks1_angleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new AngleIdentificationPanel(parentFrame));
                parentFrame.revalidate();
            }
        });
        ks2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new AreaCalculationPanel(parentFrame));
                parentFrame.revalidate();
            }
        });
        bonusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(new CompoundShapePanel(parentFrame));
                parentFrame.revalidate();
            }
        });
    }
} 