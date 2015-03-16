package com.uwsoft.editor.data.migrations.migrators;

import com.uwsoft.editor.data.migrations.IVersionMigrator;

/**
 * Created by azakhary on 1/29/2015.
 */
public class DummyMig implements IVersionMigrator {
    @Override
    public void setProject(String path) {

    }

    @Override
    public boolean doMigration() {
        return true;
    }
}
