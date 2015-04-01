/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.controlles;

import com.uwsoft.editor.controlles.handlers.ColorPickerHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

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
