package com.yckj.hhz.multimedialibrary;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.activity.BaseActivity;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;
import cn.finalteam.rxgalleryfinal.utils.OsCompat;
import cn.finalteam.rxgalleryfinal.utils.ThemeUtils;

public class MediaPageActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTvToolbarTitle;
    private TextView mTvOverAction;
    private View mToolbarDivider;

    MediaPreviewFragment mMediaPreviewFragment;

    int mPreviewPosition;
    List<MediaBean> mMediaBeanList;

    @Override
    public int getContentView() {
        return R.layout.gallery_activity_media;
    }

    @Override
    protected void onCreateOk(@Nullable Bundle savedInstanceState) {

        mPreviewPosition = getIntent().getIntExtra("PreviewPosition", 0);
        mMediaBeanList = getIntent().getParcelableArrayListExtra("MediaBeans");

        showMediaPreviewFragment();
    }

    public void showMediaPreviewFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mMediaPreviewFragment = MediaPreviewFragment.newInstance(mConfiguration, mPreviewPosition);
        ft.add(R.id.fragment_container, mMediaPreviewFragment);
        ft.show(mMediaPreviewFragment);
        ft.commit();

        String title = getString(R.string.gallery_page_title, mPreviewPosition, mMediaBeanList.size());
        mTvToolbarTitle.setText(title);
    }

    @Override
    public void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        mTvOverAction = (TextView) findViewById(R.id.tv_over_action);
        mToolbarDivider = findViewById(R.id.toolbar_divider);
    }

    @Override
    protected void setTheme() {
        Drawable closeDrawable = ThemeUtils.resolveDrawable(this, R.attr.gallery_toolbar_close_image, R.drawable.gallery_default_toolbar_close_image);
        int closeColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_close_color, R.color.gallery_default_toolbar_widget_color);
        closeDrawable.setColorFilter(closeColor, PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(closeDrawable);

        int overButtonBg = ThemeUtils.resolveDrawableRes(this, R.attr.gallery_toolbar_over_button_bg);
        if (overButtonBg != 0) {
            mTvOverAction.setBackgroundResource(overButtonBg);
        } else {
            OsCompat.setBackgroundDrawableCompat(mTvOverAction, createDefaultOverButtonBgDrawable());
        }

        float overTextSize = ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_over_button_text_size, R.dimen.gallery_default_toolbar_over_button_text_size);
        mTvOverAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, overTextSize);

        int overTextColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_text_color, R.color.gallery_default_toolbar_over_button_text_color);
        mTvOverAction.setTextColor(overTextColor);

        float titleTextSize = ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_text_size, R.dimen.gallery_default_toolbar_text_size);
        mTvToolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);

        int titleTextColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_text_color, R.color.gallery_default_toolbar_text_color);
        mTvToolbarTitle.setTextColor(titleTextColor);

        int gravity = ThemeUtils.resolveInteger(this, R.attr.gallery_toolbar_text_gravity, R.integer.gallery_default_toolbar_text_gravity);
        mTvToolbarTitle.setLayoutParams(new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, gravity));

        int toolbarBg = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_bg, R.color.gallery_default_color_toolbar_bg);
        mToolbar.setBackgroundColor(toolbarBg);

        int toolbarHeight = (int) ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_height, R.dimen.gallery_default_toolbar_height);
        mToolbar.setMinimumHeight(toolbarHeight);

        int statusBarColor = ThemeUtils.resolveColor(this, R.attr.gallery_color_statusbar, R.color.gallery_default_color_statusbar);
        ThemeUtils.setStatusBarColor(statusBarColor, getWindow());

        int dividerHeight = (int) ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_divider_height, R.dimen.gallery_default_toolbar_divider_height);
        int dividerBottomMargin = (int) ThemeUtils.resolveDimen(this, R.attr.gallery_toolbar_bottom_margin, R.dimen.gallery_default_toolbar_bottom_margin);
        LinearLayout.LayoutParams dividerLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight);
        dividerLP.bottomMargin = dividerBottomMargin;
        mToolbarDivider.setLayoutParams(dividerLP);

        Drawable dividerDrawable = ThemeUtils.resolveDrawable(this, R.attr.gallery_toolbar_divider_bg, R.color.gallery_default_toolbar_divider_bg);
        OsCompat.setBackgroundDrawableCompat(mToolbarDivider, dividerDrawable);

        setSupportActionBar(mToolbar);
    }

    private StateListDrawable createDefaultOverButtonBgDrawable() {
        int dp12 = (int) ThemeUtils.applyDimensionDp(this, 12.f);
        int dp8 = (int) ThemeUtils.applyDimensionDp(this, 8.f);
        float dp4 = ThemeUtils.applyDimensionDp(this, 4.f);
        float[] round = new float[]{dp4, dp4, dp4, dp4, dp4, dp4, dp4, dp4};
        ShapeDrawable pressedDrawable = new ShapeDrawable(new RoundRectShape(round, null, null));
        pressedDrawable.setPadding(dp12, dp8, dp12, dp8);
        int pressedColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_pressed_color, R.color.gallery_default_toolbar_over_button_pressed_color);
        pressedDrawable.getPaint().setColor(pressedColor);

        int normalColor = ThemeUtils.resolveColor(this, R.attr.gallery_toolbar_over_button_normal_color, R.color.gallery_default_toolbar_over_button_normal_color);
        ShapeDrawable normalDrawable = new ShapeDrawable(new RoundRectShape(round, null, null));
        normalDrawable.setPadding(dp12, dp8, dp12, dp8);
        normalDrawable.getPaint().setColor(normalColor);

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);

        return stateListDrawable;
    }

    public List<MediaBean> getCheckedList() {
        return mMediaBeanList;
    }
}
