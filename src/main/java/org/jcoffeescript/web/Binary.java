/*
 * Copyright 2011 Leonardo Verissimo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jcoffeescript.web;

import java.io.File;
import java.net.URL;

/*default*/ class Binary {
	
	private String content;
	private long lastModified;
	
	public Binary(URL coffeeURL, String content) {
		this.lastModified = lastModified(coffeeURL);
		this.content = content;
	}

	public boolean isOlderThan(URL coffeeURL) {
		return this.lastModified < lastModified(coffeeURL);
	}
	
	public String getContent() {
		return content;
	}
	
	public long getLastModified() {
		return lastModified;
	}

	private long lastModified(URL coffeeURL) {
		File file = new File(coffeeURL.getFile());
		// when resource is inside WAR, file doesn't exist and
		// lastModified equals zero
		return file.lastModified();
	}
}
