package com.thoughtworks.frankenstein.events;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.frankenstein.recorders.EventList;

/**
 * Understands selecting a file from a filechooser.
 */
public class SelectFilesEvent extends AbstractFrankensteinEvent {

    private String[] fileNames;
    private String fileNamesWithPath;

    public SelectFilesEvent(String[] fileNames) {
        this.fileNames = fileNames;
        fileNamesWithPath = StringUtils.join(fileNames, ',');
    }

    public SelectFilesEvent(String scriptline) {
        this.fileNamesWithPath = scriptline;
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
        return "SelectFilesEvent: " + fileNamesWithPath;
    }

    public String target() {
        return fileNamesWithPath;
    }

    public void run() {
        finder.findFileChooser(context).setSelectedFiles(files());
    }

    public String scriptLine(ScriptStrategy scriptStrategy) {
        return scriptStrategy.toMethod(action()) + scriptStrategy.enclose(scriptStrategy.array(fileNames));
    }

    private File[] files() {
        File[] files = new File[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            files[i] = new File(fileNames[i]);
        }
        return files;
    }
}
