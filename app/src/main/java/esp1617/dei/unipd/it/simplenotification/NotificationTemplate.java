package esp1617.dei.unipd.it.simplenotification;

import java.util.Calendar;

/**
 * Created by boemd on 09/05/2017.
 */

public class NotificationTemplate {
    private int id;
    private Calendar when;
    private String text;
    public NotificationTemplate(int id, Calendar when, String text){
        this.id=id;
        this.when=when;
        this.text=text;
    }

    //i metodi setter non sono necessari
    public int getId(){return id;}
    public Calendar getWhen(){return when;}
    public String getText(){return text;}

}
