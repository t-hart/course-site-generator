/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import jtps.jTPS_Transaction;
import csg.data.TAData;

/**
 *
 * @author moham_000
 */
public class change implements jTPS_Transaction {

    TAData data;
    String oldName;
    String newName;
    String oldEmail;
    String newEmail;
    boolean oldUG;
    boolean newUG;

    public change(String oldName, String oldEmail, String newName, String newEmail, TAData data, boolean oldUG, boolean newUG) {
        this.oldName = oldName;
        this.newEmail = newEmail;
        this.oldEmail = oldEmail;
        this.newName = newName;
        this.data = data;
        this.oldUG = oldUG;
        this.newUG = newUG;
    }

    @Override
    public void doTransaction() {
        if (!(oldName.equals(newName))) {
            HashMap<String, StringProperty> officeHours = data.getOfficeHours();
            Set<String> a = officeHours.keySet();

            for (Iterator<String> it = a.iterator(); it.hasNext();) {
                StringProperty cellProp = officeHours.get(it.next());
                String cellText = cellProp.getValue();
                String text = "";
                String[] k = cellText.split("\n");
                for (int j = 0; j < k.length; j++) {
                    if (k[j].equals(oldName)) {
                        text += newName;
                    } else {
                        text += k[j];
                    }
                    if (j != k.length - 1) {
                        text += "\n";
                    }
                }
                cellProp.set(text);
            }
        }
        data.removeTA(oldName);
        data.addTA(newName, newEmail, newUG);
    }

    @Override
    public void undoTransaction() {
        if (!(oldName.equals(newName))) {

            HashMap<String, StringProperty> officeHours = data.getOfficeHours();
            Set<String> a = officeHours.keySet();

            for (Iterator<String> it = a.iterator(); it.hasNext();) {
                StringProperty cellProp = officeHours.get(it.next());
                String cellText = cellProp.getValue();
                String text = "";
                String[] k = cellText.split("\n");
                for (int j = 0; j < k.length; j++) {
                    if (k[j].equals(newName)) {
                        text += oldName;
                    } else {
                        text += k[j];
                    }
                    if (j != k.length - 1) {
                        text += "\n";
                    }
                }
                cellProp.set(text);
            }
        }
        data.removeTA(newName);
        data.addTA(oldName, oldEmail, oldUG);
    }

}
