package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.thoughtworks.frankenstein.events.ActivateInternalFrameEvent;
import com.thoughtworks.frankenstein.events.CloseInternalFrameEvent;
import com.thoughtworks.frankenstein.events.InternalFrameShownEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;

/**
 * Understands recording internal frame events.
 * @author Vivek Prahlad
 */
public class InternalFrameRecorder extends AbstractComponentRecorder implements InternalFrameListener {

    public InternalFrameRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JInternalFrame.class);
    }

    void componentShown(Component component) {
        frame(component).addInternalFrameListener(this);
        recorder.record(new InternalFrameShownEvent(frame(component).getTitle()));
    }

    private JInternalFrame frame(Component component) {
        return (JInternalFrame) component;
    }

    void componentHidden(Component component) {
        frame(component).removeInternalFrameListener(this);
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        recorder.record(new CloseInternalFrameEvent(e.getInternalFrame().getTitle()));
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
        recorder.record(new ActivateInternalFrameEvent(e.getInternalFrame().getTitle()));
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
