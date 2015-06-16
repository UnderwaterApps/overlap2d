package com.uwsoft.editor.renderer.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.legacy.data.LayerItemVO;

public class LayerMapComponent extends Component {
	public ArrayList<LayerItemVO> layers = new ArrayList<>();
}
