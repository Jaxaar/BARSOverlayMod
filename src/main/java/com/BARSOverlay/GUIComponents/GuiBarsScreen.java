package com.BARSOverlay.GUIComponents;


import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class GuiBarsScreen extends GuiScreen {

        public GuiBarsScreen() {
            this.allowUserInput = true;
        }

        /**
         * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
         * window resizes, the buttonList is cleared beforehand.
         */
        public void initGui() {

        }

        /**
         * Called when the screen is unloaded. Used to disable keyboard repeat events
         */
        public void onGuiClosed() {

        }

        /**
         * Called from the main game loop to update the screen.
         */
        public void updateScreen() {

        }

        /**
         * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
         * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
         */
//        protected void keyTyped(char typedChar, int keyCode) throws IOException {
//
//        }


        /**
         * Handles mouse input.
         */
//        public void handleMouseInput() throws IOException {
//            super.handleMouseInput();
//            int i = Mouse.getEventDWheel();
//
//            if (i != 0)
//            {
//                if (i > 1)
//                {
//                    i = 1;
//                }
//
//                if (i < -1)
//                {
//                    i = -1;
//                }
//
//                if (!isShiftKeyDown())
//                {
//                    i *= 7;
//                }
//
//                this.mc.ingameGUI.getChatGUI().scroll(i);
//            }
//        }

        /**
         * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
         */
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            if (mouseButton == 0)
            {
                IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

                if (this.handleComponentClick(ichatcomponent))
                {
                    return;
                }
            }

            super.mouseClicked(mouseX, mouseY, mouseButton);
        }


        /**
         * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
         */
        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            drawRect(2,  2, 200, 240, Integer.MIN_VALUE);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        /**
         * Returns true if this GUI should pause the game when it is displayed in single-player
         */
        public boolean doesGuiPauseGame()
        {
            return false;
        }
}
