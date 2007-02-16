package com.thoughtworks.frankenstein.recorders;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.application.WorkerThreadMonitor;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.events.SelectDropDownEvent;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;
import com.thoughtworks.frankenstein.script.TestReporter;

/**
 * Ensures behaviour of the Default Script Context
 */
public class DefaultScriptContextTest extends MockObjectTestCase {
    private DefaultScriptContext context;
    private Mock testReporter;
    private WindowContext windowContext;
    private Mock finder;
    private Mock monitor;
    private ComponentFinder componentFinder;
    public Mock mockContext;

    protected void setUp() throws Exception {
        super.setUp();
        testReporter = mock(TestReporter.class);
        mockContext = mock(WindowContext.class);
        windowContext = (WindowContext) mockContext.proxy();
        finder = mock(ComponentFinder.class);
        monitor = mock(WorkerThreadMonitor.class);
        componentFinder = (ComponentFinder) finder.proxy();
        context = new DefaultScriptContext((TestReporter) testReporter.proxy(), (WorkerThreadMonitor) monitor.proxy(), windowContext, componentFinder);
    }

    public void testPropogatesExceptionIfFinishingTestFails() {
        testReporter.expects(once()).method("finishTest").will(throwException(new RuntimeException()));
        try {
            context.finishTest();
            fail();
        } catch (Exception e) {
            //Expected
        }
    }

    public void testStartsTest() {
        testReporter.expects(once()).method("startTest").with(eq("testName"));
        context.startTest("testName");
    }

    public void testNotifiesMonitorWhenPlayStarts() {
        monitor.expects(once()).method("start");
        expectFinishTest();
        context.play(Collections.EMPTY_LIST);
    }

    private void expectFinishTest() {
        testReporter.expects(once()).method("finishTest");
    }

    public void testPlayingEventSuccessfullyReportsSuccess() {
        FrankensteinEvent event = createMockFrankensteinEventAndSetPlayExpectation();
        expectSuccessReport(event);
        context.play(event);
    }

    private void expectSuccessReport(FrankensteinEvent event) {
        testReporter.expects(once()).method("reportSuccess").with(same(event));
    }

    public void testPlayingEventUnsuccessfullyReportsFailure() {
        Mock frankensteinEvent = mock(FrankensteinEvent.class);
        RuntimeException exception = new RuntimeException("Step failed");
        frankensteinEvent.expects(once()).method("play")
                .with(same(windowContext), same(componentFinder), same(context), ANYTHING)
                .will(throwException(exception));
        FrankensteinEvent event = (FrankensteinEvent) frankensteinEvent.proxy();
        testReporter.expects(once()).method("reportFailure").with(same(event), same(exception), ANYTHING);
        context.play(event);
    }

    public void testPlayOneEvent() throws InterruptedException, AWTException {
        JComboBox comboBox = new JComboBox(new Object[]{"choice1", "choice2"});
        finder.expects(once()).method("findComponent").will(returnValue(comboBox));
        monitor.expects(once()).method("start");
        monitor.expects(once()).method("waitForIdle");
        mockContext.expects(once()).method("waitForProgressBarToClose");        
        SelectDropDownEvent event = new SelectDropDownEvent("comboName2", "choice2");
        expectSuccessReport(event);
        expectFinishTest();
        context.play(list(event));
        assertEquals("choice2", comboBox.getSelectedItem());
    }

    public void testNotifiesScriptListenerOnceTestCompletes() {
        setExpectationsForListenerNotificationTests();
        List eventList = createEventList();
        ScriptListener scriptListener = createMockListenerAndSetExpectations(eventList.size());
        context.addScriptListener(scriptListener);
        context.play(eventList);
    }

    public void testRemoveScriptListener() {
        Mock listener = mock(ScriptListener.class);
        listener.expects(never()).method("scriptCompleted");
        listener.expects(never()).method("scriptStepStarted");
        ScriptListener scriptListener = (ScriptListener) listener.proxy();
        context.addScriptListener(scriptListener);
        context.removeScriptListener(scriptListener);
        setExpectationsForListenerNotificationTests();
        context.play(createEventList());
    }

    private ScriptListener createMockListenerAndSetExpectations(int sizeOfEventList) {
        Mock listener = mock(ScriptListener.class);
        for (int eventListIndex = 0; eventListIndex < sizeOfEventList; eventListIndex++) {
            listener.expects(once()).method("scriptStepStarted").with(eq(eventListIndex));
        }
        listener.expects(once()).method("scriptCompleted");
        return (ScriptListener) listener.proxy();
    }

    private void setExpectationsForListenerNotificationTests() {
        monitor.expects(once()).method("start");
        monitor.expects(atLeastOnce()).method("waitForIdle");
        mockContext.expects(atLeastOnce()).method("waitForProgressBarToClose");        
        testReporter.expects(atLeastOnce()).method("reportSuccess").withAnyArguments();
        testReporter.expects(atLeastOnce()).method("finishTest");
    }

    private List createEventList() {
        List eventList = new ArrayList();
        eventList.add(createMockFrankensteinEventAndSetPlayExpectation());
        eventList.add(createMockFrankensteinEventAndSetPlayExpectation());
        return eventList;
    }

    private FrankensteinEvent createMockFrankensteinEventAndSetPlayExpectation() {
        Mock frankensteinEventMock = mock(FrankensteinEvent.class);
        frankensteinEventMock.expects(once()).method("play")
                .with(same(windowContext), same(componentFinder), same(context), ANYTHING);
        return (FrankensteinEvent) frankensteinEventMock.proxy();
    }

    private List list(SelectDropDownEvent event) {
        return Arrays.asList(new Object[]{event});
    }
}
