/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.javadoc.internal.foundation.lang;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.javadoc.internal.ToolKit.requireNonNullArgument;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.tquadrat.foundation.javadoc.internal.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.javadoc.internal.foundation.lang.internal.LazyImpl;

/**
 *  <p>{@summary A holder for a lazy initialised object instance.}</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Lazy.java 991 2022-01-16 16:58:29Z tquadrat $
 *  @since 0.1.0
 *
 *  @param  <T> The type of the value for this instance of {@code Lazy}.
 */
@ClassVersion( sourceVersion = "$Id: Lazy.java 991 2022-01-16 16:58:29Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public interface Lazy<T>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean equals( final Object obj );

    /**
     *  Returns the value for this instance of {@code Lazy}.
     *
     *  @return The value.
     */
    public T get();

    /**
     *  {@inheritDoc}
     */
    @Override
    public int hashCode();

    /**
     *  If this {@code Lazy} instance has been initialised already, the
     *  provided
     *  {@link Consumer}
     *  will be executed; otherwise nothing happens.
     *
     *  @param  consumer    The consumer.
     */
    @SuppressWarnings( "unused" )
    public default void ifPresent( final Consumer<? super T> consumer )
    {
        if( isPresent() ) requireNonNullArgument( consumer, "consumer" ).accept( get() );
    }   //  ifPresent()

    /**
     *  Checks whether this {@code Lazy} instance has been initialised already.
     *  But even it was initialised,
     *  {@link #get()}
     *  may still return {@code null}.
     *
     *  @return {@code true} if the instance was initialised, {@code false}
     *      otherwise.
     */
    public boolean isPresent();

    /**
     *  If this instance of {@code Lazy} is initialised, the provided mapper
     *  function will be executed on the value.
     *
     *  @param  <R> The result type for the mapper function.
     *  @param  mapper  The mapper function.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the result for the mapping.
     */
    public default <R> Optional<R> map( final Function<? super T,? extends R> mapper )
    {
        final Optional<R> retValue = isPresent() ? Optional.ofNullable( mapper.apply( get() ) ) : Optional.empty();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  map()

    /**
     *  Creates a new {@code Lazy} instance that is already initialised.<br>
     *  <br>This allows to use {@code Lazy} instances also for
     *  {@linkplain Cloneable cloneable}
     *  objects, given that {@code T} is either cloneable itself or
     *  immutable.
     *
     *  @param  <T> The type of the value for the new instance of {@code Lazy}.
     *  @param  value   The value; can be {@code null}.
     *  @return The new instance.
     *
     *  @see Object#clone()
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <T> Lazy<T> of( final T value )
    {
        return new LazyImpl<>( value );
    }   //  of()

    /**
     *  Returns the value or throws the exception that is created by the
     *  given
     *  {@link Supplier}
     *  when not yet initialised.
     *
     *  @param  <X> The type of the implementation of
     *      {@link Throwable}.
     *  @param  exceptionSupplier   The supplier for the exception to throw
     *      when the instance was not yet initialised.
     *  @return The value.
     *  @throws X   When not initialised, the exception created by the given
     *      supplier will be thrown.
     */
    @SuppressWarnings( "unused" )
    public <X extends Throwable> T orElseThrow( Supplier<? extends X> exceptionSupplier ) throws X;

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString();

    /**
     *  Creates a new {@code Lazy} instance that uses the given supplier to
     *  initialise.
     *
     *  @param  <T> The type of the value for the new instance of {@code Lazy}.
     *  @param  supplier    The supplier that initialises the value for this
     *      instance on the first call to
     *      {@link #get()}.
     *  @return The new instance.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static <T> Lazy<T> use( final Supplier<T> supplier )
    {
        return new LazyImpl<>( supplier );
    }   //  use()
}
//  interface Lazy

/*
 *  End of File
 */