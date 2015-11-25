/*
 * Copyright 2014 akquinet engineering GmbH
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
package de.akquinet.engineering.vaadinator.mojo;

import com.github.javaparser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.WordUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import de.akquinet.engineering.vaadinator.dao.SourceDao;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.util.UnicodeUtil;

/**
 * Generate Code.
 * 
 * @goal codegenerator
 */
public class CodeGeneratorMojo extends AbstractMojo {

	private static final String TEMPLATE_PACKAGE = "/de/akquinet/engineering/vaadinator/templates/";

	/**
	 * The maven project.
	 * 
	 * @parameter expression="${project}"
	 * @readonly
	 */
	private MavenProject project;
	
	/**
	 * The includes
	 * 
	 * @parameter
	 */
	private String[] includes = null;
	
	/**
	 * The excludes
	 * 
	 * @parameter
	 */
	private String[] excludes = null;

	/**
	 * Generate which type of artifacts
	 */
	private String genType = GenType.ALL.toString();

	/**
	 * Generate EJB or Plain artifacts
	 * 
	 * @parameter
	 */
	private String artifactType = ArtifactType.PLAIN.toString();

	/**
	 * Generate the servlet (and web.xml et al)
	 * 
	 * @parameter
	 */
	private boolean generateServlet = true;
	
	private static enum GenType {
		SOURCES, RESOURCES, ALL, NONE
	}

	private static enum ArtifactType {
		PLAIN, EJB, ALL, NONE
	}

	public void execute() throws MojoExecutionException {
//		System.out.println(FileUtils.getFiles((new File(project.getBasedir(), "src/main/java")), includes, excludes));
//		if(true)return;
		getLog().info("Hello, code world. - I'm " + project.getBasedir().getAbsolutePath() + " doing " + artifactType);
		File src = (new File(project.getBasedir(), "src/main/java"));
		File genSrc = (new File(project.getBasedir(), "target/generated-sources"));
		try {
			processJavaFiles(src, genSrc, new SourceDao(), toValidJavaClassName(project.getArtifactId()), project.getVersion(), generateServlet,
					ArtifactType.valueOf(artifactType.toUpperCase()), GenType.valueOf(genType.toUpperCase()));
		} catch (IOException e) {
			throw new MojoExecutionException("Fehler bei Generieren", e);
		} catch (ParseException e) {
			throw new MojoExecutionException("Fehler bei Generieren", e);
		}
	}

