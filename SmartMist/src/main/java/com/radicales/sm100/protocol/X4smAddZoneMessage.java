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

/**
 * XML For Smart Mist Add Zone Message
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
public class X4smAddZoneMessage extends X4smMessage {

    private String gName;
    private int gChannel;
    private long gControlWord;
    private boolean gInitValue;
    private int gOffDelay;

    public X4smAddZoneMessage( String Name, int Channel, long ControlWord, boolean InitValue, int OffDelay ) {
        super(MSG_TYPE_ADD_ZONE);
        gName = Name;
        gChannel = Channel;
        gControlWord = ControlWord;
        gInitValue = InitValue;
        gOffDelay = OffDelay;
        encode();
    }

    private void encode() {
        String msg = "<AddZone name=\"" + gName + "\" channel=\"" + Integer.toString(gChannel) +
                "\" controlword=\"0x" + Long.toHexString(gControlWord) + "\" initvalue=\"" + Boolean.toString(gInitValue) +
                "\" offdelay=\"" + Integer.toString(gOffDelay) + "\" />";
        setData(msg);
    }

}
