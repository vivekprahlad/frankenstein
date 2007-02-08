package com.thoughtworks.frankenstein.events;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import org.jmock.Mock;

import com.thoughtworks.frankenstein.common.RobotFactory;
import com.thoughtworks.frankenstein.playback.ComponentFinder;
import com.thoughtworks.frankenstein.playback.WindowContext;

/**
 * Ensures the behavior of MoveSliderEvent
 */
public class MoveSliderEventTest extends AbstractEventTestCase {

    public void testEqualsAndHashCode() {
        MoveSliderEvent eventOne = new MoveSliderEvent("slider", 10);
        MoveSliderEvent eventTwo = new MoveSliderEvent("slider", 10);
        assertEquals(eventOne, eventTwo);
        assertEquals(eventOne.hashCode(),eventTwo.hashCode());

    }

    public void testToString() {
       assertEquals("MoveSliderEvent: slider 10",new MoveSliderEvent("slider",10).toString());
    }

    public void testAction() {
       assertEquals("MoveSlider",new MoveSliderEvent("slider",10).action());
    }

    public void testTarget() {
        assertEquals("slider",new MoveSliderEvent("slider",10).target());
    }

    public void testParameters() {
        assertEquals("10",new MoveSliderEvent("slider",10).parameters());
    }

    public void testScriptLine() {
        assertEquals("move_slider \"slider\" , \"10\"",new MoveSliderEvent("slider",10).scriptLine());
    }

    public void testPlaysEvent() throws Exception {
        JSlider slider = new JSlider(1, 100,10);
        slider.setName("slider");
        MoveSliderEvent event = new MoveSliderEvent("slider", 5);
        Mock mockComponentFinder = mock(ComponentFinder.class);
        Mock mockContext = mock(WindowContext.class);
        WindowContext context = (WindowContext) mockContext.proxy();
        Mock mockChangeListener = mock(ChangeListener.class);
        slider.addChangeListener((ChangeListener) mockChangeListener.proxy());
        mockComponentFinder.expects(once()).method("findComponent").with(same(context), eq("slider")).will(returnValue(slider));
        mockChangeListener.expects(once()).method("stateChanged");
        event.play(context, (ComponentFinder) mockComponentFinder.proxy(), null, RobotFactory.getRobot());
        waitForIdle();
    }

    protected FrankensteinEvent createEvent() {
        return new MoveSliderEvent("slider",10);
    }
}