	private static void processJavaFiles(File sourceFolderStart, File targetFolderBaseStart, SourceDao sourceDao, String projectName, String version,
			boolean genServletBase, ArtifactType artifactTypeEn, GenType genTypeEn)
			throws ParseException, IOException {
		List<BeanDescription> beanDescriptions = new ArrayList<BeanDescription>();
		File targetFolderSrcStart = new File(targetFolderBaseStart, "java");
		targetFolderSrcStart.mkdirs();
		File targetFolderResStart = new File(targetFolderBaseStart, "resources");
		targetFolderResStart.mkdirs();
		File targetFolderTesteditStart = new File(targetFolderBaseStart, "testeditor");
		targetFolderTesteditStart.mkdirs();
		File targetFolderWebStart = new File(targetFolderBaseStart, "webapp");
		targetFolderWebStart.mkdirs();
		exploreFolders(sourceFolderStart, targetFolderSrcStart, "", sourceDao, beanDescriptions);
		// ensure bean descriptions are sorted as e.g. Presenter Factories need
		// a definite order of classes ("null" as cls name can be accepted)
		Collections.sort(beanDescriptions, new Comparator<BeanDescription>() {

			@Override
			public int compare(BeanDescription o1, BeanDescription o2) {
				return String.valueOf(o1.getClassName()).compareTo(
						String.valueOf(o2.getClassName()));
			}
		});
		boolean hasDisplayBeans = false;
		boolean hasServiceBeans = false;
		for (BeanDescription beanDescription : beanDescriptions) {
			if (beanDescription.isDisplayed()) {
				hasDisplayBeans = true;
			}
			if (beanDescription.isService()) {
				hasServiceBeans = true;
			}
		}
		if (beanDescriptions.size() > 0) {
			Map<String, Object> commonMap = new HashMap<String, Object>();
			// Global/Common
			String basePckg = null;
			Set<String> displayProfileNames = new HashSet<String>();
			for (BeanDescription desc : beanDescriptions) {
				if (!(desc.isDisplayed() || desc.isMapped() || desc.isService() || desc.isWrapped())) {
					continue;
				}
				basePckg = determineCommonBasePackage(basePckg, desc);
				for (DisplayProfileDescription displayProfileDesc : desc.getDisplayProfiles()) {
					displayProfileNames.add(displayProfileDesc.getProfileName());
				}
			}
			commonMap.put("projectName", projectName);
			commonMap.put("projectVersion", version);
			commonMap.put("artifactType", artifactTypeEn.toString());
			commonMap.put("basePackage", basePckg);
			commonMap.put("beans", beanDescriptions);
			if (genTypeEn == GenType.RESOURCES || genTypeEn == GenType.ALL) {
				// bei Resources bisher nur common
				File targetFolderWebInf = new File(targetFolderWebStart, "WEB-INF");
				targetFolderWebInf.mkdir();
				File targetFolderVaadin = new File(targetFolderWebStart, "VAADIN");
				targetFolderVaadin.mkdir();
				File targetFolderThemes = new File(targetFolderVaadin, "themes");
				targetFolderThemes.mkdir();
				File targetFolderTouchkitex = new File(targetFolderThemes, "touchkitex");
				targetFolderTouchkitex.mkdir();
				if (hasDisplayBeans && genServletBase) {
					runVelocity(null, commonMap, basePckg, null, null, null, null, "stylescss.template", (new File(targetFolderTouchkitex, "styles.css")), false);
					runVelocity(null, commonMap, basePckg, null, null, null, null, "touchkitexcss.template", (new File(targetFolderTouchkitex, "touchkitex.css")), false);
					runVelocity(null, commonMap, basePckg, null, null, null, null, "beansxml.template", (new File(targetFolderWebInf, "beans.xml")), false);
					runVelocity(null, commonMap, basePckg, null, null, null, null, "webxml.template", (new File(targetFolderWebInf, "web.xml")), false);
					runVelocity(null, commonMap, basePckg, null, null, null, null, "widgetset.template",
							packageToFile(targetFolderResStart, basePckg, projectName + "Widgetset", ".gwt.xml"), false);
				}
				if (hasDisplayBeans) {
					// Internationalization
					// EINE pro Profile
					for (String displayProfileName : displayProfileNames) {
						runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
								+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "messages.template",
								packageToFile(targetFolderResStart, basePckg + ".ui." + displayProfileName + ".view", "messages", ".properties"));
					}
					// ditto testeditor
					File tests = existingFolder(targetFolderTesteditStart, projectName + "Tests");
					runVelocity(null, commonMap, null, null, null, null, null, "test/AllActionGroups.template",
							new File(tests, "AllActionGroups.xml"));
					runVelocity(null, commonMap, null, null, null, null, null, "test/ConfigTpr.template", new File(tests, "config.tpr"));
					runVelocity(null, commonMap, null, null, null, null, null, "test/TechnicalBindingTypeCollection.template", new File(tests,
							"TechnicalBindingTypeCollection.xml"));
					runVelocity(null, commonMap, null, null, null, null, null, "test/ElementList.template", new File(tests,
							"ElementList.conf"));
					File fitnesseRoot = existingFolder(tests, "FitNesseRoot");
					runVelocity(null, commonMap, null, null, null, null, null, "test/FitnesseRootProperties.template", new File(fitnesseRoot,
							"properties"));
					runVelocity(null, commonMap, null, null, null, null, null, "test/FitnesseRootUpdateDoNotCopyOverList.template", new File(
							fitnesseRoot, "updateDoNotCopyOverList"));
					runVelocity(null, commonMap, null, null, null, null, null, "test/FitnesseRootUpdateList.template", new File(fitnesseRoot,
							"updateList"));
					File projectTests = existingFolder(fitnesseRoot, projectName + "Tests");
					runVelocity(null, commonMap, null, null, null, null, null, "test/TestContent.template", new File(projectTests, "content.txt"));
					commonMap.put("prop", new String[] { "Edit", "Files", "Properties", "RecentChanges", "Refactor", "Search", "Versions",
							"WhereUsed" });
					runVelocity(null, commonMap, null, null, null, null, null, "test/Properties.template", new File(projectTests, "properties.xml"));
					commonMap.remove("prop");
					File scenarioLibrary = existingFolder(projectTests, "ScenarioLibrary");
					runVelocity(null, commonMap, null, null, null, null, null, "test/ScenarioLibraryContent.template", new File(scenarioLibrary,
							"content.txt"));
					commonMap.put("prop", new String[] { "Edit", "Files", "Properties", "RecentChanges", "Refactor", "Search", "Versions",
							"WhereUsed" });
					runVelocity(null, commonMap, null, null, null, null, null, "test/Properties.template",
							new File(scenarioLibrary, "properties.xml"));
					commonMap.remove("prop");
					File testSzenarien = existingFolder(projectTests, "TestSzenarien");
					runVelocity(null, commonMap, null, null, null, null, null, "test/TestSzenarienContent.template", new File(testSzenarien,
							"content.txt"));
					commonMap.put("prop", new String[] { "Edit", "Files", "Properties", "RecentChanges", "Refactor", "Search", "Versions", "WhereUsed" });
					runVelocity(null, commonMap, null, null, null, null, null, "test/Properties.template",
							new File(testSzenarien, "properties.xml"));
					commonMap.remove("prop");
					File frontPage = existingFolder(fitnesseRoot, "FrontPage");
					runVelocity(null, commonMap, null, null, null, null, null, "test/FrontPageContent.template", new File(frontPage, "content.txt"));
					commonMap.put("prop", new String[] { "AddChild", "Edit", "Files", "Help", "Properties", "Prune", "RecentChanges", "Refactor",
							"Search", "Static", "Suites", "Versions", "WhereUsed" });
					runVelocity(null, commonMap, null, null, null, null, null, "test/Properties.template", new File(frontPage, "properties.xml"));
					commonMap.remove("prop");
					// Bean-spezifisch
					for (BeanDescription desc : beanDescriptions) {
						if (desc.isDisplayed()) {
							File listeTest = existingFolder(projectTests, desc.getClassName() + "ListeTest");
							runVelocity(desc, commonMap, null, null, null, null, null, "test/ListeTestContent.template", new File(listeTest,
									"content.txt"));
							commonMap.put("prop", new String[] { "Edit", "Files", "Properties", "RecentChanges", "Refactor", "Search", "Test",
									"Versions", "WhereUsed" });
							runVelocity(desc, commonMap, null, null, null, null, null, "test/Properties.template", new File(listeTest,
									"properties.xml"));
							commonMap.remove("prop");
						}
					}
				}
			}
			if (genTypeEn == GenType.SOURCES || genTypeEn == GenType.ALL) {
				// jetzt common generieren
				if (hasDisplayBeans) {
					runVelocity(null, commonMap, basePckg + ".ui.presenter", null, null, null, null, "Presenter.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui.presenter", "Presenter", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui.presenter", null, null, null, null, "SubviewCapablePresenter.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui.presenter", "SubviewCapablePresenter", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui.view", null, null, null, null, "View.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui.view", "View", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui.view", null, null, null, null, "ObservableView.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui.view", "ObservableView", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui.view", null, null, null, null, "ErrorHandlerView.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui.view", "ErrorHandlerView", ".java"));
				}
				// auch für Service-Layer
				if (hasDisplayBeans || hasServiceBeans) {
					runVelocity(null, commonMap, basePckg, null, null, null, null, "BusinessException.template",
							packageToFile(targetFolderSrcStart, basePckg, "BusinessException", ".java"));
					runVelocity(null, commonMap, basePckg, null, null, null, null, "TechnicalException.template",
							packageToFile(targetFolderSrcStart, basePckg, "TechnicalException", ".java"));
				}
				// wenn wir Basis generieren wollen auch das Folgende:
				if (hasDisplayBeans && genServletBase) {
					runVelocity(null, commonMap, basePckg, null, null, null, null, "Servlet.template",
							packageToFile(targetFolderSrcStart, basePckg, projectName + "Servlet", ".java"));
					runVelocity(null, commonMap, basePckg, null, null, null, null, "UI.template",
							packageToFile(targetFolderSrcStart, basePckg, projectName + "UI", ".java"));
				}
				// dazu gehoert auch die Startseite und die Factories
				// EINE pro Profile
				if (hasDisplayBeans) {
					for (String displayProfileName : displayProfileNames) {
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".presenter", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "PresenterFactory.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".presenter", "PresenterFactory", ".java"));
					
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".presenter", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "FirstPagePresenter.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".presenter", "FirstPagePresenter", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".presenter", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "MainPresenter.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".presenter", "MainPresenter", ".java"));
					
					runVelocity(
							null,
							commonMap,
							basePckg + ".ui." + displayProfileName + ".presenter",
							null,
							basePckg + ".ui." + displayProfileName + ".presenter",
							basePckg + ".ui." + displayProfileName + ".view",
							displayProfileName,
							"FirstPagePresenterImpl.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".presenter", "FirstPagePresenterImpl",
									".java"));
					runVelocity(
							null,
							commonMap,
							basePckg + ".ui." + displayProfileName + ".presenter",
							null,
							basePckg + ".ui." + displayProfileName + ".presenter",
							basePckg + ".ui." + displayProfileName + ".view",
							displayProfileName,
							"MainPresenterImpl.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".presenter", "MainPresenterImpl",
									".java"));
					
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "ViewFactory.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "ViewFactory", ".java"));
					runVelocity(
							null,
							commonMap,
							basePckg + ".ui." + displayProfileName + ".view",
							null,
							basePckg + ".ui." + displayProfileName + ".presenter",
							basePckg + ".ui." + displayProfileName + ".view",
							displayProfileName,
							"VaadinViewFactory.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "VaadinViewFactory",
									".java"));
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "FirstPageView.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "FirstPageView", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "FirstPageViewImpl.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "FirstPageViewImpl", ".java"), false);
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "MainView.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "MainView", ".java"));
					runVelocity(null, commonMap, basePckg + ".ui." + displayProfileName + ".view", null, basePckg + ".ui." + displayProfileName
							+ ".presenter", basePckg + ".ui." + displayProfileName + ".view", displayProfileName, "MainViewImpl.template",
							packageToFile(targetFolderSrcStart, basePckg + ".ui." + displayProfileName + ".view", "MainViewImpl", ".java"), false);
				}
				}
				// Bean-spezifisch
				for (BeanDescription desc : beanDescriptions) {
					if (desc.isMapped()) {
						runVelocity(desc, commonMap, desc.getPckg(), desc.getPckg(), null, null, null, "Mapper.template",
								packageToFile(targetFolderSrcStart, desc.getPckg(), desc.getClassName(), "Mapper.java"));
					}
					if (desc.isDisplayed()) {
						// Presenter, Views
						for (DisplayProfileDescription p : desc.getDisplayProfiles()) {
							// Edit-Presenter
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "EditPresenter.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "EditPresenter.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "EditPresenterImpl.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "EditPresenterImpl.java"));
							// Edit-View
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "EditView.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "EditView.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "EditViewImpl.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "EditViewImpl.java"));
							// Add-Presenters
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "AddPresenter.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "AddPresenter.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "AddPresenterImpl.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "AddPresenterImpl.java"));
							// Add-View
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "AddView.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "AddView.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "AddViewImpl.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "AddViewImpl.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ChangePresenter.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "ChangePresenter.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ChangePresenterImpl.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "ChangePresenterImpl.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ListPresenter.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "ListPresenter.java"));
							runVelocity(desc, commonMap, desc.getPresenterPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ListPresenterImpl.template",
									packageToFile(targetFolderSrcStart, desc.getPresenterPckg(p), desc.getClassName(), "ListPresenterImpl.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "AddView.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "AddView.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ChangeView.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "ChangeView.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ChangeViewImpl.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "ChangeViewImpl.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ListView.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "ListView.java"));
							runVelocity(desc, commonMap, desc.getViewPckg(p), desc.getPckg(), desc.getPresenterPckg(p), desc.getViewPckg(p),
									p.getProfileName(), "ListViewImpl.template",
									packageToFile(targetFolderSrcStart, desc.getViewPckg(p), desc.getClassName(), "ListViewImpl.java"));
						}
					}
					if (desc.isDisplayed() || desc.isService()) {
						// Service
						runVelocity(desc, commonMap, desc.getBasePckg() + ".service", desc.getPckg(), null, null, null, "Service.template",
								packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".service", desc.getClassName(), "Service.java"));
						if (artifactTypeEn == ArtifactType.EJB || artifactTypeEn == ArtifactType.PLAIN || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".service", desc.getPckg(), null, null, null, "ServiceImpl.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".service", desc.getClassName(), "ServiceImpl.java"));
						}
						if (artifactTypeEn == ArtifactType.EJB || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".service", desc.getPckg(), null, null, null, "ServiceEjb.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".service", desc.getClassName(), "ServiceEjb.java"));
						}
						if (artifactTypeEn == ArtifactType.PLAIN || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".service", desc.getPckg(), null, null, null, "ServicePlain.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".service", desc.getClassName(), "ServicePlain.java"));
						}
						// ditto Dao
						runVelocity(desc, commonMap, desc.getBasePckg() + ".dao", desc.getPckg(), null, null, null, "Dao.template",
								packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".dao", desc.getClassName(), "Dao.java"));
						if (artifactTypeEn == ArtifactType.EJB || artifactTypeEn == ArtifactType.PLAIN || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".dao", desc.getPckg(), null, null, null, "DaoImpl.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".dao", desc.getClassName(), "DaoImpl.java"));
						}
						if (artifactTypeEn == ArtifactType.EJB || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".dao", desc.getPckg(), null, null, null, "DaoEjb.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".dao", desc.getClassName(), "DaoEjb.java"));
						}
						if (artifactTypeEn == ArtifactType.PLAIN || artifactTypeEn == ArtifactType.ALL) {
							runVelocity(desc, commonMap, desc.getBasePckg() + ".dao", desc.getPckg(), null, null, null, "DaoPlain.template",
									packageToFile(targetFolderSrcStart, desc.getBasePckg() + ".dao", desc.getClassName(), "DaoPlain.java"));
						}
					}
					if (desc.isWrapped()) {
						runVelocity(desc, commonMap, desc.getPckg(), desc.getPckg(), null, null, null, "Wrapper.template",
								packageToFile(targetFolderSrcStart, desc.getPckg(), desc.getClassName(), "Wrapper.java"));
					}
					// for all having at least one
					if (desc.isDisplayed() || desc.isMapped() || desc.isService() || desc.isWrapped()) {
						runVelocity(desc, commonMap, desc.getPckg(), desc.getPckg(), null, null, null, "Properties.template",
								packageToFile(targetFolderSrcStart, desc.getPckg(), desc.getClassName(), "Properties.java"));
						runVelocity(desc, commonMap, desc.getPckg(), desc.getPckg(), null, null, null, "Query.template",
								packageToFile(targetFolderSrcStart, desc.getPckg(), desc.getClassName(), "Query.java"));
					}
				}
			}
		}
	}

	private static String determineCommonBasePackage(String basePckg, BeanDescription desc) {
		if (basePckg == null) {
			basePckg = desc.getBasePckg();
		} else {
			// so lange verkuerzen, bis gemeinsamer Start
			while (!desc.getBasePckg().startsWith(basePckg)) {
				if (basePckg.indexOf('.') >= 0) {
					basePckg = basePckg.substring(0, basePckg.lastIndexOf('.'));
				} else {
					basePckg = "common";
					break;
				}
			}
		}
		return basePckg;
	}

	private static void exploreFolders(File sourceFolderStart, File targetFolderStart, String pckgStart, SourceDao sourceDao,
			List<BeanDescription> beanDescriptions) throws ParseException, IOException {
		for (File f : sourceFolderStart.listFiles()) {
			if (f.isDirectory()) {
				File targetFolder = new File(targetFolderStart, f.getName());
				targetFolder.mkdir();
				String pckg = pckgStart;
				if (pckg.length() > 0) {
					pckg += ".";
				}
				pckg += f.getName();
				exploreFolders(f, targetFolder, pckg, sourceDao, beanDescriptions);
			}
			if (f.isFile() && f.getName().endsWith(".java")) {
				BeanDescription desc = sourceDao.processJavaInput(new FileInputStream(f));
				desc.setPckg(pckgStart);
				beanDescriptions.add(desc);
			}
		}
	}

	private static File packageToFile(File targetFolderStart, String pckg, String className, String suffix) {
		File folder = new File(targetFolderStart, pckg.replace('.', File.separatorChar));
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return new File(folder, className + suffix);
	}
	
	private static File existingFolder(File targetStart, String name) {
		File folder = new File(targetStart, name);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}
	
	private static void runVelocity(BeanDescription desc, Map<String, Object> commonMap, String pckg, String modelPckg, String presenterPckg,
			String viewPckg, String profileName, String templateName, File outFile) throws IOException {
		runVelocity(desc, commonMap, pckg, modelPckg, presenterPckg, viewPckg, profileName, templateName, outFile, true);
	}

	private static void runVelocity(BeanDescription desc, Map<String, Object> commonMap, String pckg, String modelPckg, String presenterPckg,
			String viewPckg, String profileName, String templateName, File outFile, boolean mandatory) throws IOException {
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Template template;
		// Issue #6: for optional templates check whether it's there
		boolean runTemplate;
		if (mandatory) {
			runTemplate = true;
		} else {
			Enumeration<URL> templateResEnum = Velocity.class
					.getClassLoader().getResources(
							TEMPLATE_PACKAGE.substring(1) + templateName);
			runTemplate = templateResEnum.hasMoreElements();
		}
		if (!runTemplate) {
			return;
		}
		template = Velocity.getTemplate(TEMPLATE_PACKAGE + templateName);
		VelocityContext context = new VelocityContext();
		context.put("bean", desc);
		context.put("common", commonMap);
		context.put("package", pckg);
		context.put("modelPackage", modelPckg);
		context.put("presenterPackage", presenterPckg);
		context.put("viewPackage", viewPckg);
		context.put("profileName", profileName);
		context.put("unicodeUtil", UnicodeUtil.SINGLETON);
		Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
		template.merge(context, writer);
		writer.close();
	}

	protected String toValidJavaClassName(String name) {
		return WordUtils.capitalize(name,
				new char[] {' ','_','-' }).replaceAll(" ", "").replaceAll("-", "").replaceAll("_", "");
	}
	public static void main(String[] args) throws IOException, ParseException {
		// only for local development (in Project root of gen)
		processJavaFiles(new File("../../VaadinatorExample/AddressbookExample/src/main/java"), new File(
				"../../VaadinatorExample/AddressbookExample/target/generated-sources"), new SourceDao(), "AddressbookExample", "0.10-SNAPSHOT", true,
				ArtifactType.ALL, GenType.ALL);
	}

}