package com.mine.materialdesign;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;

import com.mine.R;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/12/2
 */
public class NavigationViewAcitivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private Menu mLvLeftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_navigation_view_activity);

    }
}
