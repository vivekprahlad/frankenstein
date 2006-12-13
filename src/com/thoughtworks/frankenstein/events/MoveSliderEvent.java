package com.thoughtworks.frankenstein.events;

import javax.swing.*;

/**
 * Understands the behavior of slider
 */
public class MoveSliderEvent extends AbstractFrankensteinEvent{

    private String sliderName;
    private int movement;

    public MoveSliderEvent(String sliderName, int movement) {
        this.sliderName = sliderName;
        this.movement = movement;
    }

    public MoveSliderEvent(String scriptline){
        this(params(scriptline)[0], Integer.parseInt(params(scriptline)[1]));
    }

    public String toString() {
        return "MoveSliderEvent: "+sliderName+" "+movement;
    }

    public String target() {
        return sliderName;
    }


    public String parameters() {
        return Integer.toString(movement);
    }

    public  void run() {
       JSlider slider = (JSlider) finder.findComponent(context, sliderName);
       slider.setValue(movement);
    }
}
