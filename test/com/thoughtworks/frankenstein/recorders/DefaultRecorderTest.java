package com.thoughtworks.frankenstein.recorders;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.events.*;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Ensures behaviour of DefaultRecorder
 */
public class DefaultRecorderTest extends MockObjectTestCase {
    private DefaultRecorder recorder;
    private Mock scriptContextMock;

    protected void setUp() throws Exception {
        super.setUp();
        scriptContextMock = mock(ScriptContext.class);
        recorder = new DefaultRecorder((ScriptContext) scriptContextMock.proxy());
    }

    public void testAddOneEvent() {
        SelectDropDownEvent event = new SelectDropDownEvent("comboName", "choice");
        recorder.record(event);
        assertEquals(1, recorder.eventList().size());
        assertEquals(event, recorder.eventList().get(0));
    }

    public void testCoalesceEventsWithSameTarget() {
        recorder.record(new SelectDropDownEvent("comboName", "choice2"));
        recorder.record(new SelectDropDownEvent("comboName", "choice"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(new SelectDropDownEvent("comboName", "choice"), recorder.eventList().get(0));
    }

    public void testDoesNotCoalesceKeystrokeEvents() {
        KeyStrokeEvent event = new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, '0');
        recorder.record(event);
        recorder.record(event);
        assertEquals(2, recorder.eventList().size());
        assertEquals(event, recorder.eventList().get(0));
        assertEquals(event, recorder.eventList().get(1));
    }

    public void testDoesNotCoalesceButtonClicks() {
        FrankensteinEvent event = new ButtonEvent("button", new ClickAction());
        recorder.record(event);
        recorder.record(event);
        assertEquals(2, recorder.eventList().size());
        assertEquals(event, recorder.eventList().get(0));
        assertEquals(event, recorder.eventList().get(1));
    }

    public void testStopTableCellEditEventAfterCancelEventRemovesCancel() {
        recorder.record(new CancelTableEditEvent("tableName"));
        recorder.record(new StopTableEditEvent("tableName"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(new StopTableEditEvent("tableName"), recorder.eventList().get(0));
    }

    public void testCancelTableCellEditEventAfterStopTableEditlEventDoesNotRecordCancel() {
        recorder.record(new StopTableEditEvent("tableName"));
        recorder.record(new CancelTableEditEvent("tableName"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(new StopTableEditEvent("tableName"), recorder.eventList().get(0));
    }

    public void testEnterTextFollowedByCancelRecordsBoth() {
        recorder.record(new EnterTextEvent("textField", "abc"));
        recorder.record(new CancelTableEditEvent("tableName"));
        assertEquals(2, recorder.eventList().size());
        assertEquals(new EnterTextEvent("textField", "abc"), recorder.eventList().get(0));
        assertEquals(new CancelTableEditEvent("tableName"), recorder.eventList().get(1));
    }

    public void testEnterTextFollowedByStopRecordsBoth() {
        recorder.record(new EnterTextEvent("textField", "abc"));
        recorder.record(new StopTableEditEvent("tableName"));
        assertEquals(2, recorder.eventList().size());
        assertEquals(new EnterTextEvent("textField", "abc"), recorder.eventList().get(0));
        assertEquals(new StopTableEditEvent("tableName"), recorder.eventList().get(1));
    }

    public void testEditTableCellFollowedByCancelTableCellEditRemovesBoth() {
        recorder.record(new EditTableCellEvent("testTable", 1, 1));
        recorder.record(new CancelTableEditEvent("testTable"));
        assertEquals(0, recorder.eventList().size());
    }

    public void testWindowActivatedAfterDialogClosedIsNotRecorded() {
        DialogClosedEvent dialogClosedEvent = new DialogClosedEvent("title");
        recorder.record(dialogClosedEvent);
        recorder.record(new ActivateWindowEvent("myWindow"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(dialogClosedEvent, recorder.eventList().get(0));
    }

    public void testWindowActivatedAfterDialogShownIsNotRecorded() {
        DialogShownEvent dialogShownEvent = new DialogShownEvent("title");
        recorder.record(dialogShownEvent);
        recorder.record(new ActivateWindowEvent("myWindow"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(dialogShownEvent, recorder.eventList().get(0));
    }

    public void testDoesNotRemoveCancelEventAfterOtherEvents() {
        recorder.record(new KeyStrokeEvent(KeyEvent.ALT_DOWN_MASK, '0'));
        recorder.record(new CancelTableEditEvent("testTable"));
        assertEquals(2, recorder.eventList().size());
        assertEquals(new CancelTableEditEvent("testTable"), recorder.eventList().get(1));
    }

    public void testAddEvent() {
        SelectDropDownEvent event = new SelectDropDownEvent("comboName", "choice");
        recorder.addEvent(event);
        recorder.addEvent(event);
        assertEquals(2, recorder.eventList().size());
        assertEquals(event, recorder.eventList().get(0));
        assertEquals(event, recorder.eventList().get(1));
    }

    public void testRemoveLastEvent() {
        recorder.addEvent(new SelectDropDownEvent("comboName1", "choice1"));
        recorder.addEvent(new SelectDropDownEvent("comboName2", "choice2"));
        recorder.removeLastEvent();
        assertEquals(1, recorder.eventList().size());
        assertEquals(new SelectDropDownEvent("comboName1", "choice1"), recorder.eventList().get(0));
    }

    public void testReplaceLastEvent() {
        recorder.addEvent(new SelectDropDownEvent("comboName1", "choice1"));
        recorder.addEvent(new SelectDropDownEvent("comboName2", "choice2"));
        recorder.replaceLastEvent(new DialogShownEvent("2"));
        assertEquals(2, recorder.eventList().size());
        assertEquals(new SelectDropDownEvent("comboName1", "choice1"), recorder.eventList().get(0));
        assertEquals(new DialogShownEvent("2"), recorder.eventList().get(1));
    }

    public void testAddRecorderListener() {
        Mock mockRecorderListener = mock(ChangeListener.class);
        recorder.addChangeListener((ChangeListener) mockRecorderListener.proxy());
        mockRecorderListener.expects(once()).method("stateChanged");
        recorder.record(new SelectDropDownEvent("comboName2", "choice2"));
    }

    public void testRemoveRecorderListener() {
        Mock mockRecorderListener = mock(ChangeListener.class);
        recorder.addChangeListener((ChangeListener) mockRecorderListener.proxy());
        recorder.removeChangeListener();
        recorder.addEvent(new SelectDropDownEvent("comboName2", "choice2"));
    }

    public void testStopRecording() {
        recorder.stop();
        recorder.record(new SelectDropDownEvent("comboName2", "choice2"));
        assertEquals(0, recorder.eventList().size());
    }

    public void testStartRecording() {
        recorder.stop();
        recorder.start();
        recorder.record(new SelectDropDownEvent("comboName2", "choice2"));
        assertEquals(1, recorder.eventList().size());
        assertEquals(new SelectDropDownEvent("comboName2", "choice2"), recorder.eventList().get(0));
    }

    public void testReset() {
        Mock mockChangeListener = mock(ChangeListener.class);
        recorder.record(new ActivateWindowEvent("title"));
        mockChangeListener.expects(once()).method("stateChanged").with(ANYTHING);
        recorder.addChangeListener((ChangeListener) mockChangeListener.proxy());
        recorder.reset();
        assertEquals(0, recorder.eventList().size());
    }

    public void testSetEventList() {
        Mock mockChangeListener = mock(ChangeListener.class);
        recorder.record(new ActivateWindowEvent("title"));
        mockChangeListener.expects(once()).method("stateChanged").with(ANYTHING);
        recorder.addChangeListener((ChangeListener) mockChangeListener.proxy());
        recorder.setEventList(new ArrayList());
        assertEquals(0, recorder.eventList().size());
    }

    public void testPlayRunsEventsInScriptContext() throws InterruptedException {
        MockScriptContext scriptContext = new MockScriptContext();
        recorder = new DefaultRecorder(scriptContext);
        recorder.record(new ActivateWindowEvent("title"));
        recorder.play();
        recorder.playerThread.join();
        assertEquals(recorder.eventList(), scriptContext.eventList);
    }

    public void testAddScriptListener() {
        ScriptListener listener = new ScriptListener() {
            public void scriptCompleted(boolean b) {
            }

            public void scriptStepStarted(int frankensteinEvent) {
            }
        };
        scriptContextMock.expects(once()).method("addScriptListener").with(same(listener));
        recorder.addScriptListener(listener);
    }

    public void testRemoveScriptListener() {
        ScriptListener listener = new ScriptListener() {
            public void scriptCompleted(boolean b) {
            }

            public void scriptStepStarted(int frankensteinEvent) {
            }
        };
        scriptContextMock.expects(once()).method("removeScriptListener").with(same(listener));
        recorder.removeScriptListener(listener);
    }

    private class MockScriptContext implements ScriptContext {
        private List eventList;

        public void startTest(String testName) {
        }

        public void play(List events) {
            eventList = events;
        }

        public boolean isScriptPassed() {
            return false;
        }

        public void addScriptListener(ScriptListener scriptListener) {

        }

        public void removeScriptListener(ScriptListener scriptListener) {

        }

        public void play(FrankensteinEvent event) {
        }

        public void startMonitor() {
        }

        public void addTestReporter(TestReporter testReporter) {
        }

        public void removeAllTestReporters() {
        }

    }
}
