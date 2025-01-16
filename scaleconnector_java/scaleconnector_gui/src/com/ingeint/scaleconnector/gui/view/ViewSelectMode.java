package com.ingeint.scaleconnector.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.ingeint.scaleconnector.gui.controller.ControllerViewSelectMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.feature.SCUIStandard;

import net.miginfocom.swing.MigLayout;

public class ViewSelectMode extends JFrame {

    private static final long serialVersionUID = -6046414331203762804L;

    private List<JButton> buttons;
    private JPanel pnlCentral;
    private JPanel pnlLogo;
    private JLabel lblLogo;
    private JLabel lblTitle; // Add a title label
    private JButton btnServer;
    private JButton btnClient;
    private JLabel lblLocale;

    private JComboBox<String> cmbSelectLocale;
    private DefaultComboBoxModel<String> cmbModelSelectLocale;

    public ViewSelectMode() {
        super("Scale Connector");
        System.out.println("Initializing ViewSelectMode...");
        
        // Set font
        setUIFont(new FontUIResource(new Font("Segoe UI", Font.PLAIN, 12)));

        // Calculate dynamic dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.4);
        int height = (int) (screenSize.height * 0.5);

        setLayout(new BorderLayout());
        setIconImage(SCUIStandard.ICON);
        setResizable(false);
        setLocationRelativeTo(null); // Center window on screen

        buttons = new ArrayList<>();

        // Create the logo panel
        createLogoPanel();
        add(pnlLogo, BorderLayout.NORTH);

        // Create the central panel with MigLayout
        createCentralPanel();
        add(pnlCentral, BorderLayout.CENTER);

        // Set size and make visible
        setSize(width, height);
        System.out.println("ViewSelectMode initialized. Width = " + width + ", Height = " + height);
    }

    private void createLogoPanel() {
        pnlLogo = new JPanel(new BorderLayout());
        lblLogo = new JLabel();
        lblLogo.setIcon(SCUIStandard.LOGO);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        lblTitle = new JLabel("Scale Connector", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.DARK_GRAY);

        pnlLogo.add(lblLogo, BorderLayout.CENTER);
        pnlLogo.add(lblTitle, BorderLayout.SOUTH);
        pnlLogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        System.out.println("Logo panel created.");
    }

    private void createCentralPanel() {
        pnlCentral = new JPanel();
        pnlCentral.setLayout(new MigLayout(
                "wrap 1, insets 20, align center",
                "[grow, center]",
                "[]20[]20[]20[]"
        ));
        pnlCentral.setBorder(BorderFactory.createTitledBorder("Select Mode"));

        // Locale label
        lblLocale = new JLabel("Locale:");
        lblLocale.setFont(new Font("Arial", Font.PLAIN, 14));
        pnlCentral.add(lblLocale, "growx");

        // Locale combo box
        String[] locales = SCUILocale.list();
        if (locales.length == 0) {
            locales = new String[]{"en_US"}; // Fallback locale
        }
        cmbModelSelectLocale = new DefaultComboBoxModel<>(locales);
        String defaultLocale = SCUIFeature.get("DEFAULT_LOCALE");
        if (defaultLocale == null || defaultLocale.isEmpty()) {
            defaultLocale = "en_US";
        }
        cmbModelSelectLocale.setSelectedItem(defaultLocale);

        cmbSelectLocale = new JComboBox<>(cmbModelSelectLocale);
        cmbSelectLocale.setToolTipText("Select the language/locale for the application");
        pnlCentral.add(cmbSelectLocale, "growx");

        // Server button
        btnServer = new JButton("Start Server");
        btnServer.setFont(new Font("Arial", Font.PLAIN, 14));
        btnServer.setToolTipText("Start the server in TCP mode");
        pnlCentral.add(btnServer, "growx");
        buttons.add(btnServer);

        // Client button
        btnClient = new JButton("Start Client");
        btnClient.setFont(new Font("Arial", Font.PLAIN, 14));
        btnClient.setToolTipText("Connect to a server in client mode");
        pnlCentral.add(btnClient, "growx");
        buttons.add(btnClient);

        System.out.println("Central panel created.");
    }

    public void loadLocale() {
        setTitle(SCUILocale.get("ViewSelectMode.title"));
        btnClient.setText(SCUILocale.get("ViewSelectMode.btnClient"));
        btnServer.setText(SCUILocale.get("ViewSelectMode.btnServer"));
        lblLocale.setText(SCUILocale.get("ViewSelectMode.lblLocale"));
    }

    public void addListener(ControllerViewSelectMode listener) {
        for (JButton button : buttons) {
            button.addActionListener(listener);
        }
        addWindowListener(listener);
        cmbSelectLocale.addItemListener(listener);
    }

    public JButton getBtnServer() {
        return btnServer;
    }

    public JButton getBtnClient() {
        return btnClient;
    }

    public JLabel getLblLocale() {
        return lblLocale;
    }

    public DefaultComboBoxModel<String> getCmbModelSelectLocale() {
        return cmbModelSelectLocale;
    }
    private void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
