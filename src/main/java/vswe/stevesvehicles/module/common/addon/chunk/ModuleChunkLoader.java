package vswe.stevesvehicles.module.common.addon.chunk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import vswe.stevesvehicles.client.gui.screen.GuiVehicle;
import vswe.stevesvehicles.localization.entry.module.LocalizationIndependence;
import vswe.stevesvehicles.module.common.addon.ModuleAddon;
import vswe.stevesvehicles.network.DataReader;
import vswe.stevesvehicles.vehicle.VehicleBase;
import vswe.stevesvehicles.client.ResourceHelper;
import vswe.stevesvehicles.module.IActivatorModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModuleChunkLoader extends ModuleAddon implements IActivatorModule{
	public ModuleChunkLoader(VehicleBase vehicleBase) {
		super(vehicleBase);
	}

	
	@Override
	public boolean hasSlots() {
		return false;
	}

	@Override
	public boolean hasGui(){
		return true;
	}

	@Override
	public int guiWidth() {
		return 80;
	}

	@Override
	public int guiHeight() {
		return 35;
	}

	@Override
	public void drawForeground(GuiVehicle gui) {
	    drawString(gui,"Chunk Loader", 8, 6, 0x404040);
	}

    @Override
    public void update() {
        super.update();

		if (!rdyToInit) {
			rdyToInit = true;
		}
		
		if (isLoadingChunk() &&  !getVehicle().hasFuelForModule() && !getVehicle().getWorld().isRemote) {
			setChunkLoading(false);
		}	
    }	
	
	private boolean rdyToInit;
	public void setChunkLoading(boolean val) {
	
		if (!isPlaceholder()) {
			updateDw(0, (byte)(val ? 1 : 0));
			
			//just to make sure
			if (!getVehicle().getWorld().isRemote && rdyToInit) {
				if (val) {
					getVehicle().initChunkLoading();
				}else{
					getVehicle().dropChunkLoading();
				}
			}
		}
	}
	
	
	private boolean isLoadingChunk() {
        return !isPlaceholder() && getDw(0) != 0;
	}

    private static final int TEXTURE_SPACING = 1;
    private static final ResourceLocation TEXTURE = ResourceHelper.getResource("/gui/chunk.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackground(GuiVehicle gui, int x, int y) {
		ResourceHelper.bindResource(TEXTURE);

		int imageId = isLoadingChunk() ? 1 : 0;
		int borderID = 0;
		if (inRect(x,y, BUTTON_RECT)) {
			borderID = 1;			
		}

		drawImage(gui, BUTTON_RECT, TEXTURE_SPACING, TEXTURE_SPACING + (TEXTURE_SPACING + BUTTON_RECT[3]) * borderID);

		int srcY = TEXTURE_SPACING + (TEXTURE_SPACING + BUTTON_RECT[3]) * 2 + imageId * (BUTTON_RECT[3] - 2 + TEXTURE_SPACING);
		drawImage(gui, BUTTON_RECT[0] + 1, BUTTON_RECT[1] + 1, TEXTURE_SPACING, srcY, BUTTON_RECT[2] - 2, BUTTON_RECT[3] - 2);
	}

	private static final int[] BUTTON_RECT = new int[] {20, 20, 24, 12};

	@Override
	public void drawMouseOver(GuiVehicle gui, int x, int y) {
		drawStringOnMouseOver(gui, getStateName(), x,y, BUTTON_RECT);
	}


	private String getStateName() {
        return LocalizationIndependence.CHUNK.translate(isLoadingChunk() ? "1" : "0");
	}

	@Override
	public void mouseClicked(GuiVehicle gui, int x, int y, int button) {
		if (button == 0) {
			if (inRect(x,y, BUTTON_RECT)) {
				sendPacketToServer(getDataWriter());
			}
		}
	}
	

	@Override
	protected void receivePacket(DataReader dr, EntityPlayer player) {
        setChunkLoading(!isLoadingChunk());
	}

	@Override
	public int numberOfDataWatchers() {
		return 1;
	}
	
	@Override
	public void initDw() {
		addDw(0,(byte)0);
	}	
	
	public int getConsumption(boolean isMoving) {
		return isLoadingChunk() ? 5 : super.getConsumption(isMoving);
	}	
	
	@Override
	protected void save(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("ChunkLoading", isLoadingChunk());
	}
	
	@Override
	protected void load(NBTTagCompound tagCompound) {
		setChunkLoading(tagCompound.getBoolean("ChunkLoading"));
	}	

	public void doActivate(int id) {
		setChunkLoading(true);
	}
	public void doDeActivate(int id) {
		setChunkLoading(false);
	}
	public boolean isActive(int id) {
		return isLoadingChunk();
	}
	
}