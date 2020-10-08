package com.hiit.timer;

public class DataSupport {

    public String properDateOnTheScreen(String date) {
        String[] arrayOfDateStrings = date.split(" ");

        switch (arrayOfDateStrings[0]) {
            case "janvier":
                arrayOfDateStrings[0] = "janvier";
                break;
            case "février":
                arrayOfDateStrings[0] = "février";
                break;
            case "mars":
                arrayOfDateStrings[0] = "mars";
                break;
            case "avril":
                arrayOfDateStrings[0] = "avril";
                break;
            case "mai":
                arrayOfDateStrings[0] = "mai";
                break;
            case "juin":
                arrayOfDateStrings[0] = "juin";
                break;
            case "juillet":
                arrayOfDateStrings[0] = "juillet";
                break;
            case "août":
                arrayOfDateStrings[0] = "août";
                break;
            case "septembre":
                arrayOfDateStrings[0] = "septembre";
                break;
            case "octobre":
                arrayOfDateStrings[0] = "octobre";
                break;
            case "novembre":
                arrayOfDateStrings[0] = "novembre";
                break;
            case "décembre":
                arrayOfDateStrings[0] = "décembre";
                break;
            default:
                arrayOfDateStrings[0] = "mois";
        }
        return arrayOfDateStrings[0] + " " + arrayOfDateStrings[1];
    }
}
