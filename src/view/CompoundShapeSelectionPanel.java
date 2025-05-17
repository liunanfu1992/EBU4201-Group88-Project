package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompoundShapeSelectionPanel extends JPanel {
    private JFrame parentFrame;
    private boolean[] completed = new boolean[9]; // 记录每题是否完成
    private JButton[] shapeButtons = new JButton[9];
    private JProgressBar progressBar;

    public CompoundShapeSelectionPanel(JFrame frame, boolean[] completedStatus) {
        this.parentFrame = frame;
        if (completedStatus != null) {
            System.arraycopy(completedStatus, 0, completed, 0, 9);
        }
        setLayout(new BorderLayout(10, 10));
        JLabel title = new JLabel("Compound Shapes Area Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        for (int i = 0; i < 9; i++) {
            String imgPath = "src/resources/images/task5/Shape " + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JButton btn = new JButton("Shape " + (i + 1), new ImageIcon(img));
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
            if (i == 4 || i == 5) { // shape5/6禁用
                btn.setEnabled(false);
                btn.setToolTipText("Not available yet");
            } else if (completed[i]) {
                btn.setBackground(new Color(180, 255, 180));
                btn.setText(btn.getText() + " ✓");
                btn.setEnabled(false);
            } else {
                int idx = i;
                btn.addActionListener(e -> {
                    parentFrame.setContentPane(new CompoundShapePanel(parentFrame, idx, completed));
                    parentFrame.revalidate();
                });
            }
            shapeButtons[i] = btn;
            gridPanel.add(btn);
        }
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        add(gridPanel, BorderLayout.CENTER);

        progressBar = new JProgressBar(0, 7);
        int done = 0;
        for (int i = 0; i < 9; i++) if (completed[i] && i != 4 && i != 5) done++;
        progressBar.setValue(done);
        progressBar.setStringPainted(true);
        progressBar.setString("Completed: " + done + "/7");
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