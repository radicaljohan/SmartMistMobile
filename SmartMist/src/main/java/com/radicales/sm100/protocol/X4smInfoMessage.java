/*
 * Copyright (C) 2012-2015 Radical Electronic Systems, South Africa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.radicales.sm100.protocol;

import java.util.List;

/**
 * XML For Smart Mist Info Message
 *
 * @author
 * Jan Zwiegers,
 * <a href="mailto:jan@radicalsystems.co.za">jan@radicalsystems.co.za</a>,
 * <a href="http://www.radicalsystems.co.za">www.radicalsystems.co.za</a>
 *
 * @version
 * <b>1.0 01/11/2014</b><br>
 * Original release.
 */
public class X4smInfoMessage extends X4smMessage {

    private List<X4smAttribute> gAttributes;
    private String gName;
    private String gFamily;
    private String gRevision;
    private int gChannels;

    public X4smInfoMessage( List<X4smAttribute> Attributes ) {
        super(MSG_TYPE_INFO);
        gAttributes = Attributes;
    }

    public void parse() throws X4smException  {

        for(X4smAttribute attr : gAttributes) {

            if(attr.Name.matches("name")) {
                gName = attr.Value;
            }
            else if(attr.Name.matches("family")) {
                gFamily = attr.Value;
            }
            else if(attr.Name.matches("revision")) {
                gRevision = attr.Value;
            }
            else if(attr.Name.matches("channels")) {
                gChannels = Integer.parseInt(attr.Value);
            }
        }
    }

    public String getName() {
        return gName;
    }

    public int getChannels() {
        return gChannels;
    }

    public String getFamily() {
        return gFamily;
    }

    public String getRevision() {
        return gRevision;
    }



}
