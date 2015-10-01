package com.uwsoft.editor.view.ui.dialog;

import com.runner.LibgdxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class ImportDialogMediatorTest {
    private ImportDialogMediator importDialogMediator;

    @Before
    public void setUp() throws Exception {
        importDialogMediator = new ImportDialogMediator();
    }

    @Test
    public void shouldHaveEightNotificationInterests() throws Exception {
        String[] listNotificationInterests = importDialogMediator.listNotificationInterests();

        assertThat(listNotificationInterests.length, is(8));
    }
}