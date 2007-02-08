package com.thoughtworks.frankenstein.events;

import javax.swing.*;

import com.thoughtworks.frankenstein.playback.MatchStrategy;

/**
 * Understands switches in tabs.
 *
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

    private String tabs(JTabbedPane pane) {
        String values = "";
        for (int i = 0; i < pane.getTabCount(); i++) {
            values += pane.getTitleAt(i) + SPACE;
        }
        return values;
    }

    public String target() {
        return tabbedPaneName;
    }

    public String parameters() {
        return tabTitle;
    }

    public void run() {
        JTabbedPane pane = (JTabbedPane) finder.findComponent(context, tabbedPaneName);
        int count = pane.getTabCount();
        for (int i = 0; i < count; i++) {
            if (MatchStrategy.matchValues(pane.getTitleAt(i), tabTitle)) {
                pane.setSelectedIndex(i);
                return;
            }
        }
        throw new RuntimeException("Could not find tab: " + tabTitle + " in tab " + tabbedPaneName
                + ", available tabs are: " + tabs(pane));
    }
}
