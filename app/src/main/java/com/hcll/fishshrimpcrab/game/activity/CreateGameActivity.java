package com.hcll.fishshrimpcrab.game.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.common.widget.bubbleseekbar.BubbleSeekBar;
import com.zcw.togglebutton.CustomToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hong on 2018/3/30.
 */

public class CreateGameActivity extends BaseActivity {

    @BindView(R.id.create_game_room_name_et)
    EditText roomNameEt;
    @BindView(R.id.create_game_base_score_tv)
    TextView baseScoreTv;
    @BindView(R.id.create_game_bring_sorce_tv)
    TextView bringSorceTv;
    @BindView(R.id.create_game_sorce_bsb)
    BubbleSeekBar sorceBsb;
    @BindView(R.id.create_game_fees_tv)
    TextView feesTv;
    @BindView(R.id.create_game_room_rate_tv)
    TextView roomRateTv;
    @BindView(R.id.create_game_duration_bsb)
    BubbleSeekBar durationBsb;
    @BindView(R.id.create_game_people_num_bsb)
    BubbleSeekBar peopleNumBsb;
    @BindView(R.id.create_game_authorize_input_ctb)
    CustomToggleButton authorizeInputCtb;
    @BindView(R.id.create_game_eat_dice_ctb)
    CustomToggleButton eatDiceCtb;
    @BindView(R.id.create_game_move_chips_ctb)
    CustomToggleButton moveChipsCtb;
    @BindView(R.id.create_game_grab_dealer_rb)
    RadioButton grabDealerRb;
    @BindView(R.id.create_game_fixed_dealer_rb)
    RadioButton fixedDealerRb;
    @BindView(R.id.create_game_turn_dealer_rb)
    RadioButton turnDealerRb;
    @BindView(R.id.create_game_dealer_rg)
    RadioGroup dealerRg;
    @BindView(R.id.create_game_dealer_bsb)
    BubbleSeekBar dealerBsb;
    @BindView(R.id.club_create_sure_tv)
    TextView sureTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        ButterKnife.bind(this);
        initTopBar();

    }

    private void initTopBar() {
        showTopBar();
        getTopBar().setTitle("组局设置");
    }

    @OnClick(R.id.club_create_sure_tv)
    public void onViewClicked() {
    }
}
