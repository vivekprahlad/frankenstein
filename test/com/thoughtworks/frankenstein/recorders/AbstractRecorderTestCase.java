package com.thoughtworks.frankenstein.recorders;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import com.thoughtworks.frankenstein.common.WaitForIdle;

/**
 * Base class for all recorder tests.
 */
class AbstractRecorderTestCase extends MockObjectTestCase {
    protected Mock mockRecorder;

    protected void setUp() throws Exception {
        super.setUp();
        mockRecorder = mock(Recorder.class);
    }

    protected void waitForIdle() {
        new WaitForIdle().waitForIdle();
    }

}
