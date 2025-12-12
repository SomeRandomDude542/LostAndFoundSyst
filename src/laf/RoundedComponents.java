package laf;

import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.Cursor;
import java.awt.Dimension;

public class RoundedComponents {
    
    // Corner constants for easy use
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_LEFT = 4;
    public static final int BOTTOM_RIGHT = 8;
    public static final int ALL_CORNERS = TOP_LEFT | TOP_RIGHT | BOTTOM_LEFT | BOTTOM_RIGHT;
    public static final int TOP_CORNERS = TOP_LEFT | TOP_RIGHT;
    public static final int BOTTOM_CORNERS = BOTTOM_LEFT | BOTTOM_RIGHT;
    public static final int LEFT_CORNERS = TOP_LEFT | BOTTOM_LEFT;
    public static final int RIGHT_CORNERS = TOP_RIGHT | BOTTOM_RIGHT;
    
    // Create rounded button with all corners
    public static JButton createRoundedButton(String text, Color bgColor, Color fgColor, int radius) {
        return createRoundedButton(text, bgColor, fgColor, radius, ALL_CORNERS);
    }
    
    // Create rounded button with specific corners
    public static JButton createRoundedButton(String text, Color bgColor, Color fgColor, int radius, int corners) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                
                Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
                g2.fill(shape);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        btn.setForeground(fgColor);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        
        return btn;
    }
    
    // Create rounded panel with all corners
    public static JPanel createRoundedPanel(Color bgColor, int radius) {
        return createRoundedPanel(bgColor, radius, ALL_CORNERS);
    }
    
    // Create rounded panel with specific corners
    public static JPanel createRoundedPanel(Color bgColor, int radius, int corners) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                
                Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
                g2.fill(shape);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        panel.setOpaque(false); // Set AFTER creating the panel
        return panel;
    }
    
    // Create rounded text field
   
 // Add border color parameter
    public static JTextField createRoundedTextField(Color bgColor, Color fgColor, Color borderColor, int radius) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);  // Use the parameter
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
            }
        };
        
        field.setOpaque(false);
        field.setBackground(bgColor);
        field.setForeground(fgColor);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        return field;
    }

    // Same for password field
    public static JPasswordField createRoundedPasswordField(Color bgColor, Color fgColor, Color borderColor, int radius) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);  // Use the parameter
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
            }
        };
        
        field.setOpaque(false);
        field.setBackground(bgColor);
        field.setForeground(fgColor);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        return field;
    }
    
    // Helper method to create the rounded shape
    private static Shape createRoundedShape(int x, int y, int width, int height, int radius, int corners) {
        Path2D path = new Path2D.Float();
        
        // Start from top-left
        if ((corners & TOP_LEFT) != 0) {
            path.moveTo(x + radius, y);
        } else {
            path.moveTo(x, y);
        }
        
        // Top edge and top-right corner
        if ((corners & TOP_RIGHT) != 0) {
            path.lineTo(x + width - radius, y);
            path.quadTo(x + width, y, x + width, y + radius);
        } else {
            path.lineTo(x + width, y);
        }
        
        // Right edge and bottom-right corner
        if ((corners & BOTTOM_RIGHT) != 0) {
            path.lineTo(x + width, y + height - radius);
            path.quadTo(x + width, y + height, x + width - radius, y + height);
        } else {
            path.lineTo(x + width, y + height);
        }
        
        // Bottom edge and bottom-left corner
        if ((corners & BOTTOM_LEFT) != 0) {
            path.lineTo(x + radius, y + height);
            path.quadTo(x, y + height, x, y + height - radius);
        } else {
            path.lineTo(x, y + height);
        }
        
        // Left edge and back to top-left
        if ((corners & TOP_LEFT) != 0) {
            path.lineTo(x, y + radius);
            path.quadTo(x, y, x + radius, y);
        } else {
            path.lineTo(x, y);
        }
        
        path.closePath();
        return path;
    }
    
 // Add this method to RoundedComponents class
    public static JPanel createRoundedPanelWithBackground(String imagePath, int radius, int corners) {
        JPanel panel = new JPanel() {
            private Image backgroundImage;
            
            {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
                    backgroundImage = icon.getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
                g2.setClip(shape);
                
                // Draw background image
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        panel.setOpaque(false);
        return panel;
    }
    
 // Create rounded checkbox
    public static JCheckBox createRoundedCheckBox(String text, Color bgColor, Color checkColor, int radius) {
        JCheckBox checkBox = new JCheckBox(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Get the icon area bounds
                int iconSize = 16;
                int x = 0;
                int y = (getHeight() - iconSize) / 2;
                
                // Draw rounded background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(x, y, iconSize, iconSize, radius, radius);
                
                // Draw border
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(x, y, iconSize, iconSize, radius, radius);
                
                // Draw checkmark if selected
                if (isSelected()) {
                    g2.setColor(checkColor);
                    g2.setStroke(new BasicStroke(2));
                    
                    // Draw checkmark
                    int cx = x + 3;
                    int cy = y + iconSize / 2;
                    g2.drawLine(cx, cy, cx + 3, cy + 3);
                    g2.drawLine(cx + 3, cy + 3, cx + 9, cy - 5);
                }
                
                g2.dispose();
            }
        };
        
        checkBox.setOpaque(false);
        checkBox.setBackground(bgColor);
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(false);
        checkBox.setContentAreaFilled(false);
        checkBox.setIcon(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                // Empty - we paint in paintComponent
            }
            
            @Override
            public int getIconWidth() {
                return 16;
            }
            
            @Override
            public int getIconHeight() {
                return 16;
            }
        });
        
        return checkBox;
    }
    
 // Create eye toggle button for password visibility
    public static JButton createEyeToggleButton(Color bgColor, int size) {
        JButton eyeButton = new JButton() {
            private boolean visible = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                
                if (visible) {
                    // Open eye
                    g2.setColor(Color.BLACK);
                    
                    // Eye outline (ellipse)
                    g2.drawArc(centerX - 8, centerY - 4, 16, 8, 0, 180);
                    g2.drawArc(centerX - 8, centerY - 4, 16, 8, 180, 180);
                    
                    // Pupil
                    g2.fillOval(centerX - 3, centerY - 3, 6, 6);
                    
                } else {
                    // Closed eye (eye with slash)
                    g2.setColor(Color.BLACK);
                    
                    // Eye outline
                    g2.drawArc(centerX - 8, centerY - 4, 16, 8, 0, 180);
                    g2.drawArc(centerX - 8, centerY - 4, 16, 8, 180, 180);
                    
                    // Slash through eye
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(centerX - 8, centerY + 6, centerX + 8, centerY - 6);
                }
                
                g2.dispose();
            }
            
            @Override
            public void setSelected(boolean selected) {
                this.visible = selected;
                super.setSelected(selected);
                repaint();
            }
            
            @Override
            public boolean isSelected() {
                return visible;
            }
        };
        
        eyeButton.setPreferredSize(new Dimension(size, size));
        eyeButton.setOpaque(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setBorderPainted(false);
        eyeButton.setFocusPainted(false);
        eyeButton.setBackground(bgColor);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return eyeButton;
    }
    
 // Create rounded text field with placeholder
    public static JTextField createRoundedTextField(Color bgColor, Color fgColor, Color borderColor, int radius, String placeholder) {
        JTextField field = new JTextField() {
            private boolean showingPlaceholder = true;
            
            {
                // Set initial placeholder
                setText(placeholder);
                setForeground(Color.GRAY);
                
                // Focus listener to handle placeholder
                addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        if (showingPlaceholder) {
                            setText("");
                            setForeground(fgColor);
                            showingPlaceholder = false;
                        }
                    }
                    
                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        if (getText().trim().isEmpty()) {
                            setText(placeholder);
                            setForeground(Color.GRAY);
                            showingPlaceholder = true;
                        }
                    }
                });
            }
            
            @Override
            public String getText() {
                // Return empty string if showing placeholder
                return showingPlaceholder ? "" : super.getText();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
            }
        };
        
        field.setOpaque(false);
        field.setBackground(bgColor);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        return field;
    }

    // Create rounded password field with placeholder
    public static JPasswordField createRoundedPasswordField(Color bgColor, Color fgColor, Color borderColor, int radius, String placeholder) {
        JPasswordField field = new JPasswordField() {
            private boolean showingPlaceholder = true;
            
            {
                // Set initial placeholder
                setText(placeholder);
                setForeground(Color.GRAY);
                setEchoChar((char) 0); // Show placeholder as regular text
                
                // Focus listener to handle placeholder
                addFocusListener(new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusGained(java.awt.event.FocusEvent e) {
                        if (showingPlaceholder) {
                            setText("");
                            setForeground(fgColor);
                            setEchoChar('•'); // Start hiding password
                            showingPlaceholder = false;
                        }
                    }
                    
                    @Override
                    public void focusLost(java.awt.event.FocusEvent e) {
                        if (getPassword().length == 0) {
                            setText(placeholder);
                            setForeground(Color.GRAY);
                            setEchoChar((char) 0); // Show placeholder
                            showingPlaceholder = true;
                        }
                    }
                });
            }
            
            @Override
            public char[] getPassword() {
                // Return empty array if showing placeholder
                return showingPlaceholder ? new char[0] : super.getPassword();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
            }
        };
        
        field.setOpaque(false);
        field.setBackground(bgColor);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        return field;
    }
    
 // Create rounded text area (for multiline input/display)
    public static JTextArea createRoundedTextArea(Color bgColor, Color fgColor, Color borderColor, int radius, int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                
                g2.dispose();
            }
        };
        
        textArea.setOpaque(false);
        textArea.setBackground(bgColor);
        textArea.setForeground(fgColor);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        return textArea;
    }
    
    public static JScrollPane createRoundedScrollPane(JComponent content, Color bgColor, int radius) {
        JScrollPane scrollPane = new JScrollPane(content) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
            }
        };
        
        scrollPane.setOpaque(false);
        scrollPane.setBackground(bgColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(bgColor);
        
        return scrollPane;
    }
    
    public static JLabel createRoundedLabel(String text, Color bgColor, Color fgColor, int radius) {
        return createRoundedLabel(text, bgColor, fgColor, radius, ALL_CORNERS);
    }

    public static JLabel createRoundedLabel(String text, Color bgColor, Color fgColor, int radius, int corners) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
                g2.fill(shape);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        label.setOpaque(false);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        return label;
    }
    
    public static JPanel createRoundedPanel(Color bgColor, Color borderColor, int radius, int corners) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                
                Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
                g2.fill(shape);
                
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                if (borderColor != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.setColor(borderColor);
                    Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                    g2.draw(shape);
                    
                    g2.dispose();
                }
            }
        };
        
        panel.setOpaque(false);
        panel.setBorder(null);  // Remove default border since we're painting our own
        return panel;
    }
    
 // Add these methods to your RoundedComponents class (keeps all existing methods intact)

 // ===== SCROLL PANE WITH BORDER COLOR =====
 public static JScrollPane createRoundedScrollPane(JComponent content, Color bgColor, Color borderColor, int radius) {
     JScrollPane scrollPane = new JScrollPane(content) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             g2.setColor(bgColor);
             g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
             g2.dispose();
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 g2.setColor(borderColor);
                 g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                 g2.dispose();
             }
         }
     };
     
     scrollPane.setOpaque(false);
     scrollPane.setBackground(bgColor);
     scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
     scrollPane.getViewport().setOpaque(false);
     scrollPane.getViewport().setBackground(bgColor);
     
     return scrollPane;
 }

 // ===== BUTTON WITH BORDER =====
 public static JButton createRoundedButton(String text, Color bgColor, Color fgColor, Color borderColor, int radius, int corners) {
     JButton btn = new JButton(text) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             if (getModel().isPressed()) {
                 g2.setColor(bgColor.darker());
             } else if (getModel().isRollover()) {
                 g2.setColor(bgColor.brighter());
             } else {
                 g2.setColor(bgColor);
             }
             
             Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
             g2.fill(shape);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                 g2.draw(shape);
                 
                 g2.dispose();
             }
         }
     };
     
     btn.setForeground(fgColor);
     btn.setOpaque(false);
     btn.setContentAreaFilled(false);
     btn.setFocusPainted(false);
     btn.setBorderPainted(false);
     
     return btn;
 }

 // ===== LABEL WITH BORDER =====
 public static JLabel createRoundedLabel(String text, Color bgColor, Color fgColor, Color borderColor, int radius, int corners) {
     JLabel label = new JLabel(text) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             g2.setColor(getBackground());
             Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
             g2.fill(shape);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                 g2.draw(shape);
                 
                 g2.dispose();
             }
         }
     };
     
     label.setOpaque(false);
     label.setBackground(bgColor);
     label.setForeground(fgColor);
     label.setHorizontalAlignment(SwingConstants.CENTER);
     
     return label;
 }

 // ===== TEXT FIELD WITH CORNERS =====
 public static JTextField createRoundedTextField(Color bgColor, Color fgColor, Color borderColor, int radius, int corners) {
     JTextField field = new JTextField() {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             g2.setColor(getBackground());
             Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
             g2.fill(shape);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                 g2.draw(shape);
                 
                 g2.dispose();
             }
         }
     };
     
     field.setOpaque(false);
     field.setBackground(bgColor);
     field.setForeground(fgColor);
     field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
     
     return field;
 }

 // ===== PASSWORD FIELD WITH CORNERS =====
 public static JPasswordField createRoundedPasswordField(Color bgColor, Color fgColor, Color borderColor, int radius, int corners) {
     JPasswordField field = new JPasswordField() {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             g2.setColor(getBackground());
             Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
             g2.fill(shape);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                 g2.draw(shape);
                 
                 g2.dispose();
             }
         }
     };
     
     field.setOpaque(false);
     field.setBackground(bgColor);
     field.setForeground(fgColor);
     field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
     
     return field;
 }

 // ===== TEXT AREA WITH CORNERS =====
 public static JTextArea createRoundedTextArea(Color bgColor, Color fgColor, Color borderColor, int radius, int corners, int rows, int columns) {
     JTextArea textArea = new JTextArea(rows, columns) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             g2.setColor(getBackground());
             Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
             g2.fill(shape);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 Shape shape = createRoundedShape(0, 0, getWidth() - 1, getHeight() - 1, radius, corners);
                 g2.draw(shape);
                 
                 g2.dispose();
             }
         }
     };
     
     textArea.setOpaque(false);
     textArea.setBackground(bgColor);
     textArea.setForeground(fgColor);
     textArea.setLineWrap(true);
     textArea.setWrapStyleWord(true);
     textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     
     return textArea;
 }

 // ===== COMBOBOX WITH BORDER =====
 public static JComboBox<String> createRoundedComboBox(String[] items, Color bgColor, Color fgColor, Color borderColor, int radius) {
     JComboBox<String> comboBox = new JComboBox<String>(items) {
         @Override
         protected void paintComponent(Graphics g) {
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             
             g2.setColor(getBackground());
             g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
             
             g2.dispose();
             super.paintComponent(g);
         }
         
         @Override
         protected void paintBorder(Graphics g) {
             if (borderColor != null) {
                 Graphics2D g2 = (Graphics2D) g.create();
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 
                 g2.setColor(borderColor);
                 g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                 
                 g2.dispose();
             }
         }
     };
     
     comboBox.setBackground(bgColor);
     comboBox.setForeground(fgColor);
     comboBox.setOpaque(false);
     comboBox.setBorder(null);
     comboBox.setFocusable(false);
     
     // Style the renderer for the dropdown items
     comboBox.setRenderer(new DefaultListCellRenderer() {
         @Override
         public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                      int index, boolean isSelected, boolean cellHasFocus) {
             Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
             
             if (isSelected) {
                 c.setBackground(bgColor.darker());
                 c.setForeground(fgColor);
             } else {
                 c.setBackground(bgColor);
                 c.setForeground(fgColor);
             }
             
             setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
             
             return c;
         }
     });
     
     return comboBox;
 }

 public static JButton createRoundedButtonWithHover(String text, Color bgColor, Color hoverColor, Color fgColor, int radius, int corners) {
	    JButton btn = new JButton(text) {
	        @Override
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            
	            if (getModel().isPressed()) {
	                g2.setColor(hoverColor.darker());
	            } else if (getModel().isRollover()) {
	                g2.setColor(hoverColor);  // Use custom hover color
	            } else {
	                g2.setColor(bgColor);
	            }
	            
	            Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
	            g2.fill(shape);
	            
	            g2.dispose();
	            super.paintComponent(g);
	        }
	    };
	    
	    btn.setForeground(fgColor);
	    btn.setOpaque(false);
	    btn.setContentAreaFilled(false);
	    btn.setFocusPainted(false);
	    btn.setBorderPainted(false);
	    
	    return btn;
	}
 
