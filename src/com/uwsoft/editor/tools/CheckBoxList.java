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

package com.uwsoft.editor.tools;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxList extends JList
{
  protected static Border noFocusBorder =
                                new EmptyBorder(1, 1, 1, 1);

  public CheckBoxList()
  {
     setCellRenderer(new CellRenderer());

     addMouseListener(new MouseAdapter()
        {
           public void mousePressed(MouseEvent e)
           {
              int index = locationToIndex(e.getPoint());

              if (index != -1) {
                 JCheckBox checkbox = (JCheckBox)
                             getModel().getElementAt(index);
                 checkbox.setSelected(
                                    !checkbox.isSelected());
                 repaint();
              }
           }
        }
     );

     setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  protected class CellRenderer implements ListCellRenderer
  {
     public Component getListCellRendererComponent(
                   JList list, Object value, int index,
                   boolean isSelected, boolean cellHasFocus)
     {
        JCheckBox checkbox = (JCheckBox) value;
        checkbox.setBackground(isSelected ?
                getSelectionBackground() : getBackground());
        checkbox.setForeground(isSelected ?
                getSelectionForeground() : getForeground());
        checkbox.setEnabled(isEnabled());
        checkbox.setFont(getFont());
        checkbox.setFocusPainted(false);
        checkbox.setBorderPainted(true);
        checkbox.setBorder(isSelected ?
         UIManager.getBorder(
          "List.focusCellHighlightBorder") : noFocusBorder);
        return checkbox;
     }
  }
  
  public void addCheckbox(JCheckBox checkBox) {
      ListModel currentList = this.getModel();
      JCheckBox[] newList = new JCheckBox[currentList.getSize() + 1];
      for (int i = 0; i < currentList.getSize(); i++) {
              newList[i] = (JCheckBox) currentList.getElementAt(i);
      }
      newList[newList.length - 1] = checkBox;
      setListData(newList);
}

}
