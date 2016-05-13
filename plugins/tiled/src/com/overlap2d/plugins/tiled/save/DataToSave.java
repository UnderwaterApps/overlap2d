package com.overlap2d.plugins.tiled.save;

import com.badlogic.gdx.utils.Array;
import com.overlap2d.plugins.tiled.data.ParameterVO;
import com.overlap2d.plugins.tiled.data.TileVO;

/**
 * Created by mariam on 3/23/16.
 */
public class DataToSave {

    private Array<TileVO> tiles;
    private ParameterVO parameterVO;

    public DataToSave() {
        tiles = new Array<>();
        parameterVO = new ParameterVO();
    }

    public void addTile(String tileDrawableName) {
        TileVO newTile = new TileVO(tileDrawableName);
        if (!tiles.contains(newTile, false)) {
            tiles.add(new TileVO(tileDrawableName));
        }
    }

    public void removeTile(String tileDrawableName) {
        tiles.forEach(tile -> {
            if (tile.regionName.equals(tileDrawableName)) {
                tiles.removeValue(tile, false);
            }
        });

    }

    public void setTileGridOffset(TileVO tileVO) {
        tiles.forEach(tile -> {
            if (tile.regionName.equals(tileVO.regionName)) {
                tile.gridOffset = tileVO.gridOffset;
                return;
            }
        });
    }

    public Array<String> getTileNames() {
        Array<String> tileNames = new Array<>();
        tiles.forEach(tile -> tileNames.add(tile.regionName));
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
