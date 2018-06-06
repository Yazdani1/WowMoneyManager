package yazdaniscodelab.wowmoneymanager.Model;

import android.widget.EditText;

/**
 * Created by Yazdani on 5/19/2018.
 */

public class Data {

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int ammount;
    private String type;
    private String note;
    private String id;
    private String date;

    public Data(int ammount, String type, String note, String id, String date) {
        this.ammount = ammount;
        this.type = type;
        this.note = note;
        this.id = id;
        this.date = date;
    }

    public Data(){

  }

}
