package com.overlap2d.plugins.tiled.save;

import com.badlogic.gdx.utils.Array;
import com.overlap2d.plugins.tiled.data.ParameterVO;

/**
 * Created by mariam on 3/23/16.
 */
public class DataToSave {

    private Array<String> tileNames;
    private ParameterVO parameterVO;

    public DataToSave() {
        tileNames = new Array<String>();
        parameterVO = new ParameterVO();
    }

    public void addTile(String tileDrawableName) {
        tileNames.add(tileDrawableName);
    }

    public void removeTile(String tileDrawableName) {
        tileNames.removeValue(tileDrawableName, false);
    }

    public Array<String> getTileNames() {
        return tileNames;
    }

    public ParameterVO getParameterVO() {
        return parameterVO;
    }

    public void setParameterVO(ParameterVO parameterVO) {
        this.parameterVO = parameterVO;
    }

    public void setGrid(float gridWidth, float gridHeight) {
        parameterVO.gridWidth = gridWidth;
        parameterVO.gridHeight = gridHeight;
    }
}
