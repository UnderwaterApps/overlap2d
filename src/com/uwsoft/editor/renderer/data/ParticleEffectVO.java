package com.uwsoft.editor.renderer.data;


public class ParticleEffectVO extends MainItemVO {
	public String particleName = "";
	public float particleWidth = 100;
	public float particleHeight = 100;
	//TODO add other ParticleEffect properties 
	
	public ParticleEffectVO() {
		super();
	}
	
	public ParticleEffectVO(ParticleEffectVO vo) {
		super(vo);
		particleName = new String(vo.particleName);
	}
}
