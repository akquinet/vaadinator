/*
 * Copyright 2016 Daniel Nordhoff-Vergien
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.akquinet.engineering.vaadinator.model;

/**
 * Artifacts which are generated per bean.
 * 
 * @author dve
 *
 */
public enum BeanArtifact {
	LIST_VIEW_IMPL("ListViewImpl"), //
	LIST_VIEW("ListView"), //
	LIST_PRES("ListPresenter"), //
	LIST_PRES_IMPL("ListPresenterImp"), //
	EDIT_VIEW_IMPL("EditViewImpl"), //
	EDIT_VIEW("EditView"), //
	EDIT_PRES("EditPresenter"), //
	EDIT_PRES_IMPL("EditPresenterImpl"), //
	ADD_VIEW("AddView"), //
	ADD_VIEW_IMPL("AddViewImpl"), //
	ADD_PRES("AddPresenter"), //
	ADD_PRES_IMPL("AddPresenterImpl"), //
	CHANGE_VIEW("ChangeView"), //
	CHANGE_VIEW_IMPL("ChangeViewImpl"), //
	CHANGE_PRES("ChangePresenter"), //
	CHANGE_PRES_IMPL("ChangePresenterImpl");

	private final String fileNameSuffix;

	private BeanArtifact(String fileNameSuffix) {
		this.fileNameSuffix = fileNameSuffix;
	}

	public String getFileNameSuffix() {
		return fileNameSuffix;
	}

	public boolean isView() {
		return this.name().contains("VIEW");
	}
}
