package com.uwsoft.editor.renderer.data;

/**
 * Created by CyberJoe on 6/18/2015.
 */
public class FrameRange {
    public String name;
    public int startFrame;
    public int endFrame;

    public FrameRange() {

    }

    public FrameRange(String name, int startFrame, int endFrame) {
        this.name = name;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
}
