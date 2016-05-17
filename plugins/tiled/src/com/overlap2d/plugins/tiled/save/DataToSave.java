package com.overlap2d.plugins.tiled.save;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.overlap2d.plugins.tiled.data.ParameterVO;
import com.overlap2d.plugins.tiled.data.TileVO;

import java.util.stream.StreamSupport;

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
            tiles.add(newTile);
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
        StreamSupport.stream(tiles.spliterator(), false)
                .filter(tile -> tile.regionName.equals(tileVO.regionName))
                .findFirst()
                .ifPresent(t -> t.gridOffset = tileVO.gridOffset);
    }

    public Vector2 getTileGridOffset(String regionName) {
        return StreamSupport.stream(tiles.spliterator(), false)
                .filter(tile -> tile.regionName.equals(regionName))
                .findFirst()
                .get()
                .gridOffset;
    }

    public TileVO getTile(String regionName) {
        return StreamSupport.stream(tiles.spliterator(), false)
                .filter(tile -> tile.regionName.equals(regionName))
                .findFirst()
                .get();
    }

    public Array<TileVO> getTiles() {
        return tiles;
    }

    public boolean containsTile(String regionName) {
        return StreamSupport.stream(tiles.spliterator(), false).anyMatch(tile -> tile.regionName.equals(regionName));
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
