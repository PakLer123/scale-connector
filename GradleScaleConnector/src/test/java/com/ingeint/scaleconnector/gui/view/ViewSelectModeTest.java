package com.ingeint.scaleconnector.gui.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class ViewSelectModeTest {
    private ViewSelectMode viewSelectMode;

    @BeforeEach
    void setUp() {
        viewSelectMode = new ViewSelectMode();
    }

    @Test
    void initializationSetsCorrectTitle() {
        assertEquals("Scale Connector", viewSelectMode.getTitle());
    }

    @Test
    void initializationSetsCorrectSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int expectedWidth = (int) (screenSize.width * 0.4);
        int expectedHeight = (int) (screenSize.height * 0.7);
        assertEquals(expectedWidth, viewSelectMode.getWidth());
        assertEquals(expectedHeight, viewSelectMode.getHeight());
    }

    @Test
    void localeComboBoxHasCorrectDefaultLocale() {
        DefaultComboBoxModel<String> model = viewSelectMode.getCmbModelSelectLocale();
        assertEquals("en_US", model.getSelectedItem());
    }

//    @Test
//    void loadLocaleUpdatesComponentTexts() {
//        viewSelectMode.loadLocale();
//        assertEquals("Select Mode", viewSelectMode.getTitle());
//        assertEquals("Client", viewSelectMode.getBtnClient().getText());
//        assertEquals("TCP Server", viewSelectMode.getBtnServer().getText());
//        assertEquals("Locale", viewSelectMode.getLblLocale().getText());
//    }

//    @Test
//    void setUIFontUpdatesAllComponents() {
//        try (MockedStatic<UIManager> uiManagerMockedStatic = Mockito.mockStatic(UIManager.class)) {
//            viewSelectMode.setUIFont(new FontUIResource(new Font("Arial", Font.PLAIN, 12)));
//            uiManagerMockedStatic.verify(() -> UIManager.put(any(), any()), atLeastOnce());
//        }
//    }
}