/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stomrage.grassarea;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Stomrage
 */
public class DebugSingleton {
    
    private static DebugSingleton instance;
    private AssetManager assetManager;
    private Node rootNode;
    
    public static DebugSingleton getInstance(){
        if(instance == null){
            instance = new DebugSingleton();
        }
        return instance;
    }
    
    public DebugSingleton(){
        
    }
    
    public void attachWireSphere(Vector3f pos, float size, ColorRGBA color) {
        Geometry g = new Geometry("wireframe sphere", new Sphere(20,20,size));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        g.setMaterial(mat);
        g.setLocalTranslation(pos);
        rootNode.attachChild(g);
    }    
    
    public void initDebug(AssetManager assetManager, Node rootNode){
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }
    
}
