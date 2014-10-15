package com.uwsoft.editor.controlles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CustomColorChooser extends JFrame {
    private JColorChooser jcc = null;
    private JLabel label = null;

    private ColorPickerHandler handler;

    public CustomColorChooser() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jcc = new JColorChooser();
        jcc.getSelectionModel().addChangeListener(new ColorSelection());
        getContentPane().add(jcc, BorderLayout.PAGE_START);

        setResizable(false);

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

    }

    public void setHandler(ColorPickerHandler handler) {
        this.handler = handler;
    }

    /**
     * A ChangeListener implementation for listening the color
     * selection of the JColorChooser component.
     */
    class ColorSelection implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            Color newColor = jcc.getColor();
            handler.ColorChoosen(newColor);
        }
    }
}