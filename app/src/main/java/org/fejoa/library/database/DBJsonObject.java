/*
 * Copyright 2017.
 * Distributed under the terms of the GPLv3 License.
 *
 * Authors:
 *      Clemens Zeidler <czei002@aucklanduni.ac.nz>
 */
package org.fejoa.library.database;

import org.json.JSONException;
import org.json.JSONObject;

import java8.util.concurrent.CompletableFuture;
import java8.util.function.Function;


public class DBJsonObject extends DBObject<JSONObject> {
    public DBJsonObject(String path) {
        super(path);
    }

    @Override
    protected CompletableFuture<Void> writeToDB(IOStorageDir dir, String path) {
        return dir.putStringAsync(path, cache.toString());
    }

    @Override
    protected CompletableFuture<JSONObject> readFromDB(IOStorageDir dir, String path) {
        return dir.readStringAsync(path).thenApply(new Function<String, JSONObject>() {
            @Override
            public JSONObject apply(String s) {
                try {
                    return new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }
}
