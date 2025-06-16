package com.unicenta.pos.ui;

import javax.swing.*;
import java.awt.*;

public class SwitchButton extends JToggleButton {

    public SwitchButton() {
        setModel(new DefaultButtonModel());
        setPreferredSize(new Dimension(45, 25));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g2.setColor(isSelected() ? new Color(0x4CAF50) : Color.LIGHT_GRAY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        // Círculo
        g2.setColor(Color.WHITE);
        int margin = 3;
        int diameter = getHeight() - margin * 2;
        int x = isSelected() ? getWidth() - diameter - margin : margin;
        g2.fillOval(x, margin, diameter, diameter);

        g2.dispose();
    }
}