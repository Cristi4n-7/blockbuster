package noname.blockbuster.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noname.blockbuster.entity.CameraEntity;

public class CameraAttributesUpdate implements IMessage
{
	protected int id;
	protected float speed;
	protected float accelerationRate;
	protected float accelerationMax;
	protected boolean canFly; 
	
	public CameraAttributesUpdate() {}
	
	public CameraAttributesUpdate(int eid, float speeed, float rate, float max, boolean fly)
	{
		id = eid;
		speed = speeed;
		accelerationRate = rate;
		accelerationMax = max;
		canFly = fly;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		id = buf.readInt();
		speed = buf.readFloat();
		accelerationRate = buf.readFloat();
		accelerationMax = buf.readFloat();
		canFly = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(id);
		buf.writeFloat(speed);
		buf.writeFloat(accelerationRate);
		buf.writeFloat(accelerationMax);
		buf.writeBoolean(canFly);
	}
	
	public static class Handler implements IMessageHandler<CameraAttributesUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(final CameraAttributesUpdate message, final MessageContext ctx)
		{
			IThreadListener world = (WorldServer)ctx.getServerHandler().playerEntity.worldObj;
			
			world.addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					Entity entity = ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.id);
					CameraEntity camera = (CameraEntity)entity;
					
					camera.speed = message.speed;
					camera.accelerationRate = message.accelerationRate;
					camera.accelerationMax = message.accelerationMax;
					camera.canFly = message.canFly;
				}
			});
			
			return null;
		}
	}
}
