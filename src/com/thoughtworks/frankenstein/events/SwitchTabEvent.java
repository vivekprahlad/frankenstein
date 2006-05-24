package com.thoughtworks.frankenstein.events;

import java.awt.*;
import javax.swing.*;

import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

/**
 * Understands switches in tabs.
 * @author Vivek Prahlad
 */
public class SwitchTabEvent extends AbstractFrankensteinEvent {
    private String tabbedPaneName;
    private String tabTitle;

    public SwitchTabEvent(String tabbedPaneName, String tabTitle) {
        this.tabbedPaneName = tabbedPaneName;
        this.tabTitle = tabTitle;
    }

    public SwitchTabEvent(String scriptLine) {
        this(params(scriptLine)[0], params(scriptLine)[1]);
    }

    public String toString() {
        return "SwitchTabEvent: " + tabbedPaneName + ", tab: " + tabTitle;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        JTabbedPane pane = (JTabbedPane) finder.findComponent(context, tabbedPaneName);
        int count = pane.getTabCount();
        for (int i=0 ; i<count; i++) {
            if (tabTitle.equals(pane.getTitleAt(i))) {
                pane.setSelectedIndex(i);
                return;
            }
        }
        throw new RuntimeException("Could not find tab: " + tabTitle + " in tab " + tabbedPaneName);
    }

    public String target() {
        return tabbedPaneName;
    }

    public String parameters() {
        return tabTitle;
    }
}
