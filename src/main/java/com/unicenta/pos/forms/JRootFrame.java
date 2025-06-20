//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2018 uniCenta & previous Openbravo POS works
//    https://unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.unicenta.pos.forms;

import com.unicenta.plugins.Application;
import com.unicenta.plugins.metrics.Metrics;
import com.unicenta.pos.config.JFrmConfig;
import com.unicenta.pos.instance.AppMessage;
import com.unicenta.pos.instance.InstanceManager;

import java.awt.BorderLayout;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.unicenta.pos.scripting.ScriptEngine;
import com.unicenta.pos.scripting.ScriptException;
import com.unicenta.pos.scripting.ScriptFactory;
import com.unicenta.pos.util.AltEncrypter;
import com.unicenta.pos.util.OSValidator;
import com.unicenta.pos.util.SessionKeepAlive;
import lombok.extern.slf4j.Slf4j;

/**
 * @author adrianromero
 */
@Slf4j
public class JRootFrame extends javax.swing.JFrame implements AppMessage {

    private InstanceManager m_instmanager = null;

    private JRootApp m_rootapp;
    private AppProperties m_props;

    private OSValidator m_OS;

    /**
     * Creates new form JRootFrame
     */
    public JRootFrame() {

        initComponents();
    }

    /**
     * @param props
     */
    public void initFrame(AppProperties props) {

        m_OS = new OSValidator();
        m_props = props;

        m_rootapp = new JRootApp();

        if (m_rootapp.initApp(m_props)) {

            if ("true".equals(props.getProperty("machine.uniqueinstance"))) {
                // Register the running application
                try {
                    m_instmanager = new InstanceManager(this);
                } catch (RemoteException | AlreadyBoundException e) {
                }
            }

            // Show the application
            add(m_rootapp, BorderLayout.CENTER);

            try {
                this.setIconImage(ImageIO.read(JRootFrame.class.getResourceAsStream("/com/unicenta/images/favicon.png")));
            } catch (IOException e) {
            }

            setTitle(AppLocal.APP_NAME + " - " + AppLocal.APP_VERSION);
            this.setUndecorated(false);
            pack();
            setLocationRelativeTo(null);

            setVisible(true);
        } else {
            new JFrmConfig(props).setVisible(true); // Show the configuration window.
        }

    }

    /**
     * @throws RemoteException
     */
    @Override
    public void restoreWindow() throws RemoteException {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (getExtendedState() == JFrame.ICONIFIED) {
                    setExtendedState(JFrame.NORMAL);
                }
                requestFocus();
            }
        });
    }

    private void startSessionKeepAlive(DataLogicSystem dataLogicSystem) {
        SessionKeepAlive sessionKeepAlive = new SessionKeepAlive(dataLogicSystem);
        sessionKeepAlive.start();
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        m_rootapp.tryToClose();

    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

        m_rootapp.releaseResources();
        System.exit(0);

    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
