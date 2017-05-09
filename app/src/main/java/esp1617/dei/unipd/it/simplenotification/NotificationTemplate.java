package esp1617.dei.unipd.it.simplenotification;

import java.util.Calendar;

/**
 * Created by boemd on 09/05/2017.
 */

class NotificationTemplate {
    private int id;
    private Calendar when;
    private String text;
    public NotificationTemplate(int id, Calendar when, String text){
        this.id=id;
        this.when=when;
        this.text=text;
    }

    //i metodi setter non sono necessari
    int getId(){return id;}
    Calendar getWhen(){return when;}
    String getText(){return text;}

}
