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
package de.akquinet.engineering.vaadinator.dao;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.InputStream;

import de.akquinet.engineering.vaadinator.annotations.Constants;
import de.akquinet.engineering.vaadinator.annotations.DisplayBean;
import de.akquinet.engineering.vaadinator.annotations.DisplayEnum;
import de.akquinet.engineering.vaadinator.annotations.DisplayProperty;
import de.akquinet.engineering.vaadinator.annotations.FieldType;
import de.akquinet.engineering.vaadinator.annotations.MapBean;
import de.akquinet.engineering.vaadinator.annotations.MapProperty;
import de.akquinet.engineering.vaadinator.annotations.ServiceBean;
import de.akquinet.engineering.vaadinator.annotations.WrappedBean;
import de.akquinet.engineering.vaadinator.model.BeanDescription;
import de.akquinet.engineering.vaadinator.model.ConstructorDescription;
import de.akquinet.engineering.vaadinator.model.ConstructorParamDescription;
import de.akquinet.engineering.vaadinator.model.DisplayProfileDescription;
import de.akquinet.engineering.vaadinator.model.DisplayPropertyProfileDescription;
import de.akquinet.engineering.vaadinator.model.EnumValueDescription;
import de.akquinet.engineering.vaadinator.model.MapProfileDescription;
import de.akquinet.engineering.vaadinator.model.MapPropertyProfileDescription;
import de.akquinet.engineering.vaadinator.model.PropertyDescription;

public class SourceDao {