//ADD THIS NEW METHOD to your RoundedComponents.java class
//(Place it right after your existing createRoundedPanelWithBackground method)

 public static JPanel createRoundedPanelWithBackground(String imagePath, Color borderColor, int borderWidth, int radius, int corners) {
	    JPanel panel = new JPanel() {
	        private Image backgroundImage;

	        {
	            try {
	                ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
	                backgroundImage = icon.getImage();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        @Override
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            
	            // HIGH QUALITY RENDERING SETTINGS - ADD THESE!
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

	            Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
	            g2.setClip(shape);

	            // Draw background image
	            if (backgroundImage != null) {
	                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	            }

	            g2.dispose();
	            super.paintComponent(g);
	        }

	        @Override
	        protected void paintBorder(Graphics g) {
	            if (borderColor != null && borderWidth > 0) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                g2.setColor(borderColor);
	                g2.setStroke(new BasicStroke(borderWidth));

	                // Adjust shape size to account for border width
	                int offset = borderWidth / 2;
	                Shape shape = createRoundedShape(
	                    offset,
	                    offset,
	                    getWidth() - borderWidth,
	                    getHeight() - borderWidth,
	                    radius,
	                    corners
	                );
	                g2.draw(shape);

	                g2.dispose();
	            }
	        }
	    };

	    panel.setOpaque(false);
	    return panel;
	}
