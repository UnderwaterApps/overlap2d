package com.uwsoft.editor.gdx.screen;

import com.uwsoft.editor.gdx.LunarEditorListener;

public abstract class Screen
{
		public LunarEditorListener game;    // the game listener
		
        public Screen(LunarEditorListener g)
        {
                this.game = g;
        }
        public abstract void render(float deltaTime);
        public abstract void pause();
        public abstract void resume(boolean b);
        public abstract void dispose();
        public abstract void show();
        public abstract void hide();
        public abstract void resize(int width, int height);
}
