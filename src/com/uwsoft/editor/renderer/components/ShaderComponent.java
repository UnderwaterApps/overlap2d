package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderComponent implements Component {
	public String shaderName;
	private ShaderProgram shaderProgram = null;

	public void setShader(String name, ShaderProgram program) {
		shaderName = name;
		shaderProgram = program;
	}

	public ShaderProgram getShader() {
		return shaderProgram;
	}

	public void clear() {
		shaderName = null;
		shaderProgram = null;
	}
}
