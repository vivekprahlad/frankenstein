package com.thoughtworks.frankenstein.application;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;

/**
 * Launches a designated applet after launching Frankenstein.
 *
 * @author Pavan.
 */
public class FrankensteinApplet extends JApplet implements AppletStub {
    private FrankensteinIntegration frankensteinIntegration;
    private Applet appletObject;


    public FrankensteinApplet() throws HeadlessException {
    }

    public FrankensteinApplet(Applet appletObject) throws HeadlessException {
        this.appletObject = appletObject;
    }

    public void init() {
        setPermissions();

        String className = getParameter("appletName");
        if (className == null) {
            throw new RuntimeException("Cannot find the parameter 'appletName'");
        }
        frankensteinIntegration = new FrankensteinIntegration(new NullApplication());
        frankensteinIntegration.start(null);
        Class mainClass;
        try {
            mainClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the applet");
        }
        try {
            appletObject = (Applet) mainClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot Instantiate the given applet!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot Instantiate the given applet!");
        }
        setLayout(new GridLayout(1, 0));
        add(appletObject);

        appletObject.setStub(this);
        appletObject.init();
    }

    public void destroy() {
        appletObject.destroy();
        frankensteinIntegration.stop();
    }

    public void validate() {
        appletObject.validate();
    }

    public void start() {
        appletObject.start();

    }

    public void stop() {
        appletObject.stop();
        frankensteinIntegration.stop();
    }

    public void appletResize(int width, int height) {
        resize(width, height);
    }

    public Applet getAppletObject() {
        return appletObject;
    }

    private void setPermissions() {
        SecurityManager securityManager = new SecurityManager();
        AWTPermission awtPermission = new AWTPermission("listenToAllAWTEvents", null);
        securityManager.checkPermission(awtPermission);
    }
}
