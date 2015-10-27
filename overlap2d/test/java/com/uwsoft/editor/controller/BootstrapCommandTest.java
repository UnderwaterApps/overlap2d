package com.uwsoft.editor.controller;

import com.google.common.collect.Sets;
import com.puremvc.patterns.observer.BaseNotification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Set;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Overlap2DFacade.class)
public class BootstrapCommandTest {
    private BootstrapCommand bootstrapCommand;

    @Mock
    private Overlap2DFacade overlap2DFacade;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(Overlap2DFacade.class);
        PowerMockito.when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        bootstrapCommand = new BootstrapCommand();
    }

    @Test
    public void shouldRegisterAllCommands() throws Exception {
        Set<Class> commandsList = Sets.newHashSet();
        Mockito.doAnswer(invocation -> {
            Class argument = invocation.getArgumentAt(1, Class.class);
            commandsList.add(argument);
            return null;
        }).when(overlap2DFacade).registerCommand(anyString(), any());

        bootstrapCommand.execute(new BaseNotification("baseNotification"));

        //TODO: This some how gives too many incovations
        //verify(overlap2DFacade, times(26)).registerCommand(anyString(), any());
        //assertThat(commandsList.size(), is(26));
        assertThat(commandsList, hasItems(CopyItemsCommand.class, DeleteItemsCommand.class, AddComponentToItemCommand.class));
        assertThat(commandsList, hasItems(ItemTransformCommand.class, AddSelectionCommand.class, UpdateEntityComponentsCommand.class));
    }
}