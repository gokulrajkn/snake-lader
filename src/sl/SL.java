/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sl;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author bayasys
 */
public class SL {

    public static home view;
    public static JFrame frame;
    private static HashMap componentMap;

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setBounds(100, 50, 850, 650);
        view = new home();
        frame.add(view);
        frame.setVisible(true);
        view.addKeyListener(view);
        view.setFocusable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void createComponentMap(JPanel parent) {
        componentMap = new HashMap<String, Component>();
        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            componentMap.put(components[i].getName(), components[i]);
        }
    }

    public static Component getComponentByName(String name, JPanel parentPnl) {
        createComponentMap(parentPnl);
        if (componentMap.containsKey(name)) {
            return (Component) componentMap.get(name);
        } else {
            return null;
        }
    }

}
