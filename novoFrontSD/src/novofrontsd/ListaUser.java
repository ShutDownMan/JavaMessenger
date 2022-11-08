/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package novofrontsd;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;
import java.awt.Component;

/**
 *
 * @author laril
 
public class ListaUser<E extends Object> extends JList<E> {
    
    public ListaUser(){
        
    }
    
    @Override
    public ListCellRenderer getCellRenderer(){
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean selected, boolean focus){
                ItemUser user = ItemUser();
                user
            }
        };
    }
}
*/