package com.thoughtworks.frankenstein.playback;

import com.thoughtworks.frankenstein.naming.UnnamedComponentMatchingRule;
import com.thoughtworks.frankenstein.naming.ComponentHierarchyWalker;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Understands finding menu items.
 *
 * @author vivek
 */
public class MenuFinder {
    private JPopupMenu popupMenu;

    public JMenuItem find(String path) {
       String[] pathElements = path.split(">");
       if (popupMenu != null) {
           return findPopupMenu(pathElements);
       } else {
           for (int i = 0; i < Frame.getFrames().length; i++) {
               try {
                   java.util.List menuBars = findMenuBar(Frame.getFrames(), i);
                   if (!menuBars.isEmpty()) {
                       return findMenuItem(menuBars, pathElements);
                   }
               } catch (Exception e) {
                   //Ignore
               }
           }
       }
       throw new RuntimeException("Unable to find menu with path: " + path);
    }

    public void menuDisplayed(JPopupMenu menu) {
        popupMenu = menu;
        Logger.getLogger("Frankenstein").info("Menu Displayed");
    }

    public void menuHidden() {
        popupMenu = null;
        Logger.getLogger("Frankenstein").info("Menu Hidden");
    }

    private JMenuItem findMenuItem(java.util.List menuBars, String[] pathElements) {
        JMenuBar bar = (JMenuBar) menuBars.get(0);
        JMenuItem menuItem = findTopLevelMenu(bar, pathElements[0]);
        for (int j = 1; j < pathElements.length; j++) {
            menuItem = findMenu((JMenu) menuItem, pathElements[j]);
        }
        return menuItem;
    }

    private JMenuItem findPopupMenu(String[] pathElements) {
        JMenuItem menuItem = findTopLevelMenu(pathElements[0]);
        for (int j = 1; j < pathElements.length; j++) {
            menuItem = findMenu((JMenu) menuItem, pathElements[j]);
        }
        popupMenu = null;
        return menuItem;
    }

    private JMenuItem findTopLevelMenu(String pathElement) {
        for (int i=0; i<popupMenu.getComponentCount(); i++) {
            if (popupMenu.getComponent(i) instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) popupMenu.getComponent(i);
                if (pathElement.equals(item.getText())) return item;
            }
        }
        throw new RuntimeException("Unable to find top level popup menu item: " + pathElement);
    }

    private java.util.List findMenuBar(Frame[] frames, int i) {
        Frame frame = frames[i];
        UnnamedComponentMatchingRule rule = new UnnamedComponentMatchingRule(JMenuBar.class);
        new ComponentHierarchyWalker().matchComponentsIn(frame, rule);
        return rule.unnamedComponents();
    }

    private JMenu findTopLevelMenu(JMenuBar bar, String pathElement) {
        for (int i = 0; i < bar.getMenuCount(); i++) {
            if (bar.getMenu(i).getText().equals(pathElement)) return bar.getMenu(i);
        }
        throw new RuntimeException("Unable to find top level menu" + pathElement);
    }

    private JMenuItem findMenu(JMenu menuItem, String pathElement) {
        for (int i = 0; i < menuItem.getMenuComponentCount(); i++) {
            if (menuItem.getMenuComponent(i) instanceof JMenuItem) {
                JMenuItem jmenuItem = (JMenuItem) menuItem.getMenuComponent(i);
                if (jmenuItem.getText().equals(pathElement)) return jmenuItem;
            }
        }
        throw new RuntimeException  ("Unable to find menu" + pathElement);
    }

}
