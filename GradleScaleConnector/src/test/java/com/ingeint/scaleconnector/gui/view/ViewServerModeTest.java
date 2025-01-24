package com.ingeint.scaleconnector.gui.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.swing.*;

import com.ingeint.scaleconnector.gui.controller.ControllerViewServerMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ViewServerModeTest {

    private ViewServerMode viewServerMode;

    @BeforeEach
    void setUp() {
        viewServerMode = new ViewServerMode();
    }

//    @Test
//    void initializationSetsCorrectTitle() {
//        assertEquals("Scale Connector - ViewServerMode.title", String.format("%s - %s", SCUIFeature.get("APP_NAME"), SCUILocale.get("ViewServerMode.title")));
//    }

    @Test
    void initializationSetsCorrectSize() {
        assertEquals(1400, viewServerMode.getWidth());
        assertEquals(600, viewServerMode.getHeight());
    }

    @Test
    void initializationSetsCorrectIcon() {
        assertNotNull(viewServerMode.getIconImage());
    }

//    @Test
//    void addListenerAddsActionListenersToButtons() {
//        ControllerViewServerMode listener = mock(ControllerViewServerMode.class);
//        viewServerMode.addListener(listener);
//
//        for (AbstractButton button : viewServerMode.getButtons()) {
//            verify(button).addActionListener(listener);
//        }
//    }

//    @Test
//    void addListenerAddsActionListenersToMenuItems() {
//        ControllerViewServerMode listener = mock(ControllerViewServerMode.class);
//        viewServerMode.addListener(listener);
//
//        for (JMenuItem menuItem : viewServerMode.getMenuItems()) {
//            verify(menuItem).addActionListener(listener);
//        }
//    }
//
//    @Test
//    void addListenerAddsWindowListener() {
//        ControllerViewServerMode listener = mock(ControllerViewServerMode.class);
//        viewServerMode.addListener(listener);
//
//        verify(viewServerMode).addWindowListener(listener);
//    }
//
//    @Test
//    void toggleStreamButtonTogglesText() {
//        viewServerMode.getBtnToggleStream().doClick();
//        assertEquals("Stop Streaming", viewServerMode.getBtnToggleStream().getText());
//
//        viewServerMode.getBtnToggleStream().doClick();
//        assertEquals("Start Streaming", viewServerMode.getBtnToggleStream().getText());
//    }

    @Test
    void readPortButtonIsEnabledInitially() {
        assertTrue(viewServerMode.getBtnReadPort().isEnabled());
    }

    @Test
    void startButtonIsEnabledInitially() {
        assertTrue(viewServerMode.getBtnStart().isEnabled());
    }

//    @Test
//    void stopButtonIsDisabledInitially() {
//        assertFalse(viewServerMode.getBtnStop().isEnabled());
//    }
}