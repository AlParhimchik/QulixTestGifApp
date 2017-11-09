/*
 * Copyright (C) 2015 Brian Lee (@hiBrianLee)
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

package com.example.sashok.qulixgifapp.activity;


import android.os.Bundle;

import com.example.sashok.qulixgifapp.mvvm.activity.ViewModelActivity;
import com.example.sashok.qulixgifapp.viewmodel.AppViewModelFactory;
import com.example.sashok.qulixgifapp.viewmodel.ViewModelFactory;

public class BaseActivity extends ViewModelActivity {

    protected ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        viewModelFactory= AppViewModelFactory.getInstanсe();

        super.onCreate(savedInstanceState);
    }
}
