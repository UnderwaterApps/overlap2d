package com.uwsoft.editor.view.ui.box;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(LibgdxRunner.class)
public class UIResolutionBoxTest {
    private UIResolutionBox uiResolutionBox;

    @Test
    @NeedGL
    public void shouldAbleToSelectResolution() throws Exception {
        ResolutionManager resolutionManager = mock(ResolutionManager.class);
        given(resolutionManager.getProxyName()).willReturn(ResolutionManager.NAME);
        Overlap2DFacade.getInstance().registerProxy(resolutionManager);
        ResolutionEntryVO vo = getResolutionEntryVO(800, 600, "origin");
        resolutionManager.currentResolutionName = "origin";
        given(resolutionManager.getOriginalResolution()).willReturn(vo);
        ResolutionEntryVO anotherVo = getResolutionEntryVO(1024, 860, "new");
        given(resolutionManager.getResolutions()).willReturn(Array.with(anotherVo));
        uiResolutionBox = new UIResolutionBox();
        uiResolutionBox.update();

        VisSelectBox<ResolutionEntryVO> visSelectBox = (VisSelectBox<ResolutionEntryVO>)
                Whitebox.getInternalState(uiResolutionBox, "visSelectBox");

        assertThat(visSelectBox.getSelected(), not(nullValue()));
        assertThat(visSelectBox.getSelected().name, is("origin"));
    }

    private ResolutionEntryVO getResolutionEntryVO(int width, int height, String name) {
        ResolutionEntryVO vo = new ResolutionEntryVO();
        vo.base = 1;
        vo.name = name;
        vo.height = height;
        vo.width = width;
        return vo;
    }
}