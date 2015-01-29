package com.uwsoft.editor.gdx.ui.payloads;


public class AssetPayloadObject {
	public AssetType type;
    public String assetName;
	public float xOffset;
	public float yOffset;

    public enum AssetType {
        Sprite, Particle, Animation, Spriter, Component
    }
}
