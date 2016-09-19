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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.github.javaparser.ParseException;

import de.akquinet.engineering.vaadinator.dao.SourceDao;
import de.akquinet.engineering.vaadinator.generator.CodeGenerator;
import de.akquinet.engineering.vaadinator.generator.DefaultCodeGenerator;
import de.akquinet.engineering.vaadinator.model.BeanArtifact;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.util.GeneratorUtil;

/**
 * Generate Code.
 * 
 * @goal codegenerator
 */
public class CodeGeneratorMojo extends AbstractMojo {
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
	private String genType = VaadinatorConfig.GenType.ALL.toString();

	/**
	 * Generate EJB or Plain artifacts
	 * 
	 * @parameter
	 */
	private String artifactType = VaadinatorConfig.ArtifactType.PLAIN.toString();

	/**
	 * Generate the servlet (and web.xml et al)
	 * 
	 * @parameter
	 */
	private boolean generateServlet = true;
	
	/**
	 * Options passed to the generators
	 * 
	 * @parameter
	 */
	private Map<String, String> generatorOptions = new HashMap<String, String>();
	
	public void execute() throws MojoExecutionException {
		// System.out.println(FileUtils.getFiles((new File(project.getBasedir(),
		// "src/main/java")), includes, excludes));
		// if(true)return;
		getLog().info("Hello, code world. - I'm " + project.getBasedir().getAbsolutePath() + " doing " + artifactType);
		File src = (new File(project.getBasedir(), "src/main/java"));
		File genSrc = (new File(project.getBasedir(), "target/generated-sources"));
		try {
			processJavaFiles(src, genSrc, new SourceDao(), toValidJavaClassName(project.getArtifactId()),
					project.getVersion(), generateServlet,
					VaadinatorConfig.ArtifactType.valueOf(artifactType.toUpperCase()),
					VaadinatorConfig.GenType.valueOf(genType.toUpperCase()));
		} catch (Exception e) {
			throw new MojoExecutionException("Fehler beim Generieren", e);
		}
	}

