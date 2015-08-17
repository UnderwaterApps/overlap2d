package com.uwsoft.editor.renderer.components.particle;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleComponent implements Component {
	public String particleName = "";
	public ParticleEffect particleEffect;
	public float worldMultiplyer = 1f;
}
