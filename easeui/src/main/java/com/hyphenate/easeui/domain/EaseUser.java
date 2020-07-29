/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.domain;

import com.hyphenate.chat.EMContact;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;

public class EaseUser extends EMContact {
	/**
	 * EaseConstant
	 * 	public static final int CHATTYPE_SINGLE = 1;
	 *     public static final int CHATTYPE_GROUP = 2;
	 *     public static final int CHATTYPE_CHATROOM = 3;
	 */
	protected int chatType = EaseConstant.CHATTYPE_SINGLE;
	protected String groupId = "";

	/**
	 * initial letter for nickname
	 */
	protected String initialLetter;
	/**
	 * avatar of the user
	 */
	protected String avatar;

	public EaseUser(String username) {
		this.username = username;
	}

	public String getInitialLetter() {
		if (initialLetter == null) {
			EaseCommonUtils.setUserInitialLetter(this);
		}
		return initialLetter;
	}

	public void setInitialLetter(String initialLetter) {
		this.initialLetter = initialLetter;
	}


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EaseUser)) {
			return false;
		}
		return getUsername().equals(((EaseUser) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}

	public int getChatType() {
		return chatType;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public String getGroupId() {
		return groupId;
	}

	public EaseUser setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}
}