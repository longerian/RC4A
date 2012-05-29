/*Copyright (C) 2012 Longerian (http://www.longerian.me)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package org.rubychina.android;

import java.util.ArrayList;
import java.util.List;

import org.rubychina.android.type.Node;
import org.rubychina.android.type.SiteGroup;
import org.rubychina.android.type.Topic;
import org.rubychina.android.type.User;

public enum GlobalResource {

	INSTANCE;
	
	private List<Topic> curTopics = new ArrayList<Topic>();
	
	private List<Node> nodes = new ArrayList<Node>();
	
	private List<User> users = new ArrayList<User>();
	
	private List<SiteGroup> sites = new ArrayList<SiteGroup>();
	
	public synchronized List<Topic> getCurTopics() {
		return curTopics;
	}

	public synchronized void setCurTopics(List<Topic> curTopics) {
		this.curTopics = curTopics;
	}

	public synchronized List<Node> getNodes() {
		return nodes;
	}

	public synchronized void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public synchronized List<User> getUsers() {
		return users;
	}

	public synchronized void setUsers(List<User> users) {
		this.users = users;
	}

	public synchronized List<SiteGroup> getSites() {
		return sites;
	}

	public synchronized void setSites(List<SiteGroup> sites) {
		this.sites = sites;
	}
	
}
