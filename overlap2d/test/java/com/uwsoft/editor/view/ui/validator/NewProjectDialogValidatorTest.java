package com.uwsoft.editor.view.ui.validator;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Dialogs.class)
public class NewProjectDialogValidatorTest {
    private NewProjectDialogValidator validator;
    @Mock
    private Stage stage;
    @Mock
    private VisValidatableTextField projectName;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        validator = new NewProjectDialogValidator();
        PowerMockito.mockStatic(Dialogs.class);
    }

    @Test
    public void shouldLogErrorIfTheProjectNameIsInvalid() throws Exception {
        given(projectName.getText()).willReturn("endWithSpace ");
        boolean validate = validator.validate(stage, projectName);
        assertThat(validate, is(false));

        given(projectName.getText()).willReturn("");
        validate = validator.validate(stage, projectName);
        assertThat(validate, is(false));
        PowerMockito.verifyStatic(times(3));
    }

    @Test
    public void shouldNotLogErrorIfTheProjectNameIsValid() throws Exception {
        given(projectName.getText()).willReturn("valid");
        boolean validate = validator.validate(stage, projectName);
        assertThat(validate, is(true));
        PowerMockito.verifyStatic(never());
    }
}