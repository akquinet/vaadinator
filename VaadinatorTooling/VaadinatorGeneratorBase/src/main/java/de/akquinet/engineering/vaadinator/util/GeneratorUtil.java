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
package de.akquinet.engineering.vaadinator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import de.akquinet.engineering.vaadinator.model.BeanArtifact;
import de.akquinet.engineering.vaadinator.model.BeanDescription;

public class GeneratorUtil {
	public static File packageToFile(File targetFolderStart, String pckg, String className, String suffix) {
		File folder = new File(targetFolderStart, pckg.replace('.', File.separatorChar));
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return new File(folder, className + suffix);
	}

	public static File existingFolder(File targetStart, String name) {
		File folder = new File(targetStart, name);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	public static void runVelocity(BeanDescription desc, Map<String, Object> commonMap, String pckg, String modelPckg,
			String presenterPckg, String viewPckg, String profileName, String templateName, File outFile,
			String templatePackage, Log log) throws IOException {
		runVelocity(desc, commonMap, pckg, modelPckg, presenterPckg, viewPckg, profileName, templateName, outFile, true,
				templatePackage, log);
	}

	public static void runVelocity(BeanDescription desc, Map<String, Object> commonMap, String pckg, String modelPckg,
			String presenterPckg, String viewPckg, String profileName, String templateName, File outFile,
			boolean mandatory, String templatePackage, Log log) throws IOException {
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Template template;
		// Issue #6: for optional templates check whether it's there
		boolean runTemplate;
		if (mandatory) {
			runTemplate = true;
		} else {
			runTemplate = isTemplateExisting(templateName, templatePackage);
		}
		if (!runTemplate) {
			return;
		}
		template = Velocity.getTemplate(templatePackage + templateName);
		String className = desc != null ? desc.getClassName() : "no description found ";
		log.debug("Create file with template: "+ template.getName() + " for bean " + className + " in profile: " + profileName);
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
		log.info("Written file: " + outFile);
	}

	public static boolean isTemplateExisting(String templateName, String templatePackage) throws IOException {
		boolean runTemplate;
		Enumeration<URL> templateResEnum = Velocity.class.getClassLoader()
				.getResources(templatePackage.substring(1) + templateName);
		runTemplate = templateResEnum.hasMoreElements();
		return runTemplate;
	}
}
