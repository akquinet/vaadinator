package de.akquinet.engineering.vaadinator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

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
			String templatePackage) throws IOException {
		runVelocity(desc, commonMap, pckg, modelPckg, presenterPckg, viewPckg, profileName, templateName, outFile, true,
				templatePackage);
	}

	public static void runVelocity(BeanDescription desc, Map<String, Object> commonMap, String pckg, String modelPckg,
			String presenterPckg, String viewPckg, String profileName, String templateName, File outFile,
			boolean mandatory, String templatePackage) throws IOException {
		Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		Template template;
		// Issue #6: for optional templates check whether it's there
		boolean runTemplate;
		if (mandatory) {
			runTemplate = true;
		} else {
			Enumeration<URL> templateResEnum = Velocity.class.getClassLoader()
					.getResources(templatePackage.substring(1) + templateName);
			runTemplate = templateResEnum.hasMoreElements();
		}
		if (!runTemplate) {
			return;
		}
		template = Velocity.getTemplate(templatePackage + templateName);
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
}
