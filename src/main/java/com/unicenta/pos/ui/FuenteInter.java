package com.unicenta.pos.ui;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class FuenteInter {

    public static Font cargarFuente(String nombre, float tamaño) {
        try {
            InputStream is = FuenteInter.class.getResourceAsStream("/fonts/" + nombre);
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(tamaño);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) tamaño);
        }
    }

    public static void aplicarFuenteGlobal() {
        Font inter = cargarFuente("Inter_18pt-Regular.ttf", 14f);
        UIManager.put("defaultFont", inter);
    }
}
