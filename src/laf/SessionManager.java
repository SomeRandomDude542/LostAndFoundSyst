package laf;

import javax.swing.*;
import java.awt.event.*;

public class SessionManager {
    private static final int INACTIVITY_TIMEOUT = 15 * 60 * 1000; // 15 minutes in milliseconds
    private Timer inactivityTimer;
    private JFrame currentFrame;
    private String username;
    
    public SessionManager(JFrame frame, String username) {
        this.currentFrame = frame;
        this.username = username;
        startInactivityTimer();
        setupActivityListeners();
    }
    
    private void startInactivityTimer() {
        inactivityTimer = new Timer(INACTIVITY_TIMEOUT, e -> handleTimeout());
        inactivityTimer.setRepeats(false);
        inactivityTimer.start();
        System.out.println("✅ Session started for: " + username + " (15 min timeout)");
    }
    
    private void setupActivityListeners() {
        // Mouse listener
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                resetTimer();
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                resetTimer();
            }
        };
        
        // Key listener
        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                resetTimer();
            }
        };
        
        // Add listeners to frame and all components
        addListenersRecursively(currentFrame, mouseListener, keyListener);
    }
    
    private void addListenersRecursively(java.awt.Container container, 
                                        MouseAdapter mouseListener, 
                                        KeyAdapter keyListener) {
        container.addMouseListener(mouseListener);
        container.addMouseMotionListener(mouseListener);
        container.addKeyListener(keyListener);
        
        for (java.awt.Component comp : container.getComponents()) {
            comp.addMouseListener(mouseListener);
            comp.addMouseMotionListener(mouseListener);
            comp.addKeyListener(keyListener);
            
            if (comp instanceof java.awt.Container) {
                addListenersRecursively((java.awt.Container) comp, mouseListener, keyListener);
            }
        }
    }
    
    private void resetTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.restart();
        }
    }
    
    private void handleTimeout() {
        System.out.println("⏱️ SESSION TIMEOUT for: " + username);
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(currentFrame,
                "⏱️ Your session has expired due to inactivity.\n" +
                "Please log in again for security.",
                "Session Timeout",
                JOptionPane.WARNING_MESSAGE);
            
            logout();
        });
    }
    
    public void logout() {
        if (inactivityTimer != null) {
            inactivityTimer.stop();
        }
        
        System.out.println("🔒 User logged out: " + username);
        
        SwingUtilities.invokeLater(() -> {
            currentFrame.dispose();
            new LoginWindow().setVisible(true);
        });
    }
    
    public void stopSession() {
        if (inactivityTimer != null) {
            inactivityTimer.stop();
        }
    }
}