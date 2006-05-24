package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.NavigateEvent;
import com.thoughtworks.frankenstein.naming.NamingStrategy;


/**
 * Understands recording when the user navigating to a menu associated with a menu bar.
 * @author Vivek Prahlad
 */
public class MenuNavigationRecorder extends AbstractComponentRecorder implements ActionListener, ChangeListener {
    private MenuElement[] selectedPath = null;

    public MenuNavigationRecorder(EventRecorder recorder, NamingStrategy namingStrategy) {
        super(recorder, namingStrategy, JMenuItem.class);
    }

    void componentShown(Component component) {
        menuItem(component).addActionListener(this);
    }

    private JMenuItem menuItem(Component component) {
        return (JMenuItem) component;
    }

    void componentHidden(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuItem(component).removeActionListener(MenuNavigationRecorder.this);
            }
        });
    }

    public void register() {
        super.register();
        MenuSelectionManager.defaultManager().addChangeListener(this);
    }

    public void unregister() {
        super.unregister();
        MenuSelectionManager.defaultManager().removeChangeListener(this);
    }

    private void menuSelected(String path) {
        recorder.record(new NavigateEvent(path));
    }

    public void actionPerformed(ActionEvent e) {
        menuSelected(menuItemText());
    }

    private String menuItemText() {
        String menuItemText = "";
        for (int i = 0; i < selectedPath.length; i++) {
            MenuElement menuElement = selectedPath[i];
            if (menuElement instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) menuElement;
                menuItemText = menuItemText + menuItem.getText() + ">";
            }
        }
        return menuItemText.substring(0, menuItemText.length() - 1);
    }

    public void stateChanged(ChangeEvent e) {
        MenuElement[] path = MenuSelectionManager.defaultManager().getSelectedPath();
        if (path != null && path.length > 0 && path[path.length - 1] instanceof JMenuItem) {
            selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        }
    }
}
