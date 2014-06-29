package vswe.stevesvehicles.modules.data.registries.carts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import vswe.stevesvehicles.client.rendering.models.ModelLever;
import vswe.stevesvehicles.client.rendering.models.ModelWheel;
import vswe.stevesvehicles.modules.data.ModuleData;
import vswe.stevesvehicles.modules.data.ModuleDataGroup;
import vswe.stevesvehicles.modules.data.ModuleRegistry;
import vswe.stevesvehicles.modules.data.ModuleSide;
import vswe.stevesvehicles.modules.data.registries.ModuleRegistryTravel;
import vswe.stevesvehicles.old.Helpers.ResourceHelper;
import vswe.stevesvehicles.old.Modules.Addons.ModuleBrake;
import vswe.stevesvehicles.old.Modules.Realtimers.ModuleAdvControl;
import vswe.stevesvehicles.vehicles.VehicleRegistry;

import static vswe.stevesvehicles.old.Helpers.ComponentTypes.*;

public class ModuleRegistryCartTravel extends ModuleRegistry {
    public ModuleRegistryCartTravel() {
        super("steves_carts_travel");

        ModuleDataGroup seats = ModuleDataGroup.getGroup(ModuleRegistryTravel.SEAT_KEY);

        ModuleData brake = new ModuleData("brake_handle", ModuleBrake.class, 12) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));
            }
        };

        brake.addShapedRecipe(  null,                   null,                   "dyeRed",
                                Items.iron_ingot,       REFINED_HANDLE,         null,
                                Items.redstone,         Items.iron_ingot,       null);


        brake.addVehicles(VehicleRegistry.CART);
        brake.addSides(ModuleSide.RIGHT);
        brake.addRequirement(seats);
        register(brake);



        ModuleData controller = new ModuleData("advanced_control_system", ModuleAdvControl.class, 38) {
            @Override
            @SideOnly(Side.CLIENT)
            public void loadModels() {
                addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png")));
                addModel("Wheel", new ModelWheel());
            }
        };

        controller.addShapedRecipe(null, GRAPHICAL_INTERFACE, null,
                Items.redstone, WHEEL, Items.redstone,
                Items.iron_ingot, Items.iron_ingot, SPEED_HANDLE);


        controller.addVehicles(VehicleRegistry.CART);
        controller.addSides(ModuleSide.RIGHT);
        controller.addRequirement(seats);
        register(controller);
    }
}