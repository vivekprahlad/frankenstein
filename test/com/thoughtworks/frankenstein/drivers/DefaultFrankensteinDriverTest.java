package com.thoughtworks.frankenstein.drivers;

import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.thoughtworks.frankenstein.events.ButtonEvent;
import com.thoughtworks.frankenstein.events.FrankensteinEvent;
import com.thoughtworks.frankenstein.events.actions.ClickAction;
import com.thoughtworks.frankenstein.recorders.ScriptContext;
import com.thoughtworks.frankenstein.recorders.ScriptListener;
import com.thoughtworks.frankenstein.script.TestReporter;
import com.thoughtworks.frankenstein.script.HtmlTestReporter;

/**
 * Ensures the behaviour of the DefaultFrankensteinDriver.
 *
 * @author Pavan
 * @author Prakash
 */
public class DefaultFrankensteinDriverTest extends MockObjectTestCase {
    private String testName = "test";
    public DefaultFrankensteinDriver frankensteinDriver = new DefaultFrankensteinDriver(new HtmlTestReporter(), testName);

    public void testClickButtonPlaysClickEvent() {
        Mock scriptContextMock = mock(ScriptContext.class);
        String buttonName = "button1";
        ButtonEvent buttonEvent = createButtonEventAction(buttonName);
        scriptContextMock.expects(once()).method("startMonitor");
        scriptContextMock.expects(once()).method("play").with(eq(buttonEvent));
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
        frankensteinDriver.clickButton(buttonName);
    }

    public void testClickButtonReceivesAnExceptionWhenPlayingAnEventThrowsAnException() {
        TestScriptContext scriptContext = new TestScriptContext(frankensteinDriver.getCompositeReporter());
        String buttonName = "button1";
        frankensteinDriver.setScriptContext(scriptContext);
        try {
            frankensteinDriver.clickButton(buttonName);
            fail();
        } catch (Exception e) {
        }
    }

    public void testCreationOfDefaultFrankensteinDriverObjectDoesAllInitialization() {
        String testName = "test";
        Mock testReporterMock = mock(TestReporter.class);
        testReporterMock.expects(once()).method("startTest").with(eq(testName));
        new DefaultFrankensteinDriver((TestReporter) testReporterMock.proxy(), testName);
    }

    public void testSettingScriptContextStartsTheNewScriptContextMonitor() {
        Mock scriptContextMock = mock(ScriptContext.class);
        scriptContextMock.expects(once()).method("startMonitor");
        frankensteinDriver.setScriptContext((ScriptContext) scriptContextMock.proxy());
    }

    private ButtonEvent createButtonEventAction(String buttonName) {
        return new ButtonEvent(buttonName, new ClickAction());
    }

    private class TestScriptContext implements ScriptContext {
        private TestReporter reporter;

        public TestScriptContext(TestReporter reporter) {
            this.reporter = reporter;
        }

        public void startTest(String testName) {
        }

        public void play(List events) {
        }

        public boolean isScriptPassed() {
            return false;
        }

        public void addScriptListener(ScriptListener listener) {
        }

        public void removeScriptListener(ScriptListener listener) {
        }

        public void play(FrankensteinEvent event) {
            reporter.reportFailure(event, new Exception(), null);
        }

        public void startMonitor() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
