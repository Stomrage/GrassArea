package com.stomrage.grassarea;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    private TerrainQuad terrain;
    private Material matTerrain;
    private DirectionalLight sun;
    private GrassArea grassArea;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        createTerrain();
        addLight();
        createGrassArea();
    }

    public void createGrassArea() {
        try {
            grassArea = new GrassArea(terrain, 8, assetManager, 75);
            grassArea.setColorTexture(assetManager.loadTexture("Textures/tile_1.png"));
            grassArea.setDissolveTexture(assetManager.loadTexture("Textures/noise.png"));
            grassArea.addDensityMap(assetManager.loadTexture("Textures/noise.png"));
            grassArea.addDensityMap(assetManager.loadTexture("Textures/noise_2.png"));
            grassArea.addLayer(0f, 0.5f, 2f, GrassArea.ColorChannel.RED_CHANNEL, GrassArea.DensityMap.DENSITY_MAP_1, 2f, 3f);
            grassArea.addLayer(0.5f, 0.5f, 2f, GrassArea.ColorChannel.BLUE_CHANNEL, GrassArea.DensityMap.DENSITY_MAP_2, 2f, 3f);
            grassArea.generate();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        GrassAreaControl grassAreaControl = new GrassAreaControl(cam);
        grassArea.addControl(grassAreaControl);
        grassArea.setAutoUpdate(true);
        rootNode.attachChild(grassArea);
    }

    private void addLight() {
        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        Vector3f sunDirection = new Vector3f(-.5f, -.5f, -.5f).normalizeLocal();
        sun.setDirection(sunDirection);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
        rootNode.addLight(sun);
    }

    private void createTerrain() {
        matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matTerrain.setBoolean("useTriPlanarMapping", false);
        matTerrain.setFloat("Shininess", 0.0f);
        matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/alpha1.png"));
        matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/alpha2.png"));
        TextureKey hmKey = new TextureKey("Textures/mountains512.png", false);
        Texture heightMapImage = assetManager.loadTexture(hmKey);
        Texture dirt = assetManager.loadTexture("Textures/Grass_1.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap", dirt);
        matTerrain.setFloat("DiffuseMap_0_scale", 64);
        Texture darkRock = assetManager.loadTexture("Textures/Grass_1.jpg");
        darkRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_1", darkRock);
        matTerrain.setFloat("DiffuseMap_1_scale", 64);
        Texture pinkRock = assetManager.loadTexture("Textures/Grass_1.jpg");
        pinkRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_2", pinkRock);
        matTerrain.setFloat("DiffuseMap_2_scale", 64);
        Texture riverRock = assetManager.loadTexture("Textures/Grass_1.jpg");
        riverRock.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_3", riverRock);
        matTerrain.setFloat("DiffuseMap_3_scale", 64);
        Texture grass = assetManager.loadTexture("Textures/Grass_1.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_4", grass);
        matTerrain.setFloat("DiffuseMap_4_scale", 64);
        Texture brick = assetManager.loadTexture("Textures/Grass_1.jpg");
        brick.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_5", brick);
        matTerrain.setFloat("DiffuseMap_5_scale", 64);
        Texture road = assetManager.loadTexture("Textures/Grass_1.jpg");
        road.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("DiffuseMap_6", road);
        matTerrain.setFloat("DiffuseMap_6_scale", 64);
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.3f);
            heightmap.load();
            heightmap.smooth(0.9f, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getScaledHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f)); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setModelBound(new BoundingBox());
        terrain.updateModelBound();
        rootNode.attachChild(terrain);
    }
}
