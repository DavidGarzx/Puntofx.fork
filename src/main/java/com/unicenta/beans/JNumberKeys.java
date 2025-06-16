package com.unicenta.beans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class JNumberKeys extends JPanel {

    private Font interBlack;
    private final Vector<JNumberEventListener> m_Listeners = new Vector<>();
    private boolean minusenabled = true;
    private boolean equalsenabled = true;
    private final Map<Character, JButton> buttonMap = new HashMap<>();
    private JLabel linkedTextField;

    private final List<String> labels = Arrays.asList(
            "CE", "×", "-", "1", "2", "3", "+", "4", "5", "6",
            "7", "8", "9", "0", ".", "="
    );

    private final Map<String, int[]> layoutMap = new HashMap<>() {{
        put("CE", new int[]{0, 0, 2, 1});
        put("×", new int[]{2, 0, 1, 1});
        put("-", new int[]{3, 0, 1, 1});
        put("1", new int[]{0, 1, 1, 1});
        put("2", new int[]{1, 1, 1, 1});
        put("3", new int[]{2, 1, 1, 1});
        put("+", new int[]{3, 1, 1, 2});
        put("4", new int[]{0, 2, 1, 1});
        put("5", new int[]{1, 2, 1, 1});
        put("6", new int[]{2, 2, 1, 1});
        put("7", new int[]{0, 3, 1, 1});
        put("8", new int[]{1, 3, 1, 1});
        put("9", new int[]{2, 3, 1, 1});
        put("0", new int[]{0, 4, 2, 1});
        put(".", new int[]{2, 4, 1, 1});
        put("=", new int[]{3, 3, 1, 2});
    }};

    public void setLinkedTextField(JLabel textField) {
        this.linkedTextField = textField;
    }

    public JNumberKeys() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setMinimumSize(new Dimension(193, 200));
        setPreferredSize(new Dimension(193, 200));

        try {
            interBlack = Font.createFont(Font.TRUETYPE_FONT,
                            Objects.requireNonNull(getClass().getResourceAsStream("/fonts/Inter_18pt-Bold.ttf")))
                    .deriveFont(Font.PLAIN, 20f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(interBlack);
        } catch (Exception e) {
            System.err.println("Error cargando fuente: " + e.getMessage());
        }

        for (String label : labels) {
            JButton btn = new JButton(label);
            configureButton(btn, label);
            int[] pos = layoutMap.get(label);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = pos[0]; gbc.gridy = pos[1];
            gbc.gridwidth = pos[2]; gbc.gridheight = pos[3];
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0; gbc.weighty = 1.0;
            gbc.insets = new Insets(1, 1, 1, 1);
            add(btn, gbc);
        }

        registerActionListeners();
    }

    private void configureButton(JButton btn, String label) {
        boolean esNumero = label.matches("[0-9\\.]");
        boolean sinFondo = label.equals("CE") || label.equals("×") || label.equals("-") || label.equals("+") || label.equals("=");

        btn.setFont(interBlack.deriveFont(25f));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setForeground(new Color(51, 58, 70));
        btn.putClientProperty("JButton.arc", 16);

        if (sinFondo) {
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
        } else {
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBackground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        }

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200))
                )
        ));

        btn.addMouseListener(new MouseAdapter() {
            Point originalLocation = null;

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(245, 245, 245));
                btn.setContentAreaFilled(true);
                btn.setOpaque(true);
                btn.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (sinFondo) {
                    btn.setOpaque(false);
                    btn.setBackground(new Color(0, 0, 0, 0));
                    btn.setContentAreaFilled(false);
                } else {
                    btn.setBackground(Color.WHITE);
                }
                btn.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                originalLocation = btn.getLocation();
                btn.setLocation(originalLocation.x + 1, originalLocation.y + 1);
                btn.setBackground(new Color(220, 220, 220));
                btn.setContentAreaFilled(true);
                btn.setOpaque(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (originalLocation != null) {
                    btn.setLocation(originalLocation);
                }
                btn.setBackground(new Color(245, 245, 245));
            }
        });

        if (esNumero) {
            buttonMap.put(label.charAt(0), btn);
        } else if (label.equals("CE")) {
            buttonMap.put('C', btn);
        }
    }

    private void registerActionListeners() {
        char[] chars = {'0','1','2','3','4','5','6','7','8','9','.','*','C','+','-','='};
        for (char ch : chars) {
            JButton btn = buttonMap.getOrDefault(ch, findButtonByChar(ch));
            if (btn != null) {
                btn.addActionListener(new MyKeyNumberListener(ch));
            }
        }
    }

    private JButton findButtonByChar(char ch) {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals(String.valueOf(ch))) {
                    return btn;
                }
            }
        }
        return null;
    }

    public void dotIs00(boolean enabled) {
        JButton dot = buttonMap.get('.') != null ? buttonMap.get('.') : findButtonByChar('.') ;
        if (dot != null) dot.setText(enabled ? "00" : ".");
    }

    public void setMinusEnabled(boolean b) {
        minusenabled = b;
        JButton btn = findButtonByChar('-');
        if (btn != null) btn.setEnabled(b);
    }

    public void setEqualsEnabled(boolean b) {
        equalsenabled = b;
        JButton btn = findButtonByChar('=');
        if (btn != null) btn.setEnabled(b);
    }

    public boolean isMinusEnabled() { return minusenabled; }
    public boolean isEqualsEnabled() { return equalsenabled; }
    public boolean isNumbersOnly() { return findButtonByChar('=') != null && findButtonByChar('=').isVisible(); }

    public void addJNumberEventListener(JNumberEventListener listener) { m_Listeners.add(listener); }
    public void removeJNumberEventListener(JNumberEventListener listener) { m_Listeners.remove(listener); }

    private class MyKeyNumberListener implements ActionListener {
        private final char m_cCad;
        public MyKeyNumberListener(char cCad) { m_cCad = cCad; }
        @Override
        public void actionPerformed(ActionEvent evt) {
            JNumberEvent oEv = new JNumberEvent(JNumberKeys.this, m_cCad);
            for (Enumeration e = m_Listeners.elements(); e.hasMoreElements();) {
                JNumberEventListener oListener = (JNumberEventListener) e.nextElement();
                oListener.keyPerformed(oEv);
            }

            if (linkedTextField != null && m_cCad == 'C') {
                linkedTextField.setText("");
            }
        }
    }
}
