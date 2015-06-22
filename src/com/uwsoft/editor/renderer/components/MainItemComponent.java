package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;

public class MainItemComponent extends Component {
    public int uniqueId = 0;
	public String itemIdentifier = "";
	public String libraryLink = "";
    public String[] tags = null;
    public String customVars = "";
	public int entityType;
    public String layer;
}
