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

package com.sprunck.openit.model;


/**
 * POJO representing a user's profile on OpenIt.
 *
 * @author Matthieu Sprunck
 */
public class User {
    /**
     * The google user id.
     */
    public String id;

    /**
     * The google display name.
     */
    public String googleDisplayName = "";

    /**
     * The google public profile url.
     */
    public String googlePublicProfileUrl = "";

    /**
     * The google public profile photo urd.
     */
    private final String googlePublicProfilePhotoUrl = "";

    /**
     * Retrieves the profile URL.
     *
     * @return an URL
     */
    public String getProfileUrl() {
        return this.googlePublicProfilePhotoUrl.split("\\?sz=")[0] + "?sz=100";
    }
}
