/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixinterface;

import net.minecraft.client.KeyMapping;

public interface IKeyMapping
{
	/*
	 * Returns whether the user is actually pressing this key on their keyboard
	 * or mouse.
	 */
	public default boolean isActuallyDown()
	{
		return wurst_isActuallyDown();
	}
	
	/**
	 * Resets the pressed state to whether or not the user is actually pressing
	 * this key on their keyboard.
	 */
	public default void resetPressedState()
	{
		wurst_resetPressedState();
	}
	
	/**
	 * Simulates the user pressing this key on their keyboard or mouse. This is
	 * much more aggressive than using {@link #setDown(boolean)} and should
	 * be used sparingly.
	 */
	public default void simulatePress(boolean pressed)
	{
		wurst_simulatePress(pressed);
	}
	
	public default void setDown(boolean down)
	{
		asVanilla().setDown(down);
	}
	
	/**
	 * Sets whether this key is being pressed, ignoring any "Toggle" option
	 * that the user may have set for it in the vanilla controls menu.
	 *
	 * <p>
	 * Use this instead of {@link #setDown(boolean)} for keys that can be
	 * toggled, like sneak and sprint. Those are {@code ToggleKeyMapping}s,
	 * whose {@code setDown()} flips the key's state when called with
	 * {@code true} and does nothing at all when called with {@code false},
	 * making it impossible to control them reliably.
	 */
	public default void setDownIgnoringToggle(boolean down)
	{
		wurst_setDownIgnoringToggle(down);
	}
	
	public default KeyMapping asVanilla()
	{
		return (KeyMapping)this;
	}
	
	/**
	 * Returns the given KeyMapping object as an IKeyMapping, allowing you to
	 * access the resetPressedState() method.
	 */
	public static IKeyMapping get(KeyMapping kb)
	{
		return (IKeyMapping)kb;
	}
	
	/**
	 * @deprecated Use {@link #isActuallyDown()} instead.
	 */
	@Deprecated
	public boolean wurst_isActuallyDown();
	
	/**
	 * @deprecated Use {@link #resetPressedState()} instead.
	 */
	@Deprecated
	public void wurst_resetPressedState();
	
	/**
	 * @deprecated Use {@link #simulatePress()} instead.
	 */
	@Deprecated
	public void wurst_simulatePress(boolean pressed);
	
	/**
	 * @deprecated Use {@link #setDownIgnoringToggle(boolean)} instead.
	 */
	@Deprecated
	public void wurst_setDownIgnoringToggle(boolean down);
}
