package com.thoughtworks.frankenstein.events;

import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.recorders.ScriptContext;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Understands selecting a file from a filechooser.
 */
public class SelectFileEvent extends AbstractFrankensteinEvent {

    private String fileName;

    public SelectFileEvent(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {
        return "SelectFileEvent: " + fileName;
    }

    public void play(WindowContext context, ComponentFinder finder, ScriptContext scriptContext, Robot robot) {
        finder.fileChooser(context).setSelectedFile(new File(fileName));
    }

    public String target() {
        return fileName;
    }
}
