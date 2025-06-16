package com.unicenta.pos.forms;

import com.unicenta.format.Formats;
import com.unicenta.plugins.Application;
import com.unicenta.plugins.metrics.Metrics;
import com.unicenta.pos.instance.InstanceQuery;
import com.unicenta.pos.ticket.TicketInfo;
import lombok.extern.slf4j.Slf4j;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Locale;
import com.formdev.flatlaf.FlatLightLaf;


@Slf4j
public class StartPOS {

    private StartPOS() {
    }

    public static boolean registerApp() {
        try {
            InstanceQuery i = new InstanceQuery();
            i.getAppMessage().restoreWindow();
            return false;
        } catch (RemoteException | NotBoundException e) {
            return true;
        }
    }

    public static void main(final String args[]) {

        SwingUtilities.invokeLater(() -> {
            if (!registerApp()) {
                System.exit(1);
            }

            AppConfig config = new AppConfig(args);
            config.load();

            // Idioma
            String slang = config.getProperty("user.language");
            String scountry = config.getProperty("user.country");
            String svariant = config.getProperty("user.variant");
            if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
                Locale.setDefault(new Locale(slang, scountry, svariant));
            }

            // Formatos numéricos y de fecha
            Formats.setIntegerPattern(config.getProperty("format.integer"));
            Formats.setDoublePattern(config.getProperty("format.double"));
            Formats.setCurrencyPattern(config.getProperty("format.currency"));
            Formats.setPercentPattern(config.getProperty("format.percent"));
            Formats.setDatePattern(config.getProperty("format.date"));
            Formats.setTimePattern(config.getProperty("format.time"));
            Formats.setDateTimePattern(config.getProperty("format.datetime"));

            // Look & Feel + Fuente Inter
            try {

                UIManager.setLookAndFeel( new FlatLightLaf() ); // <---- Cambia aquí el que quieras

                // Personalización visual
                UIManager.put("Button.arc", 999);
                UIManager.put("Component.arc", 12);
                UIManager.put("ProgressBar.arc", 999);
                UIManager.put("TextComponent.arc", 12);

                // Fuente Inter
                Font inter = Font.createFont(Font.TRUETYPE_FONT,
                                StartPOS.class.getResourceAsStream("/fonts/Inter_18pt-Regular.ttf"))
                        .deriveFont(Font.PLAIN, 14f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(inter);
                UIManager.put("defaultFont", new javax.swing.plaf.FontUIResource(inter));

                // (NO vuelvas a llamar a setLookAndFeel aquí, FlatLaf.setup() ya lo hace)
                // FlatArcIJTheme.updateUI(); // Puedes forzar updateUI, pero normalmente no es necesario

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Registro del hostname y arranque de app
            String hostname = config.getProperty("machine.hostname");
            TicketInfo.setHostname(hostname);
            applicationStarted(hostname);

            // Selección de modo de pantalla
            String screenmode = config.getProperty("machine.screenmode");

            if ("fullscreen".equals(screenmode)) {
                JRootKiosk rootkiosk = new JRootKiosk();
                try {
                    rootkiosk.initFrame(config);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JRootFrame rootframe = new JRootFrame();
                try {
                    rootframe.initFrame(config);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private static void applicationStarted(String host) {
        new Thread(() -> {
            Metrics metrics = new Metrics();
            metrics.setDevice(host);
            metrics.setUniCentaVersion(AppLocal.APP_VERSION);
            new Application().postMetrics(metrics);
        }).start();
    }
}
