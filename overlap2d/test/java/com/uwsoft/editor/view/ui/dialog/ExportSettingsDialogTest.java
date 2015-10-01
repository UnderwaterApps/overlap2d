package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.Notification;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import com.runner.util.UIHelper;
import com.uwsoft.editor.Overlap2DFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(LibgdxRunner.class)
public class ExportSettingsDialogTest {
    private ExportSettingsDialog exportSettingsDialog;

    @Before
    public void setUp() throws Exception {
        exportSettingsDialog = new ExportSettingsDialog();
    }

    @Test
    @NeedGL
    public void shouldSendExportInformationAfterClickButton() throws Exception {
        Mediator mediator = mock(Mediator.class);
        given(mediator.getMediatorName()).willReturn(ExportSettingsDialog.SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED);
        given(mediator.listNotificationInterests()).willReturn(new String[]{ExportSettingsDialog.SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED});
        Overlap2DFacade.getInstance().registerMediator(mediator);

        VisTextButton visTextButton = UIHelper.findActorByText(exportSettingsDialog, "Save Settings and Export", VisTextButton.class);

        assertThat(visTextButton.getListeners().size, is(3));
        UIHelper.invokeClickableActor(visTextButton);

        ArgumentCaptor<Notification> argument = ArgumentCaptor.forClass(Notification.class);
        verify(mediator).handleNotification(argument.capture());

        assertThat(argument.getValue().getName(), is(ExportSettingsDialog.SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED));
        assertThat(argument.getValue().getBody(), not(nullValue()));
    }
}