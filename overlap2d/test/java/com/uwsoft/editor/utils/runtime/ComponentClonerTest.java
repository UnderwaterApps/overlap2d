package com.uwsoft.editor.utils.runtime;

import com.badlogic.ashley.core.Component;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ComponentClonerTest {
    @Test
    public void shouldCloneWithDifferentTypeField() throws Exception {
        TestComponent testComponent = new TestComponent();
        testComponent.i = 10;
        testComponent.s = "Text";
        testComponent.lb = Lists.newArrayList(false, true);

        TestComponent clone = ComponentCloner.get(testComponent);

        assertThat(clone.i, is(10));
        assertThat(clone.s, is("Text"));
        assertThat(clone.lb, is(Lists.newArrayList(false, true)));
    }

    private static class TestComponent implements Component {
        public int i;
        public String s;
        public List<Boolean> lb;

        public TestComponent() {
            lb = new ArrayList<>();
        }
    }
}