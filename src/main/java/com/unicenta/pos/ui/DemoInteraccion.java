package com.unicenta.pos.ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatLaf;
import com.unicenta.pos.ui.SwitchButton;
import java.awt.event.ItemEvent;
import com.unicenta.beans.JNumberKeys;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;


class DemoInteraccion {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Demo Teclado Numérico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        // Aquí agregas el teclado que ya diseñaste
        JNumberKeys panel = new JNumberKeys();
        frame.add(panel);

        frame.setVisible(true);
    }
}
class TestNumerico {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Demo Teclado Numérico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JNumberKeys());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}