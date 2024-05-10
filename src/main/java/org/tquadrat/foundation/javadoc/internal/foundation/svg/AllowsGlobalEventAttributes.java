/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.javadoc.internal.foundation.svg;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCanPlay;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCanPlayThrough;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCancel;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnClick;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnClose;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnCueChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDblClick;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDrag;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragEnd;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragEnter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragExit;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragLeave;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragOver;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDragStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDrop;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnDurationChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnEmptied;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnEnded;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnError;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnFocus;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnInput;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnInvalid;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyDown;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyPress;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnKeyUp;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoad;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadStart;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadedData;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnLoadedMetadata;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseDown;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseEnter;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseLeave;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseMove;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseOut;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseOver;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseUp;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnMouseWheel;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPause;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPlay;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnPlaying;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnProgress;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnRateChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnReset;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnResize;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnScroll;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSeeked;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSeeking;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSelect;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnShow;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnStalled;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSubmit;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnSuspend;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnTimeUpdate;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnToggle;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnVolumeChange;
import static org.tquadrat.foundation.javadoc.internal.foundation.svg.SVGUtils.SVGATTRIBUTE_OnWaiting;

import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;

/**
 *  SVG elements that allow the global event attributes
 *  {@value SVGUtils#SVGATTRIBUTE_OnCanPlay},
 *  {@value SVGUtils#SVGATTRIBUTE_OnCanPlayThrough},
 *  {@value SVGUtils#SVGATTRIBUTE_OnCancel},
 *  {@value SVGUtils#SVGATTRIBUTE_OnChange},
 *  {@value SVGUtils#SVGATTRIBUTE_OnClick},
 *  {@value SVGUtils#SVGATTRIBUTE_OnClose},
 *  {@value SVGUtils#SVGATTRIBUTE_OnCueChange},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDblClick},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDrag},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragEnd},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragEnter},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragExit},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragLeave},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragOver},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDragStart},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDrop},
 *  {@value SVGUtils#SVGATTRIBUTE_OnDurationChange},
 *  {@value SVGUtils#SVGATTRIBUTE_OnEmptied},
 *  {@value SVGUtils#SVGATTRIBUTE_OnEnded},
 *  {@value SVGUtils#SVGATTRIBUTE_OnError},
 *  {@value SVGUtils#SVGATTRIBUTE_OnFocus},
 *  {@value SVGUtils#SVGATTRIBUTE_OnInput},
 *  {@value SVGUtils#SVGATTRIBUTE_OnInvalid},
 *  {@value SVGUtils#SVGATTRIBUTE_OnKeyDown},
 *  {@value SVGUtils#SVGATTRIBUTE_OnKeyPress},
 *  {@value SVGUtils#SVGATTRIBUTE_OnKeyUp},
 *  {@value SVGUtils#SVGATTRIBUTE_OnLoad},
 *  {@value SVGUtils#SVGATTRIBUTE_OnLoadStart},
 *  {@value SVGUtils#SVGATTRIBUTE_OnLoadedData},
 *  {@value SVGUtils#SVGATTRIBUTE_OnLoadedMetadata},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseDown},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseEnter},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseLeave},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseMove},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseOut},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseOver},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseUp},
 *  {@value SVGUtils#SVGATTRIBUTE_OnMouseWheel},
 *  {@value SVGUtils#SVGATTRIBUTE_OnPause},
 *  {@value SVGUtils#SVGATTRIBUTE_OnPlay},
 *  {@value SVGUtils#SVGATTRIBUTE_OnPlaying},
 *  {@value SVGUtils#SVGATTRIBUTE_OnProgress},
 *  {@value SVGUtils#SVGATTRIBUTE_OnRateChange},
 *  {@value SVGUtils#SVGATTRIBUTE_OnReset},
 *  {@value SVGUtils#SVGATTRIBUTE_OnResize},
 *  {@value SVGUtils#SVGATTRIBUTE_OnScroll},
 *  {@value SVGUtils#SVGATTRIBUTE_OnSeeked},
 *  {@value SVGUtils#SVGATTRIBUTE_OnSeeking},
 *  {@value SVGUtils#SVGATTRIBUTE_OnSelect},
 *  {@value SVGUtils#SVGATTRIBUTE_OnShow},
 *  {@value SVGUtils#SVGATTRIBUTE_OnStalled},
 *  {@value SVGUtils#SVGATTRIBUTE_OnSubmit},
 *  {@value SVGUtils#SVGATTRIBUTE_OnSuspend},
 *  {@value SVGUtils#SVGATTRIBUTE_OnTimeUpdate},
 *  {@value SVGUtils#SVGATTRIBUTE_OnToggle},
 *  {@value SVGUtils#SVGATTRIBUTE_OnVolumeChange},
 *  and
 *  {@value SVGUtils#SVGATTRIBUTE_OnWaiting}
 *  will implement this interface.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AllowsGlobalEventAttributes.java 977 2022-01-06 11:41:03Z tquadrat $
 *  @since 0.0.5
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "unused"} )
@ClassVersion( sourceVersion = "$Id: AllowsGlobalEventAttributes.java 977 2022-01-06 11:41:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface AllowsGlobalEventAttributes
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The global event attributes.
     */
    public static final List<String> GLOBALEVENT_ATTRIBUTES = List.of(
        SVGATTRIBUTE_OnCanPlay,
        SVGATTRIBUTE_OnCanPlayThrough,
        SVGATTRIBUTE_OnCancel,
        SVGATTRIBUTE_OnChange,
        SVGATTRIBUTE_OnClick,
        SVGATTRIBUTE_OnClose,
        SVGATTRIBUTE_OnCueChange,
        SVGATTRIBUTE_OnDblClick,
        SVGATTRIBUTE_OnDrag,
        SVGATTRIBUTE_OnDragEnd,
        SVGATTRIBUTE_OnDragEnter,
        SVGATTRIBUTE_OnDragExit,
        SVGATTRIBUTE_OnDragLeave,
        SVGATTRIBUTE_OnDragOver,
        SVGATTRIBUTE_OnDragStart,
        SVGATTRIBUTE_OnDrop,
        SVGATTRIBUTE_OnDurationChange,
        SVGATTRIBUTE_OnEmptied,
        SVGATTRIBUTE_OnEnded,
        SVGATTRIBUTE_OnError,
        SVGATTRIBUTE_OnFocus,
        SVGATTRIBUTE_OnInput,
        SVGATTRIBUTE_OnInvalid,
        SVGATTRIBUTE_OnKeyDown,
        SVGATTRIBUTE_OnKeyPress,
        SVGATTRIBUTE_OnKeyUp,
        SVGATTRIBUTE_OnLoad,
        SVGATTRIBUTE_OnLoadStart,
        SVGATTRIBUTE_OnLoadedData,
        SVGATTRIBUTE_OnLoadedMetadata,
        SVGATTRIBUTE_OnMouseDown,
        SVGATTRIBUTE_OnMouseEnter,
        SVGATTRIBUTE_OnMouseLeave,
        SVGATTRIBUTE_OnMouseMove,
        SVGATTRIBUTE_OnMouseOut,
        SVGATTRIBUTE_OnMouseOver,
        SVGATTRIBUTE_OnMouseUp,
        SVGATTRIBUTE_OnMouseWheel,
        SVGATTRIBUTE_OnPause,
        SVGATTRIBUTE_OnPlay,
        SVGATTRIBUTE_OnPlaying,
        SVGATTRIBUTE_OnProgress,
        SVGATTRIBUTE_OnRateChange,
        SVGATTRIBUTE_OnReset,
        SVGATTRIBUTE_OnResize,
        SVGATTRIBUTE_OnScroll,
        SVGATTRIBUTE_OnSeeked,
        SVGATTRIBUTE_OnSeeking,
        SVGATTRIBUTE_OnSelect,
        SVGATTRIBUTE_OnShow,
        SVGATTRIBUTE_OnStalled,
        SVGATTRIBUTE_OnSubmit,
        SVGATTRIBUTE_OnSuspend,
        SVGATTRIBUTE_OnTimeUpdate,
        SVGATTRIBUTE_OnToggle,
        SVGATTRIBUTE_OnVolumeChange,
        SVGATTRIBUTE_OnWaiting
    );

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Sets the cancel handler for this SVG element.
     *
     *  @param  value   The cancel handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnCancel
     */
    public void setCancelHandler( final String value );

    /**
     *  Sets the can-play handler for this SVG element.
     *
     *  @param  value   The can-play handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnCanPlay
     */
    public void setCanPlayHandler( final String value );

    /**
     *  Sets the can-play-through handler for this SVG element.
     *
     *  @param  value   The can-play-through handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnCanPlayThrough
     */
    public void setCanPlayThroughHandler( final String value );

    /**
     *  Sets the change handler for this SVG element.
     *
     *  @param  value   The change handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnChange
     */
    public void setChangeHandler( final String value );

    /**
     *  Sets the click handler for this SVG element.
     *
     *  @param  value   The click handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnClick
     */
    public void setClickHandler( final String value );

    /**
     *  Sets the global event attribute {@code onclose} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnClose
     */
    public void setCloseHandler( final String value );

    /**
     *  Sets the global event attribute {@code oncuechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnCueChange
     */
    public void setCueChangeHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondblclick} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDblClick
     */
    public void setDblClickHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragend} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragEnd
     */
    public void setDragEndHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragenter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragEnter
     */
    public void setDragEnterHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragexit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragExit
     */
    public void setDragExitHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondrag} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDrag
     */
    public void setDragHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragleave} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragLeave
     */
    public void setDragLeaveHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragover} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragOver
     */
    public void setDragOverHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondragstart} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDragStart
     */
    public void setDragStartHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondrop} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDrop
     */
    public void setDropHandler( final String value );

    /**
     *  Sets the global event attribute {@code ondurationchange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnDurationChange
     */
    public void setDurationChangeHandler( final String value );

    /**
     *  Sets the global event attribute {@code onemptied} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnEmptied
     */
    public void setEmptiedHandler( final String value );

    /**
     *  Sets the global event attribute {@code onended} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnEnded
     */
    public void setEndedHandler( final String value );

    /**
     *  Sets the global event attribute {@code onerror} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnError
     */
    public void setErrorHandler( final String value );

    /**
     *  Sets the global event attribute {@code onfocus} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnFocus
     */
    public void setFocusHandler( final String value );

    /**
     *  Sets the global event attribute {@code oninput} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnInput
     */
    public void setInputHandler( final String value );

    /**
     *  Sets the global event attribute {@code oninvalid} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnInvalid
     */
    public void setInvalidHandler( final String value );

    /**
     *  Sets the global event attribute {@code onkeydown} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnKeyDown
     */
    public void setKeyDownHandler( final String value );

    /**
     *  Sets the global event attribute {@code onkeypress} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnKeyPress
     */
    public void setKeyPressHandler( final String value );

    /**
     *  Sets the global event attribute {@code onkeyup} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnKeyUp
     */
    public void setKeyUpHandler( final String value );

    /**
     *  Sets the global event attribute {@code onloadeddata} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnLoadedData
     */
    public void setLoadedDataHandler( final String value );

    /**
     *  Sets the global event attribute {@code onloadedmetadata} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnLoadedMetadata
     */
    public void setLoadedMetadataHandler( final String value );

    /**
     *  Sets the global event attribute {@code onload} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnLoad
     */
    public void setLoadHandler( final String value );

    /**
     *  Sets the global event attribute {@code onloadstart} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnLoadStart
     */
    public void setLoadStartHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmousedown} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseDown
     */
    public void setMouseDownHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmouseenter} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseEnter
     */
    public void setMouseEnterHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmouseleave} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseLeave
     */
    public void setMouseLeaveHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmousemove} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseMove
     */
    public void setMouseMoveHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmouseout} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseOut
     */
    public void setMouseOutHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmouseover} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseOver
     */
    public void setMouseOverHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmouseup} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseUp
     */
    public void setMouseUpHandler( final String value );

    /**
     *  Sets the global event attribute {@code onmousewheel} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnMouseWheel
     */
    public void setMouseWheelHandler( final String value );

    /**
     *  Sets the global event attribute {@code onpause} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnPause
     */
    public void setPauseHandler( final String value );

    /**
     *  Sets the global event attribute {@code onplay} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnPlay
     */
    public void setPlayHandler( final String value );

    /**
     *  Sets the global event attribute {@code onplaying} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnPlaying
     */
    public void setPlayingHandler( final String value );

    /**
     *  Sets the global event attribute {@code onprogress} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnProgress
     */
    public void setProgressHandler( final String value );

    /**
     *  Sets the global event attribute {@code onratechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnRateChange
     */
    public void setRateChangeHandler( final String value );

    /**
     *  Sets the global event attribute {@code onreset} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnReset
     */
    public void setResetHandler( final String value );

    /**
     *  Sets the global event attribute {@code onresize} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnResize
     */
    public void setResizeHandler( final String value );

    /**
     *  Sets the global event attribute {@code onscroll} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnScroll
     */
    public void setScrollHandler( final String value );

    /**
     *  Sets the global event attribute {@code onseeked} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnSeeked
     */
    public void setSeekedHandler( final String value );

    /**
     *  Sets the global event attribute {@code onseeking} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnSeeking
     */
    public void setSeekingHandler( final String value );

    /**
     *  Sets the global event attribute {@code onselect} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnSelect
     */
    public void setSelectHandler( final String value );

    /**
     *  Sets the global event attribute {@code onshow} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnShow
     */
    public void setShowHandler( final String value );

    /**
     *  Sets the global event attribute {@code onstalled} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnStalled
     */
    public void setStalledHandler( final String value );

    /**
     *  Sets the global event attribute {@code onsubmit} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnSubmit
     */
    public void setSubmitHandler( final String value );

    /**
     *  Sets the global event attribute {@code onsuspend} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnSuspend
     */
    public void setSuspendHandler( final String value );

    /**
     *  Sets the global event attribute {@code ontimeupdate} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnTimeUpdate
     */
    public void setTimeUpdateHandler( final String value );

    /**
     *  Sets the global event attribute {@code ontoggle} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnToggle
     */
    public void setToggleHandler( final String value );

    /**
     *  Sets the global event attribute {@code onvolumechange} for this SVG element.
     *
     *  @param  value   The attribute type.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnVolumeChange
     */
    public void setVolumeChangeHandler( final String value );

    /**
     *  Sets the wait handler for this SVG element.
     *
     *  @param  value   The wait handler.
     *
     *  @see SVGUtils#SVGATTRIBUTE_OnWaiting
     */
    public void setWaitHandler( final String value );
}
//  interface AllowsGlobalEventAttributes

/*
 *  End of File
 */