package com.uwsoft.editor.data.migrations;

/**
 * Created by azakhary on 9/28/2014.
 */
public interface IVersionMigrator {

    public void setProject(String path);

    public boolean doMigration();
}
