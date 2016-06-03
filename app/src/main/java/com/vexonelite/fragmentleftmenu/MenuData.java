package com.vexonelite.fragmentleftmenu;

public class MenuData {

    private String mTitle;

    public MenuData () {}

    public void setMenuTitle (String title) {
        if ( (null != title) && (!title.isEmpty()) ) {
            mTitle = title;
        }
    }

    public String getMenuTitle () {
        return mTitle;
    }
}
