package com.uwsoft.editor.renderer.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ProjectInfoVO {

    public ResolutionEntryVO originalResolution = new ResolutionEntryVO();

    public ArrayList<ResolutionEntryVO> resolutions = new ArrayList<>();
    public ArrayList<SceneVO> scenes = new ArrayList<SceneVO>();
    public HashMap<String, String> assetMeshMap = new HashMap<String, String>();
    public HashMap<String, MeshVO> meshes = new HashMap<String, MeshVO>();

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

    public String addNewMesh(MeshVO vo) {
        int key = -1;
        if(meshes != null && meshes.size() != 0) {
            key = Integer.parseInt(Collections.max(meshes.keySet(), new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
                }
            }));
        }
        meshes.put(++key+"", vo);

        return key+"";
    }

    public String cloneMesh(String meshId) {
        MeshVO vo = meshes.get(meshId);
        if(vo == null) return meshId;

        MeshVO newMeshVO = vo.clone();
        return addNewMesh(newMeshVO);
    }
}