	private void processJavaFiles(File sourceFolderStart, File targetFolderBaseStart, SourceDao sourceDao,
			String projectName, String version, boolean genServletBase, VaadinatorConfig.ArtifactType artifactTypeEn,
			VaadinatorConfig.GenType genTypeEn) throws Exception {
		List<BeanDescription> beanDescriptions = new ArrayList<BeanDescription>();

		// ensure general target folders exist
		File targetFolderSrcStart = new File(targetFolderBaseStart, "java");
		targetFolderSrcStart.mkdirs();
		File targetFolderResStart = new File(targetFolderBaseStart, "resources");
		targetFolderResStart.mkdirs();
		File targetFolderTestSrcStart = new File(targetFolderBaseStart, "testjava");
		targetFolderTestSrcStart.mkdirs();
		File targetFolderTestResStart = new File(targetFolderBaseStart, "testresources");
		targetFolderTestResStart.mkdirs();

		exploreFolders(sourceFolderStart, targetFolderSrcStart, targetFolderTestSrcStart, "", sourceDao, beanDescriptions);
		// ensure bean descriptions are sorted as e.g. Presenter Factories need
		// a definite order of classes ("null" as cls name can be accepted)
		Collections.sort(beanDescriptions, new Comparator<BeanDescription>() {

			@Override
			public int compare(BeanDescription o1, BeanDescription o2) {
				return String.valueOf(o1.getClassName()).compareTo(String.valueOf(o2.getClassName()));
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

			List<CodeGenerator> codeGenerators = initGenerators();
			for (CodeGenerator codeGenerator : codeGenerators) {
				getLog().info("Generating code with: " + codeGenerator.getClass().getName());
				codeGenerator.generateCode(new VaadinatorConfig(projectName, basePckg, beanDescriptions, artifactTypeEn,
						genTypeEn, targetFolderBaseStart, targetFolderSrcStart, targetFolderResStart, targetFolderTestSrcStart, 
						targetFolderTestResStart, commonMap, displayProfileNames, genServletBase, hasDisplayBeans, hasServiceBeans,
						getLog(), generatorOptions));
			}
		}
	}

	protected List<CodeGenerator> initGenerators() {
		List<CodeGenerator> codeGenerators = new ArrayList<>();
		codeGenerators.add(new DefaultCodeGenerator());
		ServiceLoader<CodeGenerator> serviceLoader = ServiceLoader.load(CodeGenerator.class,
				this.getClass().getClassLoader());

		Iterator<CodeGenerator> iterator = serviceLoader.iterator();
		while (iterator.hasNext()) {
			codeGenerators.add(iterator.next());
		}

		getLog().info("Loaded code generators: " + StringUtils.join(codeGenerators, ","));
		return codeGenerators;
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

	private void exploreFolders(File sourceFolderStart, File targetFolderStart, File targetFolderTestStart, String pckgStart,
			SourceDao sourceDao, List<BeanDescription> beanDescriptions) throws ParseException, IOException {
		for (File f : sourceFolderStart.listFiles()) {
			if (f.isDirectory()) {
				File targetFolder = new File(targetFolderStart, f.getName());
				targetFolder.mkdir();
				File targetFolderTest = new File(targetFolderTestStart, f.getName());
				targetFolderTest.mkdir();
				String pckg = pckgStart;
				if (pckg.length() > 0) {
					pckg += ".";
				}
				pckg += f.getName();
				exploreFolders(f, targetFolder, targetFolderTest, pckg, sourceDao, beanDescriptions);
			}
			if (f.isFile() && f.getName().endsWith(".java")) {
				BeanDescription desc = sourceDao.processJavaInput(new FileInputStream(f));
				desc.setPckg(pckgStart);
				if (desc.isDisplayed()) {
					for (BeanArtifact exBeanArtifact : BeanArtifact.perBeanValues()) {
						if(exExists(exBeanArtifact, desc, null)){
						desc.addExt(exBeanArtifact);
						}
					}
					for (DisplayProfileDescription displayProfileDescription : desc.getDisplayProfiles()) {
						for (BeanArtifact exBeanArtifact : BeanArtifact.perProfileValues()) {
							if (exExists(exBeanArtifact, desc, displayProfileDescription)) {
								displayProfileDescription.addExt(exBeanArtifact);
							}
						}
					}
				}
				beanDescriptions.add(desc);
			}
		}
	}

	private boolean exExists(BeanArtifact beanArtifact, BeanDescription bean, DisplayProfileDescription profileDesc) {
		String pkg = null;
		if (beanArtifact.isView()) {
			pkg = bean.getViewPckg(profileDesc);
		} else if(beanArtifact.isPresenter()) {
			pkg = bean.getPresenterPckg(profileDesc);
		} else if(beanArtifact.isDao()) {
			pkg = bean.getDaoPckg();
		}
		String className = bean.getClassName();
		File exSourceFile = GeneratorUtil.packageToFile(new File(project.getBasedir(), "src/main/java"), pkg,
				className + beanArtifact.getFileNameSuffix() + "Ex", ".java");
		boolean exExists = exSourceFile.exists();
		getLog().debug("Checking if " + exSourceFile + " exists: " + exExists);
		return exExists;
	}

	protected String toValidJavaClassName(String name) {
		return WordUtils.capitalize(name, new char[] { ' ', '_', '-' }).replaceAll(" ", "").replaceAll("-", "")
				.replaceAll("_", "");
	}

	public static void main(String[] args) throws Exception {
		// only for local development (in Project root of gen)
		CodeGeneratorMojo mojo = new CodeGeneratorMojo();
		mojo.processJavaFiles(new File("../../VaadinatorExample/AddressbookExample/src/main/java"),
				new File("../../VaadinatorExample/AddressbookExample/target/generated-sources"), new SourceDao(),
				"AddressbookExample", "0.10-SNAPSHOT", true, VaadinatorConfig.ArtifactType.ALL,
				VaadinatorConfig.GenType.ALL);
	}

}