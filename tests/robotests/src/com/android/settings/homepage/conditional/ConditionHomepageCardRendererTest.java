/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.homepage.conditional;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.settings.R;
import com.android.settings.homepage.ControllerRendererPool;
import com.android.settings.homepage.HomepageCard;
import com.android.settings.testutils.SettingsRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

@RunWith(SettingsRobolectricTestRunner.class)
public class ConditionHomepageCardRendererTest {

    @Mock
    private ControllerRendererPool mControllerRendererPool;
    @Mock
    private ConditionHomepageCardController mController;
    private Context mContext;
    private ConditionHomepageCardRenderer mRenderer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = spy(RuntimeEnvironment.application);
        mRenderer = new ConditionHomepageCardRenderer(mContext, mControllerRendererPool);
    }

    @Test
    public void bindView_shouldSetListener() {
        final int viewType = mRenderer.getViewType();
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final View view = LayoutInflater.from(mContext).inflate(viewType, recyclerView, false);
        final RecyclerView.ViewHolder viewHolder = mRenderer.createViewHolder(view);
        final View card = view.findViewById(R.id.content);
        when(mControllerRendererPool.getController(mContext,
                HomepageCard.CardType.CONDITIONAL)).thenReturn(mController);

        mRenderer.bindView(viewHolder, getHomepageCard());

        assertThat(card).isNotNull();
        assertThat(card.hasOnClickListeners()).isTrue();
    }

    @Test
    public void viewClick_shouldInvokeControllerPrimaryClick() {
        final int viewType = mRenderer.getViewType();
        final RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final View view = LayoutInflater.from(mContext).inflate(viewType, recyclerView, false);
        final RecyclerView.ViewHolder viewHolder = mRenderer.createViewHolder(view);
        final View card = view.findViewById(R.id.content);
        when(mControllerRendererPool.getController(mContext,
                HomepageCard.CardType.CONDITIONAL)).thenReturn(mController);

        mRenderer.bindView(viewHolder, getHomepageCard());

        assertThat(card).isNotNull();
        card.performClick();

        verify(mController).onPrimaryClick(any(HomepageCard.class));
    }

    private HomepageCard getHomepageCard() {
        ConditionCard conditionCard = ((ConditionCard.Builder) new ConditionCard.Builder()
                .setConditionId(123)
                .setMetricsConstant(1)
                .setActionText("test_action")
                .setName("test_name")
                .setCardType(HomepageCard.CardType.CONDITIONAL)
                .setTitleText("test_title")
                .setSummaryText("test_summary")
                .setIconDrawable(mContext.getDrawable(R.drawable.ic_do_not_disturb_on_24dp))
                .setIsHalfWidth(true))
                .build();
        return conditionCard;
    }
}