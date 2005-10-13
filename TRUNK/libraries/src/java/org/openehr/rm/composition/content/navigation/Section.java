/*
 * component:   "openEHR Reference Implementation"
 * description: "Class Section"
 * keywords:    "composition"
 *
 * author:      "Rong Chen <rong@acode.se>"
 * support:     "Acode HB <support@acode.se>"
 * copyright:   "Copyright (c) 2004 Acode HB, Sweden"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL$"
 * revision:    "$LastChangedRevision$"
 * last_change: "$LastChangedDate$"
 */
package org.openehr.rm.composition.content.navigation;

import org.openehr.rm.Attribute;
import org.openehr.rm.FullConstructor;
import org.openehr.rm.common.archetyped.Archetyped;
import org.openehr.rm.common.archetyped.FeederAudit;
import org.openehr.rm.common.archetyped.Link;
import org.openehr.rm.common.archetyped.Locatable;
import org.openehr.rm.support.identification.ObjectID;
import org.openehr.rm.composition.content.ContentItem;
import org.openehr.rm.datatypes.text.DvText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a heading in a heading structure, or "section tree".
 * Instances of this class are immutable.
 *
 * @author Rong Chen
 * @version 1.0
 */
public final class Section extends ContentItem {

    /**
     * Constructs a Section
     *
     * @param uid
     * @param archetypeNodeId
     * @param name
     * @param archetypeDetails
     * @param feederAudit
     * @param links
     * @param items            null if not present
     * @throws IllegalArgumentException if items not null and empty
     */
    @FullConstructor
            public Section(@Attribute(name = "uid") ObjectID uid,
                           @Attribute(name = "archetypeNodeId", required = true) String archetypeNodeId,
                           @Attribute(name = "name", required = true) DvText name,
                           @Attribute(name = "archetypeDetails") Archetyped archetypeDetails,
                           @Attribute(name = "feederAudit") FeederAudit feederAudit,
                           @Attribute(name = "links") Set<Link> links,
                           @Attribute(name = "items") List<ContentItem> items) {
        super(uid, archetypeNodeId, name, archetypeDetails, feederAudit,
                links);
        if (items != null && items.isEmpty()) {
            throw new IllegalArgumentException("empty items");
        }
        this.items = ( items == null ?
                null : new ArrayList<ContentItem>(items) );
    }

    /**
     * Constructs a Section
     *
     * @param archetypeNodeId
     * @param name
     * @param items
     */
    public Section(String archetypeNodeId, DvText name,
                   List<ContentItem> items) {
        this(null, archetypeNodeId, name, null, null, null, items);
    }

    /**
     * String The path to an item relative to the root of this
     * archetyped structure.
     *
     * @param item
     * @return path of given item
     */
    public String pathOfItem(Locatable item) {
        return null;  // todo: implement this method
    }

    /**
     * The item at a path that is relative to this item.
     *
     * @param path
     * @return relative path
     */
    public Locatable itemAtPath(String path) {
        Locatable ret = super.itemAtPath(path);
        if(ret != null) {
            return ret;
        }
        String whole = whole();
        String tmp = path;
        if(tmp.startsWith(whole)) {
            tmp = tmp.substring(whole.length());
        }
        String attr = ROOT + "items";
        if(tmp.startsWith(attr)) {
            tmp = tmp.substring(attr.length());
            for(ContentItem item : items) {
                String node = item.whole().substring(1);
                if(tmp.startsWith(node)) {
                    if(tmp.equals(node)) {
                        return item;
                    }
                    String subpath = tmp.substring(node.length());
                    if(item.validPath(subpath))  {
                        return item.itemAtPath(subpath);
                    }
                }
            }
        }
        throw new IllegalArgumentException("invalid path: " + path);
    }

    /**
     * Return true if the path is valid with respect to the current
     * item.
     *
     * @param path
     * @return true if valid
     */
    public boolean validPath(String path) {
        try {
            itemAtPath(path);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Ordered list of content items under this section, which may
     * include more Sections or Entries
     *
     * @return unmodifiable list of ContentItem or null if not present
     */
    public List<ContentItem> getItems() {
        return items == null ? null : Collections.unmodifiableList(items);
    }

    // POJO start
    Section() {
    }

    void setItems(List<ContentItem> items) {
        this.items = items;
    }
    // POJO end

    /* fields */
    private List<ContentItem> items;
}

/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is Section.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2003-2004
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */