package src.view;

import javax.swing.*;
import java.awt.*;

public class SectorSelectionPanel extends JPanel {
    private JFrame parentFrame;
    private static boolean[] completed = new boolean[8]; // 记录每题是否完成
    private JButton[] sectorButtons = new JButton[8];
    private JProgressBar progressBar;

    public SectorSelectionPanel(JFrame frame, boolean[] completedStatus) {
        this.parentFrame = frame;
        if (completedStatus != null) {
            System.arraycopy(completedStatus, 0, completed, 0, 8);
        }
        setLayout(new BorderLayout(10, 10));
        JLabel title = new JLabel("Sector Area Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        for (int i = 0; i < 8; i++) {
            String imgPath = "src/resources/images/task6/Shape " + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JButton btn = new JButton("Sector " + (i + 1), new ImageIcon(img));
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            if (completed[i]) {
                btn.setBackground(new Color(180, 255, 180));
                btn.setText(btn.getText() + " ✓");
                btn.setEnabled(false);
            } else {
                int idx = i;
                btn.addActionListener(e -> {
                    parentFrame.setContentPane(new SectorAreaPanel(parentFrame, idx, completed));
                    parentFrame.revalidate();
                });
            }
            sectorButtons[i] = btn;
            gridPanel.add(btn);
        }
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        add(gridPanel, BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 8);
        int done = 0;
        for (int i = 0; i < 8; i++) if (completed[i]) done++;
        progressBar.setValue(done);
        progressBar.setStringPainted(true);
        progressBar.setString("Completed: " + done + "/8");
        add(progressBar, BorderLayout.SOUTH);

        JButton homeBtn = new JButton("Home");
        homeBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MainMenuPanel(parentFrame));
            parentFrame.revalidate();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.NORTH);
    }
} 