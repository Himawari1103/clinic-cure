package view.home.components.panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import constants.MenuType;
import net.miginfocom.swing.MigLayout;
import view.home.components.event.EventMenu;
import view.home.components.event.EventMenuSelected;
import view.home.components.model.ModelMenu;
import view.home.components.MenuAnimation;
import view.home.components.MenuItem;
import view.home.components.scrollbar.ScrollBarCustom;

public class Menu extends javax.swing.JPanel {

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private boolean enableMenu = true;
    private boolean showMenu = true;

    private ArrayList<MenuItem> listMenuItem = new ArrayList<>();

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);
    }


    public ImageIcon changeIconColor(ImageIcon icon, Color color) {
        Image originalImage = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                originalImage.getWidth(null),
                originalImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.drawImage(originalImage, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g2d.dispose();

        return new ImageIcon(bufferedImage);
    }

    public void initMenuItem() {
        ImageIcon reportIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/report.png")), Color.WHITE);
        ImageIcon patientIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/patient.png")), Color.WHITE);
        ImageIcon staffIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/staff.png")), Color.WHITE);
        ImageIcon appointmentIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/appointment.png")), Color.WHITE);
        ImageIcon recordIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/record.png")), Color.WHITE);
        ImageIcon receiptIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/receipt.png")), Color.WHITE);
        ImageIcon accountIcon = changeIconColor(new ImageIcon(getClass().getResource("/icon_panel/account.png")), Color.WHITE);


        addMenu(new ModelMenu(reportIcon, MenuType.REPORT));
        addMenu(new ModelMenu(patientIcon, MenuType.PATIENT));
        addMenu(new ModelMenu(staffIcon, MenuType.STAFF));
        addMenu(new ModelMenu(appointmentIcon, MenuType.APPOINTMENT));
        addMenu(new ModelMenu(recordIcon, MenuType.RECORD));
        addMenu(new ModelMenu(receiptIcon, MenuType.RECEIPT));
        addMenu(new ModelMenu(accountIcon, MenuType.ACCOUNT));
    }

    private void addMenu(ModelMenu menu) {
        MenuItem menuItem = new MenuItem(menu, getEventMenu(), event, menu.getMenuType());
        panel.add(menuItem, "h 60!");
        listMenuItem.add(menuItem);
    }

    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPressed(Component com, boolean open) {
                if (enableMenu) {
                    new MenuAnimation(layout, com).openMenu();

                    ArrayList<MenuItem> listRemainMenuItem = new ArrayList<>(listMenuItem);
                    listRemainMenuItem.remove((MenuItem) com);
                    listRemainMenuItem.forEach(e -> {
                        new MenuAnimation(layout, e).closeMenu();
                    });
                    return true;
                }
                return false;
            }
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        logo1 = new Logo();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(null);

        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 523, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addComponent(logo1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(logo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                                .addGap(30)
                                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gra = new GradientPaint(0, 0, new Color(33, 105, 249), getWidth(), 0, new Color(93, 58, 196));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private Logo logo1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
