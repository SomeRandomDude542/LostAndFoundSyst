package laf;
import javax.swing.*;
import java.awt.*;

public class ResponsiveScaler {
    // Changed to public so TicketDetailWindow can access them
    public static double scaleX = 1.0;
    public static double scaleY = 1.0;
    
    private static final int BASE_WIDTH = 548;   // Your original design size
    private static final int BASE_HEIGHT = 358;
    
    public static void initializeScale(int targetWidth, int targetHeight, int baseWidth, int baseHeight) {
        scaleX = (double) targetWidth / baseWidth;
        scaleY = (double) targetHeight / baseHeight;
        
        System.out.println("✅ Scaling from: " + baseWidth + "x" + baseHeight);
        System.out.println("✅ Scaling to: " + targetWidth + "x" + targetHeight);
        System.out.println("✅ Scale factors: " + scaleX + "x, " + scaleY + "y");
    }
    
    // Scale a component automatically
    public static void scale(Component component, int x, int y, int width, int height) {
        int scaledX = (int) (x * scaleX);
        int scaledY = (int) (y * scaleY);
        int scaledWidth = (int) (width * scaleX);
        int scaledHeight = (int) (height * scaleY);
        component.setBounds(scaledX, scaledY, scaledWidth, scaledHeight);
    }
    
    // Scale font
    public static Font scaleFont(Font font) {
        int scaledSize = (int) (font.getSize() * Math.min(scaleX, scaleY));
        return font.deriveFont((float) scaledSize);
    }
    
    // Scale all components in a container recursively
    public static void scaleAllComponents(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp.getBounds() != null) {
                Rectangle bounds = comp.getBounds();
                scale(comp, bounds.x, bounds.y, bounds.width, bounds.height);
                
                // Scale font if component has one
                if (comp instanceof JComponent) {
                    JComponent jcomp = (JComponent) comp;
                    if (jcomp.getFont() != null) {
                        jcomp.setFont(scaleFont(jcomp.getFont()));
                    }
                }
            }
            
            // Recursively scale nested containers
            if (comp instanceof Container) {
                scaleAllComponents((Container) comp);
            }
        }
    }
}