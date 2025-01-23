//// In ControllerViewServerModeTest.java
//package com.ingeint.scaleconnector.gui.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import com.ingeint.scaleconnector.base.util.Server;
//import com.ingeint.scaleconnector.gui.view.ViewServerMode;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.net.URI;
//
//public class ControllerViewServerModeTest {
//
//    private ControllerViewServerMode controller;
//    private ViewServerMode mockViewServerMode;
//    private Server mockServer;
//
//    @BeforeEach
//    void setUp() {
//        mockViewServerMode = mock(ViewServerMode.class);
//        mockServer = mock(Server.class);
//        controller = new ControllerViewServerMode();
//        controller.getViewServerMode().viewServerMode = mockViewServerMode;
//        controller.getViewServerMode(). = mockServer;
//    }
//
//    @Test
//    void actionPerformedClosesApplication() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getMenuItemClose());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockViewServerMode).dispose();
//    }
//
//    @Test
//    void actionPerformedOpensAboutDialog() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getMenuItemAbout());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockViewServerMode).setVisible(true);
//    }
//
//    @Test
//    void actionPerformedChangesMode() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getMenuItemChangeMode());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockViewServerMode).dispose();
//    }
//
//    @Test
//    void actionPerformedOpensDocumentation() throws Exception {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getMenuItemDocumentation());
//        Desktop mockDesktop = mock(Desktop.class);
//        Desktop.setDesktop(mockDesktop);
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockDesktop).browse(new URI(SCUIFeature.get("DOCUMENTATION")));
//    }
//
//    @Test
//    void actionPerformedStartsServer() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getBtnStart());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockServer).start();
//    }
//
//    @Test
//    void actionPerformedStopsServer() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getBtnStop());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockServer).stop();
//    }
//
//    @Test
//    void actionPerformedReadsPort() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getBtnReadPort());
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockViewServerMode).getLblPortValue();
//    }
//
//    @Test
//    void actionPerformedTogglesLiveStream() {
//        ActionEvent mockEvent = mock(ActionEvent.class);
//        JToggleButton mockToggleButton = mock(JToggleButton.class);
//        when(mockEvent.getSource()).thenReturn(mockViewServerMode.getBtnToggleStream());
//        when(mockViewServerMode.getBtnToggleStream()).thenReturn(mockToggleButton);
//
//        controller.actionPerformed(mockEvent);
//
//        verify(mockToggleButton).setText(anyString());
//    }
//}