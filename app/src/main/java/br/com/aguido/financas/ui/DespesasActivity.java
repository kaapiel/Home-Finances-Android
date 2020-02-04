package br.com.aguido.financas.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import br.com.aguido.financas.R;
import br.com.aguido.financas.interfaces.OnChangeFragmentListener;
import br.com.aguido.financas.utils.CustomTypefaceSpan;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Inmetrics on 15/09/2016.
 */
public class DespesasActivity extends AppCompatActivity implements OnChangeFragmentListener {

    @BindView(R.id.loading_content)
    RelativeLayout loading;

    @BindView(R.id.navigationview_home)
    NavigationView nav;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    TextView menuUserName;

    TextView menuHellow;

    private int menuIdCurrent = -1;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        initViews();
        menuItemFonts();

        View drawerHeader = nav.inflateHeaderView(R.layout.menu_header);
        menuUserName = (TextView) drawerHeader.findViewById(R.id.menu_header_name_user);
        menuHellow = (TextView) drawerHeader.findViewById(R.id.menu_header_helow);
        nav.inflateMenu(R.menu.itens_menu_logged);

    }

    private void initViews() {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                changeFragment(menuItem.getItemId());
                mDrawerLayout.closeDrawers();
                return false;
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open_desc, R.string.drawer_close_desc) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.syncState();
                closeKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerToggle.syncState();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void menuItemFonts() {
        Menu m = nav.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/MuseoSans_500.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());

//        if (mi.getTitle().equals(getBaseContext().getResources().getString(R.string.menu_item_my_account))) {
//            mNewTitle.setSpan(new TextAppearanceSpan(getBaseContext(), R.style.LiveloPontosTheme_NavigationView_TextAppearance_MyAccount), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        }

        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void changeFragment(int menuId) {

        //if (menuIdCurrent == menuId) return;

//        final Bundle bServicos = new Bundle();
//        bServicos.putString("qtdTestesOK_servicos", qtdTestesOK_servicos);
//        bServicos.putString("qtdTestesNOK_servicos", qtdTestesNOK_servicos);
//        bServicos.putString("pacote_servicos", pacote_servicos);
//        bServicos.putParcelableArrayList("testes_servicos", testes_servicos);

        menuIdCurrent = menuId;

        switch (menuId) {

            case R.id.menu_relatorioGeral:

                break;

            case R.id.menu_product_catalog:
                startActivity(new Intent(DespesasActivity.this, Despesas.class));
                break;

            case R.id.menu_exit:
                //menuIdCurrent = R.id.menu_home;
                //showDialogLogout();
                return;

            default:
                return;
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void replaceFragment(FragmentManager fm, Fragment fragment, String label, boolean toBack, Bundle b) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        fragment.setArguments(b);
        transaction.replace(R.id.content_frame, fragment, label);

        if (toBack) {
            transaction.addToBackStack(label);
        }

        transaction.commitAllowingStateLoss();
    }

    private void customizeToolbar(String title) {
    }


    private void customizeToolbarDrawable() {
        toolbar.setBackgroundResource(R.drawable.bg_toolbar_0);
    }

    private void customizeToolbarBlueAlpha() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void setFragmentOnMenu(int menuId) {
        changeFragment(menuId);
    }

    @Override
    public void setMenuSelected(int menuId) {
        menuIdCurrent = menuId;

        switch (menuId) {

            case R.id.menu_relatorioGeral:
                customizeToolbar("HOME FINANCES");
                break;

            case R.id.menu_product_catalog:
                customizeToolbar("GRÁFICOS");
                break;

        }
    }

}
