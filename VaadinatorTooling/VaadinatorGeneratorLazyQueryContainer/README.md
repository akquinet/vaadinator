Vaadinator LazyQueryContainer generator
=======================================

This plugin generates <a href="https://vaadin.com/directory#!addon/lazy-query-container">LazyQueryContainers</a> for your ``@DisplayBean`` annotated classes.

## Usage

Add a dependency to ``VaadinatorGeneratorLazyQueryContainer`` to the ``VaadinatorGenerator`` plugin in your ``<build>`` section in your ``pom.xml``:

```
<build>
   <plugins>
	  <plugin>
	     <groupId>de.akquinet.engineering.vaadin.vaadinator</groupId>
		 <artifactId>VaadinatorGenerator</artifactId>
		 <version>0.20-SNAPSHOT</version>
		 <dependencies>							
		    <dependency>
			   <groupId>de.akquinet.engineering.vaadin.vaadinator</groupId>
			   <artifactId>VaadinatorGeneratorLazyQueryContainer</artifactId>
			   <version>0.20-SNAPSHOT</version>
			</dependency>
			...
		 </dependencies>
```

You also have to add the <a href="https://vaadin.com/directory#!addon/lazy-query-container">LazyQueryContainer</a> dependency to the dependency section of your ``pom.xml``.

This will generate you this classes per bean in the ``...view.container`` package:
   * ``BeanNameLazyQuery.java`` - an internal class needed by the LazyQueryContainer add-on
   * ``BeanNameLazyQueryFactory.java`` - an internal class needed by the LazyQueryContainer add-on
   * ``BeanNameLazyQueryContainer.java`` - the class you use in your code
   
## Example
The plugin is used in the ``AddressbookExample``.