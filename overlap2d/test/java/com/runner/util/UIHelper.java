package com.runner.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.reflect.internal.WhiteboxImpl;

import java.util.List;

public class UIHelper {


    public static List<Actor> getAllActors(Table table) {
        List<Actor> actors = Lists.newArrayList();
        for (Cell cell : table.getCells()) {
            if (cell.getActor() instanceof Table) {
                actors.addAll(getAllActors((Table) cell.getActor()));
            } else {
                actors.add(cell.getActor());
            }
        }
        return actors;
    }

    public static Actor findActorByText(Table table, final String text) {
        List<Actor> allActors = getAllActors(table);
        Optional<Actor> actorOptional = FluentIterable.from(allActors).firstMatch(actor -> {
            try {
                return WhiteboxImpl.invokeMethod(actor, "getText").toString().equals(text);
            } catch (Exception e) {

            }
            return false;
        });
        return actorOptional.get();
    }

    public static <T extends Actor> T findActorByText(Table table, final String text, Class<T> clazz) {
        Actor actor = findActorByText(table, text);
        int count = 0;
        while (!actor.getClass().equals(clazz)) {
            actor = actor.getParent();
            count++;
            if (count > 5) {
                throw new RuntimeException(String.format("Can't find actor with [%s]", text));
            }
        }
        return (T) actor;
    }


    public static void invokeClickableActor(Actor actor) {
        Optional<EventListener> command = FluentIterable.from(actor.getListeners()).firstMatch(new Predicate<EventListener>() {
            @Override
            public boolean apply(EventListener eventListener) {
                try {
                    return eventListener instanceof ClickListener &&
                            StringUtils.isNotEmpty(Whitebox.getInternalState(eventListener, "command").toString());
                } catch (Exception e) {

                }
                return false;
            }
        });
        ClickListener clickListener = (ClickListener) command.get();
        clickListener.clicked(new InputEvent(), 1, 1);
    }
}
