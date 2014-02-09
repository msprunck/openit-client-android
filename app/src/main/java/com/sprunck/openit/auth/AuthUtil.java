/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.sprunck.openit.auth;

import android.text.TextUtils;
import com.google.android.gms.common.Scopes;

/**
 * Provides static utility methods to help make authenticated requests.
 */
public class AuthUtil {

    private static final String TAG = AuthUtil.class.getSimpleName();

    public static final String[] SCOPES = {
            Scopes.PLUS_LOGIN
    };

    public static final String[] ACTIONS = {
            "http://schemas.google.com/AddActivity"
    };

    public static final String SCOPE_STRING = "oauth2:" + TextUtils.join(" ", SCOPES);
}