public static JPanel createRoundedPanel(Color bgColor, Color borderColor, int borderWidth, int radius, int corners) {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            
            Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);
            g2.fill(shape);
            
            g2.dispose();
            super.paintComponent(g);
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            if (borderColor != null && borderWidth > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                
                // Adjust shape size to account for border width
                int offset = borderWidth / 2;
                Shape shape = createRoundedShape(
                    offset, 
                    offset, 
                    getWidth() - borderWidth, 
                    getHeight() - borderWidth, 
                    radius, 
                    corners
                );
                g2.draw(shape);
                
                g2.dispose();
            }
        }
    };
    
    panel.setOpaque(false);
    panel.setBorder(null);
    return panel;
}

public static JScrollPane createRoundedScrollPane(
        JComponent content,
        Color bgColor,
        Color borderColor,
        int radius,
        int corners
) {
    JScrollPane scrollPane = new JScrollPane(content) {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);

            g2.setColor(bgColor);
            g2.fill(shape);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            if (borderColor != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(borderColor);
                Shape shape = createRoundedShape(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        radius, corners
                );
                g2.draw(shape);

                g2.dispose();
            }
        }
    };

    scrollPane.setOpaque(false);
    scrollPane.setBackground(bgColor);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // clean edges

    scrollPane.getViewport().setOpaque(false);
    scrollPane.getViewport().setBackground(bgColor);

    return scrollPane;
}

public static JScrollPane createRoundedScrollPane(
        JComponent content,
        Color bgColor,
        Color borderColor,
        int borderWidth,
        int radius,
        int corners
) {
    JScrollPane scrollPane = new JScrollPane(content) {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape shape = createRoundedShape(0, 0, getWidth(), getHeight(), radius, corners);

            g2.setColor(bgColor);
            g2.fill(shape);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            if (borderColor != null && borderWidth > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(borderWidth));
                
                // Adjust for border width
                int offset = borderWidth / 2;
                Shape shape = createRoundedShape(
                        offset, offset,
                        getWidth() - borderWidth,
                        getHeight() - borderWidth,
                        radius, corners
                );
                g2.draw(shape);

                g2.dispose();
            }
        }
    };

    scrollPane.setOpaque(false);
    scrollPane.setBackground(bgColor);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    scrollPane.getViewport().setOpaque(false);
    scrollPane.getViewport().setBackground(bgColor);

    return scrollPane;
}
}