	public BeanDescription processJavaInput(InputStream in) throws ParseException {
		final BeanDescription descriptionToFill = new BeanDescription();
		CompilationUnit cu = JavaParser.parse(in, "UTF-8");
		(new VoidVisitorAdapter<Object>() {

			@Override
			public void visit(MethodDeclaration m, Object arg) {
				if (m.getAnnotations() != null) {
					System.out.println(m.getName() + ":");
					String methodName = m.getName();
					if (methodName != null && methodName.length() >= 4 && (methodName.startsWith("set") || methodName.startsWith("get"))) {
						String propName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
						for (AnnotationExpr an : m.getAnnotations()) {
							System.out.println("  " + an);
							String annoName = an.getName().toString();
							PropertyDescription newProp = descriptionToFill.getProperty(propName);
							if (newProp == null) {
								newProp = new PropertyDescription(descriptionToFill, propName, m.getType().toString());
								descriptionToFill.addProperty(newProp);
							}
							if (MapProperty.class.getSimpleName().equals(annoName)) {
								processMapPropertyAnnotation(descriptionToFill, newProp, an);
							}
							if (DisplayProperty.class.getSimpleName().equals(annoName)) {
								processDisplayPropertyAnnotation(descriptionToFill, newProp, an);
							}
						}
					}
				}
				if (m.getName().startsWith("get")) {
					descriptionToFill.addGetter(m.getName());
				}
				if (m.getName().startsWith("set")) {
					descriptionToFill.addSetter(m.getName());
				}
				super.visit(m, arg);
			}

			@Override
			public void visit(FieldDeclaration f, Object arg) {
				if (f.getVariables().size() > 0 && f.getAnnotations() != null) {
					System.out.println(f.getVariables().get(0).toString() + ":");
					String fieldName = f.getVariables().get(0).getId().toString();
					if (fieldName != null && fieldName.length() >= 1) {
						for (AnnotationExpr an : f.getAnnotations()) {
							System.out.println("  " + an);
							String annoName = an.getName().toString();
							if (MapProperty.class.getSimpleName().equals(annoName)) {
								PropertyDescription newProp = descriptionToFill.getProperty(fieldName);
								if (newProp == null) {
									newProp = new PropertyDescription(descriptionToFill, fieldName, f.getType().toString());
									descriptionToFill.addProperty(newProp);
								}
								processMapPropertyAnnotation(descriptionToFill, newProp, an);
							}
							if (DisplayProperty.class.getSimpleName().equals(annoName)) {
								PropertyDescription newProp = descriptionToFill.getProperty(fieldName);
								if (newProp == null) {
									newProp = new PropertyDescription(descriptionToFill, fieldName, f.getType().toString());
									descriptionToFill.addProperty(newProp);
								}
								processDisplayPropertyAnnotation(descriptionToFill, newProp, an);
							}
						}
					}
				}
				super.visit(f, arg);
			}

			private void processMapPropertyAnnotation(final BeanDescription descriptionToFill, PropertyDescription newProp, AnnotationExpr an) {
				newProp.setMapped(true);
				if (an instanceof NormalAnnotationExpr) {
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("profileSettings".equals(pair.getName())) {
							if (pair.getValue() instanceof ArrayInitializerExpr) {
								ArrayInitializerExpr array = (ArrayInitializerExpr) pair.getValue();
								for (Expression arrayexp : array.getValues()) {
									processMapPropertyProfileAnnotation(newProp, arrayexp);
								}
							} else {
								processMapPropertyProfileAnnotation(newProp, pair.getValue());
							}
						} else if ("ignore".equals(pair.getName())) {
							newProp.setMapped(!Boolean.valueOf(pair.getValue().toString()));
						}
					}
				}
			}

			private void processMapPropertyProfileAnnotation(PropertyDescription newProp, Expression arrayexp) {
				System.out.println("    " + arrayexp);
				// array of annotations - each a profile
				String profileName = null;
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("profileName".equals(innerpair.getName())) {
						profileName = ((StringLiteralExpr) innerpair.getValue()).getValue();
					}
				}
				MapPropertyProfileDescription newProfile = newProp.getMapPropertyProfile(profileName);
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("deep".equals(innerpair.getName())) {
						newProfile.setDeep(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("exclude".equals(innerpair.getName())) {
						newProfile.setExcluded(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("include".equals(innerpair.getName())) {
						newProfile.setIncluded(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("mandatory".equals(innerpair.getName())) {
						newProfile.setMandatory(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("profileName".equals(innerpair.getName())) {
						// s.o.
					} else if ("readonly".equals(innerpair.getName())) {
						newProfile.setReadonly(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("targetPropertyName".equals(innerpair.getName())) {
						newProfile.setTargetPropertyName(((StringLiteralExpr) innerpair.getValue()).getValue());
					} else if ("targetPropertyClassName".equals(innerpair.getName())) {
						newProfile.setTargetPropertyClassName(((StringLiteralExpr) innerpair.getValue()).getValue());
					}
				}
			}

			private void processDisplayPropertyAnnotation(final BeanDescription descriptionToFill, PropertyDescription newProp, AnnotationExpr an) {
				newProp.setDisplayed(true);
				if (an instanceof NormalAnnotationExpr) {
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("captionProp".equals(pair.getName())) {
							newProp.setCaptionProp(((StringLiteralExpr) pair.getValue()).getValue());
						} else if ("converterClassName".equals(pair.getName())) {
							newProp.setConverterClassName(((StringLiteralExpr) pair.getValue()).getValue());
							descriptionToFill.addImport(createConverterImportString(newProp.getConverterClassName()));
						} else if ("captionText".equals(pair.getName())) {
							newProp.setCaptionText(((StringLiteralExpr) pair.getValue()).getValue());
						} else if ("profileSettings".equals(pair.getName())) {
							if (pair.getValue() instanceof ArrayInitializerExpr) {
								ArrayInitializerExpr array = (ArrayInitializerExpr) pair.getValue();
								for (Expression arrayexp : array.getValues()) {
									processDisplayPropertyProfileAnnotation(newProp, arrayexp);
								}
							} else {
								processDisplayPropertyProfileAnnotation(newProp, pair.getValue());
							}
						} else if ("ignore".equals(pair.getName())) {
							newProp.setDisplayed(!Boolean.valueOf(pair.getValue().toString()));
						}
					}
				}
			}

			private void processDisplayPropertyProfileAnnotation(PropertyDescription newProp, Expression arrayexp) {
				System.out.println("    " + arrayexp);
				// array of annotations - each a profile
				String profileName = Constants.DEFAULT_DISPLAY_PROFILE;
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("profileName".equals(innerpair.getName())) {
						profileName = ((StringLiteralExpr) innerpair.getValue()).getValue();
					}
				}
				DisplayPropertyProfileDescription newProfile = newProp.getDisplayPropertyProfile(profileName);
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("customClassName".equals(innerpair.getName())) {
						newProfile.setCustomClassName(((StringLiteralExpr) innerpair.getValue()).getValue());
					} else if ("customAuswahlAusListe".equals(innerpair.getName())) {
						newProfile.setCustomAuswahlAusListe(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("customMultiAuswahlAusListe".equals(innerpair.getName())) {
						newProfile.setCustomMultiAuswahlAusListe(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("customUnboxed".equals(innerpair.getName())) {
						newProfile.setCustomUnboxed(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("exclude".equals(innerpair.getName())) {
						newProfile.setExcluded(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("fieldType".equals(innerpair.getName())) {
						newProfile.setFieldType(FieldType.valueOf(innerpair.getValue().toString().replace("FieldType.", "")));
					} else if ("include".equals(innerpair.getName())) {
						newProfile.setIncluded(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("order".equals(innerpair.getName())) {
						newProfile.setOrder(Integer.parseInt(innerpair.getValue().toString()));
					} else if ("profileName".equals(innerpair.getName())) {
						// s.o.
					} else if ("readOnly".equals(innerpair.getName())) {
						newProfile.setReadOnly(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("required".equals(innerpair.getName())) {
						newProfile.setRequired(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("sectionName".equals(innerpair.getName())) {
						newProfile.setSectionName(((StringLiteralExpr) innerpair.getValue()).getValue());
					} else if ("showInDetail".equals(innerpair.getName())) {
						newProfile.setShowInDetail(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("showInTable".equals(innerpair.getName())) {
						newProfile.setShowInTable(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("tableExpandRatio".equals(innerpair.getName())) {
						newProfile.setTableExpandRatio(Float.parseFloat(innerpair.getValue().toString()));
					} else if ("allowTextInput".equals(innerpair.getName())) {
						newProfile.setAllowTextInput(Boolean.valueOf(innerpair.getValue().toString()));
					}
				}
			}

			@Override
			public void visit(EnumConstantDeclaration c, Object arg) {
				System.out.println(c.toString() + ":");
				String fieldName = c.getName();
				if (fieldName != null && fieldName.length() >= 1) {
					// add the enum value as such (always)
					EnumValueDescription newVal = descriptionToFill.getEnumValue(fieldName);
					if (newVal == null) {
						newVal = new EnumValueDescription(descriptionToFill, fieldName);
						descriptionToFill.addEnumValue(newVal);
					}
					if (c.getAnnotations() != null) {
						for (AnnotationExpr an : c.getAnnotations()) {
							System.out.println("  " + an);
							String annoName = an.getName().toString();
							if (DisplayEnum.class.getSimpleName().equals(annoName)) {
								processDisplayEnumAnnotation(descriptionToFill, newVal, an);
							}
						}
					}
				}
			}

			private void processDisplayEnumAnnotation(final BeanDescription descriptionToFill, EnumValueDescription newVal, AnnotationExpr an) {
				if (an instanceof NormalAnnotationExpr) {
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("captionProp".equals(pair.getName())) {
							newVal.setCaptionProp(((StringLiteralExpr) pair.getValue()).getValue());
						} else if ("captionText".equals(pair.getName())) {
							newVal.setCaptionText(((StringLiteralExpr) pair.getValue()).getValue());
						}
					}
				}
			}

			@Override
			public void visit(ClassOrInterfaceDeclaration cls, Object arg) {
				if (!cls.isInterface()) {
					if (cls.getAnnotations() != null) {
						System.out.println("(class):");
						descriptionToFill.setClassName(cls.getName());
						for (AnnotationExpr an : cls.getAnnotations()) {
							System.out.println("  " + an);
							String annoName = an.getName().toString();
							if (MapBean.class.getSimpleName().equals(annoName)) {
								processMapBeanAnnotation(descriptionToFill, an);
							}
							if (DisplayBean.class.getSimpleName().equals(annoName)) {
								processDisplayBeanAnnotation(descriptionToFill, an);
							}
							if (ServiceBean.class.getSimpleName().equals(annoName)) {
								processServiceBeanAnnotation(descriptionToFill, an);
							}
							if (WrappedBean.class.getSimpleName().equals(annoName)) {
								processWrappedBeanAnnotation(descriptionToFill, an);
							}
						}
					}
				}
				super.visit(cls, arg);
			}

			private void processMapBeanAnnotation(final BeanDescription descriptionToFill, AnnotationExpr an) {
				descriptionToFill.setMapped(true);
				for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
					if (an instanceof NormalAnnotationExpr) {
						if ("ignore".equals(pair.getName())) {
							descriptionToFill.setMapped(!Boolean.valueOf(pair.getValue().toString()));
						} else if ("profiles".equals(pair.getName())) {
							if (pair.getValue() instanceof ArrayInitializerExpr) {
								ArrayInitializerExpr array = (ArrayInitializerExpr) pair.getValue();
								for (Expression arrayexp : array.getValues()) {
									processMapBeanProfileAnnotation(descriptionToFill, arrayexp);
								}
							} else {
								processMapBeanProfileAnnotation(descriptionToFill, pair.getValue());
							}
						}
					}
				}
			}

			private void processMapBeanProfileAnnotation(final BeanDescription descriptionToFill, Expression arrayexp) {
				System.out.println("    " + arrayexp);
				// array of annotations - each a profile
				String profileName = null;
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("profileName".equals(innerpair.getName())) {
						profileName = ((StringLiteralExpr) innerpair.getValue()).getValue();
					}
				}
				MapProfileDescription newProfile = descriptionToFill.getMapProfile(profileName);
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("bidirectional".equals(innerpair.getName())) {
						newProfile.setBidirectional(Boolean.valueOf(innerpair.getValue().toString()));
					} else if ("profileName".equals(innerpair.getName())) {
						// s.o.
					} else if ("target".equals(innerpair.getName())) {
						newProfile.setTargetClassName(((ClassExpr) innerpair.getValue()).getType().toString());
					}
				}
			}

			private void processDisplayBeanAnnotation(final BeanDescription descriptionToFill, AnnotationExpr an) {
				descriptionToFill.setDisplayed(true);
				if (an instanceof NormalAnnotationExpr) {
					// default profile
					descriptionToFill.getDisplayProfile(Constants.DEFAULT_DISPLAY_PROFILE);
					// actual attrs
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("ignore".equals(pair.getName())) {
							descriptionToFill.setDisplayed(!Boolean.valueOf(pair.getValue().toString()));
						} else if ("captionProp".equals(pair.getName())) {
							descriptionToFill.setCaptionProp(((StringLiteralExpr) pair.getValue()).getValue());
						} else if ("captionText".equals(pair.getName())) {
							descriptionToFill.setCaptionText(((StringLiteralExpr) pair.getValue()).getValue());
						} else if ("beanValidation".equals(pair.getName())) {
							descriptionToFill.setBeanValidation(((BooleanLiteralExpr) pair.getValue()).getValue());
						} else if ("profiles".equals(pair.getName())) {
							if (pair.getValue() instanceof ArrayInitializerExpr) {
								ArrayInitializerExpr array = (ArrayInitializerExpr) pair.getValue();
								for (Expression arrayexp : array.getValues()) {
									processDisplayBeanProfileAnnotation(descriptionToFill, arrayexp);
								}
							} else {
								processDisplayBeanProfileAnnotation(descriptionToFill, pair.getValue());
							}
						}
					}
				}
			}

			private void processDisplayBeanProfileAnnotation(final BeanDescription descriptionToFill, Expression arrayexp) {
				System.out.println("    " + arrayexp);
				// array of annotations - each a profile
				String profileName = null;
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("profileName".equals(innerpair.getName())) {
						profileName = ((StringLiteralExpr) innerpair.getValue()).getValue();
					}
				}
				DisplayProfileDescription newProfile = descriptionToFill.getDisplayProfile(profileName);
				for (MemberValuePair innerpair : ((NormalAnnotationExpr) arrayexp).getPairs()) {
					if ("profileCaptionText".equals(innerpair.getName())) {
						newProfile.setProfileCaptionText(((StringLiteralExpr) innerpair.getValue()).getValue());
					} else if ("profileCaptionProp".equals(innerpair.getName())) {
						newProfile.setProfileCaptionProp(((StringLiteralExpr) innerpair.getValue()).getValue());
					} else if ("order".equals(innerpair.getName())) {
						newProfile.setOrder(Integer.parseInt(innerpair.getValue().toString()));
					} else if ("profileName".equals(innerpair.getName())) {
						// s.o.
					} else if ("showOnFirstPage".equals(innerpair.getName())) {
						newProfile.setShowOnFirstPage(Boolean.valueOf(innerpair.getValue().toString()));
					}
				}
			}

			private void processServiceBeanAnnotation(final BeanDescription descriptionToFill, AnnotationExpr an) {
				descriptionToFill.setService(true);
				if (an instanceof NormalAnnotationExpr) {
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("ignore".equals(pair.getName())) {
							descriptionToFill.setService(!Boolean.valueOf(pair.getValue().toString()));
						}
					}
				}
			}

			private void processWrappedBeanAnnotation(final BeanDescription descriptionToFill, AnnotationExpr an) {
				descriptionToFill.setWrapped(true);
				if (an instanceof NormalAnnotationExpr) {
					for (MemberValuePair pair : ((NormalAnnotationExpr) an).getPairs()) {
						if ("ignore".equals(pair.getName())) {
							descriptionToFill.setWrapped(!Boolean.valueOf(pair.getValue().toString()));
						}
					}
				}
			}

			@Override
			public void visit(ImportDeclaration imp, Object arg) {
				if (!imp.toString().contains("de.akquinet.engineering.vaadinator.annotations.")) {
					descriptionToFill.addImport(imp.toString().trim());
				}
				super.visit(imp, arg);
			}

			@Override
			public void visit(ConstructorDeclaration n, Object arg) {
				ConstructorDescription newCons = new ConstructorDescription(descriptionToFill);
				if (n.getParameters() != null) {
					for (Parameter p : n.getParameters()) {
						newCons.addParam(new ConstructorParamDescription(newCons, p.getId().getName(), p.getType().toString()));
					}
				}
				descriptionToFill.addConstructor(newCons);
				super.visit(n, arg);
			}

			@Override
			public void visit(EnumDeclaration n, Object arg) {
				System.out.println("(enum):");
				descriptionToFill.setClassName(n.getName());
				descriptionToFill.setEnumeration(true);
				super.visit(n, arg);
			}
		}).visit(cu, null);
		return descriptionToFill;
	}	

	protected String createConverterImportString(String converterClassName) {
		if(converterClassName.contains(".")) {
			return "import " + converterClassName + ";";
		}
		else {
			return "import com.vaadin.data.util.converter." + converterClassName+";"; 
		}
	}
}
