package com.thoughtworks.frankenstein.events;

import java.io.File;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Understands selecting a file from a filechooser.
 */
public class SelectFilesEvent extends AbstractFrankensteinEvent {

    private String[] fileNames;
    private String scriptline;

    public SelectFilesEvent(String scriptline) {
        this.scriptline = scriptline;
        this.fileNames = scriptline.split(",");
    }

    public void record(EventList list, FrankensteinEvent lastEvent) {
        if (lastEvent instanceof SelectFileEvent || lastEvent instanceof SelectFilesEvent)
          list.replaceLastEvent(this);
        else {
            list.addEvent(this);
        }
    }

    public String toString() {
        return "SelectFilesEvent: " + scriptline;
    }

    public String target() {
        return scriptline;
    }

    public void run() {
        finder.findFileChooser(context).setSelectedFiles(files());
    }

    private File[] files() {
        File[] files = new File[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            files[i] = new File(fileNames[i]);
        }
        return files;
    }
}
