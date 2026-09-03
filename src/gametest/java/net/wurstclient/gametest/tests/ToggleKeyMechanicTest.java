/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.gametest.tests;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Options;
import net.wurstclient.gametest.SingleplayerTest;
import net.wurstclient.mixinterface.IKeyMapping;

/**
 * Checks that Wurst can control the sneak key even when the user has set
 * "Sneak: Toggle" in the vanilla controls menu.
 *
 * <p>
 * Everything runs inside a single client task, so that no game tick can
 * change the key's state while the test is looking at it.
 */
public final class ToggleKeyMechanicTest extends SingleplayerTest
{
	public ToggleKeyMechanicTest(ClientGameTestContext context,
		TestSingleplayerContext spContext)
	{
		super(context, spContext);
	}
	
	@Override
	protected void runImpl()
	{
		logger.info("Testing setDownIgnoringToggle() on the sneak key");
		
		context.runOnClient(mc -> {
			Options options = mc.options;
			boolean wasToggleCrouch = options.toggleCrouch().get();
			
			try
			{
				testSneakKey(options);
				
			}finally
			{
				options.toggleCrouch().set(wasToggleCrouch);
				IKeyMapping.get(options.keyShift).setDownIgnoringToggle(false);
			}
		});
	}
	
	private void testSneakKey(Options options)
	{
		IKeyMapping sneakKey = IKeyMapping.get(options.keyShift);
		
		// With "Sneak: Toggle", vanilla's setDown() flips the key instead of
		// setting it, and setDown(false) does nothing at all. If Mojang ever
		// changes this, setDownIgnoringToggle() can be removed.
		options.toggleCrouch().set(true);
		sneakKey.setDownIgnoringToggle(false);
		options.keyShift.setDown(false);
		assertDown(options, false, "setDown(false) with Sneak: Toggle");
		options.keyShift.setDown(true);
		assertDown(options, true, "setDown(true) with Sneak: Toggle");
		options.keyShift.setDown(true);
		assertDown(options, false, "setDown(true) twice with Sneak: Toggle");
		
		// setDownIgnoringToggle() must always set the key's state, no matter
		// how often it's called or how the user set up their controls.
		for(boolean toggleCrouch : new boolean[]{true, false})
		{
			options.toggleCrouch().set(toggleCrouch);
			String mode = " with Sneak: " + (toggleCrouch ? "Toggle" : "Hold");
			
			sneakKey.setDownIgnoringToggle(true);
			assertDown(options, true, "setDownIgnoringToggle(true)" + mode);
			sneakKey.setDownIgnoringToggle(true);
			assertDown(options, true,
				"setDownIgnoringToggle(true) twice" + mode);
			sneakKey.setDownIgnoringToggle(false);
			assertDown(options, false, "setDownIgnoringToggle(false)" + mode);
		}
	}
	
	private void assertDown(Options options, boolean expected, String what)
	{
		boolean actual = options.keyShift.isDown();
		if(actual == expected)
			return;
		
		throw new RuntimeException(what + ": expected the sneak key to be "
			+ (expected ? "down" : "up") + ", but it was "
			+ (actual ? "down" : "up"));
	}
}
