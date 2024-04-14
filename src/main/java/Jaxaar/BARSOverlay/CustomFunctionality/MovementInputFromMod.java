package Jaxaar.BARSOverlay.CustomFunctionality;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



//*** IMPORTANT ****
//Don't mind this, and Don't say anything about it PLEASE
//#Pranking with a mod

@SideOnly(Side.CLIENT)
public class MovementInputFromMod extends MovementInput
{
    public float strafe = 0.0F;
    public float forward = 0.0F;
    public boolean jumping = false;
    public boolean sneaking = false;


    public MovementInputFromMod(int forward, int strafe, boolean jump, boolean sneak)
    {
        this.forward = forward;
        this.strafe = strafe;
        this.jumping = jump;
        this.sneaking = sneak;
    }

    public MovementInputFromMod()
    {

    }

    public void updatePlayerMoveState()
    {
        this.moveForward = forward;
        this.moveStrafe = strafe;
        this.sneak = sneaking;
        this.jump = jumping;
        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}