package com.thoughtworks.frankenstein.recorders;

import com.thoughtworks.frankenstein.naming.NamingStrategy;
import com.thoughtworks.frankenstein.events.AssertLabelEvent;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 */
public class AssertLabelRecorder extends AbstractComponentRecorder implements MouseListener {
    public AssertLabelRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JLabel.class);
    }

    void componentShown(Component component) {
       label(component).addMouseListener(this);
    }

    private JLabel label(Component component) {
        return ((JLabel)component);
    }

    void componentHidden(Component component) {
        label(component).removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
       if ((e.getButton() == MouseEvent.BUTTON3)&&(e.getModifiers()==(6))){
            JLabel label = (JLabel) e.getSource();
            recorder.record(new AssertLabelEvent("label", label.getText()));
        }
    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
