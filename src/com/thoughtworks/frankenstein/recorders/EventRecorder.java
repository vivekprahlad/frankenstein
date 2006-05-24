package com.thoughtworks.frankenstein.recorders;

import java.util.List;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;

/**
 * Low level event recorder.
 * @author Vivek Prahlad
 */
public interface EventRecorder {
    void record(FrankensteinEvent event);

    void addChangeListener(ChangeListener listener);

    void removeChangeListener();

    List eventList();

    void setEventList(List events);
}
