package test;

import control.Controller;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class ValidationTest {

    private Controller controller = new Controller();

    @Test
    public void test1(){
        //Inserisco un input valido
        System.out.println("Test con dati validi");
        boolean flag = validateInput("11:00", "13:00", "2012-12-12");
        Assert.assertTrue(flag);
    }

    @Test
    public void test2(){
        //Inserisco orario con formato errato
        System.out.println("Test con formato orario non valido");
        boolean flag = validateInput("11,00", "13:00", "2012-12-12");
        Assert.assertFalse(flag);
    }

    @Test
    public void test3(){
        //Inserisco data errata
        System.out.println("Test con data non valida");
        boolean flag = validateInput("11:00", "13:00", "2018-02-30");
        Assert.assertFalse(flag);
    }

    @Test
    public void test4(){
        //Inserisco data con formato errato
        System.out.println("Test con formato data non valido");
        boolean flag = validateInput("11:00", "13:00", "12-12-2012");
        Assert.assertFalse(flag);
    }

    @Test
    public void test5(){
        //Inserisco orario errato
        System.out.println("Test con orario non valido");
        boolean flag = validateInput("11:00", "10:00", "2012-12-12");
        Assert.assertFalse(flag);
    }

    @Test
    public void test6(){
        //Inserisco un input vuoto
        System.out.println("Test con campi vuoti");
        boolean flag = validateInput("", "", "");
        Assert.assertTrue(flag);
    }

    private boolean validateInput(String begin, String end, String date){
        boolean flag = true;

        if(!begin.isEmpty()) {
            if (!controller.timeParse(begin)) {
                flag = false;
            }
        }
        if(!end.isEmpty()) {
            if (!controller.timeParse(end)) {
                flag = false;
            }
        }
        if(!date.isEmpty()) {
            if (!controller.dateParse(date)) {
                flag = false;
            }
        }
        if(flag && !begin.isEmpty() && !end.isEmpty()) {
            if(end.compareTo(begin) < 0) {
                flag = false;
            }
        }
        return flag;
    }
}

