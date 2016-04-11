package com.overlap2d.plugins.tiled.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.overlap2d.plugins.tiled.data.AttributeVO;
import com.overlap2d.plugins.tiled.data.CategoryVO;

/**
 * Created by mariam on 2/5/16.
 */
public class Category extends Table {

    private CategoryVO categoryVO;
    private Array<AttributeVO> attributes;
    private Table attrTable;

    public Category(CategoryVO categoryVO) {
        this.categoryVO = categoryVO;

        attributes = categoryVO.attributes;

//        setDebug(true);

        VisLabel title = new VisLabel(categoryVO.title);
        add(title)
                .padTop(2)
                .left()
                .top();

        attrTable = new Table();
        add(attrTable)
                .padLeft(5);

        attributes.forEach(attributeVO -> addAttribute(attributeVO));
    }

    public void reInitView(Array<AttributeVO> attributes) {
        attrTable.clear();
        this.attributes = attributes;
        attributes.forEach(attributeVO -> addAttribute(attributeVO));
        attrTable.pack();
    }

    private void addAttribute(AttributeVO attributeVO) {
        Attribute attr = new Attribute(attributeVO);
        attrTable.add(attr)
                .top()
                .right()
                .padBottom(5)
                .row();
    }

    public AttributeVO getAttributeVO(String title) {
        for (AttributeVO attributeVO : attributes) {
            if (attributeVO.title.equals(title)) {
                return attributeVO;
            }
        }
        return new AttributeVO();
    }
}
