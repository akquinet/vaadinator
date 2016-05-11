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
	ADD_PRES_IMPL("AddPresnterImpl"), //
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
