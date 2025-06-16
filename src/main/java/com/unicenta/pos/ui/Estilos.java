package com.unicenta.pos.ui;
import java.awt.Font;

public class Estilos {

    public static Font titulo() {
        return FuenteInter.cargarFuente("Inter_18pt-Bold.ttf", 18f);
    }

    public static Font subtitulo() {
        return FuenteInter.cargarFuente("Inter_18pt-Medium.ttf", 16f);
    }

    public static Font boton() {
        return FuenteInter.cargarFuente("Inter_18pt-Medium.ttf", 14f);
    }

    public static Font texto() {
        return FuenteInter.cargarFuente("Inter_18pt-Regular.ttf", 14f);
    }

    public static Font detalle() {
        return FuenteInter.cargarFuente("Inter_18pt-Light.ttf", 12f);
    }
}