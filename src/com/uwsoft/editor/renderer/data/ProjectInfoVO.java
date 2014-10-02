package com.uwsoft.editor.renderer.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ProjectInfoVO {

    public ResolutionEntryVO originalResolution = new ResolutionEntryVO();

    public ArrayList<ResolutionEntryVO> resolutions = new ArrayList<>();
    public ArrayList<SceneVO> scenes = new ArrayList<SceneVO>();
    public HashMap<String, Integer> assetMeshMap = new HashMap<String, Integer>();
    public HashMap<Integer, MeshVO> meshes = new HashMap<Integer, MeshVO>();

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        json.prettyPrint(str);
        return str;
    }

    public ResolutionEntryVO getResolution(String name) {
        for (ResolutionEntryVO resolution : resolutions) {
            if (resolution.name.equalsIgnoreCase(name)) {
                return resolution;
            }
        }
        return null;
    }

    public Integer addNewMesh(MeshVO vo) {
        Integer key = -1;
        if(meshes != null && meshes.size() != 0) {
            key = Collections.max(meshes.keySet());
        }
        meshes.put(++key, vo);

        return key;
    }

    public Integer cloneMesh(Integer meshId) {
        MeshVO vo = meshes.get(meshId);
        if(vo == null) return meshId;

        MeshVO newMeshVO = vo.clone();
        return addNewMesh(newMeshVO);
    }
}
