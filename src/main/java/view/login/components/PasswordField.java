package view.login.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class PasswordField extends JPasswordField {

    public String getHelperText() {
        return helperText;
    }

    public void setHelperText(String helperText) {
        this.helperText = helperText;
        repaint();
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    private final Animator animator;
    private boolean animateHinText = true;
    private float location;
    private boolean show;
    private boolean mouseOver = false;
    private String labelText = "Label";
    private String helperText = "";
    private int spaceHelperText = 15;
    private Color lineColor = new Color(3, 155, 216);
    private boolean showPw = false;
    private Rectangle2D showPwBound;
    private char defaultEchoChar;

    public PasswordField() {
        setBorder(new EmptyBorder(20, 3, 23, 3));
        setSelectionColor(new Color(76, 204, 255));
        defaultEchoChar = getEchoChar();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                mouseOver = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showPw(e);
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(showPwBound != null && showPwBound.contains(e.getPoint())){
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                showing(false);
            }

            @Override
            public void focusLost(FocusEvent fe) {
                showing(true);
            }
        });
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                animateHinText = getText().isEmpty();
            }

            @Override
            public void timingEvent(float fraction) {
                location = fraction;
                repaint();
            }

        };
        animator = new Animator(300, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
    }

    private void showing(boolean action) {
        if (animator.isRunning()) {
            animator.stop();
        } else {
            location = 1;
        }
        animator.setStartFraction(1f - location);
        show = action;
        location = 1f - location;
        animator.start();
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        int width = getWidth();
        int height = getHeight();
        if (mouseOver) {
            g2.setColor(lineColor);
        } else {
            g2.setColor(new Color(150, 150, 150));
        }
        g2.fillRect(2, height - spaceHelperText - 1, width - 4, 1);
        createHintText(g2);
        createLineStyle(g2);
        createHelperText(g2);
        createIcon(g2);
        g2.dispose();
    }

    private void createIcon(Graphics2D g2) {
        ImageIcon icon = null;

        String status = null;
        if (showPw) {
            status = " Hiện";
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon_login/show.png")));
        } else {
            status = " Ẩn";
            icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon_login/hidden.png")));
        }

        g2.setColor(new Color(150, 150, 150));
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(status, g2);

        int textX = (int) (getWidth() - r2.getWidth());
        int textY = (int) (getHeight() - r2.getHeight()) / 2 + ft.getAscent();

        int iconX = (int) (getWidth() - icon.getIconWidth() - r2.getWidth());
        int iconY = (getHeight() - icon.getIconHeight()) / 2;
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - location);
            } else {
                size = 18 * location;
            }
        } else {
            size = 18;
        }
        g2.drawString(status, textX, (int) (textY - size));
        icon.paintIcon(this, g2, iconX, (int) (iconY - size));

        showPwBound = new Rectangle2D.Double(iconX, iconY - size, icon.getIconWidth() + r2.getWidth(), icon.getIconHeight());
    }

    private void showPw(MouseEvent e){
        if(showPwBound.contains(e.getPoint())){
            showPw = !showPw;
            if(showPw){
                setEchoChar((char)0);
            } else {
                setEchoChar(defaultEchoChar);
            }
        }
    }

    private void createHintText(Graphics2D g2) {
        Insets in = getInsets();
        g2.setColor(new Color(150, 150, 150));
        FontMetrics ft = g2.getFontMetrics();
        Rectangle2D r2 = ft.getStringBounds(labelText, g2);
        double height = getHeight() - in.top - in.bottom;
        double textY = (height - r2.getHeight()) / 2;
        double size;
        if (animateHinText) {
            if (show) {
                size = 18 * (1 - location);
            } else {
                size = 18 * location;
            }
        } else {
            size = 18;
        }
        g2.drawString(labelText, in.right, (int) (in.top + textY + ft.getAscent() - size));
    }

    private void createLineStyle(Graphics2D g2) {
        if (isFocusOwner()) {
            double width = getWidth() - 4;
            int height = getHeight() - spaceHelperText;
            g2.setColor(lineColor);
            double size;
            if (show) {
                size = width * (1 - location);
            } else {
                size = width * location;
            }
            double x = (width - size) / 2;
            g2.fillRect((int) (x + 2), height - 2, (int) size, 2);
        }
    }

    private void createHelperText(Graphics2D g2) {
        if (helperText != null && !helperText.isEmpty()) {
            Insets in = getInsets();
            int height = getHeight() - 15;
            g2.setColor(new Color(255, 76, 76));
            Font font = getFont();
            g2.setFont(font.deriveFont(0, font.getSize() - 1));
            FontMetrics ft = g2.getFontMetrics();
            Rectangle2D r2 = ft.getStringBounds(labelText, g2);
            double textY = (15 - r2.getHeight()) / 2f;
            g2.drawString(helperText, in.right, (int) (height + ft.getAscent() - textY));
        }
    }

    @Override
    public void setText(String string) {
        if (!getText().equals(string)) {
            showing(string.isEmpty());
        }
        super.setText(string);
    }
}
