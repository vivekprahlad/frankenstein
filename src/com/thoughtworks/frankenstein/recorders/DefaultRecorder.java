package com.thoughtworks.frankenstein.recorders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.playback.ComponentFinder;

/**
 * Adds events to a list.
 *
 * @author Vivek Prahlad
 */
public class DefaultRecorder implements Recorder, EventList, Runnable {
    private List events = new ArrayList();
    private List scriptListeners = new ArrayList();
    private FrankensteinEvent lastEvent = FrankensteinEvent.NULL;
    private ChangeListener listener;
    private ChangeEvent changeEvent = new ChangeEvent(this);
    private boolean stopped = false;
    protected Thread playerThread;
    private ScriptContext scriptContext;

    public DefaultRecorder(ScriptContext scriptContext) {
        this.scriptContext = scriptContext;
    }

    public DefaultRecorder(WorkerThreadMonitor monitor,
                           WindowContext context,
                           ComponentFinder finder) {
        this(new DefaultScriptContext(monitor, context, finder));
    }

    public void record(FrankensteinEvent event) {
        if (stopped) return;
        event.record(this, lastEvent);
        lastEvent = events.isEmpty() ? FrankensteinEvent.NULL : (FrankensteinEvent) events.get(events.size() - 1);
        fireChangeEvent();
    }

    private void fireChangeEvent() {
        if (listener != null)
            listener.stateChanged(changeEvent);
    }

    public void addChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void removeChangeListener() {
        this.listener = null;
    }

    public void start() {
        stopped = false;
    }

    public void stop() {
        stopped = true;
    }

    public List eventList() {
        return Collections.unmodifiableList(events);
    }

    public void setEventList(List events) {
        this.events = events;
        fireChangeEvent();
    }

    public void addEvent(FrankensteinEvent event) {
        events.add(event);
    }

    public void removeLastEvent() {
        events.remove(events.size() - 1);
    }

    public void replaceLastEvent(FrankensteinEvent event) {
        events.set(events.size() - 1, event);
    }

    public void play() {
        stop();
        playerThread = new Thread(this);
        playerThread.start();
    }

    public void reset() {
        events.clear();
        lastEvent = FrankensteinEvent.NULL;
        fireChangeEvent();
    }

    public void addScriptListener(ScriptListener listener) {
        scriptListeners.add(listener);
    }

    public void removeScriptListener(ScriptListener listener) {
        scriptListeners.remove(listener);
    }

    public void run() {
        scriptContext.play(eventList());
        for (Iterator iterator = scriptListeners.iterator(); iterator.hasNext();) {
            ScriptListener scriptListener = (ScriptListener) iterator.next();
            scriptListener.scriptCompleted(scriptContext.isScriptPassed());
        }
    }
}
