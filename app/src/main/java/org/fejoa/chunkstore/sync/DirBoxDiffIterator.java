/*
 * Copyright 2016.
 * Distributed under the terms of the GPLv3 License.
 *
 * Authors:
 *      Clemens Zeidler <czei002@aucklanduni.ac.nz>
 */
package org.fejoa.chunkstore.sync;

import org.fejoa.chunkstore.FlatDirectoryBox;


public class DirBoxDiffIterator extends DiffIterator<FlatDirectoryBox.Entry> {
    public DirBoxDiffIterator(String basePath, FlatDirectoryBox ours, FlatDirectoryBox theirs) {
        super(basePath, ours == null ? null : ours.getEntries(), theirs.getEntries(),
                new NameGetter<FlatDirectoryBox.Entry>() {
                    @Override
                    public String getName(FlatDirectoryBox.Entry entry) {
                        return entry.getName();
                    }
                });
    }
}
