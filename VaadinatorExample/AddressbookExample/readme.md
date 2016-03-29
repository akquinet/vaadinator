# Vaadinator Addressbook Example
## Running
This example has different profiles which have to be selected:
- desktop or touchkit
- tomcat, tomcatex or jboss

To run the tomcat desktop variant call `mvn jetty:run -Pdesktop,tomcat`, then go with your webbrowser to http://localhost:8080.

For the touchkit tomcat variant you first have to build the widgetset with `mvn vaadin:compile -Ptouchkit,tomcat`, then launch the war with `mvn jett:run-war -Ptouchkit,tomcat`.
