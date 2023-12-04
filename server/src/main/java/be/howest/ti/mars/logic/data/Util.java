package be.howest.ti.mars.logic.data;

import java.sql.Date;

public class Util {

    private Util() {}
    public static Date convertToSqlDate(java.util.Date javaDate){
        return new java.sql.Date(javaDate.getTime());
    }
}
