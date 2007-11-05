package com.thoughtworks.frankenstein.application;

import com.thoughtworks.frankenstein.events.ActivateWindowEvent;
import com.thoughtworks.frankenstein.naming.DefaultNamingStrategy;
import com.thoughtworks.frankenstein.playback.DefaultWindowContext;
import com.thoughtworks.frankenstein.recorders.ComponentRecorder;
import org.jmock.MockObjectTestCase;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ensures behaviour of DefaultFrankensteinIntegration
 */
public class FrankensteinIntegrationTest extends MockObjectTestCase {
    private DefaultFrankensteinIntegration integration;
    private MockJFrame frame;

    protected void setUp() throws Exception {
        frame = new MockJFrame();
        integration = new DefaultFrankensteinIntegration(TestMainClass.class, frame,
                new NullWorkerThreadMonitor(), new DefaultWindowContext(), new DefaultNamingStrategy());
    }

    protected void tearDown() throws Exception {
        if (integration != null) {
            integration.stop();
        }
        frame.dispose();
    }

    public void testLaunchesMain() {
        String[] args = new String[]{"abc", "def"};
        integration.start(args);
        assertSame(TestMainClass.args(), args);
        integration.stop();
    }

    public void testDoesNotLaunchMainForClassesWithoutMainMethod() {
        DefaultFrankensteinIntegration integration = new DefaultFrankensteinIntegration(FrankensteinIntegrationTest.class, new MockJFrame(), new NullWorkerThreadMonitor(), new DefaultWindowContext(), new DefaultNamingStrategy());
        try {
            integration.start(null);
            fail();
        } catch (Exception e) {
        }
        integration.stop();
    }

    public void testDoesNotRegisterInvalidEvent() {
        try {
            integration.registerEvent(TestMainClass.class);
            fail();
        } catch (Exception e) {
        }
    }

    public void testRegistersValidEvent() {
        integration.registerEvent(TestFrankensteinEvent.class);
    }

    public void testRegistersRecorderWhenInRecordAndPlayMode() {
        TestComponentRecorder.registerCalled = false;
        TestComponentRecorder.unregisterCalled = false;
        integration.registerRecorder(TestComponentRecorder.class);
        integration.start(null);
        frame.getContentPane().add(new JButton("Test"));
        integration.stop();
        assertTrue(TestComponentRecorder.registerCalled);
        assertTrue(TestComponentRecorder.unregisterCalled);
        integration = null;
    }

    public void testDoesNotRegisterRecorderWhenInPlayMode() {
        DefaultFrankensteinIntegration frankensteinIntegrationInPlayMode =
                new DefaultFrankensteinIntegration(TestMainClass.class,
                        new MockJFrame(),
                        new NullWorkerThreadMonitor(),
                        new DefaultWindowContext(),
                        new DefaultNamingStrategy()
                );
        frankensteinIntegrationInPlayMode.setRecorderMode(RecorderMode.PLAY_MODE);
        TestComponentRecorder.registerCalled = false;
        TestComponentRecorder.unregisterCalled = false;
        frankensteinIntegrationInPlayMode.registerRecorder(TestComponentRecorder.class);
        frankensteinIntegrationInPlayMode.start(null);
        frankensteinIntegrationInPlayMode.stop();
        assertFalse(TestComponentRecorder.registerCalled);
        assertFalse(TestComponentRecorder.unregisterCalled);
        frankensteinIntegrationInPlayMode = null;
    }

    public void testChangesLogLevel() {
        integration.setLogLevel(Level.WARNING);
        assertEquals(Level.WARNING, Logger.getLogger("Frankenstein").getLevel());
    }

    public void testStartsRecorder() {
        integration.record();
        integration.eventRecorder.record(new ActivateWindowEvent("test"));
        assertEquals(1, integration.eventRecorder.eventList().size());
    }

    private class MockJFrame extends JFrame {
        public void setVisible(boolean b) {
        }
    }

    private static class TestMainClass {
        static String[] arg;

        static String[] args() {
            return arg;
        }

        public static void main(String[] args) {
            arg = args;
        }
    }

    public static class TestComponentRecorder implements ComponentRecorder {
        private static boolean registerCalled, unregisterCalled;

        public void register() {
            registerCalled = true;
        }

        public void unregister() {
            unregisterCalled = true;
        }
    }

}